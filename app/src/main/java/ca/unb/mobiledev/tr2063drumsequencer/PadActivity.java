package ca.unb.mobiledev.tr2063drumsequencer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.tr2063drumsequencer.soundbank.SoundbankActivity;

public class PadActivity extends AppCompatActivity {

    private Button[] pads;
    private AsyncPlayer[] asyncPlayers;
    private SoundPool soundPool;
    private int[] soundPoolIds;
    private Uri[] uris;
    private String[] resStrings;
    private final int numberOfSamples = 4;
    private SharedPreferences prefs;
    private String soundbankName;
    private final int STARTING_ID = 4566;

    private Runnable playback;
    private AudioAttributes attributes;

    private Button recordButton;

    private TextView soundbankNameTextView;

    private static final int SOUNDBANK_REQUEST_CODE = 400;
    private static final int RECORD_REQUEST_CODE = 500;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            Intent sequencerIntent = new Intent(this, SequencerActivity.class);
            startActivity(sequencerIntent);
            return;
        }

        soundbankNameTextView = findViewById(R.id.soundbank_name_textview);
        recordButton = findViewById(R.id.pad_record_button);

        recordButton.setOnClickListener(this::onRecordButtonClick);

        prefs = getSharedPreferences("drumpad", 0 );

        pads = new Button[numberOfSamples];
        asyncPlayers = new AsyncPlayer[numberOfSamples];
        uris = new Uri[numberOfSamples];
        resStrings = new String[numberOfSamples];
        soundPoolIds = new int[numberOfSamples];

        loadPreferences();
        setupPadListeners();

        Button padLibraryButton = findViewById(R.id.pad_library_button);
        padLibraryButton.setOnClickListener(this::onLibraryButtonClick);
    }

    public void setupPadListeners() {
        LinearLayout ll = findViewById(R.id.pad_view);
        int llchildren = ll.getChildCount();
        int count = 0;
        for (int i = 0; i < llchildren; i++) {
            View v = ll.getChildAt(i);
            if (v instanceof LinearLayout) {
                int vchildren = ((LinearLayout) v).getChildCount();
                for (int j = 0; j < vchildren; j++) {
                    View vb =  ((LinearLayout) v).getChildAt(j);
                    if (vb instanceof Button) {
                        pads[count] = (Button) vb;
                        //setup players onClick
                        pads[count].setId(STARTING_ID + count);
                        pads[count].setOnClickListener(this::onPadButtonClick);
                        count++;
                    }
                }
            }
        }
    }

    private void loadPreferences() {
        String defaultRes1 = "android:resource//" + this.getPackageName() +
                "/" + R.raw.kick;
        String defaultRes2 = "android:resource//" + this.getPackageName() +
                "/" + R.raw.hhc;
        String defaultRes3 = "android:resource//" + this.getPackageName() +
                "/" + R.raw.hho;
        String defaultRes4 = "android:resource//" + this.getPackageName() +
                "/" + R.raw.snare;


        resStrings[0] = prefs.getString("res1", defaultRes1);
        resStrings[1] = prefs.getString("res2", defaultRes2);
        resStrings[2] = prefs.getString("res3", defaultRes3);
        resStrings[3] = prefs.getString("res4", defaultRes4);

        soundbankName = prefs.getString("name", "Basic");

        for (int i = 0; i < uris.length; i++) {
            uris[i] = Uri.parse(resStrings[i]);
            asyncPlayers[i] = new AsyncPlayer(String.valueOf(i+1));
            //soundPoolIds[i] = soundPool.load(resStrings[i],0);
        }

        attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();


        soundbankNameTextView.setText(soundbankName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOUNDBANK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String name = b.getString("name");
                String res1 = b.getString("res1");
                String res2 = b.getString("res2");
                String res3 = b.getString("res3");
                String res4 = b.getString("res4");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name", name);
                editor.putString("res1", res1);
                editor.putString("res2", res2);
                editor.putString("res3", res3);
                editor.putString("res4", res4);
                editor.commit();

                loadPreferences();
                uris[0] = Uri.parse(res1);
                uris[1] = Uri.parse(res2);
                uris[2] = Uri.parse(res3);
                uris[3] = Uri.parse(res4);

                soundbankNameTextView.setText(name);
            }
        }

    }

    public void onPadButtonClick(View v) {
        for (int i = 0; i < pads.length; i++) {
            if (v.getId() == pads[i].getId()) {
                int padNo = v.getId() - STARTING_ID;
                padPlay(padNo);
            }
        }
    }

    public void onRecordButtonClick(View v) {
        Intent recordIntent = new Intent(this, RecorderActivity.class);
        startActivity(recordIntent);
    }

    public void padPlay(int padNo) {
        // play sound periodically

        playback = new Runnable() {

            public void run() {
                //soundPool.play(soundPoolIds[padNo], streamVolume, streamVolume,
                        //1, 0, 1);
                asyncPlayers[padNo].play(getApplicationContext(), uris[padNo],
                       false, attributes);

            }
        };

        Thread thandler = new Thread(playback);
        thandler.start();
    }

    public void onLibraryButtonClick(View v) {
        Intent soundBankIntent = new Intent(this,SoundbankActivity.class);
        startActivityForResult(soundBankIntent, SOUNDBANK_REQUEST_CODE);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Intent sequencerIntent = new Intent(this, SequencerActivity.class);
        startActivity(sequencerIntent);
    }
}

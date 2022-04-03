package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ca.unb.mobiledev.tr2063drumsequencer.entity.SoundbankItem;
import ca.unb.mobiledev.tr2063drumsequencer.sequencer.Sequencer;
import ca.unb.mobiledev.tr2063drumsequencer.soundbank.SoundbankActivity;

public class SequencerActivity extends AppCompatActivity {
    public static int numberOfBeats = 8;

    public static int numberOfSamples = 4;

    public Sampler mySample;
    public ImageButton playButton;
    public ImageButton pauseButton;
    public Button clearButton;

    public RadioButton quarterRadioButton;
    public RadioButton eighthRadioButton;

    public SeekBar tempoBar;

    public TextView tempoText;
    public TextView tempoBarText;

    public EditText textViewRes1;
    public EditText textViewRes2;
    public EditText textViewRes3;
    public EditText textViewRes4;

    FrameLayout rootLayout;

    GridLayout mainLayout;

    ProgressBarView progressBarView;

    Sequencer sequencer;

    ToggleButton samplersButtons[][] = new ToggleButton[numberOfSamples][numberOfBeats];

    LinearLayout boardLayouts[] = new LinearLayout[numberOfSamples];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do a sharedpreferences save of this and tempo as well
        sequencer = new Sequencer(this, numberOfSamples, numberOfBeats);
        sequencer.setSample(0, R.raw.kick);
        sequencer.setSample(1, R.raw.hhc);
        sequencer.setSample(2, R.raw.hho);
        sequencer.setSample(3, R.raw.snare);

        // Use the whole device screen.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        clearButton = findViewById(R.id.clearButton);
        eighthRadioButton = findViewById(R.id.eighthRadio);
        quarterRadioButton = findViewById(R.id.quarterRadio);
        tempoBar = findViewById(R.id.tempoBar);
        tempoBarText = findViewById(R.id.tempoBarText);
        tempoText = findViewById(R.id.tempoText);

        quarterRadioButton.setChecked(true);

        tempoBar.setMin(60);
        tempoBar.setMax(260);
        tempoBar.setProgress(120);

        tempoBarText.setText(tempoBar.getProgress() + "");

        tempoBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                tempoBarText.setText(seekBar.getProgress() + "");
                sequencer.setBpm(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        pauseButton.setOnClickListener(v -> {
            sequencer.stop();
            playButton.setVisibility(View.VISIBLE);
            clearButton.setEnabled(true);
        });

        //Set the names of the tracks
        textViewRes1 = findViewById(R.id.textview_1);
        textViewRes2 = findViewById(R.id.textview_2);
        textViewRes3 = findViewById(R.id.textview_3);
        textViewRes4 = findViewById(R.id.textview_4);

        textViewRes1.setText(getResources().getResourceEntryName(R.raw.kick));
        textViewRes2.setText(getResources().getResourceEntryName(R.raw.hhc));
        textViewRes3.setText(getResources().getResourceEntryName(R.raw.hho));
        textViewRes4.setText(getResources().getResourceEntryName(R.raw.snare));

        quarterRadioButton.setOnClickListener(v -> quarterRadioButton
                .setChecked(!eighthRadioButton.isChecked()));

        eighthRadioButton.setOnClickListener(v -> eighthRadioButton
                .setChecked(!quarterRadioButton.isChecked()));

        clearButton.setOnClickListener(this::clearSeq);

        prepareBoard();

        playButton.setOnClickListener(v -> {
            playButton.setVisibility(View.INVISIBLE);
            clearButton.setEnabled(false);
            sequencer.play();
        });
    }

    public void pauseSeq(View v) {
        mySample.pause();
    }

    public void playSeq(View v) { mySample.play(); }

    public void clearSeq(View v) { clearBoard(); }


    @Override
    public void onPause() {
        super.onPause();
        sequencer.stop();
    }

    @Override
    protected void onResume() {
        sequencer.play();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("TEST", "Menu item click");
        switch (item.getItemId()) {
            case R.id.select_sample:
                // file picker
                Intent i = new Intent(SequencerActivity.this, SoundbankActivity.class);
                startActivityForResult(i, 0);
                break;
            case R.id.toggle_sequencer:
                sequencer.toggle();
                break;
            case R.id.preferences:
                Intent preferencesActivity = new Intent(getBaseContext(), Preferences.class);
                startActivityForResult(preferencesActivity, 1);
                break;
            case R.id.add_column:
                Log.e("TEST", "Adding columns");
                Intent addColumnActivity = new Intent(getBaseContext(), AddColumnPicker.class);
                startActivityForResult(addColumnActivity, 2);
                // sequencer.addColumns(amount);
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            Bundle b = data.getExtras();
            String res1 = b.getString("res1");
            String res2 = b.getString("res2");
            String res3 = b.getString("res3");
            String res4 = b.getString("res4");

            Uri res1Path = Uri.parse(res1);
            Uri res2Path = Uri.parse(res2);
            Uri res3Path = Uri.parse(res3);
            Uri res4Path = Uri.parse(res4);

            String s1 = res1.substring(res1.lastIndexOf('/')+1);
            String s2 = res2.substring(res2.lastIndexOf('/')+1);;
            String s3 = res3.substring(res3.lastIndexOf('/')+1);
            String s4 = res4.substring(res4.lastIndexOf('/')+1);


            try {
                int i1 = Integer.parseInt(s1);
                s1 = getResources().getResourceEntryName(i1);
            }
            catch (Exception e) { }
            try {
                int i2 = Integer.parseInt(s2);
                s2 = getResources().getResourceEntryName(i2);
            }
            catch (Exception e) {
            }
            try {
                int i3 = Integer.parseInt(s3);
                s3 = getResources().getResourceEntryName(i3);
            }
            catch (Exception e) { }
            try {
                int i4 = Integer.parseInt(s4);
                s4 = getResources().getResourceEntryName(i4);
            }
            catch (Exception e) { }

            textViewRes1.setText(s1);
            textViewRes2.setText(s2);
            textViewRes3.setText(s3);
            textViewRes4.setText(s4);

            sequencer.setSample(0, res1Path);
            sequencer.setSample(1, res2Path);
            sequencer.setSample(2, res3Path);
            sequencer.setSample(3, res4Path);

        }

//        switch (requestCode) {
//            case 0:
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri path = (Uri) data.getExtras().get("path");
//                    Log.i("ANDROIDEXPLORER", "path: " + path);
//                    // reset the latest sample
//                    sequencer.setSample(3, path);
//                    // TODO it should actually add a new sample rather than
//                    // overwriting an existing one
//                    // TODO rethink the ui so it allows addition/removing
//                    // samples on
//                    // runtime
//                } else if (resultCode == RESULT_CANCELED) {
//                    // User didn't select a file. Nothing to do here.
//                }
//                break;
//            case 1:
//                if (resultCode == 1) {
//                    // Coming from preferences
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                    String newBpm = prefs.getString("bpm", "120");
//                    Log.e("TEST", "new bpm is " + Integer.parseInt(newBpm));
//                    sequencer.setBpm(Integer.parseInt(newBpm));
//                }
//                break;
//            case 2:
//                Bundle b = data.getExtras();
//                int amount = b.getInt("amount");
//                Log.e("TEST", "Adding " + amount + " columns");
//                break;
//            default:
//                break;
//        }
    }

    private void prepareBoard() {
        createLayouts();
        createBoardButtons();
    }

    private void createLayouts() {
        rootLayout = findViewById(R.id.frame_layout);
        mainLayout = findViewById(R.id.grid_layout);
        mainLayout.setColumnCount(numberOfBeats);
        mainLayout.setRowCount(numberOfSamples);

        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        )));
    }

    private void createBoardButtons() {
        //FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        int width = rootLayout.getLayoutParams().width;
        int height = rootLayout.getLayoutParams().height;

        int buttonWidth = width / numberOfBeats;
        int buttonHeight = height / numberOfSamples;

        progressBarView = new ProgressBarView(this, width, height,
                buttonWidth, sequencer.getBpm());
        sequencer.setOnBPMListener(progressBarView);
        rootLayout.addView(progressBarView);

        SamplerToggleListener samplerListener = new SamplerToggleListener(sequencer, this,
                numberOfSamples, numberOfBeats);

        for (int samplePos = 0; samplePos < numberOfSamples; samplePos++) {
            Log.d("Board", "Button width: " + buttonWidth);
            for (int beatPos = 0; beatPos < numberOfBeats; beatPos++) {
                samplersButtons[samplePos][beatPos] = new ToggleButton(this);
                samplersButtons[samplePos][beatPos].setTextOff("");
                samplersButtons[samplePos][beatPos].setTextOn("");
                samplersButtons[samplePos][beatPos].setText("");
                samplersButtons[samplePos][beatPos].setBackgroundResource(R.drawable.custom_button);
                samplersButtons[samplePos][beatPos].setId(numberOfBeats * samplePos + beatPos);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = buttonHeight;
                param.width = buttonWidth;
                param.setGravity(Gravity.CENTER);
                param.columnSpec = GridLayout.spec(beatPos);
                param.rowSpec = GridLayout.spec(samplePos);
                samplersButtons[samplePos][beatPos].setLayoutParams(param);
                samplersButtons[samplePos][beatPos].setOnClickListener(samplerListener);
                mainLayout.addView(samplersButtons[samplePos][beatPos]);
            }
        }
    }

    private void clearBoard() {
        for (int samplePos = 0; samplePos < numberOfSamples; samplePos++) {
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View v = mainLayout.getChildAt(i);
                if (v instanceof ToggleButton) {
                    ((ToggleButton) v).setChecked(false);
                }
            }
        }

        sequencer.clear();
    }


    public int convertPixelsToDips(float pxs) {
        return (int) (pxs / getResources().getDisplayMetrics().density);
    }

}
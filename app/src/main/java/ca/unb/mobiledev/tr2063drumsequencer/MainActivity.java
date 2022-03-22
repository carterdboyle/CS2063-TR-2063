package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;

import ca.unb.mobiledev.tr2063drumsequencer.sequencer.Sequencer;

public class MainActivity extends Activity {
    public static final int TOTAL_BEATS = 8;

    public static final int TOTAL_SAMPLES = 4;

    public Sampler mySample;
    public ImageButton playButton;
    public ImageButton pauseButton;
    public Button clearButton;

    public RadioButton quarterRadioButton;
    public RadioButton eighthRadioButton;

    public SeekBar tempoBar;

    public TextView tempoText;
    public TextView tempoBarText;

    FrameLayout rootLayout;

    LinearLayout mainLayout;

    ProgressBarView progressBarView;

    Sequencer sequencer;

    ToggleButton samplersButtons[][] = new ToggleButton[TOTAL_SAMPLES][TOTAL_BEATS];

    LinearLayout boardLayouts[] = new LinearLayout[TOTAL_SAMPLES];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sequencer = new Sequencer(this, TOTAL_SAMPLES, TOTAL_BEATS);
        sequencer.setSample(0, R.raw.kick);
        sequencer.setSample(1, R.raw.hhc);
        sequencer.setSample(2, R.raw.hho);
        sequencer.setSample(3, R.raw.snare);

        // Use the whole device screen.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        pauseButton.setOnClickListener(v -> {
            pauseSeq(v);
            playButton.setEnabled(true);
            clearButton.setEnabled(true);
        });

        quarterRadioButton.setOnClickListener(v -> quarterRadioButton
                .setChecked(!eighthRadioButton.isChecked()));

        eighthRadioButton.setOnClickListener(v -> eighthRadioButton
                .setChecked(!quarterRadioButton.isChecked()));

        clearButton.setOnClickListener(this::clearSeq);

        prepareBoard();

        //mySample = new Sampler(this, this);

        playButton.setOnClickListener(v -> {
            playButton.setEnabled(false);
            clearButton.setEnabled(false);
            playSeq(v);
        });
    }

    public void pauseSeq(View v) {
        mySample.pause();
    }

    public void playSeq(View v) { mySample.play(); }

    public void clearSeq(View v) { mySample.clear(); }


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
                Intent i = new Intent(MainActivity.this, AndroidExplorer.class);
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

        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("path");
                    Log.i("ANDROIDEXPLORER", "path: " + path);
                    // reset the latest sample
                    sequencer.setSample(3, path);
                    // TODO it should actually add a new sample rather than
                    // overwriting an existing one
                    // TODO rethink the ui so it allows addition/removing
                    // samples on
                    // runtime
                } else if (resultCode == RESULT_CANCELED) {
                    // User didn't select a file. Nothing to do here.
                }
                break;
            case 1:
                if (resultCode == 1) {
                    // Coming from preferences
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    String newBpm = prefs.getString("bpm", "120");
                    Log.e("TEST", "new bpm is " + Integer.parseInt(newBpm));
                    sequencer.setBpm(Integer.parseInt(newBpm));
                }
                break;
            case 2:
                Bundle b = data.getExtras();
                int amount = b.getInt("amount");
                Log.e("TEST", "Adding " + amount + " columns");
                break;
            default:
                break;
        }
    }

    private void prepareBoard() {
        createLayouts();
        createBoardButtons();
    }

    private void createLayouts() {
        //rootLayout = new FrameLayout(this);

        rootLayout = findViewById(R.id.frame_layout);

        mainLayout = new LinearLayout(this);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        for (int samplePos = 0; samplePos < TOTAL_SAMPLES; samplePos++) {
            boardLayouts[samplePos] = new LinearLayout(this);
            boardLayouts[samplePos].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1));
            boardLayouts[samplePos].setBackgroundColor(Color.rgb(255, 0, 0));
            mainLayout.addView(boardLayouts[samplePos]);
        }

        rootLayout.addView(mainLayout);
    }

    private void createBoardButtons() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        int width = frameLayout.getLayoutParams().width;
        int height = frameLayout.getLayoutParams().height;

        int buttonWidth = width / TOTAL_BEATS;
        int buttonHeight = height / TOTAL_SAMPLES;

        progressBarView = new ProgressBarView(this, width, height,
                buttonWidth, sequencer.getBpm());
        sequencer.setOnBPMListener(progressBarView);
        rootLayout.addView(progressBarView);

        SamplerToggleListener samplerListener = new SamplerToggleListener(sequencer, this,
                TOTAL_SAMPLES, TOTAL_BEATS);

        for (int samplePos = 0; samplePos < TOTAL_SAMPLES; samplePos++) {
            Log.d("Board", "Button width: " + buttonWidth);
            for (int beatPos = 0; beatPos < TOTAL_BEATS; beatPos++) {
                samplersButtons[samplePos][beatPos] = new ToggleButton(this);
                samplersButtons[samplePos][beatPos].setTextOff("");
                samplersButtons[samplePos][beatPos].setTextOn("");
                samplersButtons[samplePos][beatPos].setText("");
                samplersButtons[samplePos][beatPos].setBackgroundResource(R.drawable.custom_button);
                samplersButtons[samplePos][beatPos].setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                samplersButtons[samplePos][beatPos].setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                samplersButtons[samplePos][beatPos].setId(TOTAL_BEATS * samplePos + beatPos);
                samplersButtons[samplePos][beatPos].setOnClickListener(samplerListener);
                boardLayouts[samplePos].addView(samplersButtons[samplePos][beatPos]);
            }
        }
    }


    public int convertPixelsToDips(float pxs) {
        return (int) (pxs / getResources().getDisplayMetrics().density);
    }

}
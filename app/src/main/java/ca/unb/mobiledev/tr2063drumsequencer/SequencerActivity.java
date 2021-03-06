package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
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
    public static int defaultTempo = 120;
    public static int numberOfSamples = 4;
    public static int numberOfPatterns = 4;

    private int selectedPattern;

    public ImageButton playButton;
    public ImageButton pauseButton;
    public Button clearButton;
    public Button libraryButton;

    public ToggleButton[] patternButtons;

    //Defunct
    //public RadioButton quarterRadioButton;
    //public RadioButton eighthRadioButton;

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

    SharedPreferences sharedPreferences;
    SharedPreferences patternPreferences;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sequencer = new Sequencer(this, numberOfSamples, numberOfBeats);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT){
            Intent drumPadIntent = new Intent(this, PadActivity.class);
            startActivity(drumPadIntent);
            return;
        }

        //Get sharedPreferences
        sharedPreferences = getSharedPreferences("sequencer", 0);

        int defaultPattern = 0; //Pattern A;
        selectedPattern = sharedPreferences.getInt("pattern", defaultPattern);
        loadPatternPreferences(selectedPattern);

        // Use the whole device screen.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //No toolbar needed
        //Toolbar myToolbar = findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        clearButton = findViewById(R.id.clearButton);
        libraryButton = findViewById(R.id.library_button);

        //eighthRadioButton = findViewById(R.id.eighthRadio);
        //quarterRadioButton = findViewById(R.id.quarterRadio);
        tempoBar = findViewById(R.id.tempoBar);
        tempoBarText = findViewById(R.id.tempoBarText);
        tempoText = findViewById(R.id.tempoText);

        //quarterRadioButton.setChecked(true);


        //Set the names of the tracks
        textViewRes1 = findViewById(R.id.textview_1);
        textViewRes2 = findViewById(R.id.textview_2);
        textViewRes3 = findViewById(R.id.textview_3);
        textViewRes4 = findViewById(R.id.textview_4);

//        quarterRadioButton.setOnClickListener(v -> quarterRadioButton
//                .setChecked(!eighthRadioButton.isChecked()));
//
//        eighthRadioButton.setOnClickListener(v -> eighthRadioButton
//                .setChecked(!quarterRadioButton.isChecked()));

        loadOtherConfigs();

        clearButton.setOnClickListener(this::clearSeq);
        libraryButton.setOnClickListener(this::onLibraryButtonClick);

        prepareBoard();

        playButton.setOnClickListener(v -> {
            playButton.setVisibility(View.INVISIBLE);
            clearButton.setEnabled(false);
            sequencer.play();
        });
    }

    private void loadOtherConfigs() {
        String defaultRes1 = "android.resource:" + "//" +
                getApplicationContext().getPackageName() + "/" + R.raw.kick;
        String defaultRes2 = "android.resource:" + "//" +
                getApplicationContext().getPackageName() + "/" + R.raw.hhc;
        String defaultRes3 = "android.resource:" + "//" +
                getApplicationContext().getPackageName() + "/" + R.raw.hho;
        String defaultRes4 = "android.resource:" + "//" +
                getApplicationContext().getPackageName() + "/" +  R.raw.snare;

        String res1 = patternPreferences.getString("resId1", defaultRes1);
        String res2 = patternPreferences.getString("resId2", defaultRes2);
        String res3 = patternPreferences.getString("resId3", defaultRes3);
        String res4 = patternPreferences.getString("resId4", defaultRes4);

        int tempo = patternPreferences.getInt("tempo", defaultTempo);

        sequencer.setSample(0, Uri.parse(res1));
        sequencer.setSample(1, Uri.parse(res2));
        sequencer.setSample(2, Uri.parse(res3));
        sequencer.setSample(3, Uri.parse(res4));
        sequencer.setBpm(tempo);

        String[] patternLabels = {"A", "B", "C", "D"};

        patternButtons = new ToggleButton[numberOfPatterns];
        patternButtons[0] = findViewById(R.id.a_button);
        patternButtons[1] = findViewById(R.id.b_button);
        patternButtons[2] = findViewById(R.id.c_button);
        patternButtons[3] = findViewById(R.id.d_button);

        for (int i = 0; i < patternButtons.length; i++ ) {
            if (i == selectedPattern) {
                patternButtons[i].setChecked(true);
            }
            patternButtons[i].setOnClickListener(this::onPatternButtonClick);
            patternButtons[i].setText(patternLabels[i]);
            patternButtons[i].setTextOn(patternLabels[i]);
            patternButtons[i].setTextOff(patternLabels[i]);
        }

        textViewRes1.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                SharedPreferences.Editor editor = patternPreferences.edit();
                String val = String.valueOf(v.getText());
                editor.putString("textViewRes1Text", val);
                editor.commit();
            }
            return false;
        });

        textViewRes2.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                SharedPreferences.Editor editor = patternPreferences.edit();
                String val = String.valueOf(v.getText());
                editor.putString("textViewRes2Text", val);
                editor.commit();
            }
            return false;
        });

        textViewRes3.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                SharedPreferences.Editor editor = patternPreferences.edit();
                String val = String.valueOf(v.getText());
                editor.putString("textViewRes3Text", val);
                editor.commit();
            }
            return false;
        });

        textViewRes4.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                SharedPreferences.Editor editor = patternPreferences.edit();
                String val = String.valueOf(v.getText());
                editor.putString("textViewRes4Text", val);
                editor.commit();
            }
            return false;
        });

        String textViewRes1Text = patternPreferences
                .getString("textViewRes1Text", getProperName(res1));
        String textViewRes2Text = patternPreferences
                .getString("textViewRes2Text", getProperName(res2));
        String textViewRes3Text = patternPreferences
                .getString("textViewRes3Text", getProperName(res3));
        String textViewRes4Text = patternPreferences
                .getString("textViewRes4Text", getProperName(res4));

        textViewRes1.setText(textViewRes1Text);
        textViewRes2.setText(textViewRes2Text);
        textViewRes3.setText(textViewRes3Text);
        textViewRes4.setText(textViewRes4Text);

        tempoBar.setMax(200);
        tempoBar.setProgress(tempo);

        tempoBarText.setText(tempoBar.getProgress() + "");

        tempoBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (tempoBar.getProgress() <= 60)  {
                    tempoBar.setProgress(60);
                }
                tempoBarText.setText(seekBar.getProgress() + "");
                sequencer.setBpm(seekBar.getProgress());

                SharedPreferences.Editor editor = patternPreferences.edit();
                editor.putInt("tempo", seekBar.getProgress());
                editor.commit();
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

    }

    void loadPatternPreferences(int selectedPattern) {
        switch (selectedPattern) {
            case 0:
                patternPreferences = getSharedPreferences("patternA", 0);
                break;
            case 1:
                patternPreferences = getSharedPreferences("patternB", 0);
                break;
            case 2:
                patternPreferences = getSharedPreferences("patternC", 0);
                break;
            case 3:
                patternPreferences = getSharedPreferences("patternD", 0);
                break;
            default:
                break;
        }
    }

    private void loadPatternConfig() {
        for (int i = 0; i < numberOfSamples; i++) {
            for (int j = 0; j < numberOfBeats; j++) {
                boolean v = patternPreferences.
                        getBoolean(
                                "cell" + (i * numberOfBeats + j), false);
                samplersButtons[i][j].setChecked(v);
                if (v) {
                    sequencer.enableCell(i,j);
                }
                else {
                    sequencer.disableCell(i,j);
                }
            }
        }
    }

    private void onPatternButtonClick(View view) {
        for (ToggleButton b : patternButtons) {
            if (!b.equals(findViewById(view.getId()))) {
                b.setChecked(false);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (view.getId()) {
            case R.id.a_button:
                selectedPattern = 0;
                break;
            case R.id.b_button:
                selectedPattern = 1;
                break;
            case R.id.c_button:
                selectedPattern = 2;
                break;
            case R.id.d_button:
                selectedPattern = 3;
                break;
            default:
                break;
        }
        editor.putInt("pattern", selectedPattern);
        editor.commit();
        sequencer.stopAllPlayers();
        loadPatternPreferences(selectedPattern);
        loadOtherConfigs();
        updateBoardButtons();
        loadPatternConfig();
    }

    public void clearSeq(View v) {
        clearBoard();
    }

    private void onLibraryButtonClick(View v) {
        sequencer.stop();
        playButton.setVisibility(View.VISIBLE);
        clearButton.setEnabled(true);
        Intent i = new Intent(SequencerActivity.this, SoundbankActivity.class);
        startActivityForResult(i, 0);
    }


    @Override
    public void onPause() {
        super.onPause();
        sequencer.stop();
    }

    @Override
    protected void onResume() {
        //sequencer.play();
        super.onResume();
    }

    public String getProperName(String stringIn) {
        String s = stringIn.substring(stringIn.lastIndexOf('/')+1);
        try {
            int i = Integer.parseInt(s);
            s = getResources().getResourceEntryName(i);
        }
        catch (Exception e) { }

        return s;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && data != null) {
            Bundle b = data.getExtras();
            String res1 = b.getString("res1");
            String res2 = b.getString("res2");
            String res3 = b.getString("res3");
            String res4 = b.getString("res4");

            Uri res1Path = Uri.parse(res1);
            Uri res2Path = Uri.parse(res2);
            Uri res3Path = Uri.parse(res3);
            Uri res4Path = Uri.parse(res4);

            String s1 = getProperName(res1);
            String s2 = getProperName(res2);
            String s3 = getProperName(res3);
            String s4 = getProperName(res4);

            SharedPreferences.Editor editor = patternPreferences.edit();
            editor.putString("resId1", res1);
            editor.putString("resId2", res2);
            editor.putString("resId3", res3);
            editor.putString("resId4", res4);
            editor.putString("textViewRes1Text", s1);
            editor.putString("textViewRes2Text", s2);
            editor.putString("textViewRes3Text", s3);
            editor.putString("textViewRes4Text", s4);
            editor.commit();

            textViewRes1.setText(s1);
            textViewRes2.setText(s2);
            textViewRes3.setText(s3);
            textViewRes4.setText(s4);

            sequencer.setSample(0, res1Path);
            sequencer.setSample(1, res2Path);
            sequencer.setSample(2, res3Path);
            sequencer.setSample(3, res4Path);

        }
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

    private void updateBoardButtons() {
        SamplerToggleListener samplerListener = new SamplerToggleListener(sequencer,
                patternPreferences, getApplicationContext(),
                numberOfSamples, numberOfBeats);

        for (int samplePos = 0; samplePos < numberOfSamples; samplePos++) {
            for (int beatPos = 0; beatPos < numberOfBeats; beatPos++) {
                samplersButtons[samplePos][beatPos].setOnClickListener(samplerListener);
            }
        }

    }

    private void createBoardButtons() {
        //FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        ViewTreeObserver vto = rootLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                int width  = rootLayout.getMeasuredWidth();
                int height = rootLayout.getMeasuredHeight();

                int buttonWidth = width / numberOfBeats;
                int buttonHeight = height / numberOfSamples;

                progressBarView = new ProgressBarView(getApplicationContext(), width, height,
                        buttonWidth, sequencer.getBpm());
                sequencer.setOnBPMListener(progressBarView);
                rootLayout.addView(progressBarView);

                SamplerToggleListener samplerListener = new SamplerToggleListener(sequencer,
                        patternPreferences, getApplicationContext(),
                        numberOfSamples, numberOfBeats);

                for (int samplePos = 0; samplePos < numberOfSamples; samplePos++) {
                    Log.d("Board", "Button width: " + buttonWidth);
                    for (int beatPos = 0; beatPos < numberOfBeats; beatPos++) {
                        samplersButtons[samplePos][beatPos] = new ToggleButton(getApplicationContext());
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

                loadPatternConfig();

            }
        });


//        int width = rootLayout.getLayoutParams().width;
//        int height = rootLayout.getLayoutParams().height;

//        int buttonWidth = width / numberOfBeats;
//        int buttonHeight = height / numberOfSamples;
//
//        progressBarView = new ProgressBarView(this, width, height,
//                buttonWidth, sequencer.getBpm());
//        sequencer.setOnBPMListener(progressBarView);
//        rootLayout.addView(progressBarView);
//
//        SamplerToggleListener samplerListener = new SamplerToggleListener(sequencer, this,
//                numberOfSamples, numberOfBeats);
//
//        for (int samplePos = 0; samplePos < numberOfSamples; samplePos++) {
//            Log.d("Board", "Button width: " + buttonWidth);
//            for (int beatPos = 0; beatPos < numberOfBeats; beatPos++) {
//                samplersButtons[samplePos][beatPos] = new ToggleButton(this);
//                samplersButtons[samplePos][beatPos].setTextOff("");
//                samplersButtons[samplePos][beatPos].setTextOn("");
//                samplersButtons[samplePos][beatPos].setText("");
//                samplersButtons[samplePos][beatPos].setBackgroundResource(R.drawable.custom_button);
//                samplersButtons[samplePos][beatPos].setId(numberOfBeats * samplePos + beatPos);
//                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
//                param.height = buttonHeight;
//                param.width = buttonWidth;
//                param.setGravity(Gravity.CENTER);
//                param.columnSpec = GridLayout.spec(beatPos);
//                param.rowSpec = GridLayout.spec(samplePos);
//                samplersButtons[samplePos][beatPos].setLayoutParams(param);
//                samplersButtons[samplePos][beatPos].setOnClickListener(samplerListener);
//                mainLayout.addView(samplersButtons[samplePos][beatPos]);
//            }
//        }
    }

    private void clearBoard() {
        for (int samplePos = 0; samplePos < numberOfSamples; samplePos++) {
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View v = mainLayout.getChildAt(i);
                if (v instanceof ToggleButton) {
                    SharedPreferences.Editor editor = patternPreferences.edit();
                    ((ToggleButton) v).setChecked(false);
                    editor.putBoolean("cell" + i, ((ToggleButton) v).isChecked());
                    editor.commit();
                }
            }
        }

        sequencer.clear();
    }


    public int convertPixelsToDips(float pxs) {
        return (int) (pxs / getResources().getDisplayMetrics().density);
    }

}
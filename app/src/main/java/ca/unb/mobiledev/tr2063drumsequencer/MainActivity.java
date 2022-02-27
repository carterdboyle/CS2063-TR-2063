package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;

public class MainActivity extends Activity {
    public Sampler mySample;
    public ImageButton playButton;
    public ImageButton pauseButton;
    public Button clearButton;

    public RadioButton quarterRadioButton;
    public RadioButton eighthRadioButton;

    public SeekBar tempoBar;

    public TextView tempoText;
    public TextView tempoBarText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mySample = new Sampler(this, this);

        playButton.setOnClickListener(v -> {
            playButton.setEnabled(false);
            clearButton.setEnabled(false);
            playSeq(v);
        });

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

    }

    public void pauseSeq(View v) {
        mySample.pause();
    }

    public void playSeq(View v) { mySample.play(); }

    public void clearSeq(View v) { mySample.clear(); }


}
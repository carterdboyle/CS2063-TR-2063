package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    public Sampler mySample;
    public ImageButton playButton;
    public ImageButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);

        mySample = new Sampler(this, this);

        playButton.setOnClickListener(v -> {
            playButton.setEnabled(false);
            playSeq(v);
        });

        pauseButton.setOnClickListener(v -> {
            pauseSeq(v);
            playButton.setEnabled(true);
        });
    }

    public void pauseSeq(View v) {
        mySample.pause();
    }

    public void playSeq(View v) { mySample.play(); }

}
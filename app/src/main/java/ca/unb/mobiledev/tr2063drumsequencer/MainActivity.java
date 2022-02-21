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
        playButton = (ImageButton) findViewById(R.id.playButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setVisibility(View.INVISIBLE);
                playSeq(v);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSeq(v);
                playButton.setVisibility(View.VISIBLE);
            }
        });

        mySample = new Sampler(this, this);

    }

    public void playSeq(View v) {
        mySample.play();
    }

    public void pauseSeq(View v) {
        mySample.pause();
    }

}
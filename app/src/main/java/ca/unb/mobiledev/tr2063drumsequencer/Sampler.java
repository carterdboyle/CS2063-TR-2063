package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class Sampler extends Thread {

    SoundPool soundPool;
    public boolean playing;

    private Context myContext;
    private Activity parentActivity;

    private ImageButton pauseButton;

    public Sampler(Context appContext, Activity parentActivity) {

        pauseButton = parentActivity.findViewById(R.id.pauseButton);

        this.myContext = appContext;
        this.parentActivity = parentActivity;

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    }

    public void play() {
        LooperThread thread = new LooperThread(myContext, parentActivity, soundPool);
        thread.start();
    }

    public void pause(){
        if (pauseButton.isPressed()) {
            playing = false;
        }
    }
}

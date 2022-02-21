package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class LooperThread extends Thread{

    private ToggleButton button1;
    private ToggleButton button2;
    private ToggleButton button3;
    private ToggleButton button4;

    private int snareId;

    boolean playing = true;
    int tempo = 120;
    int qPerBar = 1;

    private ImageButton playButton;
    private ImageButton pauseButton;

    private SoundPool soundPool;

    public LooperThread(Context context, Activity parentActivity, SoundPool soundPool) {
        snareId = soundPool.load(context, R.raw.snare, 1);
        this.soundPool = soundPool;

        playButton = (ImageButton) parentActivity.findViewById(R.id.playButton);
        pauseButton = (ImageButton) parentActivity.findViewById(R.id.pauseButton);

        button1 = (ToggleButton) parentActivity.findViewById(R.id.button1);
        button2 = (ToggleButton) parentActivity.findViewById(R.id.button2);
        button3 = (ToggleButton) parentActivity.findViewById(R.id.button3);
        button4 = (ToggleButton) parentActivity.findViewById(R.id.button4);
    }

    @Override
    public void run() {
        while (playing) {

            if (button1.isChecked()) {
                soundPool.play(snareId, 10, 10, 1, 0, 1);
            }
            try {
                Thread.sleep(60000 / (tempo * qPerBar));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (button2.isChecked()) {
                soundPool.play(snareId, 10, 10, 1, 0, 1);
            }
            try {
                Thread.sleep(60000 / (tempo * qPerBar));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (button3.isChecked()) {
                soundPool.play(snareId, 10, 10, 1, 0, 1);
            }
            try {
                Thread.sleep(60000 / (tempo * qPerBar));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (button4.isChecked()) {
                soundPool.play(snareId, 10, 10, 1, 0, 1);
            }
            try {
                Thread.sleep(60000 / (tempo * qPerBar));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(pauseButton.isPressed()){
                playing = false;
            }
        }
    }
}

package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.util.concurrent.Executors;

public class Sampler {

    //SoundPool soundPool;
    AsyncPlayer mediaPlayer;
    public boolean playing;

    private Context myContext;
    private Activity parentActivity;

    private ImageButton pauseButton;
    private ImageButton playButton;
    private ToggleButton button1;
    private ToggleButton button2;
    private ToggleButton button3;
    private ToggleButton button4;

    private int tempo;

    //1 for quarter note, 2 for eighth, 4 for 16th note
    private int beatDiv;

    private AudioAttributes attributes;

    public Sampler(Context appContext, Activity parentActivity) {

        pauseButton = parentActivity.findViewById(R.id.pauseButton);

        this.myContext = appContext;
        this.parentActivity = parentActivity;

        //Get these from the UI
        tempo = 144;
        beatDiv = 2;

        playButton = parentActivity.findViewById(R.id.playButton);
        pauseButton = parentActivity.findViewById(R.id.pauseButton);

        button1 = parentActivity.findViewById(R.id.button1);
        button2 = parentActivity.findViewById(R.id.button2);
        button3 = parentActivity.findViewById(R.id.button3);
        button4 = parentActivity.findViewById(R.id.button4);

        playing = true;

        attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

    }

    public void pause() {
        playing = false;

//        if (soundPool != null) {
//            soundPool.release();
//            soundPool = null;
//        }

//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
    }

    public void play() {
        playing = true;
        execute();
    }

    public void execute() {
        //soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        //snareId = soundPool.load(myContext, R.raw.snare, 1);

        //mediaPlayer = MediaPlayer.create(myContext, R.raw.snare);


        mediaPlayer = new AsyncPlayer("SNARE");

        Executors.newSingleThreadExecutor().execute(() -> {
            // Simulating long-running operation
            while (playing) {
                if (button1.isChecked()) {
                    mediaPlayer.play(myContext,
                            Uri.parse("android.resource://" +
                                    parentActivity.getPackageName() +
                                    "/"
                                    + R.raw.snare),
                            false,
                            attributes);
                    Log.i("DRUMMER", "HIT 1");
                }
                sleep();

                if (button2.isChecked()) {
                    mediaPlayer.play(myContext,
                            Uri.parse("android.resource://" +
                                    parentActivity.getPackageName() +
                                    "/"
                                    + R.raw.snare),
                            false,
                            attributes);
                    Log.i("DRUMMER", "HIT 2");
                }
                sleep();

                if (button3.isChecked()) {
                    mediaPlayer.play(myContext,
                            Uri.parse("android.resource://" +
                                    parentActivity.getPackageName() +
                                    "/"
                                    + R.raw.snare),
                            false,
                            attributes);
                    Log.i("DRUMMER", "HIT 3");
                }
                sleep();

                if (button4.isChecked()) {
                    mediaPlayer.play(myContext,
                            Uri.parse("android.resource://" +
                                    parentActivity.getPackageName() +
                                    "/"
                                    + R.raw.snare),
                            false,
                            attributes);
                    Log.i("DRUMMER", "HIT 4");
                }
                sleep();

                if (pauseButton.isPressed()) {
                    playing = false;
                }

            }
        });
    }

    private void sleep() {
        try {
            int mDelay = 60000 / (tempo * beatDiv) ;
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

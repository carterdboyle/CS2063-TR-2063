package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.Executors;

public class Sampler {
    //SoundPool soundPool;
    AsyncPlayer snareAPlayer;
    AsyncPlayer kickAPlayer;

    public boolean playing;

    private Context myContext;
    private Activity parentActivity;

    private ImageButton pauseButton;
    private ImageButton playButton;
    private Button clearButton;

    private ToggleButton snareButton1;
    private ToggleButton snareButton2;
    private ToggleButton snareButton3;
    private ToggleButton snareButton4;

    private ToggleButton kickButton1;
    private ToggleButton kickButton2;
    private ToggleButton kickButton3;
    private ToggleButton kickButton4;

    private TextView tempoBarText;

    private int tempo;

    //1 for quarter note, 2 for eighth, 4 for 16th note
    private int beatDiv;

    private AudioAttributes attributes;

    public Sampler(Context appContext, Activity parentActivity) {

        pauseButton = parentActivity.findViewById(R.id.pauseButton);

        this.myContext = appContext;
        this.parentActivity = parentActivity;

        playButton = parentActivity.findViewById(R.id.playButton);
        pauseButton = parentActivity.findViewById(R.id.pauseButton);

        snareButton1 = parentActivity.findViewById(R.id.snareButton1);
        snareButton2 = parentActivity.findViewById(R.id.snareButton2);
        snareButton3 = parentActivity.findViewById(R.id.snareButton3);
        snareButton4 = parentActivity.findViewById(R.id.snareButton4);

        kickButton1 = parentActivity.findViewById(R.id.kickButton1);
        kickButton2 = parentActivity.findViewById(R.id.kickButton2);
        kickButton3 = parentActivity.findViewById(R.id.kickButton3);
        kickButton4 = parentActivity.findViewById(R.id.kickButton4);

        tempoBarText = parentActivity.findViewById(R.id.tempoBarText);

        playing = true;

        attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        //Get these from the UI
        CharSequence text = tempoBarText.getText();
        tempo = Integer.parseInt(text.toString());
        beatDiv = ((RadioButton) (parentActivity
                .findViewById(R.id.quarterRadio)))
                .isChecked() ? 1 : 2;

    }

    public void pause() {
        playing = false;
    }

    public void play() {
        playing = true;
        beatDiv = ((RadioButton) (parentActivity
                .findViewById(R.id.quarterRadio)))
                .isChecked() ? 1 : 2;
        CharSequence text = tempoBarText.getText();
        tempo = Integer.parseInt(text.toString());
        execute();
    }

    public void clear() {
        snareButton1.setChecked(false);
        snareButton2.setChecked(false);
        snareButton3.setChecked(false);
        snareButton4.setChecked(false);

        kickButton1.setChecked(false);
        kickButton2.setChecked(false);
        kickButton3.setChecked(false);
        kickButton4.setChecked(false);
    }

    public void execute() {
        snareAPlayer = new AsyncPlayer("SNARE");
        kickAPlayer = new AsyncPlayer("KICK");
        processSnareTrack();
        processKickTrack();

    }

    private void processSnareTrack() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Simulating long-running operation
            while (playing) {
                if (snareButton1.isChecked()) {
                    playSnare(snareAPlayer);
                    Log.i("SNARE", "HIT 1");
                }
                sleep();

                if (snareButton2.isChecked()) {
                    playSnare(snareAPlayer);
                    Log.i("SNARE", "HIT 2");
                }
                sleep();

                if (snareButton3.isChecked()) {
                    playSnare(snareAPlayer);
                    Log.i("SNARE", "HIT 3");
                }
                sleep();

                if (snareButton4.isChecked()) {
                    playSnare(snareAPlayer);
                    Log.i("SNARE", "HIT 4");
                }
                sleep();

                if (pauseButton.isPressed()) {
                    playing = false;
                }

            }
        });
    }

    private void processKickTrack() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Simulating long-running operation
            while (playing) {
                if (kickButton1.isChecked()) {
                    playKick(kickAPlayer);
                    Log.i("KICK", "HIT 1");
                }
                sleep();

                if (kickButton2.isChecked()) {
                    playKick(kickAPlayer);
                    Log.i("KICK", "HIT 2");
                }
                sleep();

                if (kickButton3.isChecked()) {
                    playKick(kickAPlayer);
                    Log.i("KICK", "HIT 3");
                }
                sleep();

                if (kickButton4.isChecked()) {
                    playKick(kickAPlayer);
                    Log.i("KICK", "HIT 4");
                }
                sleep();

                if (pauseButton.isPressed()) {
                    playing = false;
                }

            }
        });
    }

    private void playSnare(AsyncPlayer sPlayer) {
        sPlayer.play(myContext,
                Uri.parse("android.resource://" +
                        parentActivity.getPackageName() +
                        "/"
                        + R.raw.snare),
                false,
                attributes);
    }

    private void playKick(AsyncPlayer sPlayer) {
        sPlayer.play(myContext,
                Uri.parse("android.resource://" +
                        parentActivity.getPackageName() +
                        "/"
                        + R.raw.kick),
                false,
                attributes);
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

package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button helpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        startButton = findViewById(R.id.intro_start_button);
        helpButton = findViewById(R.id.intro_help_button);

        startButton.setOnClickListener(this::onStartButtonClick);
        helpButton.setOnClickListener(this::onHelpButtonClick);
    }

    public void onStartButtonClick(View v) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent sequencerIntent = new Intent(this, SequencerActivity.class);
            startActivity(sequencerIntent);
        }
        else {
            Intent drumPadIntent = new Intent(this, PadActivity.class);
            startActivity(drumPadIntent);
        }
    }

    public void onHelpButtonClick(View v) {
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }
}

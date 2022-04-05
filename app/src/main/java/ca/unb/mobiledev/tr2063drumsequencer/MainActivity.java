package ca.unb.mobiledev.tr2063drumsequencer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent sequencerIntent = new Intent(this, SequencerActivity.class);
            startActivity(sequencerIntent);
        }
        else {
            Intent drumPadIntent = new Intent(this, PadActivity.class);
            startActivity(drumPadIntent);
        }
    }
}

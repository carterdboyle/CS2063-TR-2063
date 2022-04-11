package ca.unb.mobiledev.tr2063drumsequencer;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    private Button okButton;
    private TextView helpTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        okButton = findViewById(R.id.help_ok_button);
        okButton.setOnClickListener(this::onOkButtonClick);

        helpTextView = findViewById(R.id.help_howtouse_textview);
        helpTextView.setMovementMethod(new ScrollingMovementMethod());

    }

    public void onOkButtonClick(View v) {
        Intent introIntent = new Intent(this, MainActivity.class);
        startActivity(introIntent);
    }
}



package ca.unb.mobiledev.tr2063drumsequencer.soundbank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ca.unb.mobiledev.tr2063drumsequencer.AndroidExplorer;
import ca.unb.mobiledev.tr2063drumsequencer.R;

public class SoundbankCreationActivity extends AppCompatActivity {
    private final int ANDROID_EXPLORER_RES1 = 1;
    private final int ANDROID_EXPLORER_RES2 = 2;
    private final int ANDROID_EXPLORER_RES3 = 3;
    private final int ANDROID_EXPLORER_RES4 = 4;

    private EditText nameEditText;
    private TextView res1PathTextView;
    private TextView res2PathTextView;
    private TextView res3PathTextView;
    private TextView res4PathTextView;

    private Button res1ExplorerButton;
    private Button res2ExplorerButton;
    private Button res3ExplorerButton;
    private Button res4ExplorerButton;

    private Button createSoundbankButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundbank_creation);

        nameEditText = findViewById(R.id.name_edittext);
        res1PathTextView = findViewById(R.id.res1_path_textview);
        res2PathTextView = findViewById(R.id.res2_path_textview);
        res3PathTextView = findViewById(R.id.res3_path_textview);
        res4PathTextView = findViewById(R.id.res4_path_textview);

        res1ExplorerButton = findViewById(R.id.res1_explorer_button);
        res2ExplorerButton = findViewById(R.id.res2_explorer_button);
        res3ExplorerButton = findViewById(R.id.res3_explorer_button);
        res4ExplorerButton = findViewById(R.id.res4_explorer_button);

        createSoundbankButton = findViewById(R.id.create_soundbank_button);

        res1ExplorerButton.setOnClickListener(l -> {
            Intent i = new Intent(this, AndroidExplorer.class);
            startActivityForResult(i, ANDROID_EXPLORER_RES1);
        });
        res2ExplorerButton.setOnClickListener(l -> {
            Intent i = new Intent(this, AndroidExplorer.class);
            startActivityForResult(i, ANDROID_EXPLORER_RES2);
        });
        res3ExplorerButton.setOnClickListener(l -> {
            Intent i = new Intent(this, AndroidExplorer.class);
            startActivityForResult(i, ANDROID_EXPLORER_RES3);
        });
        res4ExplorerButton.setOnClickListener(l -> {
            Intent i = new Intent(this, AndroidExplorer.class);
            startActivityForResult(i, ANDROID_EXPLORER_RES4);
        });

        createSoundbankButton.setOnClickListener(l -> {
            if (nameEditText.getText() == null || res1PathTextView.getText() == null
                || res2PathTextView.getText() == null || res3PathTextView.getText() == null
                || res4PathTextView.getText() == null) {
                new AlertDialog.Builder(this).setIcon(R.drawable.icon)
                        .setTitle("No fields can be empty!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
            else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", String.valueOf(nameEditText.getText()));
                resultIntent.putExtra("res1", String.valueOf(res1PathTextView.getText()));
                resultIntent.putExtra("res2", String.valueOf(res2PathTextView.getText()));
                resultIntent.putExtra("res3", String.valueOf(res3PathTextView.getText()));
                resultIntent.putExtra("res4", String.valueOf(res4PathTextView.getText()));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ANDROID_EXPLORER_RES1:
                if (resultCode == Activity.RESULT_OK) {
                    String path = (String) data.getExtras().get("path");
                    Log.i("ANDROIDEXPLORER", "path: " + path);
                    // reset the latest sample
                    res1PathTextView.setText(String.valueOf(path));
                } else if (resultCode == RESULT_CANCELED) {
                    // User didn't select a file. Nothing to do here.
                }
                break;
            case ANDROID_EXPLORER_RES2:
                if (resultCode == Activity.RESULT_OK) {
                    String path = (String) data.getExtras().get("path");
                    Log.i("ANDROIDEXPLORER", "path: " + path);
                    // reset the latest sample
                    res2PathTextView.setText(String.valueOf(path));
                } else if (resultCode == RESULT_CANCELED) {
                    // User didn't select a file. Nothing to do here.
                }
                break;
            case ANDROID_EXPLORER_RES3:
                if (resultCode == Activity.RESULT_OK) {
                    String path = (String) data.getExtras().get("path");
                    Log.i("ANDROIDEXPLORER", "path: " + path);
                    // reset the latest sample
                    res3PathTextView.setText(String.valueOf(path));
                } else if (resultCode == RESULT_CANCELED) {
                    // User didn't select a file. Nothing to do here.
                }
                break;
            case ANDROID_EXPLORER_RES4:
                if (resultCode == Activity.RESULT_OK) {
                    String path = (String) data.getExtras().get("path");
                    Log.i("ANDROIDEXPLORER", "path: " + path);
                    // reset the latest sample
                    res4PathTextView.setText(String.valueOf(path));
                } else if (resultCode == RESULT_CANCELED) {
                    // User didn't select a file. Nothing to do here.
                }
                break;
//            case 1:
//                if (resultCode == 1) {
//                    // Coming from preferences
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                    String newBpm = prefs.getString("bpm", "120");
//                    Log.e("TEST", "new bpm is " + Integer.parseInt(newBpm));
//                    sequencer.setBpm(Integer.parseInt(newBpm));
//                }
//                break;
//            case 2:
//                Bundle b = data.getExtras();
//                int amount = b.getInt("amount");
//                Log.e("TEST", "Adding " + amount + " columns");
//                break;
            default:
                break;
        }
    }


}

package ca.unb.mobiledev.tr2063drumsequencer.soundbank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ca.unb.mobiledev.tr2063drumsequencer.R;
import ca.unb.mobiledev.tr2063drumsequencer.entity.SoundbankItem;
import ca.unb.mobiledev.tr2063drumsequencer.ui.SoundbankItemViewModel;
import ca.unb.mobiledev.tr2063drumsequencer.ui.SoundbankItemsAdapter;

public class SoundbankActivity extends AppCompatActivity {

    private final String TAG = "SoundbankActivity";
    public final int CREATE_SOUNDBANK = 1;

    private ListView mSoundbankListView;
    private SoundbankItemViewModel mSoundbankItemViewModel;
    private SoundbankItemsAdapter mSoundbankItemsAdapter;

    private Button mInsertButton;
    private Button mRemoveButton;
    private Button mOkButton;
    private TextView mResultsTextView;

    private SoundbankItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soundbank_selector);

        mSoundbankListView = findViewById(R.id.soundbank_listview);
        mInsertButton = findViewById(R.id.insert_button);
        mRemoveButton = findViewById(R.id.remove_button);
        mOkButton = findViewById(R.id.ok_button);
        mResultsTextView = findViewById(R.id.soundbank_results_textview);

        mSoundbankListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (SoundbankItem) mSoundbankListView.getItemAtPosition(i);
            }
        });
        mInsertButton.setOnClickListener(l -> {
            onInsertButtonClick();
        });

        mOkButton.setOnClickListener(l -> {
            onOkButtonClick();
        });

        mRemoveButton.setOnClickListener(l -> {
            onRemoveButtonClick();
        });

        // Set the ViewModel
        mSoundbankItemViewModel = new ViewModelProvider(this).get(
                SoundbankItemViewModel.class);

        if (mSoundbankItemViewModel != null) {
            mSoundbankItemViewModel.countItems().observe(this, count -> {
                if (count == 0) {
                    mResultsTextView.setText("No data found.");
                } else {
                    Log.i(TAG, count + "soundbanks found.");
                    mResultsTextView.setText(count + " soundbanks found.");
                }
            });

            mSoundbankItemViewModel.listAllItems().observe(this, results -> {
                if (results != null) {
                    mSoundbankItemsAdapter = new SoundbankItemsAdapter(
                            getApplicationContext(), results);
                    mSoundbankListView.setAdapter(mSoundbankItemsAdapter);
                }
                mSoundbankItemsAdapter.notifyDataSetChanged();
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_SOUNDBANK) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                String name = b.getString("name");
                String res1 = b.getString("res1");
                String res2 = b.getString("res2");
                String res3 = b.getString("res3");
                String res4 = b.getString("res4");
                addItem(name, res1, res2, res3, res4);
            }
        }

    }

    private void addItem(String name, String res1, String res2, String res3, String res4) {
        // TODO
        //  Make a call to the view model to create a record in the database table

        mSoundbankItemViewModel.insert(name, res1, res2, res3, res4);
    }

    private void removeItem(SoundbankItem item) {
        mSoundbankItemViewModel.delete(item);
    }

    public void onOkButtonClick() {
        if (selectedItem != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("name", selectedItem.getName());
            returnIntent.putExtra("res1", selectedItem.getRes1());
            returnIntent.putExtra("res2", selectedItem.getRes2());
            returnIntent.putExtra("res3", selectedItem.getRes3());
            returnIntent.putExtra("res4", selectedItem.getRes4());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else {
            new AlertDialog.Builder(this).setIcon(R.drawable.icon)
                    .setTitle("Select an item!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

    public void onRemoveButtonClick() {
        if (selectedItem != null) {
            removeItem(selectedItem);
        }
        else {
            new AlertDialog.Builder(this).setIcon(R.drawable.icon)
                    .setTitle("Select an item!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

    public void onInsertButtonClick() {
        Intent createSoundbankIntent = new Intent(this, SoundbankCreationActivity.class);
        startActivityForResult(createSoundbankIntent, CREATE_SOUNDBANK);
    }
}
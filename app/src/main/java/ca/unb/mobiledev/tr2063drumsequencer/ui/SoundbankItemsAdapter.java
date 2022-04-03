package ca.unb.mobiledev.tr2063drumsequencer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.unb.mobiledev.tr2063drumsequencer.entity.SoundbankItem;
import ca.unb.mobiledev.tr2063drumsequencer.R;

public class SoundbankItemsAdapter extends ArrayAdapter<SoundbankItem> {
    public SoundbankItemsAdapter(Context context, List<SoundbankItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SoundbankItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.soundbank_list_layout,
                    parent, false);
        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.name_textview);
        TextView tvId = convertView.findViewById(R.id.id_textview);

        // TODO
        //  Set the text used by tvName and tvNum using the data object
        //  This will need to updated once the entity model has been updated
        tvName.setText(item.getName());
        tvId.setText(String.valueOf(item.getId()));

        // Return the completed view to render on screen
        return convertView;
    }
}

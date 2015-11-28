package com.uab.es.cat.foodnetwork.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.R;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;

import java.util.List;

/**
 * Created by ramonmacias on 28/11/15.
 */
public class DonationForTransportArrayAdapter extends ArrayAdapter<String> {

    private final List<String> values;
    private final Context context;
    private final List<DonationDTO> valuesDTO;

    public DonationForTransportArrayAdapter(Context context, List<String> values, List<DonationDTO> valuesDTO){
        super(context, R.layout.list_of_donation_for_transport, values);
        this.context = context;
        this.values = values;
        this.valuesDTO = valuesDTO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_of_donation_for_transport, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView textViewStateOfDonation = (TextView) rowView.findViewById(R.id.stateOfDonation);
        textView.setText(getContext().getString(R.string.my_donation_of) + ": " + String.valueOf(valuesDTO.get(position).getInsertDate()));
        String stateText = Constants.STATE.get(valuesDTO.get(position).getState());
        textViewStateOfDonation.setText(getContext().getString(R.string.state) + ": " + stateText);

        //String s = values.get(position);
        /*String s = String.valueOf(valuesDTO.get(position).getIdDonation());
        if (s.startsWith("1")) {
            imageView.setImageResource(R.drawable.ic_check_black_24dp);
        } else {
            imageView.setImageResource(R.drawable.ic_close_black_24dp);
        }*/

        return rowView;
    }
}

package com.uab.es.cat.foodnetwork.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.uab.es.cat.foodnetwork.R;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramonmacias on 6/11/15.
 */
public class DonationArrayAdapter extends ArrayAdapter<String> {

    private final List<String> values;
    private final Context context;
    private final List<DonationDTO> valuesDTO;

    public DonationArrayAdapter(Context context, List<String> values, List<DonationDTO> valuesDTO){
        super(context, R.layout.activity_donations, values);
        this.context = context;
        this.values = values;
        this.valuesDTO = valuesDTO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_donations, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView textViewStateOfDonation = (TextView) rowView.findViewById(R.id.stateOfDonation);
        textView.setText(getContext().getString(R.string.my_donation_of) + ": " + String.valueOf(valuesDTO.get(position).getInsertDate()));
        String stateText = Constants.STATE.get(valuesDTO.get(position).getState());
        textViewStateOfDonation.setText(getContext().getString(R.string.state) + ": " + stateText);


        int sampleColorGreen = Color.parseColor("#2B4722");
        int sampleColorRed = Color.parseColor("#D75E5E");
        int sampleColorYellow = Color.parseColor("#EAD36B");

        if(valuesDTO.get(position).getState() == 1){
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound("A", sampleColorGreen);

            imageView.setImageDrawable(drawable);
        }else if(valuesDTO.get(position).getState() == 2){
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound("C", sampleColorYellow);

            imageView.setImageDrawable(drawable);
        }else {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound("F", sampleColorRed);

            imageView.setImageDrawable(drawable);
        }
        return rowView;
    }
}

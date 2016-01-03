package com.uab.es.cat.foodnetwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.uab.es.cat.foodnetwork.R;
import com.uab.es.cat.foodnetwork.dto.RankingDTO;

import java.util.List;

/**
 * Created by ramonmacias on 3/1/16.
 */
public class NeighborhoodRankingAdapter extends ArrayAdapter<String> {

    private final List<String> values;
    private final Context context;
    private List<RankingDTO> valuesDTO;

    public NeighborhoodRankingAdapter(Context context, List<String> values, List<RankingDTO> valuesDTO){
        super(context, R.layout.activity_donations, values);
        this.context = context;
        this.values = values;
        this.valuesDTO = valuesDTO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_ranking_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView textViewNumberOfDonations = (TextView) rowView.findViewById(R.id.numberOfDonations);
        textViewNumberOfDonations.setText(getContext().getString(R.string.number_of_donations) + ": " + String.valueOf(valuesDTO.get(position).getNumberOfDonationsCompleted()));
        textView.setText(valuesDTO.get(position).getNeighborhood());

        String firstLetter = String.valueOf(valuesDTO.get(position).getNeighborhood().charAt(0)).toUpperCase();

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(getItem(position));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);

        imageView.setImageDrawable(drawable);

        return rowView;

    }
}

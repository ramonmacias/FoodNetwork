package com.uab.es.cat.foodnetwork.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.R;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramonmacias on 28/11/15.
 */
public class DonationForTransportArrayAdapter extends ArrayAdapter<String> {

    private final List<String> values;
    private final Context context;
    private final List<DonationDTO> valuesDTO;
    public List<DonationDTO> selectedDonationDTO = new ArrayList<DonationDTO>();
    public ArrayList selectedIds = new ArrayList();

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
        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getContext());
        CacheDbHelper cacheDbHelper = new CacheDbHelper();

        LocationDTO locationDTO = new LocationDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(valuesDTO.get(position).getIdUser());
        locationDTO.setIdLocation(valuesDTO.get(position).getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
        View rowView = inflater.inflate(R.layout.list_of_donation_for_transport, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView textViewStateOfDonation = (TextView) rowView.findViewById(R.id.stateOfDonation);
        TextView textViewAddress = (TextView) rowView.findViewById(R.id.address);
        TextView textViewUser = (TextView) rowView.findViewById(R.id.user);
        TextView textViewPhoneNumber = (TextView) rowView.findViewById(R.id.phoneNumber);
        TextView textViewTotalWeight = (TextView) rowView.findViewById(R.id.totalWeight);
        TextView textViewTimeZone = (TextView) rowView.findViewById(R.id.timeZone);
        textView.setText(": " + String.valueOf(valuesDTO.get(position).getInsertDate()));
        String stateText = Constants.STATE.get(valuesDTO.get(position).getState());
        textViewStateOfDonation.setText(": " + stateText);

        if(locationDTO.getStreetName() != null && !"".equals(locationDTO.getStreetName())){
            String address = Utilities.getGoogleAddress(locationDTO.getStreetName(), locationDTO.getBuildingNumber(), locationDTO.getCity());
            textViewAddress.setText(": " + address);
        }else {
            textViewAddress.setText(": " + locationDTO.getCompleteAdrressFromGeocoder());
        }
        textViewUser.setText(": " + userDTO.getName());
        textViewPhoneNumber.setText(": " + String.valueOf(userDTO.getPhoneNumber()));
        textViewTotalWeight.setText(": " +  valuesDTO.get(position).getTotalWeight() + "kg");
        textViewTimeZone.setText(": " + valuesDTO.get(position).getInitialHour() + " a " + valuesDTO.get(position).getFinalHour());
        if(rowView != null){
            if (selectedIds.contains(position)) {
                rowView.setSelected(true);
                rowView.setPressed(true);
                rowView.setBackgroundColor(Color.parseColor("#C8E8FF"));
            } else {
                rowView.setSelected(false);
                rowView.setPressed(false);
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        return rowView;
    }

    public void toggleSelected(Integer position) {
        if(selectedIds.contains(position)) {
            selectedIds.remove(position);
            selectedDonationDTO.remove(valuesDTO.get(position));
        }
        else {
            selectedIds.add(position);
            selectedDonationDTO.add(valuesDTO.get(position));
        }
    }

}

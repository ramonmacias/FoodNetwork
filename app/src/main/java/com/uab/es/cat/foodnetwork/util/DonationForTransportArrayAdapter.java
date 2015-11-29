package com.uab.es.cat.foodnetwork.util;

import android.content.Context;
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
        textView.setText(getContext().getString(R.string.my_donation_of) + ": " + String.valueOf(valuesDTO.get(position).getInsertDate()));
        String stateText = Constants.STATE.get(valuesDTO.get(position).getState());
        textViewStateOfDonation.setText(getContext().getString(R.string.state) + ": " + stateText);

        if(locationDTO.getStreetName() != null && !"".equals(locationDTO.getStreetName())){
            String address = Utilities.getGoogleAddress(locationDTO.getStreetName(), locationDTO.getBuildingNumber(), locationDTO.getCity());
            textViewAddress.setText(getContext().getString(R.string.address) + ": " + address);
        }else {
            textViewAddress.setText(getContext().getString(R.string.address) + ": " + locationDTO.getCompleteAdrressFromGeocoder());
        }
        textViewUser.setText(getContext().getString(R.string.user) + ": " + userDTO.getName());
        textViewPhoneNumber.setText(getContext().getString(R.string.phone) + ": " + "607779462");
        textViewTotalWeight.setText(getContext().getString(R.string.total_weight) + ": " +  valuesDTO.get(position).getTotalWeight() + "kg");
        textViewTimeZone.setText(getContext().getString(R.string.time_zone) + ": " + valuesDTO.get(position).getInitialHour() + " a " + valuesDTO.get(position).getFinalHour());

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

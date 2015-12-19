package com.uab.es.cat.foodnetwork;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.UserSession;
import com.uab.es.cat.foodnetwork.util.Utilities;

/**
 * Created by ramonmacias on 19/12/15.
 */
public class ViewProfileFragment extends Fragment {

    private TextView textViewName;
    private TextView textViewLastName;
    private TextView textViewInitialHour;
    private TextView textViewFinalHour;
    private TextView textViewActionRadio;
    private TextView textViewTypeVehicle;
    private TextView textViewTmeZone;
    private TextView textViewActionRadioTitle;
    private TextView textViewTypeVehicleTitle;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);

        textViewName = (TextView) rootView.findViewById(R.id.name);
        textViewLastName = (TextView) rootView.findViewById(R.id.lastName);
        textViewInitialHour = (TextView) rootView.findViewById(R.id.initial_hour);
        textViewFinalHour = (TextView) rootView.findViewById(R.id.final_hour);
        textViewActionRadio = (TextView) rootView.findViewById(R.id.actionRadio);
        textViewTypeVehicle = (TextView) rootView.findViewById(R.id.type_vehicles);
        textViewTmeZone = (TextView) rootView.findViewById(R.id.timeZone);
        textViewActionRadioTitle = (TextView) rootView.findViewById(R.id.actionRadioTitle);
        textViewTypeVehicleTitle = (TextView) rootView.findViewById(R.id.type_vehicles_title);

        UserDTO userDTO = new UserDTO();

        long userId = UserSession.getInstance(getContext()).getUserId();
        String userType = UserSession.getInstance(getContext()).getUserType();

        userDTO.setIdUser(userId);

        FoodNetworkDbHelper mDbHelper = new FoodNetworkDbHelper(getContext());

        CacheDbHelper cacheDbHelper = new CacheDbHelper();
        userDTO = (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);


        long idLocation = userDTO.getIdLocation();
        if(idLocation != 0){
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setIdLocation(idLocation);
            locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);
            TextView textViewNeighborhood = (TextView) rootView.findViewById(R.id.neighborhood);
            TextView textViewDistrict = (TextView) rootView.findViewById(R.id.district);
            TextView textViewAddress = (TextView) rootView.findViewById(R.id.address);
            textViewNeighborhood.setText(locationDTO.getNeighborhood());
            textViewDistrict.setText(locationDTO.getDistrict());
            textViewAddress.setText(Utilities.getFullAddress(locationDTO.getStreetName(), locationDTO.getBuildingNumber(), locationDTO.getFloor(), locationDTO.getDoor()));
        }

        textViewName.setText(userDTO.getName());
        textViewLastName.setText(userDTO.getLastName());

        if (!"D".equals(userType)){
            textViewActionRadio.setText(String.valueOf(userDTO.getActionRadio()));
            textViewInitialHour.setText(userDTO.getInitialHour());
            textViewFinalHour.setText(userDTO.getFinalHour());
            textViewTypeVehicle.setText(userDTO.getTypeOfVehicle());
        }else {
            textViewActionRadio.setVisibility(View.GONE);
            textViewInitialHour.setVisibility(View.GONE);
            textViewFinalHour.setVisibility(View.GONE);
            textViewTypeVehicle.setVisibility(View.GONE);
            textViewTmeZone.setVisibility(View.GONE);
            textViewTypeVehicleTitle.setVisibility(View.GONE);
            textViewActionRadioTitle.setVisibility(View.GONE);
        }



        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

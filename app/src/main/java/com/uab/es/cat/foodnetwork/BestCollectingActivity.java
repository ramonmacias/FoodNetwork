package com.uab.es.cat.foodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;
import com.uab.es.cat.foodnetwork.util.Constants;
import com.uab.es.cat.foodnetwork.util.UserSession;
import com.uab.es.cat.foodnetwork.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class BestCollectingActivity extends AppCompatActivity {

    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private List<DonationDTO> donations;
    private List<DonationDTO> donationsAux;
    private UserDTO userDTO;
    private String finalHour;
    private String initialHour;
    private int actionRadio;
    private double latitudeProfile;
    private double longitudeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_collecting);

        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getApplicationContext());
        donationsAux = new ArrayList<DonationDTO>();

        userDTO = obtainUserInformation();
        donations = cacheDbHelper.getReadyAndCurrentDonations(mDbHelper);

        filterDonationsByUserInformation(userDTO);
    }

    private UserDTO obtainUserInformation(){
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(UserSession.getInstance(getApplicationContext()).getUserId());
        return (UserDTO) cacheDbHelper.getById(userDTO, mDbHelper);
    }

    private void filterDonationsByUserInformation(UserDTO userDTO){
        finalHour = userDTO.getFinalHour();
        initialHour = userDTO.getInitialHour();
        actionRadio = userDTO.getActionRadio();

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(userDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        latitudeProfile = Double.valueOf(locationDTO.getLatitude());
        longitudeProfile = Double.valueOf(locationDTO.getLongitude());

        boolean longitudeCriteria = false;
        boolean rangeOfHoursCriteria = false;

        for(int i = 0; i < donations.size(); i++){
            longitudeCriteria = validateLongitudeCriteria(donations.get(i));
            rangeOfHoursCriteria = validateRangeOfHoursCriteria(donations.get(i));

            if(longitudeCriteria && rangeOfHoursCriteria){
                donationsAux.add(donations.get(i));
            }
        }
        donations.clear();
        donations.addAll(donationsAux);
    }

    private boolean validateLongitudeCriteria(DonationDTO donationDTO){
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setIdLocation(donationDTO.getIdLocation());
        locationDTO = (LocationDTO) cacheDbHelper.getById(locationDTO, mDbHelper);

        int distance = Utilities.calculateDistanceBetweenTwoPoints(longitudeProfile, latitudeProfile, Double.valueOf(locationDTO.getLongitude()), Double.valueOf(locationDTO.getLatitude()));

        if(distance > actionRadio){
            return false;
        }else {
            return true;
        }
    }

    private boolean validateRangeOfHoursCriteria(DonationDTO donationDTO){

        int finalOrdinal = Constants.HOURS.get(donationDTO.getFinalHour());
        int initialOrdinal = Constants.HOURS.get(donationDTO.getInitialHour());

        return true;
    }

    private void algorithmToFindBestWay(){
        /*cargarDatos();
        Comparator cmp = new Comparator<Element>() {
            public int compare(Element x, Element y) {
                return (int) (x.getWeight() - y.getWeight());
            }
        };
        Collections.sort(almacen,cmp);  // ordena usando el comparador anterior
        Collections.reverse(almacen);   // reversa el orden de los elementos

        double pesoMochila=0;
        double distanciaAcomulada=0;
        int    posicion=0;
        while(pesoMochila<maximumWeight && posicion < almacen.size() && distanciaAcomulada<maximumDistance) {
            Element tmp = almacen.get(posicion);
            if((pesoMochila + tmp.getWeight() <= maximumWeight) && (distanciaAcomulada + tmp.getDistance() <= maximumDistance)) {
                mochila.add(tmp);
                pesoMochila+=tmp.getWeight();
                distanciaAcomulada += tmp.getDistance();
            }
            posicion++;
        }

        System.out.println("Peso maximo: " + maximumWeight);
        for(Element x : mochila){
            System.out.println(x.getName() + " Dis: " + x.getDistance() + " Weight: " + x.getWeight());
        }*/
    }
}

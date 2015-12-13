package com.uab.es.cat.foodnetwork.database;

import com.uab.es.cat.foodnetwork.dto.BaseDTO;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.FoodsDTO;
import com.uab.es.cat.foodnetwork.dto.RankingDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by ramonmacias on 18/10/15.
 */
public interface DatabaseHandler {

    public long insert(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);

    public void update(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);

    public void delete(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);

    public BaseDTO getById(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper);

    public List<FoodsDTO> getFoodsOfDonation(long idDonation, FoodNetworkDbHelper mDbHelper);

    public List<DonationDTO> getReadyAndCurrentDonations(FoodNetworkDbHelper mDbHelper);

    public List<DonationDTO> getDonationsByState(FoodNetworkDbHelper mDbHelper, int state);

    public List<UserDTO> getRankingUser(FoodNetworkDbHelper mDbHelper);

    public List<RankingDTO> getRankingDistricts(FoodNetworkDbHelper mDbHelper);

    public List<RankingDTO> getRankingNeighborhood(FoodNetworkDbHelper mDbHelper);
}

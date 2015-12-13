package com.uab.es.cat.foodnetwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ramonmacias on 12/12/15.
 */
public class UserRankingFragment extends Fragment {

    private ListView ItemsLst;
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;

    public UserRankingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_ranking_fragment, container, false);

        ItemsLst = (ListView) view.findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getRankingUsers());
        ItemsLst.setAdapter(adapter);

        return view;

    }

    private List<String> getRankingUsers(){
        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getContext());
        List<String> rankingOfUsers = new ArrayList<String>();

        List<UserDTO> rankingUsersDTO = cacheDbHelper.getRankingUser(mDbHelper);
        for(UserDTO userWithRanking : rankingUsersDTO){
            rankingOfUsers.add(userWithRanking.getName() + " " + userWithRanking.getLastName() + " " + getString(R.string.donations) + ": " + userWithRanking.getNumberOfDonationsCompleted());
        }

        return rankingOfUsers;
    }
}

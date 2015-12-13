package com.uab.es.cat.foodnetwork;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.RankingDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramonmacias on 12/12/15.
 */
public class DistrictRankingFragment extends Fragment{

    private ListView ItemsLst;
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;

    public DistrictRankingFragment() {
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
        View view = inflater.inflate(R.layout.district_ranking_fragment, container, false);

        ItemsLst = (ListView) view.findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getRankingDistrict());
        ItemsLst.setAdapter(adapter);

        return view;
    }

    private List<String> getRankingDistrict(){
        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getContext());
        List<String> rankingOfDistricts = new ArrayList<String>();

        List<RankingDTO> rankingDistrictsDTO = cacheDbHelper.getRankingDistricts(mDbHelper);
        for(RankingDTO districtWithRanking : rankingDistrictsDTO){
            rankingOfDistricts.add(districtWithRanking.getDistrict() + " " + getString(R.string.donations) + ": " + districtWithRanking.getNumberOfDonationsCompleted());
        }

        return rankingOfDistricts;

    }
}

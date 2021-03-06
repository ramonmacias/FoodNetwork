package com.uab.es.cat.foodnetwork;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uab.es.cat.foodnetwork.adapter.NeighborhoodRankingAdapter;
import com.uab.es.cat.foodnetwork.database.CacheDbHelper;
import com.uab.es.cat.foodnetwork.database.FoodNetworkDbHelper;
import com.uab.es.cat.foodnetwork.dto.RankingDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramonmacias on 12/12/15.
 */
public class NeighborhoodRankingFragment extends Fragment {

    private ListView ItemsLst;
    private CacheDbHelper cacheDbHelper;
    private FoodNetworkDbHelper mDbHelper;
    private List<RankingDTO> rankingNeighborhoodDTO;

    public NeighborhoodRankingFragment() {
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
        View view = inflater.inflate(R.layout.neighborhood_ranking_fragment, container, false);

        List<String> strings = getRankingNeihgborhood();

        ItemsLst = (ListView) view.findViewById(R.id.listview);

        NeighborhoodRankingAdapter adapter = new NeighborhoodRankingAdapter(getContext(), strings, rankingNeighborhoodDTO);
        ItemsLst.setAdapter(adapter);

        return view;
    }

    private List<String> getRankingNeihgborhood(){
        cacheDbHelper = new CacheDbHelper();
        mDbHelper = new FoodNetworkDbHelper(getContext());
        List<String> rankingOfNeighborhoods = new ArrayList<String>();

        rankingNeighborhoodDTO = cacheDbHelper.getRankingNeighborhood(mDbHelper);
        for(RankingDTO neighborhoodWithRanking : rankingNeighborhoodDTO){
            rankingOfNeighborhoods.add(neighborhoodWithRanking.getNeighborhood() + " " + getString(R.string.donations) + ": " + neighborhoodWithRanking.getNumberOfDonationsCompleted());
        }

        return rankingOfNeighborhoods;

    }
}

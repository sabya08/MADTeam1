package com.team1.RestaurantExpress;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by Sabya on 3/27/2015.
 */
public class ItemsTitleFragmentAdapter extends ItemsFragmentAdapter {

    public ItemsTitleFragmentAdapter(FragmentManager fm,Map<String,List<MenuItem>> menuMap) {
        super(fm,menuMap);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Log.d("Category in",ItemsFragmentAdapter.menuMap.keySet().toArray()[position % menuMap.size()].toString());
        return ItemsFragmentAdapter.menuMap.keySet().toArray()[position % menuMap.size()].toString();

    }
}

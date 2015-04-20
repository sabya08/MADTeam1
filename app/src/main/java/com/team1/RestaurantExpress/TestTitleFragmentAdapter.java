package com.team1.RestaurantExpress;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.List;
import java.util.Map;

class TestTitleFragmentAdapter extends TestFragmentAdapter {
    public TestTitleFragmentAdapter(FragmentManager fm,Map<String,List<MenuItem>> menuMap) {
        super(fm,menuMap);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("Title",TestFragmentAdapter.menuMap.keySet().toArray()[position % menuMap.size()].toString());
        return TestFragmentAdapter.menuMap.keySet().toArray()[position % menuMap.size()].toString();
    }
}
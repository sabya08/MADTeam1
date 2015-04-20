package com.team1.RestaurantExpress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TestFragmentAdapter extends FragmentPagerAdapter {
    //protected static final String[] CONTENT = new String[] { "Starters", "Indian", "Main Course", "Desserts"};
    //protected static final int[] XML = {R.xml.list_a,R.xml.list_b,R.xml.list_c,R.xml.list_d};

    protected static Map<String,List<MenuItem>> menuMap = new HashMap<String, List<MenuItem>>();
    //protected static Set<String> titleSet = new HashSet<String>();
    private int mCount = menuMap.size();


    public TestFragmentAdapter(FragmentManager fm,Map<String,List<MenuItem>> menuMap)
    {
        super(fm);
        this.menuMap =menuMap;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % menuMap.size();
        String category = menuMap.keySet().toArray()[index].toString();
        Log.d("Category", category);
        List<MenuItem> listMenu = menuMap.get(category);
        return TestFragment.newInstance(category,listMenu);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    public void set(int count)
    {
        mCount = count;
        notifyDataSetChanged();
    }

    /*public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }*/
}
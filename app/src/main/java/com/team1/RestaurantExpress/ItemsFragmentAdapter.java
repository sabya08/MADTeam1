package com.team1.RestaurantExpress;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sabya on 3/27/2015.
 */
 class ItemsFragmentAdapter extends FragmentPagerAdapter {

    protected static Map<String,List<MenuItem>> menuMap = new HashMap<String, List<MenuItem>>();
    //protected static Set<String> titleSet = new HashSet<String>();
    private int mCount = menuMap.size();




    public ItemsFragmentAdapter(FragmentManager fm,Map<String,List<MenuItem>> menuMap)
    {
        super(fm);
        this.menuMap =menuMap;
        /*List<MenuItem> menuItemList = new ArrayList<MenuItem>();
        menuMap.clear();
        MenuItem item = new MenuItem();
        item.setItem_Active(true);
        item.setItem_Category("Starter");
        item.setItem_Description("Kebab");
        item.setItem_Name("Chicken Kebab");
        item.setItem_ID(1);
        item.setItem_Price(20.00);
        menuItemList.add(item);
        menuMap.put("Starter", menuItemList);

        menuItemList = new ArrayList<MenuItem>();
        item = new MenuItem();
        item.setItem_Active(true);
        item.setItem_Category("Indian");
        item.setItem_Description("Kebab");
        item.setItem_Name("Veg Kebab");
        item.setItem_ID(2);
        item.setItem_Price(202.00);
        menuItemList.add(item);
        menuMap.put("Indian",menuItemList);*/

        mCount = menuMap.size();
    }
    @Override
    public Fragment getItem(int position) {
        int index = position % menuMap.size();
        String category = menuMap.keySet().toArray()[index].toString();
        Log.d("Category",category );
        List<MenuItem> listMenu = menuMap.get(category);
        return ItemFragment.newInstance(category,listMenu);
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



}

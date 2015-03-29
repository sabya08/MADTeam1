package com.team1.RestaurantExpress;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.view.MenuItem;

import com.viewpagerindicator.PageIndicator;

import java.util.Random;

/**
 * Created by Sabya on 3/28/2015.
 */
public class BaseItemFragment extends FragmentActivity {

    private static final Random RANDOM = new Random();

    ItemsFragmentAdapter itemAdapter;
    ViewPager itemPager;
    PageIndicator itemIndicator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

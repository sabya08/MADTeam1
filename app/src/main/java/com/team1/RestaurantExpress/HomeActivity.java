package com.team1.RestaurantExpress;
/**
 * Created by Sabya on 3/20/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class HomeActivity extends BaseSampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(0);
        setContentView(R.layout.activity_home);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void takeOrder(View v)
    {
        Intent intent = new Intent(HomeActivity.this,TableActivity.class);
        startActivity(intent);
    }

    public void viewUpdateMenu(View v)
    {
        Intent intent = new Intent(HomeActivity.this,UpdateItems.class);
        startActivity(intent);
    }
}

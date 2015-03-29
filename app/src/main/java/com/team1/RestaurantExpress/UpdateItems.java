package com.team1.RestaurantExpress;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateItems extends BaseItemFragment implements ItemFragment.OnFragmentInteractionListener,
                                                              MainFrag.callListener, UpdateItemDetailsFragment.callListener{

    FragmentManager fm;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);


        //takes care of left slider
        new LoadAllMenuItems().execute();

        // this sets up the right bar fragment and connects MainFrag to it.
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.update_right_frag_container);
//        Log.d("CreateUpdate",fragment.toString());
        if (fragment == null) {
            Log.d("inside f==null","inside");
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.update_right_frag_container, new UpdateItemDetailsFragment());
            ft.commit();
            //f = (MainFrag)fm.findFragmentById(R.id.right_frag_container);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_items, menu);
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

    UpdateItemDetailsFragment f;
    @Override
    public void onFragmentInteraction(com.team1.RestaurantExpress.MenuItem item)
    {
        Log.d("ItemClicked",item.getItem_Name());
        FragmentManager fm = getSupportFragmentManager();
        Log.d("Fragment",Integer.toString(fm.getBackStackEntryCount()));
        if(fm.getBackStackEntryCount()==0){
            f = (UpdateItemDetailsFragment)fm.findFragmentById(R.id.update_right_frag_container);

            Log.d("Fragment",f.toString());
        }
        else{
            fm.popBackStack();
            fm.executePendingTransactions();
        }

        if(f!=null && item!=null)
        {
            f.showItem(item);
            Log.d("insideIF","yes");
        }


    }




    /*
    to load all the data created an async class.
    * */
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    //private static String url_all_products = "http://10.0.0.6/android_connect/get_all_items.php";



    // products JSONArray
    JSONArray products = null;

    @Override
    public void onButtonClick(BaseItem abc) {

    }

    class LoadAllMenuItems extends AsyncTask<String, String, String> {

        Map<String,List<com.team1.RestaurantExpress.MenuItem>> menuMap = new HashMap<String, List<com.team1.RestaurantExpress.MenuItem>>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateItems.this);
            pDialog.setMessage("Loading menu items. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.get_all_items, "GET", params);

            // Check your log cat for JSON reponse
            // Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Config.TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(Config.TAG_PRODUCTS);

                    // looping through All Products
                    ArrayList<com.team1.RestaurantExpress.MenuItem> menuItemList = new ArrayList<com.team1.RestaurantExpress.MenuItem>();

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        com.team1.RestaurantExpress.MenuItem mitem = new com.team1.RestaurantExpress.MenuItem();

                        mitem.setItem_ID(Integer.parseInt(c.getString(Config.TAG_PID)));
                        mitem.setItem_Name(c.getString(Config.TAG_NAME));
                        mitem.setItem_Price(c.getDouble(Config.TAG_PRICE));
                        mitem.setItem_Description(c.getString(Config.TAG_DESCRIPTION));
                        mitem.setItem_Category(c.getString(Config.TAG_CATEGORY));
                        menuItemList.add(mitem);

                        /*// creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(Config.TAG_PID, id);
                        map.put(Config.TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);*/
                    }
                    for(com.team1.RestaurantExpress.MenuItem menuItem: menuItemList)
                    {
                        if(menuMap.get(menuItem.getItem_Category())!= null)
                        {
                            menuMap.get(menuItem.getItem_Category()).add(menuItem);
                        }
                        else
                        {
                            List<com.team1.RestaurantExpress.MenuItem> newList = new ArrayList<com.team1.RestaurantExpress.MenuItem>();
                            newList.add(menuItem);
                            menuMap.put(menuItem.getItem_Category(),newList);
                        }
                    }

                } else {
                    // no products found
                    // Launch Add New product Activity
                    /*Intent i = new Intent(getApplicationContext(),
                            AddItems.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    //ItemsFragmentAdapter.menuMap = menuMap;
                    //notify();
                    itemAdapter = new ItemsTitleFragmentAdapter(getSupportFragmentManager(),menuMap);
                    itemPager = (ViewPager)findViewById(R.id.updatePager);
                    itemPager.setAdapter(itemAdapter);
                    TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.updateIndicator);
                    indicator.setViewPager(itemPager);
                    indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);
                    itemIndicator = indicator;
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    /*ListAdapter adapter = new SimpleAdapter(
                            ViewItems.this, productsList,
                            R.layout.list_items, new String[]{TAG_PID,
                            TAG_NAME},
                            new int[]{R.id.itemid, R.id.itemname});
                    // updating listview

                    setListAdapter(adapter);*/
                }
            });

        }

    }
}

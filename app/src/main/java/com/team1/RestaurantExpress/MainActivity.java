package com.team1.RestaurantExpress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.team1.RestaurantExpress.volley.AppController;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseSampleActivity implements TestFragment.fragListener,
																MainFrag.callListener,
																DetailFrag.delListener,
																DetailFrag.numListener,
                                                                DetailFrag.confirmOrderListener{
	FragmentManager fm;
	Fragment fragment;
	ArrayList<MenuItem> myitemlist = new ArrayList<MenuItem>();
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private List<MenuItem> confirmOrderList =  new ArrayList<MenuItem>();
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    int tableID;

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    //private static String url_all_products = "http://10.0.0.6/android_connect/get_all_items.php";



    // products JSONArray
    JSONArray products = null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(0);
        setContentView(R.layout.main_activity);
        tableID = getIntent().getIntExtra("tableID",0);
        new LoadAllMenuItems().execute();
	       
        // this gets the swiping menu fragment set up and running
        //mAdapter = new TestTitleFragmentAdapter(getSupportFragmentManager(),);
        //mPager = (ViewPager)findViewById(R.id.pager);
        //mPager.setAdapter(mAdapter);
        //TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        //indicator.setViewPager(mPager);
        //indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
        //mIndicator = indicator;
        
        
        // this sets up the right bar fragment and connects MainFrag to it.
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.right_frag_container); 
        
        if (fragment == null) {

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.right_frag_container, new MainFrag());
            ft.commit(); 
                               
        }


        new LoadOrderedItems().execute();
            
        //in case a listener is required that activates on every page change. This listener can send the messages to the rightFrag
       /* mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this, "Changed to page " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/
    }
	

	MainFrag f;
	@Override
	public void onItemClick(MenuItem item) {
        Log.d("ItemClicked", item.getItem_Name());
        FragmentManager fm = getSupportFragmentManager();
        Log.d("Fragment",Integer.toString(fm.getBackStackEntryCount()));
        if(fm.getBackStackEntryCount()==0){
            f = (MainFrag)fm.findFragmentById(R.id.right_frag_container);

            //Log.d("Fragment",f.toString());
        }
        else{
            fm.popBackStack();
            fm.executePendingTransactions();
        }

        if(f!=null && item!=null)
        {
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            f.showItem(item,imageLoader);
            Log.d("insideIF","yes");
        }
	}
	
	/**
	 * This interface switches the right fragment activity with the details fragment.
	 *
	 */
	@Override
	public void onButtonClick(MenuItem item) {
		// TODO Auto-generated method stub
		if(item!=null)
			myitemlist.add(item);
		

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.right_frag_container, new DetailFrag(myitemlist));
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * This interface is coming from the delete fragment and is supposed to handle the deletion of the listview
	 */
	@Override
	public void onDelClick(int i) {

	}

	@Override
	public void onNumChange(int i,int p) {

		MenuItem bit = myitemlist.get(p);
		bit.setItem_Qty(i);
		myitemlist.set(p, bit);
	}

    @Override
    public void onConfirmClick(){
        //confirmOrderList = menuItemList;
        new SaveOrder().execute();
    }

    class LoadAllMenuItems extends AsyncTask<String, String, String> {

        Map<String,List<MenuItem>> menuMap = new HashMap<String, List<MenuItem>>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading menu items. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
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
                    //TestFragmentAdapter.menuMap = menuMap;
                    //notify();
                    mAdapter = new TestTitleFragmentAdapter(getSupportFragmentManager(),menuMap);
                    mPager = (ViewPager)findViewById(R.id.pager);
                    mPager.setAdapter(mAdapter);
                    TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
                    indicator.setViewPager(mPager);
                    indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);
                    mIndicator = indicator;
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

    class SaveOrder extends AsyncTask<String, String, String> {

        //Map<String,List<MenuItem>> menuMap = new HashMap<String, List<MenuItem>>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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
            JSONArray jsonArray = new JSONArray();
            //JSONObject confirmItemsJson = new JSONObject();
            for (int i=0; i < myitemlist.size(); i++)
            {
                jsonArray.put(myitemlist.get(i).getJSONObject());
            }

            params.add(new BasicNameValuePair("confirmedOrder",jsonArray.toString()));
            params.add(new BasicNameValuePair("tableID",Integer.toString(tableID)));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.save_confirmed_order, "POST", params);

            // Check your log cat for JSON response
            Log.d("All Products: ", json.toString());



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
                    Toast.makeText(MainActivity.this,"Order placed successfuly",Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

        }

    }

    class LoadOrderedItems extends AsyncTask<String, String, String> {

        //Map<String,List<MenuItem>> menuMap = new HashMap<String, List<MenuItem>>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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


            params.add(new BasicNameValuePair("Table_ID",Integer.toString(tableID)));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.get_ordered_items_table, "GET", params);

            // Check your log cat for JSON reponse
            // Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Config.TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(Config.TAG_PRODUCTS);
                    Log.d("Ordered Items",products.toString());
                    // looping through All Products
                    ArrayList<com.team1.RestaurantExpress.MenuItem> menuItemList = new ArrayList<com.team1.RestaurantExpress.MenuItem>();

                    for (int i = 0; i < products.length(); i++) {
                        Log.d("In For Order","In for loop");
                        JSONObject c = products.getJSONObject(i);
                        Log.d("Order Items:","After");
                        // Storing each json item in variable
                        com.team1.RestaurantExpress.MenuItem mitem = new com.team1.RestaurantExpress.MenuItem();

                        mitem.setItem_ID(Integer.parseInt(c.getString(Config.TAG_PID)));
                        mitem.setItem_Name(c.getString(Config.TAG_NAME));
                        mitem.setItem_Price(c.getDouble(Config.TAG_PRICE));
                        mitem.setItem_Description(c.getString(Config.TAG_DESCRIPTION));
                        mitem.setItem_Category(c.getString(Config.TAG_CATEGORY));
                        mitem.setItem_Qty(Integer.parseInt(c.getString("Quantity")));
                        menuItemList.add(mitem);

                        /*// creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(Config.TAG_PID, id);
                        map.put(Config.TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);*/
                    }
                    myitemlist = menuItemList;
                    Log.d("MyItemListCount",Integer.toString(myitemlist.size()));

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
                    //notifyAll();

                }
            });

        }

    }
	
}
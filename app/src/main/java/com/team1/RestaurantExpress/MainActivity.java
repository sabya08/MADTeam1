package com.team1.RestaurantExpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.team1.RestaurantExpress.volley.AppController;
import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseSampleActivity implements TestFragment.fragListener,
																MainFrag.callListener,
																DetailFrag.delListener,
																DetailFrag.numListener,
                                                                DetailFrag.confirmOrderListener,
                                                                DetailFrag.placeOrderListener,
                                                                DetailFrag.cancelOrderListener
{
	FragmentManager fm;
	Fragment fragment;
	ArrayList<MenuItem> myitemlist = new ArrayList<MenuItem>();
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private List<MenuItem> confirmOrderList =  new ArrayList<MenuItem>();
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    int tableID;
    final Context context = this;
    ArrayList<HashMap<String, String>> productsList;
    private String emailID;
    double sum = 0;
    int minutes = 0;

    //Email Validator --
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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
	       
        new LoadOrderedItems().execute();

        pattern = Pattern.compile(EMAIL_PATTERN);
    }
	

	MainFrag f;
	@Override
	public void onItemClick(MenuItem item) {
        Log.d("ItemClicked", item.getItem_Name());
        FragmentManager fm = getSupportFragmentManager();
        Log.d("Fragment",Integer.toString(fm.getBackStackEntryCount()));
        if(fm.getBackStackEntryCount()==0){


            //Log.d("Fragment",f.toString());
            f = (MainFrag)fm.findFragmentById(R.id.right_frag_container);
        }
        else{
            fm.popBackStack();
            fm.executePendingTransactions();
        }

        if(f != null && item != null)
        {
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            f.showItem(item, imageLoader);
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
		//add logic to increase the quantity of existing items in the  myitemList

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.right_frag_container, new DetailFrag(myitemlist,minutes));
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
    public void onPlaceClick(){
        //confirmOrderList = menuItemList;
        if(myitemlist.size()>0)
            new SaveOrder().execute();


        else
        {
            Log.d("OnConfirmClick","Inside On Confirm Click");
            Toast.makeText(this,"Please add atleast one item to the order!!",Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onConfirmClick(double sum) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);
        this.sum = sum;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setMessage("Enter Email :");
        alertDialogBuilder.setCancelable(false);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        // set dialog message
        alertDialogBuilder
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                emailID = userInput.getText().toString();
                                if (validate(emailID)) {
                                    new ConfirmOrder().execute();
                                    new SendEmailReceipt().execute();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email entered is invalid!!.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        //AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialogBuilder.show();

    }


    @Override
    public void onCancelClick()
    {
        if(myitemlist.size()>0)
            new ConfirmOrder().execute();
    }
    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

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
                    mIndicator.notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
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
            pDialog.setMessage("Saving menu items. Please wait...");
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
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    finish();
                    startActivity(intent);
                    //new LoadOrderedItems().execute();
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
                    String dateString = json.getString(Config.TAG_TIME);
                    Log.d("Date",dateString);
                    minutes = getTimeInMinutes(dateString);
                    Log.d("Time Elapsed",Integer.toString(minutes));
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
                        mitem.setTimeElapsed(minutes);
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
                    fm = getSupportFragmentManager();
                    fragment = fm.findFragmentById(R.id.right_frag_container);

                    if (fragment == null) {

                        /*FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.right_frag_container, new MainFrag());
                        ft.commit(); */

                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.right_frag_container, new MainFrag());
                        //ft.addToBackStack(null);
                        ft.commit();

                    }

                }
            });



        }
        private int getTimeInMinutes(String dateString)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = null;
            try {
                d = df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long time = d.getTime();
            SimpleDateFormat dateformat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            try {
                date = dateformat.parse(date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("Current time",date.toString());
            long timeDifference = d.getTime() - date.getTime();
            Long lminutes = 59 - timeDifference/(1000*60)%60;
            int minutes = Integer.parseInt(lminutes.toString());
            return minutes;
        }

    }

    class ConfirmOrder extends AsyncTask<String, String, String> {

        //Map<String,List<MenuItem>> menuMap = new HashMap<String, List<MenuItem>>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {



           /* super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Finalizing Order. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONArray jsonArray = new JSONArray();
            //JSONObject confirmItemsJson = new JSONObject();
            /*for (int i=0; i < myitemlist.size(); i++)
            {
                jsonArray.put(myitemlist.get(i).getJSONObject());
            }*/

            //params.add(new BasicNameValuePair("confirmedOrder",jsonArray.toString()));
            params.add(new BasicNameValuePair("Table_ID",Integer.toString(tableID)));
            Log.d("Table_ID",Integer.toString(tableID));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.finalize_confirmed_order, "POST", params);

            // Check your log cat for JSON response
            Log.d("Order Finalized ", json.toString());

            //sendEmail();

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

                    Toast.makeText(MainActivity.this,"Order Processed Successfully",Toast.LENGTH_SHORT).show();
                    //sendEmail();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    finish();
                    startActivity(intent);
                    //new LoadOrderedItems().execute();
                }
            });

        }



    }

    class SendEmailReceipt extends AsyncTask<String, String, String> {

        boolean emailSent = false;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {

        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            Mail m = new Mail("restaurantexpressnew@gmail.com", "RExpress123");

            String[] toArr = {emailID};

            m.setTo(toArr);
            m.setFrom("customercare@restaurantexpress.com");
            m.setSubject("Thanks for Dining!!");
            m.setBody("Hello Customer,\n We really appreciate your presence at our restaurant.\n Your total bill is $"+sum+".\n " +
                    "Please leave us your valuable feedback at restaurantexpressnew@gmail.com.\n\nThanks,\nCustomer Care");

            try {
                //m.addAttachment("/sdcard/filelocation");

                if(m.send()) {
                    Log.d("MailApp", "Email Sent");
                    emailSent = true;
                    //Toast.makeText(MainActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("MailApp", "Email not Sent");
                    emailSent = false;
                    //Toast.makeText(MainActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("MailApp", "Could not send email", e);
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
                    if(emailSent)
                        Toast.makeText(MainActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();

                    //new LoadOrderedItems().execute();
                }
            });

        }



    }


}
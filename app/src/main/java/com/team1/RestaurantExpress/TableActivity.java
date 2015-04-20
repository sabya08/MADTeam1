package com.team1.RestaurantExpress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.MailTo;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class TableActivity extends Activity {

    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    JSONArray tableList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        new LoadTableStatus().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table, menu);
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

    public void tableClicked(View v)
    {
        int tableId = v.getId();
        Intent intent = new Intent(TableActivity.this,MainActivity.class);
        intent.putExtra("tableID",tableId);
        switch (tableId)
        {
            case R.id.tb1:
                Toast.makeText(this,"Table 1 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",1);

                break;
            case R.id.tb2:
                Toast.makeText(this,"Table 2 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",2);
                break;
            case R.id.tb3:
                Toast.makeText(this,"Table 3 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",3);
                break;
            case R.id.tb4:
                Toast.makeText(this,"Table 4 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",4);
                break;
            case R.id.tb5:
                Toast.makeText(this,"Table 5 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",5);
                break;
            case R.id.tb6:
                Toast.makeText(this,"Table 6 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",6);
                break;
            case R.id.tb7:
                Toast.makeText(this,"Table 7 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",7);
                break;
            case R.id.tb8:
                Toast.makeText(this,"Table 8 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",8);
                break;
            case R.id.tb9:
                Toast.makeText(this,"Table 9 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",9);
                break;
            case R.id.tb10:
                Toast.makeText(this,"Table 10 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",10);
                break;
            case R.id.tb11:
                Toast.makeText(this,"Table 11 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",11);
                break;
            case R.id.tb12:
                Toast.makeText(this,"Table 12 clicked!!",Toast.LENGTH_SHORT).show();
                intent.putExtra("tableID",12);
                break;
        }
        startActivity(intent);

    }

    //Load status of tables
    class LoadTableStatus extends AsyncTask<String, String, String> {

        List<Integer> tableIdList = new ArrayList<Integer>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TableActivity.this);
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
            JSONObject json = jParser.makeHttpRequest(Config.get_table_status, "GET", params);

            // Check your log cat for JSON reponse
            // Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Config.TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    tableList = json.getJSONArray(Config.TAG_TABLE_LIST);
                    for(int i =0; i< tableList.length();i++)
                    {
                        JSONObject c = tableList.getJSONObject(i);
                        Log.d("TableStatus","Inside for loop, "+Integer.toString(c.getInt(Config.TAG_TABLE_ID)));
                        tableIdList.add(c.getInt(Config.TAG_TABLE_ID));
                    }

                    //finish();
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
                    for(Integer i: tableIdList)
                    {
                        Log.d("TableStatus",Integer.toString(i));
                        switch(i)
                        {
                            case 1:
                                findViewById(R.id.tb1).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 2:
                                findViewById(R.id.tb2).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 3:
                                findViewById(R.id.tb3).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 4:
                                findViewById(R.id.tb4).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 5:
                                findViewById(R.id.tb5).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 6:
                                findViewById(R.id.tb6).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 7:
                                findViewById(R.id.tb7).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 8:
                                findViewById(R.id.tb8).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 9:
                                findViewById(R.id.tb9).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 10:
                                findViewById(R.id.tb10).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 11:
                                findViewById(R.id.tb11).setBackgroundResource(R.drawable.butt_other);
                                break;
                            case 12:
                                findViewById(R.id.tb12).setBackgroundResource(R.drawable.butt_other);
                                break;

                        }
                    }

                }
            });

        }

    }
}

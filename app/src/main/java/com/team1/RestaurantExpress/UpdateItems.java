package com.team1.RestaurantExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.team1.RestaurantExpress.volley.AppController;
import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateItems extends BaseItemFragment implements ItemFragment.OnFragmentInteractionListener,
                                                               UpdateItemDetailsFragment.callListener, AddItemsFragment.OnAddItemFragmentInteractionListener{

    FragmentManager fm;
    Fragment fragment;
    int REQUEST_CAMERA = 0;
    int SELECT_FILE =1;
    com.team1.RestaurantExpress.MenuItem menuItem = new com.team1.RestaurantExpress.MenuItem();
    ImageView ivImage;
    JSONParser jsonParser = new JSONParser();
    static ArrayList<String> categoryList = new ArrayList<String>();
    File file = null;
    String selectedImageURI =  null;

    boolean uploadImageFlag = false;
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
        new LoadAllCategories().execute();
        hideSoftKeyboard();

    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            f.showItem(item,imageLoader);
            Log.d("insideIF","yes");
        }


    }




    /*
    to load all the data created an async class.
    * */
    private ProgressDialog pDialog;
    private ProgressDialog LDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    //private static String url_all_products = "http://10.0.0.6/android_connect/get_all_items.php";



    // products JSONArray
    JSONArray products = null;

    @Override
    public void onButtonClick(com.team1.RestaurantExpress.MenuItem menuItem,String text,View v) {
        this.menuItem = menuItem;
     if(text.equals(Config.BUTTON_UPDATE))
     {
        new SaveProductDetails().execute();
     }
     else if(text.equals(Config.BUTTON_DELETE))
     {
        new DeleteProduct().execute();
     }

    }

    @Override
    public void onAddButtonClick() {
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT);
    }

    AddItemsFragment addFragment;
    public void addNewItem(View v)
    {
        Log.d("ItemClicked", "In addNewItem");

        //Fragment newFragment = CountingFragment.newInstance(mStackLevel);
        addFragment = new AddItemsFragment();

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.update_right_frag_container, addFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();




        /*FragmentManager fm = getSupportFragmentManager();
        Log.d("Fragment",Integer.toString(fm.getBackStackEntryCount()));
        if(fm.getBackStackEntryCount()==0){


            fm.beginTransaction().add(R.id.update_right_frag_container,addFragment).commit();
            //addFragment = (AddItemsFragment)fm.findFragmentById(R.id.update_right_frag_container);

//            Log.d("Fragment",f.toString());
        }
        else{
            fm.popBackStack();
            fm.executePendingTransactions();
        }*/


    }


    com.team1.RestaurantExpress.MenuItem addMenuItem = new com.team1.RestaurantExpress.MenuItem();
    @Override
    public void onAddItemFragmentInteraction(com.team1.RestaurantExpress.MenuItem menuItem) {
        Log.d("Update Items","Clicked on Add Items");
        this.addMenuItem = menuItem;
        new AddNewItem().execute();

    }

    public void selectImage(View v)
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        ivImage = (ImageView)findViewById(R.id.add_item_image);
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateItems.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, REQUEST_CAMERA);
                    uploadImageFlag = true;
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                    uploadImageFlag = true;
                } else if (items[item].equals("Cancel")) {
                    uploadImageFlag = false;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);
                    selectedImageURI = f.getPath();
                    // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    ivImage.setImageBitmap(bm);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, UpdateItems.this);
                selectedImageURI = tempPath;
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                ivImage.setImageBitmap(bm);
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String uploadFile(int menuID) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.IMAGE_UPLOAD_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            // publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            File sourceFile = new File(selectedImageURI);

            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));
            Log.d("AddMenu ID",Integer.toString(menuID));
            entity.addPart("menuid",new StringBody(Integer.toString(menuID)));
            // Extra parameters if you want to pass to server
            entity.addPart("website",
                    new StringBody("www.sabyasachi.com"));
            entity.addPart("email", new StringBody("sabyaasachi@gmail.com"));

            //totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    class LoadAllMenuItems extends AsyncTask<String, String, String> {

        Map<String,List<com.team1.RestaurantExpress.MenuItem>> menuMap = new HashMap<String, List<com.team1.RestaurantExpress.MenuItem>>();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LDialog = new ProgressDialog(UpdateItems.this);
            LDialog.setMessage("Loading menu items. Please wait...");
            LDialog.setIndeterminate(false);
            LDialog.setCancelable(false);
            LDialog.show();
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
           // pDialog.dismiss();
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
                    LDialog.dismiss();
                }
            });


        }

    }

    /**
     * Background Async Task to  Save product Details
     * */
     ProgressDialog sDialog;
     class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sDialog = new ProgressDialog(UpdateItems.this);
            sDialog.setMessage("Saving menu item ...");
            sDialog.setIndeterminate(false);
            sDialog.setCancelable(true);
            sDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            Log.d("Update","Update background method called");

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Config.TAG_PID, Integer.toString(menuItem.getItem_ID())));
            params.add(new BasicNameValuePair(Config.TAG_NAME, menuItem.getItem_Name()));
            params.add(new BasicNameValuePair(Config.TAG_PRICE, Double.toString(menuItem.getItem_Price())));
            params.add(new BasicNameValuePair(Config.TAG_DESCRIPTION, menuItem.getItem_Description()));
            params.add(new BasicNameValuePair(Config.TAG_CATEGORY, menuItem.getItem_Category()));
            params.add(new BasicNameValuePair(Config.TAG_ITEMACTIVE,"1"));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(Config.UPDATE_MENU_ITEM,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(Config.TAG_SUCCESS);

                if (success == 1 && uploadImageFlag) {
                    Log.d("Item Updated","Success");
                    String responseString = uploadFile(menuItem.getItem_ID());
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);

                } else {
                    // failed to update product
                    Log.d("Failed:","Item not updated");
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
            // dismiss the dialog once product updated
            sDialog.dismiss();
            finish();
            Intent intent = new Intent(UpdateItems.this,UpdateItems.class);
            startActivity(intent);

        }
    }

    ProgressDialog dDialog;
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dDialog = new ProgressDialog(UpdateItems.this);
            dDialog.setMessage("Deleting Item...");
            dDialog.setIndeterminate(false);
            dDialog.setCancelable(true);
            dDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", Integer.toString(menuItem.getItem_ID())));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        Config.url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Menu Item", json.toString());

                // json success tag
                success = json.getInt(Config.TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once product deleted
            dDialog.dismiss();
            Intent intent = new Intent(UpdateItems.this,UpdateItems.class);
            startActivity(intent);

        }



    }

    ProgressDialog cDialog;
    class LoadAllCategories extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cDialog = new ProgressDialog(UpdateItems.this);
            cDialog.setMessage("Loading all categories. Please wait...");
            cDialog.setIndeterminate(false);
            cDialog.setCancelable(false);
            cDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.get_all_categories, "GET", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Config.TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(Config.TAG_CATEGORIES);

                    // looping through All Categories

                    categoryList.clear();
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable

                        String category = new String(c.getString(Config.TAG_CATEGORY_NAME));
                        Log.d("CategoryDB",category);
                        categoryList.add(category);
                        Log.d("CategoryList",Integer.toString(categoryList.size()));

                    }




                } else {
                    //add code to handle no categories
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
            cDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {


                }
            });

        }

    }
    ProgressDialog aDialog;
    class AddNewItem extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        int menuID;
         @Override
        protected void onPreExecute() {
            super.onPreExecute();
             aDialog = new ProgressDialog(UpdateItems.this);
             aDialog.setMessage("Adding Menu Item..");
             aDialog.setIndeterminate(false);
             aDialog.setCancelable(true);
             aDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = addMenuItem.getItem_Name();
            String price = Double.toString(addMenuItem.getItem_Price());
            String description = addMenuItem.getItem_Description();
            String category = addMenuItem.getItem_Category();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("category",category));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(Config.url_add_menu_item,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(Config.TAG_SUCCESS);
                menuID = json.getInt(Config.TAG_MENU_ID);

                if (success == 1) {
                    String responseString = uploadFile(menuID);
                    // successfully created product
                    //Intent i = new Intent(getApplicationContext(), ViewItems.class);
                    //startActivity(i);

                    //getFragmentManager().popBackStackImmediate();

                    // closing this screen

                    finish();
                } else {
                    // failed to create product
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
            // dismiss the dialog once done
            aDialog.dismiss();
            Toast.makeText(getBaseContext(),"Menu Item added Successfully!!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateItems.this,UpdateItems.class);
            startActivity(intent);
        }

    }
}

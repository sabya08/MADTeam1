package com.team1.RestaurantExpress;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.IOException;
import java.io.InputStream;


/**
 * This fragment is for the RightSideBar.

 *
 */
public class MainFrag extends Fragment  {
	private Button button_add;
	private ImageView imview;
	private TextView tv,tv_descrip,price;
	DetailFrag det_frag = new DetailFrag();
	
	private callListener mCallback;
	private Button button_view;
    private MenuItem menuItem;


	 // Container Activity must implement this interface
	    public interface callListener {
	        public void onButtonClick(MenuItem menuItem);
	    }
	    
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            mCallback = (callListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnHeadlineSelectedListener");
	        }
	    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("MainFrag","In onCreateView");
        View view = inflater.inflate(R.layout.mainpagefrag, container, false);

        button_add = (Button) view.findViewById(R.id.fragment_button_add_order);
        button_view = (Button) view.findViewById(R.id.fragment_button_view_order);
        button_add.setVisibility(View.GONE);
        //button_view.setVisibility(View.GONE);
        imview = (ImageView) view.findViewById(R.id.test_image);
        tv = (TextView) view.findViewById(R.id.title);
        tv_descrip = (TextView) view.findViewById(R.id.description);
        button_add.setOnClickListener(new OnClickListener() {
        	
        	/**
        	 * This function is getting called whenever the button on the right side is called.
        	 * a new fragment is created and  the xml resource of the currently selected item sent to it..
        	 */
            @Override
            public void onClick(View v) {
                    /*FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.right_frag_container, det_frag);
                    ft.addToBackStack(null);
                    ft.commit();*/
            	if(menuItem!=null)
                    menuItem.setItem_Qty(1);
            		mCallback.onButtonClick(menuItem);
                    
            }
            
        });
        
        button_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            		mCallback.onButtonClick(null);
                    
            }
            
        });


        
        return view;
    }
    //public XmlResourceParser xItem;
   // public BaseItem item=null;
    public void update(XmlResourceParser xmlItem)
    {
    	String s = xmlItem.getAttributeValue(null,"pic");
        Log.d("ImagePath",s);
    	String t = xmlItem.getAttributeValue(null,"title");
    	String d = xmlItem.getAttributeValue(null,"description");
    	String p = xmlItem.getAttributeValue(null,"price");
    	//item = new BaseItem(s,t,d,p);
    	loadDataFromAsset(s);
    	
    	tv.setText(t);
    	tv_descrip.setText(d);
    	//button.setText(s);
    	imview.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein));
    	tv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left));
        button_add.setVisibility(View.VISIBLE);
        button_view.setVisibility(View.VISIBLE);
    }

    public void showItem(MenuItem item,ImageLoader imageLoader)
    {
        menuItem = item;
        tv.setText(item.getItem_Name());
        tv_descrip.setText(item.getItem_Description());
        button_add.setVisibility(View.VISIBLE);
        button_view.setVisibility(View.VISIBLE);
        tv.setEnabled(false);
        tv_descrip.setEnabled(false);


        // If you are using normal ImageView
        imageLoader.get(Config.base_url_image + menuItem.getItem_ID() + ".jpg", new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Image", "Image Load Error: " + error.getMessage());
                imview.setImageDrawable(getResources().getDrawable(R.drawable.noimage));
                imview.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein));
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imview.setImageBitmap(response.getBitmap());
                    imview.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein));
                }
            }
        });
        tv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left));

    }
    
    public void loadDataFromAsset(String s) {
    	// load image
    	try {
	    	// get input stream
	    	InputStream ims = getActivity().getAssets().open(s);
	    	// load image as Drawable
	    	Drawable d = Drawable.createFromStream(ims, null);
	    	// set image to ImageView
	    	imview.setImageDrawable(d);
	    	//button.setText("OK");
	    	
    	}
    	catch(IOException ex) {
    		button_add.setText("ERRROR?");
    		return;
    	}
    
    }
}
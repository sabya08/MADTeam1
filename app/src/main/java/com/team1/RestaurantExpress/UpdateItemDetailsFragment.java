package com.team1.RestaurantExpress;

import android.app.Activity;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamanland.fab.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sabya on 3/29/2015.
 */
public class UpdateItemDetailsFragment extends Fragment {
    private Button button_edit;
    private ImageView imview;
    private TextView tv,tv_descrip,price;
    //DetailFrag det_frag = new DetailFrag();

    private callListener mCallback;
    private Button button_delete;
    private Button button_camera;
    private MenuItem menuItem;

    private FloatingActionButton addItemButton;
    // Container Activity must implement this interface
    public interface callListener {
        public void onButtonClick(MenuItem menuItem, String buttonText, View v);
        public void onAddButtonClick();
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

        View view = inflater.inflate(R.layout.updateitemsfrag, container, false);

        button_edit = (Button) view.findViewById(R.id.fragment_button_edit);
        button_delete = (Button) view.findViewById(R.id.fragment_button_delete);
        button_camera = (Button) view.findViewById(R.id.btncamera);
        addItemButton = (FloatingActionButton) view.findViewById(R.id.add_item);
        button_edit.setText("Edit");
        button_delete.setText("Delete");
        button_edit.setVisibility(View.GONE);
        button_delete.setVisibility(View.GONE);

        price = (TextView) view.findViewById(R.id.item_price);
        price.setVisibility(View.GONE);
        imview = (ImageView) view.findViewById(R.id.item_image);
        tv = (TextView) view.findViewById(R.id.item_title);
        tv.setEnabled(false);
        tv_descrip = (TextView) view.findViewById(R.id.item_description);
        tv_descrip.setEnabled(false);

        button_edit.setOnClickListener(new View.OnClickListener() {

            /**
             * This function is getting called whenever the button on the right side is called.
             * a new fragment is created and  the xml resource of the currently selected item sent to it..
             */
            @Override
            public void onClick(View v) {
                   /* FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.right_frag_container, det_frag);
                    ft.addToBackStack(null);
                    ft.commit(); */

                String buttonText = button_edit.getText().toString();
                clickedEdit(v);

                menuItem.setItem_Description(tv_descrip.getText().toString());
                menuItem.setItem_Price(Double.parseDouble(price.getText().toString()));
                menuItem.setItem_Name(tv.getText().toString());

                if (menuItem != null)

                    mCallback.onButtonClick(menuItem,buttonText,v);

            }

        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedDelete(v);



            }

        });

        /*addItemButton.setOnClickListener((v) -> {
            mCallback.onAddButtonClick();
        });*/

        return view;
    }
    //public XmlResourceParser xItem;
    public BaseItem item=null;

    private void onAddItem(View v)
    {
        mCallback.onAddButtonClick();
    }

    public void showItem(MenuItem item, ImageLoader imageLoader)
    {

        menuItem = item;
        tv.setText(item.getItem_Name());
        tv_descrip.setText(item.getItem_Description());
        price.setText(item.getItem_Price().toString());
        price.setVisibility(View.VISIBLE);
        button_edit.setVisibility(View.VISIBLE);
        button_delete.setVisibility(View.VISIBLE);
        tv.setEnabled(false);
        tv_descrip.setEnabled(false);
        price.setEnabled(false);


// If you are using normal ImageView
        imageLoader.get(Config.base_url_image+menuItem.getItem_ID()+".jpg", new ImageLoader.ImageListener() {

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
            button_edit.setText("ERRROR?");
            return;
        }

    }

    public void clickedEdit(View v)
    {
        Log.d("Clicked","Inside Edit");
        if(button_edit.getText()==Config.BUTTON_EDIT)
        {
            setPageContentEnabled(true);

        }
        else
        {
            setPageContentEnabled(false);
        }
    }

    public void clickedDelete(final View v)
    {
        Log.d("Clicked","Inside Delete");
        if(button_delete.getText()==Config.BUTTON_DELETE)
        {
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirm_delete)
                    .setMessage(R.string.really_delete)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            //YourClass.this.finish();
                            mCallback.onButtonClick(menuItem,button_delete.getText().toString(),v);
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
        else
        {
            setPageContentEnabled(false);
        }
    }

    public void setPageContentEnabled(boolean enabled)
    {
        if(enabled)
        {
            tv.setEnabled(enabled);
            button_delete.setText(Config.BUTTON_CANCEL);
            button_edit.setText(Config.BUTTON_UPDATE);
            tv_descrip.setEnabled(enabled);
            button_camera.setVisibility(View.VISIBLE);
            price.setEnabled(enabled);
        }
        else
        {
            tv.setEnabled(enabled);
            button_delete.setText(Config.BUTTON_DELETE);
            button_edit.setText(Config.BUTTON_EDIT);
            tv_descrip.setEnabled(enabled);
            button_camera.setVisibility(View.GONE);
            price.setEnabled(enabled);
        }
    }



}

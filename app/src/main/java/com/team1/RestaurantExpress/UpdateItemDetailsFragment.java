package com.team1.RestaurantExpress;

import android.app.Activity;
import android.content.res.XmlResourceParser;
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

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sabya on 3/29/2015.
 */
public class UpdateItemDetailsFragment extends Fragment {
    private Button button_edit;
    private ImageView imview;
    private TextView tv,tv_descrip;
    //DetailFrag det_frag = new DetailFrag();

    private callListener mCallback;
    private Button button_delete;

    // Container Activity must implement this interface
    public interface callListener {
        public void onButtonClick(BaseItem abc);
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

        imview = (ImageView) view.findViewById(R.id.item_image);
        tv = (TextView) view.findViewById(R.id.item_title);
        tv_descrip = (TextView) view.findViewById(R.id.item_description);
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
                if (item != null)
                    mCallback.onButtonClick(item);

            }

        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onButtonClick(null);

            }

        });

        return view;
    }
    //public XmlResourceParser xItem;
    public BaseItem item=null;


    public void showItem(MenuItem item)
    {
        tv.setText(item.getItem_Name());
        tv_descrip.setText(item.getItem_Description());
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

}

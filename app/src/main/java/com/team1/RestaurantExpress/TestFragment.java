package com.team1.RestaurantExpress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * the LEFT side panel implementation.
 * reads an XML file.
 *
 *
 */
public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    static TextView tv;
    fragListener mCallback;

    private String category;
    private List<MenuItem> menuItemList;

    public interface fragListener {
        public void onItemClick(MenuItem item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (fragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    public static TestFragment newInstance(String category, List<MenuItem> menuList) {
        TestFragment fragment = new TestFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(category);
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.category = builder.toString();
        fragment.menuItemList = menuList;
        return fragment;
    }

    private String mContent = "???";
    private int xml_select = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView view = new ListView(getActivity());
        String[] from = new String[]{"str", "price"};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        for (MenuItem menuItem : menuItemList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("str", menuItem.getItem_Name());
            map.put("price", menuItem.getItem_Price().toString());
            items.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, android.R.layout.simple_list_item_2, from, to);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Send the event to the host activity
                mCallback.onItemClick(menuItemList.get(position));
            }
        });

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
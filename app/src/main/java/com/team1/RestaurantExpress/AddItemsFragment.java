package com.team1.RestaurantExpress;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import javax.xml.datatype.Duration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemsFragment.OnAddItemFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Button addButton;
    private Button cancelButton;
    private EditText itemName;
    private EditText itemDescription;
    private EditText itemPrice;
    private ImageView imageView;

    Spinner spinner = null;
    private OnAddItemFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemsFragment newInstance(String param1, String param2) {
        AddItemsFragment fragment = new AddItemsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AddItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,UpdateItems.categoryList);
        View v = inflater.inflate(R.layout.fragment_add_items, container, false);
        spinner = (Spinner)v.findViewById(R.id.category_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        itemName = (EditText)v.findViewById(R.id.add_item_title);
        itemDescription = (EditText)v.findViewById(R.id.add_item_description);
        itemPrice = (EditText)v.findViewById(R.id.add_item_price);
        itemPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        imageView = (ImageView)v.findViewById(R.id.add_item_image);
        addButton =(Button)v.findViewById(R.id.fragment_button_add);
        cancelButton = (Button)v.findViewById(R.id.fragment_button_cancel);
        addButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                clickedAdd(v);
            }

        });
        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                clickedCancel(v);
            }

        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAddItemFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddItemFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddItemFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onAddItemFragmentInteraction(MenuItem menuItem);
    }

    public void clickedAdd(View v)
    {
        Log.d("In Add Items","Hello");
        MenuItem menuItem = new MenuItem();
        Log.d("ItemName",itemName.getText().toString());
        if(itemName.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Enter Item Name", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("ItemDescription",itemDescription.getText().toString());
        if(itemDescription.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Enter Item Description",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("ItemPrice",itemPrice.getText().toString());
        if(itemPrice.getText().toString().isEmpty())
        {
            itemPrice.setText("0");
            Toast.makeText(getActivity(),"Enter Item Price",Toast.LENGTH_SHORT).show();
            return;
        }
        if(imageView.getDrawable() == null)
        {
            Toast.makeText(getActivity(),"Image no selected",Toast.LENGTH_SHORT).show();
            return;
        }

        menuItem.setItem_Name(itemName.getText().toString());
        menuItem.setItem_Description(itemDescription.getText().toString());
        menuItem.setItem_Price(Double.parseDouble(itemPrice.getText().toString()));
        menuItem.setItem_Category(spinner.getSelectedItem().toString());

        if(mListener!=null)
        {
            mListener.onAddItemFragmentInteraction(menuItem);
        }

    }

    public void clickedCancel(View v)
    {
        Log.d("In Cancel","Cancel");
    }
}

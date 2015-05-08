package com.team1.RestaurantExpress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * basically this class is for the second page or the listing of all selected items for the user
 *
 *
 *
 *
 */
public class DetailFrag extends Fragment {
	public Button button_back,button_menu, button_place,button_cancel,button_confirm;
    public TextView timeElapsed;
	public ListView lv;
	ArrayList<MenuItem> myitemlist;
    ArrayList<MenuItem> dbitemlist;
	public int n=0;
	public String strng="default";
	private SimpleAdapter arrayAdapter;
	List<Map<String, String>> items;
	delListener mCallback;
	numListener numCallback;
    placeOrderListener onPlaceCallback;
    double sum = 0;
    int minutes =0;
    confirmOrderListener onConfirmOderCallback;
    cancelOrderListener onCancelOrderCallback;
	private static final int ID_UP     = 1;
	private static final int ID_DOWN   = 2;
	
	 // Container Activity must implement these interfaces
		// this listener is to inform the parent activity that an item has been deleted from the menu
	    public interface delListener {
	        public void onDelClick(int i);
	    }
	    
		// this listener is to inform the parent activity that an item's number 
	    public interface numListener {
	        public void onNumChange(int i,int p);
	    }

        public interface placeOrderListener {
            public void onPlaceClick(ArrayList<MenuItem> changedList);
        }

        public interface confirmOrderListener{
            public void onConfirmClick(double sum);
        }

        public interface cancelOrderListener{
            public void onCancelClick();
        }
	    
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            mCallback = (delListener) activity;
	            numCallback = (numListener) activity;
                onPlaceCallback = (placeOrderListener)activity;
                onConfirmOderCallback = (confirmOrderListener)activity;
                onCancelOrderCallback = (cancelOrderListener)activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement Listeners!!");
	        }
	    }
	
	    // 2 constructsors 
	public DetailFrag()
	{
		
	}

    DetailFrag(ArrayList<MenuItem> s, int minutes, ArrayList<MenuItem> db)
	{
		myitemlist=s;
        dbitemlist = db;
        this.minutes = minutes;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // first prepare the list view to accept the items
        final View view = inflater.inflate(R.layout.detail, container, false);
        button_back = (Button) view.findViewById(R.id.button1);
        button_cancel = (Button) view.findViewById(R.id.button4);
        button_menu = (Button) view.findViewById(R.id.button2);
        button_place = (Button) view.findViewById(R.id.button3);
        button_confirm = (Button) view.findViewById(R.id.button5);
        timeElapsed = (TextView) view.findViewById(R.id.time_elapsed);
        ActionItem nextItem = new ActionItem(ID_DOWN, "Next", getResources().getDrawable(R.drawable.menu_down_arrow));
        ActionItem prevItem = new ActionItem(ID_UP, "Prev", getResources().getDrawable(R.drawable.menu_up_arrow));

        prevItem.setSticky(true);
        nextItem.setSticky(true);
        final QuickAction quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);

        //add action items into QuickAction
        quickAction.addActionItem(nextItem);
        quickAction.addActionItem(prevItem);

        //Set listener for action item clicked
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                ActionItem actionItem = quickAction.getActionItem(pos);

                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_UP) {
                    Toast.makeText(getActivity(), "Some action can be customized here.", Toast.LENGTH_SHORT).show();
                } else if (actionId == ID_DOWN) {
                    Toast.makeText(getActivity(), "Clear/Menu/Call Waiter/ anything/..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lv = (ListView) view.findViewById(R.id.listview1);
        timeElapsed.setText(minutes + " min elapsed");
        if (dbitemlist.size() <= 0) {
            button_place.setText("Place Order");
            button_confirm.setVisibility(View.GONE);
            //timeElapsed.setVisibility(View.GONE);
        } else {
            button_place.setText("Update Order");
            button_confirm.setVisibility(View.VISIBLE);
        }

        String[] arr1 = new String[myitemlist.size()];
        String[] arr2 = new String[myitemlist.size()];
        String[] arr3 = new String[myitemlist.size()];
        for (int i = 0; i < myitemlist.size(); i++) {
            arr1[i] = myitemlist.get(i).getItem_Name();
            arr2[i] = myitemlist.get(i).getItem_Price().toString();
            arr3[i] = "Qty: " + Integer.toString(myitemlist.get(i).getItem_Qty());
        }

        String[] from = new String[]{"str", "price", "numbs"};
        int[] to = new int[]{R.id.textp1, R.id.textp2, R.id.textp3};
        items = new ArrayList<Map<String, String>>();

        for (int i = 0; i < arr1.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("str", String.format("%s", arr1[i]));
            map.put("price", String.format("%s", arr2[i]));
            map.put("numbs", String.format("%s", arr3[i]));
            items.add(map);
        }

        arrayAdapter = new SimpleAdapter(getActivity(), items, R.layout.menulist, from, to);

        lv.setAdapter(arrayAdapter);


        // this is the single click listener..
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View mview,
                                    final int position, long id) {
                Context mContext = getActivity();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dialog, (ViewGroup) mview.findViewById(R.id.layout_root));
                final NumberPicker np = (NumberPicker) layout.findViewById(R.id.numberPicker1);
                np.setMaxValue(10);
                np.setMinValue(1);
                np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(layout);
                builder.setCancelable(true);
                //builder.setIcon(R.drawable.dialog_question);
                builder.setTitle("Change Number of items.");
                builder.setInverseBackgroundForced(true);
                // this is the dialog element that implements the changing number shz
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        numCallback.onNumChange(np.getValue(), position);
                        Map<String, String> mss = items.get(position);
                        mss.put("numbs", String.format("Qty: %s", Integer.toString(np.getValue())));
                        items.set(position, mss);
                        arrayAdapter.notifyDataSetChanged();
                        setbill(view);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        // long click to delete code
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            boolean flag = false;
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                //	Toast.makeText(getActivity(), Integer.toString(pos), Toast.LENGTH_SHORT).show();

                    items.remove(pos);
                    myitemlist.remove(pos);
                    arrayAdapter.notifyDataSetChanged();
                    setbill(view);
                    mCallback.onDelClick(pos);
                    Toast.makeText(getActivity(),"Item Removed!!",Toast.LENGTH_SHORT).show();
                    return true;

            }
        });


        // GTF back
        button_back.setOnClickListener(new OnClickListener() {
        
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
            
        });
        
     // GTF back
        button_menu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                quickAction.show(v);
            }

        });
        setbill(view);
        
        button_place.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MenuItem> changedList = null;
                if(button_place.getText().toString().equals("Update Order"))
                {
                    changedList = findChangedList(myitemlist,dbitemlist);
                }
                onPlaceCallback.onPlaceClick(changedList);
            }
        });


        button_confirm.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onConfirmOderCallback.onConfirmClick(sum);
            }
        });

        button_cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {


                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.confirm_cancel)
                        .setMessage(R.string.really_cancel)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (dbitemlist.size() > 0)
                                    onCancelOrderCallback.onCancelClick();
                                else {
                                    items.clear();
                                    myitemlist.clear();
                                    arrayAdapter.notifyDataSetChanged();
                                    sum =0;
                                    setbill(view);
                                }
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
        });


        return view;
	}

    public ArrayList<MenuItem> findChangedList(ArrayList<MenuItem> mylist, ArrayList<MenuItem> dbList)
    {
        ArrayList<MenuItem> changedList = new ArrayList<MenuItem>();
        boolean itemFound  = false;
        boolean itemQtyChanged = false;
        for(MenuItem mitem : mylist)
        {
            itemFound =false;
            itemQtyChanged =false;
            MenuItem menuItem =  new MenuItem();
            for(MenuItem ditem : dbList)
            {
                if(mitem.getItem_ID() == ditem.getItem_ID())
                {
                    if(mitem.getItem_Qty() != ditem.getItem_Qty())
                        itemQtyChanged = true;
                    itemFound = true;
                    break;
                }
            }
            if(itemFound && itemQtyChanged)
            {
                menuItem =mitem;
                menuItem.setAction_Required("UPDATE");
                changedList.add(menuItem);
            }
            else if(!itemFound)
            {
                menuItem = mitem;
                menuItem.setAction_Required("ADD");
                changedList.add(menuItem);
            }

        }
        for(MenuItem ditem : dbList)
        {
            itemFound =false;
            itemQtyChanged =false;
            MenuItem menuItem =  new MenuItem();
            for(MenuItem mitem : mylist)
            {
                  if(mitem.getItem_ID() == ditem.getItem_ID())
                  {
                      itemFound = true;
                      break;
                  }
            }
            if(!itemFound)
            {
                menuItem = ditem;
                menuItem.setAction_Required("DELETE");
                changedList.add(ditem);
            }
        }


        return changedList;
    }
	
	public void setbill(View view)
	{
		TextView child = (TextView)view.findViewById(R.id.bill_amount);
//        /Log.d();

        for(int i=0;i<myitemlist.size();i++)
        {
        	double p = myitemlist.get(i).getItem_Price();
        	sum+= myitemlist.get(i).getItem_Qty()*p;
        }
        if(child!=null){

        	child.setText(Double.toString(Math.round(sum)));
        	Log.d("SET Bill","Bill is set");
        }
        	
	}
}

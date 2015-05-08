package com.team1.RestaurantExpress;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sabya on 3/27/2015.
 */
public class MenuItem {

    public int Item_ID;
    public String Item_Name;
    public String Item_Description;
    public Double Item_Price;
    public boolean Item_Active;
    public String Item_Category;
    public String Item_Image_Path;
    public int Item_Qty;
    public int TimeElapsed;

    public int getItem_ID() {
        return Item_ID;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public String getItem_Description() {
        return Item_Description;
    }

    public Double getItem_Price() {
        return Item_Price;
    }

    public boolean isItem_Active() {
        return Item_Active;
    }

    public String getItem_Category() {
        return Item_Category;
    }

    public void setItem_ID(int item_ID) {
        Item_ID = item_ID;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public void setItem_Description(String item_Description) {
        Item_Description = item_Description;
    }

    public void setItem_Price(Double item_Price) {
        Item_Price = item_Price;
    }

    public void setItem_Active(boolean item_Active) {
        Item_Active = item_Active;
    }

    public void setItem_Category(String item_Category) {
        Item_Category = item_Category;
    }

    public String getItem_Image_Path()
    {
        return Item_Image_Path;
    }

    public void setItem_Image_Path(String image_path)
    {
        this.Item_Image_Path = image_path;
    }

    public void setItem_Qty(int qty) { this.Item_Qty = qty; }

    public int getItem_Qty() { return this.Item_Qty; }

    public int getTimeElapsed(){ return this.TimeElapsed; }

    public void setTimeElapsed(int timeElapsed) {this.TimeElapsed = timeElapsed; }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ItemID", Item_ID);
            obj.put("ItemName", Item_Name);
            obj.put("ItemCategory", Item_Category);
            obj.put("ItemActive",Item_Active);
            obj.put("Quantity",Item_Qty);
            obj.put("ItemPrice",Item_Price);
            obj.put("ItemDescription",Item_Description);
        } catch (JSONException e) {
            //trace("DefaultListItem.toString JSONException: "+e.getMessage());
        }
        return obj;
    }

}

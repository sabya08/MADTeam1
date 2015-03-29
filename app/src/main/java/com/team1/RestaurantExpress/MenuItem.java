package com.team1.RestaurantExpress;

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
}

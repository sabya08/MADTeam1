package com.team1.RestaurantExpress;

/**
 * Created by Sabya on 3/19/2015.
 */
public class Config {
    public static final String IMAGE_UPLOAD_URL = "http://54.148.34.170/android_connect/image_upload.php";
    public static final String IMAGE_DIRECTORY_NAME = "Android Image Upload";
    public static final String IMAGE_DOWNLOAD_URL_BASE = "http://54.148.34.170/android_connect/uploads/IMG_";
    public static final String UPDATE_MENU_ITEM = "http://54.148.34.170/android_connect/update_item.php";

    public static String get_all_items = "http://54.148.34.170/android_connect/get_all_items.php";
    public static String url_delete_product = "http://54.148.34.170/android_connect/delete_item.php";
    public static String get_all_categories = "http://54.148.34.170/android_connect/get_all_categories.php";
    public static String url_add_menu_item = "http://54.148.34.170/android_connect/create_menu_item.php";
    public static String base_url_image = "http://54.148.34.170/android_connect/uploads/IMG_";
    public static String save_confirmed_order = "http://54.148.34.170/android_connect/save_confirmed_order.php";
    public static String get_table_status = "http://54.148.34.170/android_connect/get_table_status.php";
    public static String get_ordered_items_table = "http://54.148.34.170/android_connect/get_ordered_items_table.php";

    // JSON Node names

    public static final String TAG_ITEMACTIVE = "Item_Active";
    public static final String TAG_PID = "Item_ID";
    public static final String TAG_NAME = "Item_Name";
    public static final String TAG_PRICE = "Item_Price";
    public static final String TAG_DESCRIPTION = "Item_Description";
    public static final String TAG_CATEGORY = "Category";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_PRODUCTS = "products";
    public static final String TAG_TABLE_LIST = "table_list";
    public static final String TAG_CATEGORIES = "categories";
    public static final String TAG_CATEGORY_NAME = "Category_Name";
    public static final String TAG_TABLE_ID = "Table_ID";
    //Update Items page constant
    public static final String BUTTON_EDIT = "Edit";
    public static final String BUTTON_DELETE = "Delete";
    public static final String BUTTON_UPDATE = "Update";
    public static final String BUTTON_CANCEL = "Cancel";

}

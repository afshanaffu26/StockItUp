package com.example.stockitup.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class has all the constants of the app defined.
 */
public class AppConstants {

    /**
     * Application name
     */
    public static final String APP_NAME = "StockItUp";

    /**
     * the key of the map is category name
     * the value of the map is the category document id
     */
    public static Map<String, String> CATEGORIES_MAP = new HashMap<String, String>();

    /**
     * the key of the map is offer name
     * the value of the map is the offer value
     */
    public static Map<String, String> OFFERS_MAP = new HashMap<String, String>();

    /**
     * the customer care number for support
     */
    public static String CUSTOMER_CARE_NUMBER;

    /**
     * the toll free number for support
     */
    public static String TOLL_FREE_NUMBER;

    /**
     * firestore collection called Categories
     */
    public static final String CATEGORY_COLLECTION = "Categories";

    /**
     * firestore collection called Essentials
     */
    public static final String ESSENTIALS_COLLECTION = "Essentials";

    /**
     * firestore collection called Offers
     */
    public static final String OFFERS_COLLECTION = "Offers";

    /**
     * firestore collection called AppSupport
     */
    public static final String APP_SUPPORT_COLLECTION = "AppSupport";

    /**
     * firestore collection called FAQ
     */
    public static final String FAQ_COLLECTION = "FAQ";

    /**
     * firestore collection called Items
     */
    public static final String ITEMS_COLLECTION_DOCUMENT = "Items";

    /**
     * firestore collection called Address
     */
    public static final String ADDRESS_COLLECTION = "Address";

    /**
     * firestore collection called Cart
     */
    public static final String CART_COLLECTION = "Cart";

    /**
     * firestore collection called Orders
     */
    public static final String ORDERS_COLLECTION = "Orders";

    /**
     * firestore collection called AllOrders
     */
    public static final String ORDERS_COLLECTION_DOCUMENT = "AllOrders";

    /**
     * firestore collection called Users
     */
    public static final String USER_COLLECTION = "Users";

    /**
     * Admin email id
     */
    public static final String ADMIN_EMAIL = "teamstockitup@gmail.com";

    /**
     * Admin gmail app password
     */
    public static final String ADMIN_PASSWORD = "gvzungbsuznhniux";

}
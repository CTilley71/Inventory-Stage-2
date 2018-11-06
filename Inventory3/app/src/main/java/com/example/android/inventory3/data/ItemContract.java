package com.example.android.inventory3.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
/**
 * API Contract for the Inventory app.
 */
public final class ItemContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ItemContract() {}
    /**
     * Inner class that defines constant values for the items database table.
     * Each entry in the table represents a single item.
     */

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventory3";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.inventory3/items/ is a valid path for
     * looking at inventory data.
     */
    public static final String PATH_ITEMS = "inventory";

    public static final class ItemEntry implements BaseColumns {

        /**
         * The content URI to access the inventory data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath( BASE_CONTENT_URI, PATH_ITEMS );

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;


        /**
         * Name of database table for items
         */
        public final static String TABLE_NAME = "items";
        /**
         * Unique ID number for the item (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;
        /**
         * Name of the product.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";
        /**
         * price of the item.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRICE = "price";
        /**
         * quantity of the product
         * <p>
         * Type:TEXT
         */
        public final static String COLUMN_QUANTITY = "quantity";
        /**
         * supplier of the item.
         * <p>
         * The only possible values are {@link #SUPPLIER_IBM}, {@link #SUPPLIER_GOOGLE},
         * or {@link #SUPPLIER_BAXTER}.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        /**
         * supplier phone number.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";
        /**
         * Possible values for the supplier name for the item.
         */
        public static final int SUPPLIER_IBM = 0;
        public static final int SUPPLIER_GOOGLE = 1;
        public static final int SUPPLIER_BAXTER = 2;


        /**
         * Returns whether or not the given supplier_name is {@link #SUPPLIER_IBM}, {@link #SUPPLIER_GOOGLE },
         * or {@link #SUPPLIER_BAXTER}.
         */
        public static boolean isValidSupplier(int supplier_name) {
            if (supplier_name == SUPPLIER_IBM || supplier_name == SUPPLIER_GOOGLE || supplier_name == SUPPLIER_BAXTER) {
                return true;
            }
            return false;
        }
    }
}


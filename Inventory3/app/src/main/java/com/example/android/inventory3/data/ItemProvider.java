package com.example.android.inventory3.data;

import android.content.ClipData;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.Provider;

/**
 * {@link ContentProvider} for Inventory app.
 */

public class ItemProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ItemProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int ITEM = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int ITEM_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher( UriMatcher.NO_MATCH );

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI( ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEM );

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI( ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEM_ID );
    }

    /**
     * Database helper object
     */
    private ItemDbHelper dbHelper;


    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        dbHelper = new ItemDbHelper( getContext() );
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;


        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match( uri );
        switch (match) {
            case ITEM:
                // For the ITEMS code, query the items table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the items table.
                cursor = database.query( ItemContract.ItemEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder );
                break;

            case ITEM_ID:
                // For the ITEMS_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.inventory3/items/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf( ContentUris.parseId( uri ) )};

                // This will perform a query on the Items table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query( ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder );
                break;
            default:
                throw new IllegalArgumentException( "Cannot query unknown URI " + uri );
        }
        return cursor;

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match( uri );
        switch (match) {
            case ITEM:
                return insertItem( uri, contentValues );
            default:
                throw new IllegalArgumentException( "Insertion is not supported for " + uri );
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertItem(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString( ItemContract.ItemEntry.COLUMN_PRODUCT_NAME );
        if (name == null) {
            throw new IllegalArgumentException( "Item requires a name" );
        }

               // If the quantity is provided, check that it's greater than or equal to 0
        Integer quantity = values.getAsInteger( ItemContract.ItemEntry.COLUMN_QUANTITY );
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException( "Item requires valid count" );
        }

        //Any price is valid (including null, in case price varies)

        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        // Insert the new pet with the given values
        long id = database.insert( ItemContract.ItemEntry.TABLE_NAME, null, values );
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e( LOG_TAG, "Failed to insert row for " + uri );
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId( uri, id );
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match( uri );
        switch (match) {
            case ITEM:
                return updateItem( uri, contentValues, selection, selectionArgs );
            case ITEM_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf( ContentUris.parseId( uri ) )};
                return updateItem( uri, contentValues, selection, selectionArgs );
            default:
                throw new IllegalArgumentException( "Update is not supported for " + uri );
        }
    }


    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey( ItemContract.ItemEntry.COLUMN_PRODUCT_NAME )) {
            String name = values.getAsString( ItemContract.ItemEntry.COLUMN_PRODUCT_NAME );
            if (name == null) {
                throw new IllegalArgumentException( "Item requires a name" );
            }
        }

        // If the {@link PetEntry#COLUMN_SUPPLIER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(ItemContract.ItemEntry.COLUMN_SUPPLIER_NAME)) {
            Integer gender = values.getAsInteger(ItemContract.ItemEntry.COLUMN_SUPPLIER_NAME);
            if (gender == null || !ItemContract.ItemEntry.isValidSupplier(gender)) {
                throw new IllegalArgumentException("Item requires valid supplier name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(ItemContract.ItemEntry.COLUMN_QUANTITY)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(ItemContract.ItemEntry.COLUMN_QUANTITY);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Item requires valid Quantity");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEM:
                // Delete all rows that match the selection and selection args
                return database.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
            case ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEM:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}




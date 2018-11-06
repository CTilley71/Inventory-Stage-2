package com.example.android.inventory3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventory3.data.ItemContract;
import com.example.android.inventory3.data.ItemCursorAdapter;
import com.example.android.inventory3.data.ItemDbHelper;
import com.example.android.inventory3.data.ItemContract.ItemEntry;

import java.util.List;

public class CatalogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_catalog );



        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent( CatalogActivity.this, EditorActivity.class );
                                        startActivity( intent );
                                    }
                                }
        );

        // Setup FAB to open EditorActivity
        FloatingActionButton search = (FloatingActionButton) findViewById( R.id.search );
        search.setOnClickListener( new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent( CatalogActivity.this, ViewItemActivity.class );
                                           startActivity( intent );
                                       }
                                   }
        );

        // Setup FAB to open EditorActivity
        FloatingActionButton edit = (FloatingActionButton) findViewById( R.id.edit );
        edit.setOnClickListener( new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         Intent intent = new Intent( CatalogActivity.this, EditItemActivity.class );
                                         startActivity( intent );
                                     }
                                 }
        );
    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        //define columns to display
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_PRODUCT_NAME,
                ItemContract.ItemEntry.COLUMN_PRICE,
                ItemContract.ItemEntry.COLUMN_QUANTITY
        };


        // Perform a query on the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to access the inventory data.
        Cursor cursor = getContentResolver().query(
                ItemContract.ItemEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null);

         //ceate a listView
        ListView itemListView = (ListView) findViewById(R.id.list );

        //set up an adapter or each item in the list
        ItemCursorAdapter adapter = new ItemCursorAdapter( this, cursor );

        //attach adapter to list view
        itemListView.setAdapter( adapter );


    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertItem() {
        // Create a ContentValues object where column names are the keys,
        // and Inventory attributes for one item
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_PRODUCT_NAME, "Lenovo 360");
        values.put(ItemEntry.COLUMN_PRICE, "35");
        values.put(ItemEntry.COLUMN_QUANTITY, 7);

        // Insert a new row for Lenovo 360 into the provider using the ContentResolver.
        // Use the {@link ItemEntry#CONTENT_URI} to indicate that we want to insert
        // into the items database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


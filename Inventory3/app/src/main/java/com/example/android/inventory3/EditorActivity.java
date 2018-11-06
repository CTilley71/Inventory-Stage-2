package com.example.android.inventory3;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventory3.data.ItemContract;
import com.example.android.inventory3.data.ItemDbHelper;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {


    public static final String LOG_TAG = ItemDbHelper.class.getSimpleName();

    /** EditText field to enter the items's name */
    private EditText ProductNameEditText;

    /** EditText field to enter the items' price*/
    private EditText PriceEditText;

    /** EditText field to enter the item's quantity */
    private EditText QuantityEditText;

    //EditText field to enter the supplier
    private EditText SupplierName;

    /** EditText field to enter the supplier Phone Number */
    private EditText SupplierPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        ProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        PriceEditText = (EditText) findViewById(R.id.edit_price);
        QuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        SupplierName = (EditText) findViewById(R.id.edit_supplier_name);
        SupplierPhone = (EditText) findViewById(R.id.edit_supplier_phone);
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save item to database
                insertItem();

                Log.e(LOG_TAG, "activity opens");
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertItem(){
        String productNameString = ProductNameEditText.getText().toString();
        String priceString =  PriceEditText.getText().toString();
        String quantityString = QuantityEditText.getText().toString();
        int quantity = Integer.parseInt(quantityString);
        String supplierName = SupplierName.getText().toString();
        String supplierPhoneString = SupplierPhone.getText().toString();
        int supplierPhone = Integer.parseInt(supplierPhoneString);

        // Create database helper
        ItemDbHelper DbHelper = new ItemDbHelper(this);
        // Gets the database in write mode
        SQLiteDatabase db = DbHelper.getWritableDatabase();
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put( ItemContract.ItemEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put( ItemContract.ItemEntry.COLUMN_PRICE, priceString);
        values.put( ItemContract.ItemEntry.COLUMN_QUANTITY, quantity);
        values.put( ItemContract.ItemEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put( ItemContract.ItemEntry.COLUMN_SUPPLIER_PHONE, supplierPhone);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert( ItemContract.ItemEntry.TABLE_NAME, null, values);
        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }


}


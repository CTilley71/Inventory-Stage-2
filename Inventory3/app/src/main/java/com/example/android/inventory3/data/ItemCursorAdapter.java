package com.example.android.inventory3.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory3.R;

/**
 * {@link ItemCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of inventory data as its data source. This adapter knows
 * how to create list items for each row of inventory data in the {@link Cursor}.
 */
public class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from( context ).inflate( R.layout.list_item, parent, false);
    }

    /**
     * This method binds the inventory data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current inventory can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //find the views we are interseted in
        TextView nameTextView =  (TextView) view.findViewById( R.id.name );
        TextView priceTextView =  (TextView) view.findViewById( R.id.price );
        TextView quantityTextView =  (TextView) view.findViewById( R.id.quantity );

        //get the column attributes we need
        int nameColumnIndex = cursor.getColumnIndex( ItemContract.ItemEntry.COLUMN_PRODUCT_NAME );
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex( ItemContract.ItemEntry.COLUMN_QUANTITY );

        //get the inventory attributes
        String productName = cursor.getString( nameColumnIndex );
        String price = cursor.getString (priceColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex );

        //set text views = values
        nameTextView.setText( productName  );
        priceTextView.setText( price );
        quantityTextView.setText( quantity );


    }
}

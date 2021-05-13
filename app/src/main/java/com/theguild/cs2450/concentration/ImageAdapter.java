/** ImageAdapter
 * This class contains the methods used to display the cards of the Concentration game on a grid.
 * @author  Matthew Scott Bronco #014966432
 * @since   2021-05-12
 */

package com.theguild.cs2450.concentration;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    // The Card Views to be displayed in a grid
    private ArrayList<ImageView> mCardImageViews;

    // The constructor that takes in the card views to be displayed
    public ImageAdapter(ArrayList<ImageView> cardImageViews) {
        mCardImageViews = cardImageViews;
    }

    /** getCount()
     * Tells the grid view how many cards from the card list should be displayed on the screen.
     * @return int The number of cards to be displayed. In this case, all of them.
     */
    public int getCount() {
        return mCardImageViews.size();
    }

    /** getItem()
     * Not implemented in this adapter
     */
    public Object getItem(int position) {
        return null;
    }

    /** getItemId()
     * Not implemented in this adapter
     */
    public long getItemId(int position) {
        return 0;
    }

    /** getView()
     * Returns to the grid view the image view for the card at "position".
     * @param position The position to access.
     * @param convertView Old view to re-use, if applicable.
     * @param parent The parent view group.
     * @return The image view for a card at the given position.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        return mCardImageViews.get(position);
    }
}



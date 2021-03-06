/*
 * Copyright (C) 2016  Hendrik Borghorst & Frederik Luetkes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.gateshipone.odyssey.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.gateshipone.odyssey.models.ArtistModel;
import org.gateshipone.odyssey.views.GridViewItem;

public class ArtistsGridViewAdapter extends GenericViewAdapter<ArtistModel> {

    private final GridView mRootGrid;

    /**
     * The parent grid to adjust the layoutparams.
     */
    private final Context mContext;

    public ArtistsGridViewAdapter(Context context, GridView rootGrid) {
        super();

        mContext = context;
        mRootGrid = rootGrid;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistModel artist = (ArtistModel)getItem(position);
        String label = artist.getArtistName();
        String imageURL = artist.getArtistURL();

        // Check if a view can be recycled
        if (convertView != null) {
            GridViewItem gridItem = (GridViewItem) convertView;

            // Make sure to reset the layoutParams in case of change (rotation for example)
            ViewGroup.LayoutParams layoutParams = gridItem.getLayoutParams();
            layoutParams.height = mRootGrid.getColumnWidth();
            layoutParams.width = mRootGrid.getColumnWidth();
            gridItem.setLayoutParams(layoutParams);

            gridItem.setTitle(label);
            gridItem.setImageURL(imageURL);
        } else {
            // Create new view if no reusable is available
            convertView = new GridViewItem(mContext, label, imageURL, new android.widget.AbsListView.LayoutParams(mRootGrid.getColumnWidth(), mRootGrid.getColumnWidth()));
        }

        // Check if the scroll speed currently is already 0, then start the image task right away.
        if (mScrollSpeed == 0) {
            ((GridViewItem) convertView).startCoverImageTask();
        }
        return convertView;
    }
}

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

package org.gateshipone.odyssey.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gateshipone.odyssey.R;

public class PlaylistsListViewItem extends LinearLayout{

    private final TextView mTitleView;

    /**
     * Constructor that only initialize the layout.
     */
    public PlaylistsListViewItem(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_item_playlists, this, true);

        mTitleView = (TextView) findViewById(R.id.item_playlists_title);
    }

    /**
     * Constructor that already sets the values for each view.
     */
    public PlaylistsListViewItem(Context context, String title) {
        this(context);

        mTitleView.setText(title);
    }

    /**
     * Sets the title for the Playlist.
     */
    public void setTitle(String title) {
        mTitleView.setText(title);
    }
}

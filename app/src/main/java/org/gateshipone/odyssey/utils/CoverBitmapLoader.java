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

package org.gateshipone.odyssey.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;

import org.gateshipone.odyssey.models.TrackModel;

public class CoverBitmapLoader {
    private final CoverBitmapListener mListener;
    private final Context mContext;
    private TrackModel mTrack;

    public CoverBitmapLoader(Context context, CoverBitmapListener listener) {
        mContext = context;
        mListener = listener;
    }

    /**
     * Load the image for the given track from the mediastore.
     */
    public void getImage(TrackModel track) {
        if (track != null) {
            mTrack = track;
            // start the loader thread to load the image async
            Thread loaderThread = new Thread(new DownloadRunner());
            loaderThread.start();
        }
    }

    private class DownloadRunner implements Runnable {

        /**
         * Load the image for the given track from the mediastore.
         */
        @Override
        public void run() {
            String where = android.provider.MediaStore.Audio.Albums.ALBUM_KEY + "=?";

            String whereVal[] = { mTrack.getTrackAlbumKey() };

            Cursor cursor = PermissionHelper.query(mContext, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums.ALBUM_ART}, where, whereVal, "");

            if(cursor != null) {
                String coverPath = null;
                if (cursor.moveToFirst()) {
                    coverPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                }
                if (coverPath != null) {
                    BitmapDrawable cover = (BitmapDrawable) BitmapDrawable.createFromPath(coverPath);
                    mListener.receiveBitmap(cover);
                }

                cursor.close();
            }
        }
    }

    /**
     * Callback if image was loaded.
     */
    public interface CoverBitmapListener {
        void receiveBitmap(BitmapDrawable bm);
    }
}
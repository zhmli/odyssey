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

package org.gateshipone.odyssey.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import org.gateshipone.odyssey.models.TrackModel;
import org.gateshipone.odyssey.utils.MusicLibraryHelper;
import org.gateshipone.odyssey.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

public class TrackLoader extends AsyncTaskLoader<List<TrackModel>> {

    private final Context mContext;

    /**
     * The album key if tracks of a specific album should be loaded.
     */
    private final String mAlbumKey;

    /**
     * The playlist id if tracks of a specific playlist should be loaded.
     */
    private final long mPlaylistID;

    public TrackLoader(Context context) {
        super(context);
        mContext = context;
        mAlbumKey = "";
        mPlaylistID = -1;
    }

    public TrackLoader(Context context, String albumKey) {
        super(context);
        mContext = context;
        mAlbumKey = albumKey;
        mPlaylistID = -1;
    }

    public TrackLoader(Context context, long playlistID) {
        super(context);
        mContext = context;
        mAlbumKey = "";
        mPlaylistID = playlistID;
    }

    /**
     * Load all tracks from the mediastore or a subset if a filter is set.
     */
    @Override
    public List<TrackModel> loadInBackground() {
        // Create cursor for content retrieval
        Cursor trackCursor;

        int trackTitleColumnIndex = -1;
        int trackDurationColumnIndex = -1;
        int trackNumberColumnIndex = -1;
        int trackArtistColumnIndex = -1;
        int trackAlbumColumnIndex = -1;
        int trackURLColumnIndex = -1;
        int trackAlbumKeyColumnIndex = -1;
        int trackIdColumnIndex = -1;

        if (mPlaylistID != -1) {
            // load playlist tracks
            trackCursor = PermissionHelper.query(mContext, MediaStore.Audio.Playlists.Members.getContentUri("external", mPlaylistID), MusicLibraryHelper.projectionPlaylistTracks, "", null, "");

            if (trackCursor != null) {
                trackTitleColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE);
                trackDurationColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DURATION);
                trackNumberColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TRACK);
                trackArtistColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST);
                trackAlbumColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM);
                trackURLColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA);
                trackAlbumKeyColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM_KEY);
                trackIdColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID);
            }
        } else {

            if (mAlbumKey.isEmpty()) {
                // load all tracks
                trackCursor = PermissionHelper.query(mContext, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MusicLibraryHelper.projectionTracks, "", null, MediaStore.Audio.Media.TITLE + " COLLATE NOCASE");
            } else {
                // load album tracks

                String whereVal[] = {mAlbumKey};

                String where = android.provider.MediaStore.Audio.Media.ALBUM_KEY + "=?";

                trackCursor = PermissionHelper.query(mContext, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MusicLibraryHelper.projectionTracks, where, whereVal, MediaStore.Audio.Media.TRACK);
            }

            if (trackCursor != null) {
                trackTitleColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                trackDurationColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                trackNumberColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
                trackArtistColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                trackAlbumColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                trackURLColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                trackAlbumKeyColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);
                trackIdColumnIndex = trackCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            }
        }

        ArrayList<TrackModel> tracks = new ArrayList<>();

        if (trackCursor != null) {
            for (int i = 0; i < trackCursor.getCount(); i++) {
                trackCursor.moveToPosition(i);

                String title = trackCursor.getString(trackTitleColumnIndex);
                long duration = trackCursor.getLong(trackDurationColumnIndex);
                int no = trackCursor.getInt(trackNumberColumnIndex);
                String artist = trackCursor.getString(trackArtistColumnIndex);
                String album = trackCursor.getString(trackAlbumColumnIndex);
                String url = trackCursor.getString(trackURLColumnIndex);
                String albumKey = trackCursor.getString(trackAlbumKeyColumnIndex);
                long id = trackCursor.getLong(trackIdColumnIndex);

                TrackModel track = new TrackModel(title, artist, album, albumKey, duration, no, url, id);
                tracks.add(track);

            }
            trackCursor.close();
        }

        return tracks;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}

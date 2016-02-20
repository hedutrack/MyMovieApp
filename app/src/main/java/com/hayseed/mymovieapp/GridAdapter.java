package com.hayseed.mymovieapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 */
public class GridAdapter extends ArrayAdapter
{
    private static final String TAG = GridAdapter.class.getSimpleName ();

    public GridAdapter (Activity context, List<MovieDB> movieList)
    {
        super (context, 0, movieList);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        // The magic starts with the super () in the constructor
        // public ArrayAdapter(Context context, @LayoutRes int resource, @NonNull List<T> objects)
        // public T getItem(int position)
        //Integer referenceId = (Integer) getItem (position);
        MovieDB movieDB = (MovieDB) getItem (position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from (getContext ()).inflate(R.layout.fragment_main_image, parent, false);
        }

        String posterPath = movieDB.getPosterPath ();
        Uri.Builder uri = new Uri.Builder ();
        uri.scheme ("http").authority ("image.tmdb.org")
                .appendPath ("t")
                .appendPath ("p")
                .appendPath ("w185")
                .appendEncodedPath (posterPath);

        Log.d (TAG, "url="+uri.toString ());

        ImageView imageView = (ImageView) convertView.findViewById (R.id.fragment_main_poster);
        Context c = parent.getContext ();
        Picasso.with (c).load (uri.toString ()).into (imageView);
        //imageView.setImageResource (referenceId);

        return convertView;
    }

}

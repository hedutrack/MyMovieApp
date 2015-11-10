package com.hayseed.mymovieapp;

import android.app.Activity;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

/**
 *
 */
public class GridAdapter extends ArrayAdapter
{
    private static final String TAG = GridAdapter.class.getSimpleName ();

    public GridAdapter (Activity context, List<Integer> movieList)
    {
        super (context, 0, movieList);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        //return super.getView (position, convertView, parent);

        Integer referenceId = (int) getItem (position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from (getContext ()).inflate(R.layout.fragment_main_image, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById (R.id.fragment_main_poster);
        imageView.setImageResource (referenceId.intValue ());

        return convertView;
    }
}

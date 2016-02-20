package com.hayseed.mymovieapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

/**
 *
 */
public class GridFragment extends Fragment
{
    ArrayList<MovieDB> movieList;

    private GridAdapter             gridAdapter;
    private OnImageSelectedListener listener;

    public interface OnImageSelectedListener
    {
        public void OnImageSelected (Integer imageId);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // inflate the view
        View rootView = inflater.inflate (R.layout.fragment_main, container, false);

        // create the adapter
        gridAdapter = new GridAdapter (getActivity (), movieList);

        // Get the grid view
        GridView gridView = (GridView) rootView.findViewById (R.id.gridview);

        // Attach the adapter
        gridView.setAdapter (gridAdapter);

        // set the click listener
        gridView.setOnItemClickListener (new OnItemClickListener ()
        {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                // Invoke the callback implemented in MainActivity
                listener = (OnImageSelectedListener) getActivity ();
                listener.OnImageSelected (new Integer (position));
            }
        });

        return rootView;
    }

    // TODO Temporary stub to keep things moving along.
    // TODO Not the best way of passing data to a fragment.
    public void setAdapterData (ArrayList<MovieDB> list)
    {
        movieList = list;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}

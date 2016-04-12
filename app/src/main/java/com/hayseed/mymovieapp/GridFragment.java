package com.hayseed.mymovieapp;

import android.app.Fragment;
import android.content.Context;
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
    private GridView gridView;
    private OnImageSelectedListener listener;

    public interface OnImageSelectedListener
    {
        public void OnImageSelected (Integer imageId);
    }

    @Override
    public void onAttach (Context context)
    {
        super.onAttach (context);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // inflate the view
        View rootView = inflater.inflate (R.layout.fragment_main, container, false);

        // create the adapter

        gridAdapter = new GridAdapter (getActivity (), movieList == null ? new ArrayList<MovieDB> () : movieList);

        // Get the grid view
        gridView = (GridView) rootView.findViewById (R.id.gridview);

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

    @Override
    public void onResume ()
    {
        super.onResume ();

        gridAdapter.notifyDataSetChanged ();
    }

    // TODO Temporary stub to keep things moving along.
    // TODO Not the best way of passing data to a fragment.
    public void setAdapterData (ArrayList<MovieDB> list)
    {
        movieList = list;

        // TODO To refresh the grid view.  Is this the best approach?
        gridAdapter = new GridAdapter (getActivity (), movieList);
        gridView.setAdapter (gridAdapter);
    }
}

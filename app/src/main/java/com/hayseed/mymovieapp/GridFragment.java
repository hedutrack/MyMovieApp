package com.hayseed.mymovieapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.Arrays;

/**
 *
 */
public class GridFragment extends Fragment
{

    private GridAdapter gridAdapter;

    private Integer [] images =
            {
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine,
                    R.drawable.sunshine
            };

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //return super.onCreateView (inflater, container, savedInstanceState);

        // inflate the view
        View rootView = inflater.inflate (R.layout.fragment_main, container, false);

        // create the adapter
        gridAdapter = new GridAdapter (getActivity (), Arrays.asList (images));

        // attach to GridLayout
        GridView gridView = (GridView) rootView.findViewById (R.id.gridview_layout);
        gridView.setAdapter (gridAdapter);

        return rootView;
    }
}

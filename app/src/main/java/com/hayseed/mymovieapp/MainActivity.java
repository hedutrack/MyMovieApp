package com.hayseed.mymovieapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        // The fragment
        GridFragment gridFragment = new GridFragment ();

        // Get the fragment manager
        FragmentManager fragmentManger = getFragmentManager ();
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction ();

        // Add the fragment.  The magic lies in associating the fragment to a container. In this
        // case, the container is defined in activity_main.xml.  There is a method for not
        // associating the fragment to a container, but I'm not sure of the meaning of that.
        fragmentTransaction.add (R.id.content_frame, gridFragment);
        fragmentTransaction.commit ();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected (item);
    }
}

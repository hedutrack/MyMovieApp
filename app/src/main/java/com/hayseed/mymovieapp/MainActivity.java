package com.hayseed.mymovieapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements GridFragment.OnImageSelectedListener, MovieDetailFragment.OnMovieDetailBackListener
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        Toolbar theToolBar = (Toolbar) findViewById (R.id.main_toolbar);
        setSupportActionBar (theToolBar);

        // The fragment
        GridFragment gridFragment = new GridFragment ();

        // Get the fragment manager
        FragmentManager     fragmentManger      = getFragmentManager ();
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction ();

        // Add the fragment.  The magic lies in associating the fragment to a container. In this
        // case, the container is defined in activity_main.xml.  There is a method for not
        // associating the fragment to a container, but I'm not sure of the meaning of that.
        fragmentTransaction.add (R.id.content_frame, gridFragment);
        //fragmentTransaction.addToBackStack (null);
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

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected (item);
    }

    /**
     * Callback from GridFragment
     * <p/>
     * See <a href="http://developer.android.com/guide/components/fragments.html">Android Fragments</a>
     *
     * @param pos
     */
    public void OnImageSelected (Integer pos)
    {
        MovieDetailFragment detailFragment = new MovieDetailFragment ();

        Bundle b = new Bundle ();
        b.putInt ("Position", pos);

        detailFragment.setArguments (b);

        //Toast.makeText (this, String.valueOf (pos), Toast.LENGTH_LONG).show ();
        FragmentManager fragmentManager = getFragmentManager ();

        FragmentTransaction transaction = fragmentManager.beginTransaction ();
        transaction.replace (R.id.content_frame, detailFragment);
        transaction.addToBackStack (null);
        transaction.commit ();


    }

    /**
     * Callback from MovieDetailFragment.  Used to signal back button.
     */
    public void OnDetailBack ()
    {
        FragmentManager fragmentManager = getFragmentManager ();
        fragmentManager.popBackStack ();
        getSupportActionBar ().setDisplayShowTitleEnabled (true);
        getSupportActionBar ().setTitle ("MyMovieApp");
        getSupportActionBar ().setDisplayOptions (0);
    }
}

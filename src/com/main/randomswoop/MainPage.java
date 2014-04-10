package com.main.randomswoop;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 10, 2014
 * 
 */
public class MainPage extends Activity
{
    // Tracks how many attempts are made to laod the page
    private static int numberOfAttemptsToLoadNewPage_ = 0;
    private static FileObserver fo;

    // Tag used for the fragment
    public final static String FRAG1_TAG = "FRAG1";

    // references to the shake fragment
    private ShakeFragment shakeActivity_;

    /**
     * instance of the settings task
     */
    private static SharedPreferences preference_;

    /**
     * Keeps track of whether an image is already displayed or now
     */
    public static boolean inputCorrect;

    /**
     * settings menu Intent, used to access settings and preference information
     */
    private Intent settingsIntention_;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        settingsIntention_ =
                new Intent( MainPage.this,
                        SettingsPreferenceActivity.class );
        // Set up the preference
        preference_ = getSharedPreferences(
                getString( R.string.pref_title_file ),
                Context.MODE_PRIVATE );

        // restoring references when the Activity is restoring itself
        if ( savedInstanceState != null )
        {
            shakeActivity_ =
                    (ShakeFragment) getFragmentManager().findFragmentByTag(
                            FRAG1_TAG );
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode,
            Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );

        setContentView( R.layout.main );
        Bundle extras = data.getExtras();

        Toast.makeText(
                getApplicationContext(),
                "Welcome to Random Swoop!",
                Toast.LENGTH_LONG * 2 ).show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent( MotionEvent event )
    {

        Toast.makeText(
                getApplicationContext(),
                "Oh, you touched me!",
                Toast.LENGTH_LONG * 2 ).show();
        return super.onTouchEvent( event );
    }

    /**
     * This function is called when the shaken activity was closed
     */
    public void ShakenClosed()
    {
        // shakeMeter_ = new ShakeMeter();

        // Make sure the keyboard doesn't try and show it's ugly head
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }

    /**
     * @return the preference
     */
    public static SharedPreferences getPreferences()
    {
        return preference_;
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )
    {

        // treat key event as a trigger for showing menu
        if ( keyCode == KeyEvent.KEYCODE_DPAD_CENTER )
        {
            // This occurs when the user has tapped the side of the glasses
            Toast.makeText(
                    getApplicationContext(),
                    "Opening options menu",
                    Toast.LENGTH_LONG ).show();
            displayWebactivity();

            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.meme_menu, menu );
        // inflater.inflate( R.menu.settings, menu ); // TODO
        // inflater.inflate( R.menu.about, menu );

        return true;
    }

    /**
     * @param imageURL
     * 
     */
    private void displayWebactivity()
    {
        try
        {
            if ( isOnline( MainPage.this ) )
            {
                // Make sure there isn't already a page loading
                if ( null != shakeActivity_ && shakeActivity_.isPageLoading() )
                {
                    if ( numberOfAttemptsToLoadNewPage_ < 2 )
                    {
                        numberOfAttemptsToLoadNewPage_++;
                        Toast.makeText(
                                getApplicationContext(),
                                "I like the energy! But A page is already loading...",
                                Toast.LENGTH_SHORT / 2 ).show();
                        return;
                    }
                    else
                    {
                        // Remove the current process, start a new one
                        shakeActivity_.stopPageLoading();
                        FragmentTransaction newFragment =
                                getFragmentManager().beginTransaction()
                                        .remove( shakeActivity_ );

                        Toast.makeText(
                                getApplicationContext(),
                                "Restarted loading...",
                                Toast.LENGTH_SHORT / 2 ).show();
                    }
                }

                shakeActivity_ = new ShakeFragment();

                FragmentTransaction newFragment =
                        getFragmentManager().beginTransaction()
                                .add( R.id.frame1, shakeActivity_,
                                        FRAG1_TAG );
                newFragment.commit();
                numberOfAttemptsToLoadNewPage_ = 0;
                return;

            }
            else
            {
                Toast.makeText(
                        getApplicationContext(),
                        "You must have internet access to Swoop! Please check your connection...",
                        Toast.LENGTH_LONG * 2 ).show();
            }
        }
        catch ( Exception ex )
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Something went wrong with the Swoop, please try again!",
                    Toast.LENGTH_LONG ).show();
        }

    }

    /**
     * Checks to see if the device is online
     * 
     * @param context
     * @return if it's online or not
     */
    public static boolean isOnline( Context context )
    {
        ConnectivityManager cm =
                (ConnectivityManager) context
                        .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ( netInfo != null && netInfo.isConnected() )
        {
            return true;
        }
        return false;
    }

    /**
     * Adds the about fragment to the view
     */
    private void displayAboutFragment()
    {
        // prepare the instructions box
        Dialog aboutBox =
                new Dialog( MainPage.this );
        aboutBox.requestWindowFeature( Window.FEATURE_LEFT_ICON );
        aboutBox.setContentView( R.layout.dialog_about );
        // set the message to display
        aboutBox.setTitle( "About Hashtag Swoop" );

        aboutBox.show();
        aboutBox.setFeatureDrawableResource( Window.FEATURE_LEFT_ICON,
                R.drawable.dizzy_bird_icon );

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Make sure the keyboard doesn't try and show it's ugly head
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );

        switch ( item.getItemId() )
        {
        case R.id.action_settings:
            this.startActivity( settingsIntention_ );
            break;
        case R.id.action_about:
            displayAboutFragment();
            break;
        }

        return true;
    }

}

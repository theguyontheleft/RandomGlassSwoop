package com.main.randomswoop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.glass.app.Card;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 10, 2014
 * 
 */
public class MainPage extends Activity
{

    private int actionCode = 0;
    private Uri outputFileUri;
    private Uri mCapturedImageURI;
    private static FileObserver fo;

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

        Intent takePictureIntent =
                new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        startActivityForResult( takePictureIntent, actionCode );

    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode,
            Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );

        if ( requestCode == actionCode && resultCode == Activity.RESULT_OK )
        {
            setContentView( R.layout.main );
            Bundle extras = data.getExtras();

            // find path to file and trigger activity to input voice captions
            if ( extras.containsKey( "picture_file_path" ) )
            {
                String picture_file_path =
                        extras.getString( "picture_file_path" );

                Intent mIntent =
                        new Intent( getBaseContext(), AddCaption.class );

                mIntent.putExtra(
                        getResources().getString( R.string.picture_path ),
                        picture_file_path );

                startActivity( mIntent );
                finish();
            }

        }
        else
        {
            // Image capture cancelled
            Card card2 = new Card( getApplicationContext() );
            card2.setText( "No camera" );
            setContentView( card2.toView() );
            // TODO: allow for retry from menu
        }
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

}

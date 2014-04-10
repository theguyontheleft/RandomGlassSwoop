package com.main.randomswoop;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 1, 2014 * This is the shake fragment class, it handles the shake
 *          operations and picks a random website from the 9 choises in the
 *          settings
 * 
 * 
 */
public class ShakeFragment extends Fragment
{
    MainPage activityListener;

    /**
     * Instance to the webview
     */
    private static WebView webView_;

    private static String URL1 = "http://mobile.memestache.com/random"; // good
    private static String URL2 = "http://www.imgur.com/random"; // Good
    // "m.imgur.com/random";
    private static String URL3 = "http://memebase.cheezburger.com/"; // Very
                                                                     // slow,
                                                                     // but good
    private static String URL4 = "http://knowyourmeme.com/photos/trending"; // good
    private static String URL5 = "http://m.reddit.com/random"; // good
    private static String URL6 = "http://m.quickmeme.com"; // Good
    private static String URL7 = "http://www.memes.com/random"; // Good
    // ** URL 8 is Not selected by default**
    private static String URL8 = "http://www.funcage.com/"; // Good
    private static String URL9 = "http://www.ohnorobot.com/random.pl"; // Good
    private static String URL10 = "http://m.fatpita.net/";

    // private static String URL11 = "";
    // private static String URL12 = "";

    // String delim = "/b";
    private static final String DEFAULT_URLS = "/b1/b2/b3/b4/b5/b6/b7/b8/b";

    /*********************************************************
     * Other Possible Sites
     * 
     * http://www.quotev.com/?random&t=quiz
     * 
     * (No mobile site, looks terrible)
     * http://hilariousfunnypictures.com/item.php *** http://www.randomimage.us/
     * *** http://quicklol.com/?task=randompost
     * 
     * 
     * 
     * *Kicks and giggles*:
     * https://www.facebook.com/pages/Jimbo-Slice/332341698705
     * 
     *********************************************************/

    // Progress dialog to show
    private ProgressDialog pd_;

    private static SharedPreferences preference_;

    // Keeps track of the page loading and if a page is already loaded
    private static boolean pageLoaded_ = false;
    private static boolean pageLoading_ = false;

    // Random number generator used to pick the URL
    private static Random numberGenerator_ = new Random();

    // Store the string of the websites chosen in settings
    private String websiteURLNumbers;

    private static ImageView dizzyBird_;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        // Set up the preference
        preference_ = MainPage.getPreferences();

        updateWebsitesToChooseFrom();
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState )
    {
        container.removeAllViews();

        // linking together the xml and the view object
        // Note that each fragment has its own layout file
        View view = inflater.inflate( R.layout.fragment_shake,
                container, false );

        dizzyBird_ = (ImageView) view.findViewById( R.id.imageView1 );

        webView_ = (WebView) view.findViewById( R.id.webPage );
        if ( null != webView_ )
        {
            // Start the progress dialog
            pd_ =
                    ProgressDialog
                            .show( getActivity(),
                                    getString( R.string.loading_menu_title ),
                                    "Please wait, your random feed is loading...",
                                    true );
            pageLoading_ = true;

            // Set the page loaded to be true, set the visibility of the error
            // dizzy bird and the webpage
            if ( !pageLoaded_ )
            {
                dizzyBird_
                        .setBackgroundResource( R.drawable.dizzy_bird_no_qoute );
                pageLoaded_ = true;
            }

            dizzyBird_.setVisibility( View.VISIBLE );

            // webchrome client checks the progress to disable the dialog
            webView_.setWebChromeClient( new WebChromeClient()
            {

                public void onProgressChanged( WebView view, int progress )
                {
                    if ( progress >= 90 )
                    {
                        pd_.dismiss();
                        pageLoading_ = false;
                    }
                }
            } );

            webView_.setWebViewClient( new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading( WebView view,
                        String url )
                {
                    return false;
                }

                @Override
                public void onPageStarted( WebView view, String url,
                        Bitmap favicon )
                {
                    if ( !pd_.isShowing() )
                    {
                        pd_.show();
                    }
                }

                @Override
                public void onPageFinished( WebView view, String url )
                {
                    if ( null != pd_ && pd_.isShowing() )
                    {
                        dizzyBird_.setVisibility( View.INVISIBLE );
                        pd_.dismiss();
                    }

                    pageLoaded_ = true;
                    webView_.bringToFront();
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * android.webkit.WebViewClient#onReceivedError(android.webkit
                 * .WebView, int, java.lang.String, java.lang.String)
                 */
                @Override
                public void onReceivedError( WebView view, int errorCode,
                        String description, String failingUrl )
                {
                    super.onReceivedError( view, errorCode, description,
                            failingUrl );

                    errorDisplayingSelection();
                }
            } );

            // Set up the settings
            WebSettings webSettings = webView_.getSettings();
            webSettings.setLoadWithOverviewMode( true );
            webSettings.setAllowFileAccess( true );
            // webSettings.setEnableSmoothTransition( true );
            webSettings.setJavaScriptEnabled( true );
            webSettings.setDisplayZoomControls( true );
            webSettings.setBuiltInZoomControls( true );
            // webView_.capturePicture();
            // webView_.canZoomIn();

            loadRandomURL();

        }

        return view;
    }

    /**
     * Displays the image behind the webview, so if it didn't load, the user
     * knows to shake it again
     */
    private void displayErrorGraphic()
    {
        if ( null != dizzyBird_ )
        {
            dizzyBird_.setVisibility( View.VISIBLE );
        }
    }

    /**
     * Chooses from the 9 possible websites, or whatever the user has selected
     */
    private void loadRandomURL()
    {
        // TODO this may not need to be called again;
        // updateWebsitesToChooseFrom();
        int numberOfURLtoLoad = generateRandomNumber( 8 );

        switch ( numberOfURLtoLoad )
        {
        case 1:
            webView_.loadUrl( URL1 );
            break;
        case 2:
            webView_.loadUrl( URL2 );
            break;
        case 3:
            webView_.loadUrl( URL3 );
            break;
        case 4:
            webView_.loadUrl( URL4 );
            break;
        case 5:
            webView_.loadUrl( URL5 );
            break;
        case 6:
            webView_.loadUrl( URL6 );
            break;
        case 7:
            webView_.loadUrl( URL7 );
            break;
        case 8:
            webView_.loadUrl( URL8 );
            break;
        case 9:
            webView_.loadUrl( URL9 ); // Not included originally
            break;
        case 10:
            webView_.loadUrl( URL10 );
            break;
        default:
            errorDisplayingSelection();
            break;
        }

    }

    /**
     * This method is called when the website fails to display.
     */
    private void errorDisplayingSelection()

    {
        if ( null != pd_ && pd_.isShowing() )
        {
            pd_.dismiss();
        }

        Toast.makeText(
                activityListener,
                "Failed to Swoop! Shake again...",
                Toast.LENGTH_LONG ).show();

        pageLoaded_ = false;

        displayErrorGraphic();
    }

    /**
     * This method generates a random number that represents a URL that was
     * selected.
     * 
     * @param numbersNotToGenerate
     * @param maxNumberToGenerate
     * @return
     */
    private int generateRandomNumber( int maxNumberToGenerate )
    {
        // Delim used to parse the string
        String delims = "/b";

        if ( null == websiteURLNumbers || 1 > websiteURLNumbers.length()
                // This check is for the older versions of the app that may not
                // have the delim in the string
                || !websiteURLNumbers.contains( delims ) )
        {
            websiteURLNumbers = DEFAULT_URLS;
        }

        String[] arrayOfNumbers = websiteURLNumbers.split( delims );
        ArrayList<Integer> arrayOfNumbersToChooseFrom =
                new ArrayList<Integer>();

        for ( String currentNumber : arrayOfNumbers )
        {
            try
            {
                arrayOfNumbersToChooseFrom.add( Integer
                        .parseInt( currentNumber ) );
            }
            catch ( NumberFormatException e )
            {
                // Will Throw exception!
                // do something! anything to handle the exception.
                System.err.append( "Couldn't convert \"" + currentNumber
                        + " to an integer!" );
            }
        }

        if ( 1 == arrayOfNumbersToChooseFrom.size() )
        {
            return arrayOfNumbersToChooseFrom.get( 0 );
        }
        else
        {

            int randomNumber =
                    numberGenerator_
                            .nextInt( arrayOfNumbersToChooseFrom.size() );

            return arrayOfNumbersToChooseFrom.get( randomNumber );
        }
    }

    /**
     * This method updates the websites to choose from
     */
    private void updateWebsitesToChooseFrom()
    {
        if ( null != preference_ )
        {
            websiteURLNumbers = preference_.getString(
                    getString( R.string.pref_title_sites ),
                    getString( R.string.pref_title_sites ) );
        }

        if ( null != websiteURLNumbers )
        {
            if ( 1 > websiteURLNumbers.length() )
            {
                // activityListener.promptSettings(); // Settings are different
                // in google glass
                // Toast.makeText(
                // activityListener,
                // "Select the websites to load",
                // Toast.LENGTH_SHORT ).show();
                // websiteURLNumbers = DEFAULT_URLS;
            }
            else if ( websiteURLNumbers
                    .contains( getString( R.string.pref_title_sites ) ) )
            {
                websiteURLNumbers = DEFAULT_URLS;
            }
        }
    }

    /**
     * @param arg0
     * @param arg1
     */
    public void onAccuracyChanged( Sensor arg0, int arg1 )
    {
        // TODO Do something here if sensor accuracy changes.
    }

    @Override
    public void onAttach( Activity activity )
    {
        super.onAttach( activity );

        activityListener = (MainPage) activity;
    }

    @Override
    public void onResume()
    {
        if ( webView_ != null )
        {
            webView_.onResume();
        }
        super.onResume();

        updateWebsitesToChooseFrom();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        webView_.onPause();
        pageLoaded_ = false;
        pageLoading_ = false;
        dizzyBird_.setVisibility( View.INVISIBLE );

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
     * android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu( ContextMenu menu, View v,
            ContextMenuInfo menuInfo )
    {
        super.onCreateContextMenu( menu, v, menuInfo );
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state
     * of the WebView.
     */
    @Override
    public void onDestroy()
    {
        stopPageLoading();

        super.onDestroy();
    }

    /**
     * If the username was entered, welcome the user, tell them what time it is.
     */
    private void welcomeUserBack()
    {
        String userName =
                preference_.getString( getString( R.string.pref_title_name ),
                        getString( R.string.pref_title_name ) );
        if ( "" != userName )
        {
            Toast.makeText(
                    activityListener,
                    "Welcome back " + userName + "!",
                    Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * @return if the page is currently loading
     */
    public boolean isPageLoading()
    {
        return pageLoading_;
    }

    /**
     * Stops the loading page and gets rid of some of the resources.
     */
    public void stopPageLoading()
    {
        if ( null != webView_ )
        {
            webView_.destroy();
            webView_ = null;
        }

        if ( null != pd_ )
        {
            pd_.dismiss();
            pageLoading_ = false;
        }
    }
}

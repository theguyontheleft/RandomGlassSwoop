package com.main.randomswoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jimmy Dagres
 * 
 * @version March 8, 2014
 * 
 *          This activity displays a list of all of the setting preferences.
 *          When selected a dialog appears allowing the user to change the
 *          setting. Each setting is stored in a non volatile shared preference
 *          file.
 * 
 * 
 */
public class SettingsPreferenceActivity extends Activity
{
    /**
     * listview that displays the settings options
     */
    private ListView settingsListView_;

    /**
     * Common handles to the preference file
     */
    private SharedPreferences preference_;

    /**
     * Delim used to separate the website preferences stored
     */
    static String delim = "/b";

    /**
     * Common handles to the preference editor
     */
    private SharedPreferences.Editor preferenceEditor_;

    /**
     * @return the name saved in the preferences
     */
    public String getName()
    {
        return preference_.getString( getString( R.string.pref_title_name ),
                getString( R.string.pref_title_name ) );
    }

    /**
     * @return the game length in the preferences
     */
    public String getWebsitesShuffled()
    {
        return preference_.getString(
                getString( R.string.pref_title_sites ),
                getString( R.string.pref_title_sites ) );
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

        settingsListView_ = (ListView) findViewById( R.id.settingsListView1 );
        preference_ =
                getSharedPreferences( getString( R.string.pref_title_file ),
                        Context.MODE_PRIVATE );

        try
        {
            preferenceEditor_ = preference_.edit();
        }
        catch ( Exception ex )
        {
            System.err.print( "Error fetching preference file" );
        }

        initializeSettingsList();

        // Makes sure a proper game length and name is set
        checkForValidPreferences();
    }

    /**
     * Initializes the settings list and sets up it's on click event
     */
    private void initializeSettingsList()
    {
        String[] settings_Array =
                getResources().getStringArray( R.array.settings_array );

        final ArrayList<String> list = new ArrayList<String>();

        for ( int i = 0; i < settings_Array.length; ++i )
        {
            list.add( settings_Array[i] );
        }

        final StableArrayAdapter adapter =
                new StableArrayAdapter( this,
                        android.R.layout.simple_list_item_1, list );
        settingsListView_.setAdapter( adapter );

        settingsListView_.setOnItemClickListener( new OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> parent, View view,
                    int position, long id )
            {
                // Call the function to handle the updated preference
                handleUpdatePreferenceSelection( position );
            }
        } );
    }

    /**
     * This function is called when any on click event for the settings is hit,
     * it then handles the appropriate action for modified the specifically
     * selected preference.
     * 
     * @param positionOfSettingToUpdate
     */
    public void
            handleUpdatePreferenceSelection( int positionOfSettingToUpdate )
    {
        LayoutInflater li = LayoutInflater.from( getBaseContext() );

        switch ( positionOfSettingToUpdate )
        {
        case 0: // The name preference
            View nameView =
                    li.inflate( R.layout.dialog_update_name_settings, null );

            displayUpdateSettingsDialog(
                    getString( R.string.pref_title_name ),
                    preference_.getString(
                            getString( R.string.pref_title_name ),
                            getString( R.string.pref_title_name ) ), nameView );
            break;

        case 1: // The website preference
            displayWebsiteDialog(
                    getString( R.string.pref_title_sites ),
                    preference_.getString(
                            getString( R.string.pref_title_sites ),
                            getString( R.string.pref_title_sites ) ) );
            break;
        }

    }

    /**
     * This function uses a dialog containing a radiogroup, it performs almost
     * the same as the displayUpdateSettingsDialog except it's designed for the
     * difficulty button. It creates a reference to the websites checkbuttons
     * called "difficultytRadioGroup1".
     * 
     * @param difficultyPreferenceKey
     *            name of the setting being updated
     * @param currentDifficultyPreference
     *            current value of the preference
     */
    private void displayWebsiteDialog(
            final String difficultyPreferenceKey,
            final String currentDifficultyPreference )
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from( getBaseContext() );
        View promptsView =
                li.inflate( R.layout.dialog_update_websites_settings, null );
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder( this );

        // set prompts.xml to alert dialog builder and sets the title
        alertDialogBuilder.setView( promptsView );
        alertDialogBuilder.setTitle( "Update Website Options" );

        // Define all of the difficulty options
        final CheckBox checkBox1 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox1 );
        final CheckBox checkBox2 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox2 );
        final CheckBox checkBox3 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox3 );
        final CheckBox checkBox4 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox4 );
        final CheckBox checkBox5 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox5 );
        final CheckBox checkBox6 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox6 );
        final CheckBox checkBox7 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox7 );
        final CheckBox checkBox8 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox8 );
        final CheckBox checkBox9 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox9 );
        final CheckBox checkBox10 =
                (CheckBox) promptsView
                        .findViewById( R.id.checkBox10 );

        // final CheckBox checkBox10 = *** IF you add another add delim to the
        // check for checkbox "2"
        // (CheckBox) promptsView
        // .findViewById( R.id.checkBox10 );

        // Check the appropriate boxes
        if ( currentDifficultyPreference
                .contentEquals( getString( R.string.pref_title_sites ) ) )
        {
            checkBox1.setChecked( true );
            checkBox2.setChecked( true );
            checkBox3.setChecked( true );
            checkBox4.setChecked( true ); // Don't initialize trending
                                          // knowyourmeme
            checkBox5.setChecked( true );
            checkBox6.setChecked( true );
            checkBox7.setChecked( true );
            checkBox8.setChecked( true );
            checkBox9.setChecked( true );
            checkBox10.setChecked( true );

        }
        else
        {
            checkBox1
                    .setChecked( currentDifficultyPreference.contains( "1"
                            + delim ) );
            checkBox2.setChecked( currentDifficultyPreference.contains( "2" ) );
            checkBox3.setChecked( currentDifficultyPreference.contains( "3" ) );
            checkBox4.setChecked( currentDifficultyPreference.contains( "4" ) );
            checkBox5.setChecked( currentDifficultyPreference.contains( "5" ) );
            checkBox6.setChecked( currentDifficultyPreference.contains( "6" ) );
            checkBox7.setChecked( currentDifficultyPreference.contains( "7" ) );
            checkBox8.setChecked( currentDifficultyPreference.contains( "8" ) );
            checkBox9.setChecked( currentDifficultyPreference.contains( "9" ) );
            checkBox10
                    .setChecked( currentDifficultyPreference.contains( "10" ) );
        }

        alertDialogBuilder
                .setCancelable( false )
                .setPositiveButton( "OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void
                                    onClick( DialogInterface dialog, int id )
                            {
                                String newSettings = "";

                                // Website 1 is Memestache
                                if ( checkBox1.isChecked() )
                                {
                                    newSettings = newSettings + "1" + delim;
                                }

                                // Website 2 is Imagur
                                if ( checkBox2.isChecked() )
                                {
                                    newSettings = newSettings + "2" + delim;
                                }

                                // Website 3 is www.cheezburger.com
                                if ( checkBox3.isChecked() )
                                {
                                    newSettings = newSettings + "3" + delim;
                                }

                                // Website 4 is http://knowyourmeme.com/
                                if ( checkBox4.isChecked() )
                                {
                                    newSettings = newSettings + "4" + delim;
                                }

                                // Website 5 is redit
                                if ( checkBox5.isChecked() )
                                {
                                    newSettings = newSettings + "5" + delim;
                                }

                                // Website 6 is Quickmeme.com
                                if ( checkBox6.isChecked() )
                                {
                                    newSettings = newSettings + "6" + delim;
                                }

                                // Website 7 is memes.com
                                if ( checkBox7.isChecked() )
                                {
                                    newSettings = newSettings + "7" + delim;
                                }

                                // Website 8 is
                                if ( checkBox8.isChecked() )
                                {
                                    newSettings = newSettings + "8" + delim;
                                }

                                // Website 9 is
                                if ( checkBox9.isChecked() )
                                {
                                    newSettings = newSettings + "9" + delim;
                                }

                                // Website 10 is
                                if ( checkBox10.isChecked() )
                                {
                                    newSettings = newSettings + "10" + delim;
                                }

                                if ( 1 > newSettings.length() )
                                {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "You must choose at least one website! ;O",
                                            Toast.LENGTH_LONG * 3 ).show();

                                    displayWebsiteDialog(
                                            difficultyPreferenceKey,
                                            currentDifficultyPreference );

                                    return;
                                }

                                updatePreference( difficultyPreferenceKey,
                                        newSettings );
                            }
                        } )
                .setNegativeButton( "Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void
                                    onClick( DialogInterface dialog, int id )
                            {
                                dialog.cancel();
                            }
                        } );

        // Create the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show the dialog
        alertDialog.show();
    }

    /**
     * This function will create a dialog with the passed setting to be updated
     * and if it is updated then it will return the newly updated settings
     * string.
     * 
     * @param settingsToBeUpdated
     *            this is the string the dialog will list is being updated
     * @param currentStringForTheSetting
     *            current value of the given setting
     * @param promptsView
     *            dialog box
     */
    private void displayUpdateSettingsDialog(
            final String settingsToBeUpdated,
            String currentStringForTheSetting, View promptsView )
    {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder( this );

        // set prompts.xml to alert dialog builder and sets the title
        alertDialogBuilder.setView( promptsView );
        alertDialogBuilder.setTitle( "Update stored " + settingsToBeUpdated );

        // Display text with the settings to be updated.
        final TextView tv1;
        tv1 = (TextView) promptsView.findViewById( R.id.updateSettingsText );
        tv1.setText( "New " + settingsToBeUpdated + ":" );

        // Display the current settings in the edit text box
        final EditText result =
                (EditText) promptsView
                        .findViewById( R.id.editTextDialogUserInput );
        result.setHint( currentStringForTheSetting );

        // set dialog message
        alertDialogBuilder
                .setCancelable( false )
                .setPositiveButton( "OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void
                                    onClick( DialogInterface dialog, int id )
                            {
                                String newSettingsValue =
                                        result.getText().toString().trim();

                                // Make sure a blank setting isn't entered
                                if ( !"".equals( newSettingsValue ) )
                                {
                                    // Check game length options
                                    if ( settingsToBeUpdated
                                            .contains( getString( R.string.pref_title_sites ) ) )
                                    {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Please enter a valid website",
                                                Toast.LENGTH_LONG ).show();

                                        return;
                                    }

                                    // Update the setting preference
                                    updatePreference( settingsToBeUpdated,
                                            newSettingsValue );
                                }
                                else
                                {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Your " + settingsToBeUpdated
                                                    + " was NOT saved.",
                                            Toast.LENGTH_LONG ).show();
                                }
                            }
                        } )
                .setNegativeButton( "Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void
                                    onClick( DialogInterface dialog, int id )
                            {
                                dialog.cancel();
                            }
                        } );

        // Create the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show the dialog
        alertDialog.show();
    }

    /**
     * Updates the preference of the passed preference
     * 
     * @param preferenceToUpdate
     *            name of the preference to be changed
     * @param newPreferenceValue
     *            new value for the preference
     */
    public void updatePreference( String preferenceToUpdate,
            String newPreferenceValue )
    {
        preferenceEditor_.putString( preferenceToUpdate, newPreferenceValue );

        preferenceEditor_.commit();
        Toast.makeText(
                getApplicationContext(),
                preferenceToUpdate + " has been updated!", Toast.LENGTH_SHORT )
                .show();

        preference_ =
                getSharedPreferences( getString( R.string.pref_title_file ),
                        Context.MODE_PRIVATE );
    }

    /**
     * Closes the settings activity when the main activity first initializes it,
     * if all of the required settings are value.
     */
    private void closeSettingsOnCreation()
    {
        if ( allPreferencesSet() )
        {
            MainPage.inputCorrect = true;
            finish();
        }
    }

    /**
     * Handles inserting the array into the listview
     */
    private class StableArrayAdapter extends ArrayAdapter<String>
    {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter( Context context, int textViewResourceId,
                List<String> objects )
        {
            super( context, textViewResourceId, objects );
            for ( int i = 0; i < objects.size(); ++i )
            {
                mIdMap.put( objects.get( i ), i );
            }
        }

        @Override
        public long getItemId( int position )
        {
            String item = getItem( position );
            return mIdMap.get( item );
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }
    }

    /**
     * Checks to see if a game time has been set that's not the default
     * 
     * @return whether a game time number has been entered
     */
    public boolean isWebsitePrefernceSet()
    {
        return !getString( R.string.pref_title_sites ).contains(
                preference_.getString(
                        getString( R.string.pref_title_sites ),
                        getString( R.string.pref_title_sites ) ) );
    }

    /**
     * Checks to see if a name has been set that's not the default
     * 
     * @return whether a custom name has been entered
     */
    public boolean isNamePreferenceSet()
    {
        return !getString( R.string.pref_title_name ).contains(
                preference_.getString( getString( R.string.pref_title_name ),
                        getString( R.string.pref_title_name ) ) );
    }

    /**
     * Checks to see if there is a valid name entered, and raises a toast for
     * the three possible cases:
     * 
     * First case: a name and game time needs to entered.
     * 
     * Second case: a name needs to be entered.
     * 
     * Third case:game time needs to be entered.
     * 
     * @param validGameLengthExists
     *            if this is true then a game length needs to be entered
     */
    private void
            checkForValidNameAndMakeToast( boolean validPhoneNumberExists )
    {
        if ( !isNamePreferenceSet() )
        {
            handleUpdatePreferenceSelection( 0 );

            // First case: a name and a phone number needs to entered.
            if ( validPhoneNumberExists )
            {
                Toast.makeText( getApplicationContext(),
                        "Please enter a valid name and check your websites.",
                        Toast.LENGTH_LONG ).show();
            }
            else
            // Second case: a name needs to be entered.
            {
                Toast.makeText( getApplicationContext(),
                        "Please enter a valid name.", Toast.LENGTH_LONG )
                        .show();
            }
        }
    }

    /**
     * Checks to make sure a valid phone number and name is entered. If a phone
     * number is not entered then it checks to see if the current device is a
     * tablet. If it is a tablet then bring up the settings for the user to
     * enter their phone number. Else it's a phone, so set the phone number
     * preference to the device's phone number. If the name is not set then
     * bring up the name preference dialog
     */
    private void checkForValidPreferences()
    {
        // Check to see if a phone number preference is saved other than the
        // default
        if ( !isWebsitePrefernceSet() )
        {
            handleUpdatePreferenceSelection( 1 );
            // Tell the user they need to enter a valid phone number and
            // check to see if the user needs to enter a name as well
            checkForValidNameAndMakeToast( true );

            return;
        }

        // Check to see if the user needs to enter a name
        checkForValidNameAndMakeToast( false );
    }

    /**
     * @param context
     *            context of the current device being used
     * @return if it's a tablet or not
     */
    public static boolean isTablet( Context context )
    {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Checks to see if all of the preferences are set
     * 
     * @return if the required preferences are set
     */
    public boolean allPreferencesSet()
    {
        if ( isNamePreferenceSet() )
        {
            if ( isWebsitePrefernceSet() )
            {
                return true;
            }
        }

        return false;
    }
}
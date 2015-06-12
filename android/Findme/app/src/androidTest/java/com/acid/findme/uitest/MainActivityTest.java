package com.acid.findme.uitest;

import android.net.ConnectivityManager;
import android.test.ActivityInstrumentationTestCase2;

import com.acid.findme.ErrorActivity;
import com.acid.findme.MainActivity;
import com.acid.findme.R;
import com.acid.findme.ResultActivity;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo hanSolo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        hanSolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {

    }

    public void testGoButton() {
        hanSolo.waitForText(hanSolo.getString(R.string.button_go));
        hanSolo.clickOnButton(hanSolo.getString(R.string.button_go));
        hanSolo.assertCurrentActivity("wrong activity", ResultActivity.class);

    }

    public void testDataDisabled() {
        hanSolo.setMobileData(false);
        hanSolo.sleep(8000);
        hanSolo.finishOpenedActivities();
        this.launchActivity("com.acid.findme", MainActivity.class, null);
        hanSolo.sleep(2000);
        hanSolo.assertCurrentActivity("wrong activity", ErrorActivity.class);
        hanSolo.setMobileData(true);
        hanSolo.sleep(8000);
    }

    public void testClickFilterLatLngDialog() {
        hanSolo.clickOnMenuItem(hanSolo.getString(R.string.action_filterlatlng));

        assertTrue("Could not find the dialog!", hanSolo.searchText(hanSolo.getString(R.string.dialog_filterLatLng_title)));
    }

    public void testClickSettings() {
        hanSolo.clickOnMenuItem(hanSolo.getString(R.string.action_settings));

        assertTrue("Could not find the dialog!", hanSolo.searchText("Einstellungen"));
    }

}
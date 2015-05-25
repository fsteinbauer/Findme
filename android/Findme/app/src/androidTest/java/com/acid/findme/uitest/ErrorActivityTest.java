package com.acid.findme.uitest;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.acid.findme.ErrorActivity;
import com.acid.findme.MainActivity;
import com.acid.findme.R;
import com.acid.findme.ResultActivity;
import com.robotium.solo.Solo;

public class ErrorActivityTest extends ActivityInstrumentationTestCase2<ErrorActivity> {

    private Solo hanSolo;

    public ErrorActivityTest() {
        super(ErrorActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        hanSolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {

    }

    public void testRestartButton() {
        hanSolo.waitForText(hanSolo.getString(R.string.button_restart));
        hanSolo.clickOnButton(hanSolo.getString(R.string.button_restart));
        // TODO can't assert on Crashing APP
    }

}
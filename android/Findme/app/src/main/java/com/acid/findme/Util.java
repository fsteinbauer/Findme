package com.acid.findme;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by florian on 22.04.2015.
 */
public class Util {
    public static void startErrorActivity(Activity originActivity, String errorMessage ){
        Intent intent = new Intent(originActivity, ErrorActivity.class);
        intent.putExtra(Var.ERROR_ID, errorMessage);
        originActivity.startActivity(intent);
    }

    public static void startErrorActivity(Activity originActivity, int errorMessageId ){
        startErrorActivity(originActivity, originActivity.getResources().getString(errorMessageId));
    }

    public static void startErrorActivity(Activity originActivity ){
        Intent intent = new Intent(originActivity, ErrorActivity.class);
        originActivity.startActivity(intent);
    }
}

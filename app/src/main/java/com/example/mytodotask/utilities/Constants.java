package com.example.mytodotask.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Constants {
    public static final String DATABASE_NAME = "TaskDB";

    public static boolean isEmptyOrNull(String string) {
        return string == null || string.isEmpty() || string.matches("");
    }

    public static void showMessage(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

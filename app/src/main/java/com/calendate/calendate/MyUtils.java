package com.calendate.calendate;

import android.content.Context;
import com.beardedhen.androidbootstrap.BootstrapButton;


/**
 * Created by Hilay on 18-יוני-2017.
 */

public class MyUtils {

    public static String fixEmail(String s) {
        return s.replace(".", ",");
    }

    public static void fixBootstrapButton(Context context, BootstrapButton button) {
        button.setBootstrapBrand(new CustomBootstrapStyle(context));
    }
}

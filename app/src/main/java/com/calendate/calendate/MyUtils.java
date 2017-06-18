package com.calendate.calendate;

import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Created by Hilay on 18-יוני-2017.
 */

public class MyUtils {

    public static String fixEmail(String s){
        return s.replace(".",",");
    }
}

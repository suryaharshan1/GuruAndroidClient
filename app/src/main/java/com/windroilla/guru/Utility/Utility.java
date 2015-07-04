package com.windroilla.guru.Utility;

import android.net.Uri;

import java.io.File;

/**
 * Created by Surya Harsha Nunnaguppala on 2/7/15.
 */
public class Utility {
    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }
}

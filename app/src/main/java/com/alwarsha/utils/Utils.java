package com.alwarsha.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

/**
 * Created by Farid on 4/24/14.
 */
public class Utils {

    public static Bitmap getBitmapFromStorage(String fileName) {
        Bitmap image = null;
        String deviceImageInSD = Environment.getExternalStorageDirectory()
                .toString()
                + File.separator + "MyDir" + File.separator
                + fileName + ".jpg";

        Bitmap deviceBitmap = null;
        try {
            deviceBitmap = BitmapFactory.decodeFile(deviceImageInSD);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                deviceBitmap = BitmapFactory.decodeFile(deviceImageInSD,
                        options);
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
            }
        }
        return deviceBitmap;
    }

    public static File getFileFromStorage(String fileName) {
        Bitmap image = null;
        String deviceImageInSD = Environment.getExternalStorageDirectory()
                .toString()
                + File.separator + "MyDir" + File.separator
                + fileName;

        File deviceFile = new File(deviceImageInSD);

        return deviceFile;
    }
}

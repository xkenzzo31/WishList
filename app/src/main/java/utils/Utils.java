package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.SyncStateContract;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by lucas on 23/11/2017.
 */

public final class Utils {
    private Utils() {}

    public static float dpToPixels(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static Bitmap resizeBitmap(Context context,Bitmap bitmap){

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int heightScreen =  (metrics.heightPixels /3) - (int) dpToPixels(10,context);
        int widthScreen = metrics.widthPixels - (int) dpToPixels(30,context);
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        int new_width = 0;
        int new_height = 0;

        // first check if we need to scale width
        if (bmWidth > widthScreen) {
            //scale width to fit
            new_width = widthScreen;
            //scale height to maintain aspect ratio
            new_height = (new_width * bmHeight) / bmWidth;
        }

        // then check if we need to scale even with the new height
        if (bmHeight > heightScreen) {
            //scale height to fit instead
            new_height = heightScreen;
            //scale width to maintain aspect ratio
            new_width = (new_height * bmWidth) / bmHeight;
        }


        // "RECREATE" THE NEW BITMAP
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
        return resized;
    }


}

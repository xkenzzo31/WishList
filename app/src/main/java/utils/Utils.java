package utils;

import android.content.Context;
import android.util.DisplayMetrics;

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
}

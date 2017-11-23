package Utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by lucas on 23/11/2017.
 */

public final class Utils {
    public static float dpToPixels(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

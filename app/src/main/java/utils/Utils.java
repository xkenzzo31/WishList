package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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

    public static Bitmap resizeBitmap(@NonNull Bitmap bitmap, int screenWidth, int screenHeight){


        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        int new_width = 0;
        int new_height = 0;

        // first check if we need to scale width
        if (bmWidth > screenWidth) {
            //scale width to fit
            new_width = screenWidth;
            //scale height to maintain aspect ratio
            new_height = (new_width * bmHeight) / bmWidth;
        }

        // then check if we need to scale even with the new height
        if (bmHeight > screenHeight) {
            //scale height to fit instead
            new_height = screenHeight;
            //scale width to maintain aspect ratio
            new_width = (new_height * bmWidth) / bmHeight;
        }
        if (bmWidth < screenWidth) {
            //scale height to fit instead
            new_width = screenWidth ;
            //scale width to maintain aspect ratio
            new_height = (new_width * bmHeight) / bmWidth;
        }

        if (bmHeight < screenHeight) {
            //scale height to fit instead
            new_height = screenHeight;
            //scale width to maintain aspect ratio
            new_width = (new_height * bmWidth) / bmHeight;
        }


        // "RECREATE" THE NEW BITMAP

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
            return resized;

    }
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public static class GetScreenSize {
        Context context;
        DisplayMetrics metrics;

        public GetScreenSize(Context context) {
            this.context = context;
            getMetrics();
        }
        private DisplayMetrics getMetrics(){
            metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            return metrics;
        }

        public int getScreenHeight(){
            return  metrics.heightPixels;
        }

        public int getScreenWidht(){
            return metrics.widthPixels;
        }


    }


}

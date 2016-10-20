package pl.marcinwroblewski.e_miasto.Bitmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Admin on 18.10.2016.
 */

public class BitmapScaler {

    public static Bitmap scaleTo1920(Bitmap original) {
        Log.d("Bitmap scaler (before)", "Width: " + original.getWidth() + ", height: " + original.getHeight());

        int widerSide = (original.getWidth() > original.getHeight())
                ? original.getWidth() : original.getHeight();

        if(widerSide <= 1920) return original;

        float scaleFactor = 1920f/(float)(widerSide);
        Log.d("Bitmap scaler (after)", "Width: " + (int)(original.getWidth()*scaleFactor) + ", height: " + (int)(original.getHeight()*scaleFactor) + ", scaleFactor: " + scaleFactor);
        return Bitmap.createScaledBitmap(original, (int)(original.getWidth()*scaleFactor), (int)(original.getHeight()*scaleFactor), false);
    }

}

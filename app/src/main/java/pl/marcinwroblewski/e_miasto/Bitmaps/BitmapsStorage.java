package pl.marcinwroblewski.e_miasto.Bitmaps;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Marcin Wróblewski on 16.10.2016.
 */

public class BitmapsStorage {

    private Context context;

    public BitmapsStorage(Context context) {
        this.context = context;
    }


    public String saveToInternalStorage(Bitmap bitmapImage, String name){
        bitmapImage = BitmapScaler.scaleTo1920(bitmapImage);

        Log.d("Saving bitmap", name);
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create imageDir
        File mypath = new File(directory, name + ".jpg");

        Log.d("Saving bitmap", mypath.toString());

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d("Save bitmap", mypath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public File loadImageFromStorage(String path, String name) {
        File image = new File(path, name + ".jpg");
        Log.d("Load bitmap", image.toString());
        return image;
    }

    public Bitmap fileToBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public boolean deleteImageFromStorage(File image) {
        return image.delete();
    }

}

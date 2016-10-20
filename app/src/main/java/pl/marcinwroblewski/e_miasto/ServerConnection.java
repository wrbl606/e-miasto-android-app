package pl.marcinwroblewski.e_miasto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import pl.marcinwroblewski.e_miasto.Bitmaps.BitmapToBase64;

/**
 * Created by Admin on 17.10.2016.
 */

public class ServerConnection {

    private Context context;
    private String userLogin, userPassword;

    public ServerConnection() {}

    public ServerConnection(Context context, String userLogin, String userPassword) {
        this.context = context;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    private String getBasicAuth() {
        return "Basic " + Base64.encodeToString((userLogin + ":" + userPassword).getBytes(), Base64.NO_WRAP);
    }

    public void sendBitmap(Bitmap bitmap) {
        Log.d("Server connection", "Send bitmap: " + BitmapToBase64.convert(bitmap));
    }

    public static JSONObject getComplain(long id) throws JSONException {
        return new JSONObject("{\"complain\":{\"id\":\"1\",\"date_complain\":\"2000-01-01\",\"date_resolve\":null,\"complain_acceptance\":true,\"complain_title\":\"Example\",\"complain_gps_longitude\":\"43.233445\",\"complain_gps_latitude\":\"24.3232323\",\"complain_description\":\"Example description can be long as fk\",\"complain_images\":[\"complain\\/uploads\\/234432432_324312.jpg\",\"complain\\/uploads\\/4324234_124.jpg\"]}}");
    }
}

package pl.marcinwroblewski.e_miasto;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by wrbl on 19.10.16.
 */

public class ComplainsStorage {

    public static void saveComplain(Context context, Complain complain) throws JSONException, IOException {
        JSONObject complainJSON = new JSONObject();
        complainJSON.put("id", complain.getId());
        complainJSON.put("title", complain.getTitle());
        complainJSON.put("imageLocal", complain.getPhoto().getAbsoluteFile().toString());
        complainJSON.put("content", complain.getContent());
        complainJSON.put("dateCreated", complain.getDateCreated().getTime());
        complainJSON.put("dateSolved", complain.getDateSolved().getTime());
        complainJSON.put("accepted", complain.isAccepted());

        File complainFile = new File(context.getFilesDir(), "complain" + complain.getId() + ".dat");
        FileOutputStream fos = new FileOutputStream(complainFile, false);
        fos.write(complainJSON.toString().getBytes());
    }

    public static Complain loadComplain(Context context, long complainId) throws FileNotFoundException, JSONException {
        File eventFile = new File(context.getFilesDir(), "complain" + complainId + ".dat");

        Scanner scanner = new Scanner(eventFile, "UTF-8" );
        String text = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

        JSONObject complainJSON = new JSONObject(text);

        Complain complain = new Complain(
                complainJSON.getLong("id"),
                complainJSON.getString("title"),
                complainJSON.getString("content"),
                new File(complainJSON.getString("imageLocal")),
                complainJSON.getBoolean("accepted"),
                new Date(complainJSON.getLong("dateCreated")),
                new Date(complainJSON.getLong("dateSolved"))
        );
        return complain;
    }
}

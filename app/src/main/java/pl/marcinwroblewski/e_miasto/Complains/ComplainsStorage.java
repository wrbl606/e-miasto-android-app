package pl.marcinwroblewski.e_miasto.Complains;

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
        complainJSON.put("imagePath", complain.getPhoto());
        complainJSON.put("content", complain.getContent());

        if(complain.getDateCreated() != null)
            complainJSON.put("dateCreated", complain.getDateCreated().getTime());
        else
            complainJSON.put("dateCreated", 0);
        if(complain.getDateSolved() != null)
            complainJSON.put("dateSolved", complain.getDateSolved().getTime());
        else
            complainJSON.put("dateSolved", 0);

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
                complainJSON.getString("imagePath"),
                complainJSON.getBoolean("accepted"),
                new Date(complainJSON.getLong("dateCreated")),
                new Date(complainJSON.getLong("dateSolved"))
        );
        return complain;
    }
}
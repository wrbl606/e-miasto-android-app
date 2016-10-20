package pl.marcinwroblewski.e_miasto.Events;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Admin on 17.10.2016.
 */

public class EventsStorage {

    public static void saveEvent(Context context, Event event) throws JSONException, IOException {
        JSONObject eventJSON = new JSONObject();
        eventJSON.put("id", event.getId());
        eventJSON.put("name", event.getName());
        eventJSON.put("imageLocal", event.getImage().getAbsoluteFile().toString());

        JSONArray intrestsJSON = new JSONArray();
        for(String intrest : event.getIntrests())
            intrestsJSON.put(intrest);

        eventJSON.put("intrests", intrestsJSON);
        eventJSON.put("description", event.getDescription());

        File eventFile = new File(context.getFilesDir(), "event" + event.getId() + ".dat");
        FileOutputStream fos = new FileOutputStream(eventFile, false);
        fos.write(eventJSON.toString().getBytes());
    }

    public static Event loadEvent(Context context, long eventId) throws FileNotFoundException, JSONException {
        File eventFile = new File(context.getFilesDir(), "event" + eventId + ".dat");

        Scanner scanner = new Scanner(eventFile, "UTF-8" );
        String text = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

        JSONObject eventJSON = new JSONObject(text);

        Set<String> intrests = new HashSet<>();
        JSONArray intrestsJSON = eventJSON.getJSONArray("intrests");

        for (int i = 0; i < intrestsJSON.length(); i++) {
            intrests.add(intrestsJSON.getString(i));
        }

        Event event = new Event(
                eventJSON.getString("name"),
                eventJSON.getLong("id"),
                new File(eventJSON.getString("imageLocal")),
                intrests,
                eventJSON.getString("description"));
        return event;
    }

}

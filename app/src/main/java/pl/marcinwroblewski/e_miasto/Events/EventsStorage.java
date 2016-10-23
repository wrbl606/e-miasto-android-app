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
        if(context == null) return;

        JSONObject eventJSON = new JSONObject();
        eventJSON.put("id", event.getId());
        eventJSON.put("name", event.getName());
        eventJSON.put("imagePath", event.getImage());
        eventJSON.put("date", event.getDate());

        JSONArray interestsJSON = new JSONArray();
        for(String interest : event.getIntrests())
            interestsJSON.put(interest);

        eventJSON.put("interests", interestsJSON);
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

        Set<String> interests = new HashSet<>();
        JSONArray interestsJSON = eventJSON.getJSONArray("interests");

        for (int i = 0; i < interestsJSON.length(); i++) {
            interests.add(interestsJSON.getString(i));
        }

        Event event = new Event(
                eventJSON.getString("name"),
                eventJSON.getLong("id"),
                eventJSON.getString("imagePath"),
                interests,
                eventJSON.getString("description"),
                eventJSON.getString("date").substring(0, 9));
        return event;
    }

}

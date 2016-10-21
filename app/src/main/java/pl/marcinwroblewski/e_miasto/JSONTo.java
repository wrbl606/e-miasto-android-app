package pl.marcinwroblewski.e_miasto;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pl.marcinwroblewski.e_miasto.Complains.Complain;
import pl.marcinwroblewski.e_miasto.Complains.ComplainsStorage;
import pl.marcinwroblewski.e_miasto.Events.Event;
import pl.marcinwroblewski.e_miasto.Events.EventsStorage;

/**
 * Created by Admin on 21.10.2016.
 */

public class JSONTo {

    public static ArrayList<Event> events(Context context, JSONArray array) throws JSONException, IOException {
        ArrayList<Event> events = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject eventJSON = array.getJSONObject(i);

            Set<String> interests = new HashSet<>();
            for(int j = 0; j < eventJSON.getJSONArray("interests").length() - 1; j++) {
                JSONObject interestJSON = eventJSON.getJSONArray("interests").getJSONObject(i);
                interests.add(interestJSON.getString("name"));
            }
            String imagePath = "";
            if(eventJSON.getJSONArray("images").length() > 0) {
                JSONObject imageJSON = (eventJSON.getJSONArray("images")).getJSONObject(0);
                imagePath = "http://188.137.38.116:5000/uploads/parties/" + imageJSON.get("name");
            }

            events.add(new Event(
                    eventJSON.getString("title"),
                    eventJSON.getLong("id"),
                    imagePath,
                    interests,
                    eventJSON.getString("description")
            ));

            EventsStorage.saveEvent(context, new Event(
                    eventJSON.getString("title"),
                    eventJSON.getLong("id"),
                    imagePath,
                    interests,
                    eventJSON.getString("description")
            ));
        }

        return events;
    }

    public static ArrayList<Complain> complains(Context context, JSONArray array) throws JSONException, IOException {
        ArrayList<Complain> complains = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject complainJSON = array.getJSONObject(i);

            String imagePath = "";
            if(complainJSON.getJSONArray("images").length() > 0) {
                JSONObject imageJSON = (complainJSON.getJSONArray("images")).getJSONObject(0);
                imagePath = "http://188.137.38.116:5000/uploads/complains/" + imageJSON.get("name");
            }

            complains.add(new Complain(
                    complainJSON.getLong("id"),
                    complainJSON.getString("title"),
                    complainJSON.getString("description"),
                    imagePath,
                    complainJSON.getBoolean("acceptance"),
                    null,
                    null
            ));

            ComplainsStorage.saveComplain(context, new Complain(
                    complainJSON.getLong("id"),
                    complainJSON.getString("title"),
                    complainJSON.getString("description"),
                    imagePath,
                    complainJSON.getBoolean("acceptance"),
                    null,
                    null
            ));
        }

        return complains;
    }
}

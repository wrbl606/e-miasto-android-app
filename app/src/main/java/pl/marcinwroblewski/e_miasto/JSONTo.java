package pl.marcinwroblewski.e_miasto;

import android.content.Context;
import android.util.Log;

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
 * Created by Marcin Wróblewski on 21.10.2016.
 */

public class JSONTo {

    public static ArrayList<Event> events(Context context, JSONArray array) throws JSONException, IOException {
        ArrayList<Event> events = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject eventJSON = array.getJSONObject(i);

            Set<String> interests = new HashSet<>();
            JSONArray interestsJSON = eventJSON.getJSONArray("interests");
            Log.d("interests", interestsJSON.toString() + " => " + interestsJSON.length());
            for(int j = 0; j < interestsJSON.length(); j++) {
                JSONObject interestJSON = interestsJSON.getJSONObject(j);
                interests.add(interestJSON.getString("name"));
            }
            String imagePath = "";
            if(eventJSON.getJSONArray("images").length() > 0) {
                JSONObject imageJSON = (eventJSON.getJSONArray("images")).getJSONObject(0);
                imagePath = "http://188.137.38.116:5000/uploads/parties/" + imageJSON.get("name");
            }

            Event event = new Event(
                    eventJSON.getString("title"),
                    eventJSON.getLong("id"),
                    imagePath,
                    interests,
                    eventJSON.getString("description"),
                    eventJSON.getString("party_date"));

            events.add(event);
            EventsStorage.saveEvent(context,event);
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

            Complain complain = new Complain(
                    complainJSON.getLong("id"),
                    complainJSON.getString("title"),
                    complainJSON.getString("description"),
                    imagePath,
                    complainJSON.getBoolean("acceptance"),
                    complainJSON.getString("date_complain"),
                    null
            );

            complains.add(complain);
            ComplainsStorage.saveComplain(context, complain);
        }

        return complains;
    }
}

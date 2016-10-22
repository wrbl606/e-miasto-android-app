package pl.marcinwroblewski.e_miasto.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.io.FileNotFoundException;

import pl.marcinwroblewski.e_miasto.Events.Event;
import pl.marcinwroblewski.e_miasto.Events.EventsStorage;
import pl.marcinwroblewski.e_miasto.IntrestsViewGenerator;
import pl.marcinwroblewski.e_miasto.R;

public class EventDetailsActivity extends AppCompatActivity {

    private long eventId;
    private Event event;
    private ImageView mainEventImage;
    private TextView description, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(getIntent().hasExtra("eventId")) {
            eventId = getIntent().getLongExtra("eventId", -1);
            Log.d("Event details", "Event id: " + eventId);
            try {
                event = EventsStorage.loadEvent(getApplicationContext(), eventId);
            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
            }
        }

        toolbar.setTitle(event.getName());
        setSupportActionBar(toolbar);

        mainEventImage = (ImageView) findViewById(R.id.event_main_image);
        description = (TextView) findViewById(R.id.event_description);
        description.setText(event.getDescription());

        date = (TextView) findViewById(R.id.event_date);
        date.setText(event.getDate());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().displayImage(event.getImage(), mainEventImage);

        LinearLayout interestsContainer = (LinearLayout) findViewById(R.id.event_intrests);
        interestsContainer.removeAllViews();
        for(String interest : event.getIntrests())
            interestsContainer.addView(IntrestsViewGenerator.getView(getApplicationContext(), interest));
    }
}

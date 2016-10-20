package pl.marcinwroblewski.e_miasto.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    private TextView description;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(getApplicationContext()).load(event.getImage()).into(mainEventImage);

        LinearLayout intrestsContainer = (LinearLayout) findViewById(R.id.event_intrests);
        intrestsContainer.removeAllViews();
        for(String intrest : event.getIntrests())
            intrestsContainer.addView(IntrestsViewGenerator.getView(getApplicationContext(), intrest));
    }
}

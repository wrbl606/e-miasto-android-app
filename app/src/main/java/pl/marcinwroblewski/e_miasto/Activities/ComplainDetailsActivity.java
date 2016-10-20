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

import pl.marcinwroblewski.e_miasto.Complain;
import pl.marcinwroblewski.e_miasto.ComplainsStorage;
import pl.marcinwroblewski.e_miasto.Events.Event;
import pl.marcinwroblewski.e_miasto.Events.EventsStorage;
import pl.marcinwroblewski.e_miasto.IntrestsViewGenerator;
import pl.marcinwroblewski.e_miasto.R;

public class ComplainDetailsActivity extends AppCompatActivity {

    private long complainId;
    private Complain complain;
    private ImageView mainEventImage;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(getIntent().hasExtra("complainId")) {
            complainId = getIntent().getLongExtra("complainId", -1);
            Log.d("Complain details", "Complain id: " + complainId);
            try {
                complain = ComplainsStorage.loadComplain(getApplicationContext(), complainId);
            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
            }
        }

        toolbar.setTitle(complain.getTitle());
        setSupportActionBar(toolbar);

        mainEventImage = (ImageView) findViewById(R.id.event_main_image);
        description = (TextView) findViewById(R.id.event_description);
        description.setText(complain.getContent());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(getApplicationContext()).load(complain.getPhoto()).into(mainEventImage);
    }
}

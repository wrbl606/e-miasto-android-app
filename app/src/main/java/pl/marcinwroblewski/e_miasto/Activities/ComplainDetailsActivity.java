package pl.marcinwroblewski.e_miasto.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

import java.io.FileNotFoundException;

import pl.marcinwroblewski.e_miasto.Complains.Complain;
import pl.marcinwroblewski.e_miasto.Complains.ComplainsStorage;
import pl.marcinwroblewski.e_miasto.R;

public class ComplainDetailsActivity extends AppCompatActivity {

    private long complainId;
    private Complain complain;
    private ImageView mainEventImage;
    private TextView description, dateCreated;
    private View acceptance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_details);
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

        mainEventImage = (ImageView) findViewById(R.id.complain_main_image);
        description = (TextView) findViewById(R.id.complain_description);
        description.setText(complain.getContent());

        dateCreated = (TextView) findViewById(R.id.complain_date);
        dateCreated.setText(complain.getDateCreated());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().displayImage(complain.getPhoto(), mainEventImage);

        if(complain.isAccepted()) {
            acceptance = findViewById(R.id.acceptance);
            acceptance.setVisibility(View.VISIBLE);
        }
    }
}

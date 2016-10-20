package pl.marcinwroblewski.e_miasto.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import pl.marcinwroblewski.e_miasto.R;

public class RegisterActivity extends AppCompatActivity {

    public static RegisterActivity instance;

    private Handler firstRegistrationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View firstStepButton = findViewById(R.id.register_send_email_and_password);
        firstStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFirstStepAsyncTask firstStepAsyncTask = new RegisterFirstStepAsyncTask();
                firstStepAsyncTask.execute();
                Log.d("Clicked", "!");
            }
        });

        instance = this;
    }

    public void showSecondCard() {
        CardView secondRegisterStepCard = (CardView) findViewById(R.id.second_card);
        secondRegisterStepCard.setVisibility(View.VISIBLE);

        RegisterSecondStepAsyncTask secondStepAsyncTask = new RegisterSecondStepAsyncTask();
        secondStepAsyncTask.execute();
    }

    public void setupSecondCardCheckboxes(List<JSONObject> checkboxes) throws JSONException {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        LinearLayout checkboxesContainer = (LinearLayout) findViewById(R.id.checkboxes_container);

        for(int i = 0; i < checkboxes.size(); i++) {
            Log.d("Checkboxes", checkboxes.get(i).getString("name"));
            AppCompatCheckBox checkBox = (AppCompatCheckBox) inflater.inflate(R.layout.checkbox, null);
            checkBox.setText("|" + checkboxes.get(i).getString("name"));
            checkBox.setTag("" + checkboxes.get(i).getLong("id"));

            checkboxesContainer.addView(checkBox);
        }
    }

    class RegisterFirstStepAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("First step", "Register");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSecondCard();
                }
            });

            return null;
        }


    }

    class RegisterSecondStepAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("Second step", "Register");

            final ArrayList<JSONObject> intrestsList = new ArrayList<>();

            for(int i = 0; i < 30; i++) {
                try {
                    JSONObject intrests = new JSONObject();
                    intrests.put("id", i);
                    intrests.put("name", "interest " + i);
                    intrestsList.add(intrests);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setupSecondCardCheckboxes(intrestsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }
}

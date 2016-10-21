package pl.marcinwroblewski.e_miasto.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import pl.marcinwroblewski.e_miasto.Fragments.Dialog.ErrorDialogFragment;
import pl.marcinwroblewski.e_miasto.R;
import pl.marcinwroblewski.e_miasto.Requests;

public class RegisterActivity extends AppCompatActivity {

    private EditText login, password;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);

        View firstStepButton = findViewById(R.id.register_send_email_and_password);
        firstStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterFirstStepAsyncTask firstStepAsyncTask = new RegisterFirstStepAsyncTask();
                firstStepAsyncTask.execute(
                        login.getText().toString(),
                        password.getText().toString()
                );
                Log.d("Clicked", "!");
            }
        });

        View secondStepButton = findViewById(R.id.send_intrests);
        secondStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getIntrestsSelected().size() <= 0) {
                    showDialog("Proszę wskazać chociaż jedno zainteresowanie");
                    return;
                }

                int[] ids = new int[getIntrestsSelected().size()];
                int counter = 0;
                for(int value : getIntrestsSelected()) {
                    Log.d("Interest selected", "" + value);
                    ids[counter] = value;
                    counter++;
                }
                SendInterestsAsyncTask interestsAsyncTask = new SendInterestsAsyncTask();
                interestsAsyncTask.execute();
            }
        });

        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = preferences.edit();
    }

    void showDialog(String text) {
        DialogFragment newFragment = ErrorDialogFragment.newInstance(text);
        newFragment.show(getSupportFragmentManager(), "Error");
    }

    public void showSecondCard() {
        View firstLoadingScreen = findViewById(R.id.first_card_loading);
        firstLoadingScreen.setVisibility(View.GONE);

        CardView secondRegisterStepCard = (CardView) findViewById(R.id.second_card);
        secondRegisterStepCard.setVisibility(View.VISIBLE);

        DownloadInterestsAsyncTask secondStepAsyncTask = new DownloadInterestsAsyncTask();
        secondStepAsyncTask.execute();
    }

    public void setupSecondCardCheckboxes(JSONArray checkboxes) throws JSONException {
        View secondLoadingScreen = findViewById(R.id.second_card_loading);
        secondLoadingScreen.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        LinearLayout checkboxesContainer = (LinearLayout) findViewById(R.id.checkboxes_container);

        for(int i = 0; i < checkboxes.length(); i++) {
            Log.d("Checkboxes", (checkboxes.getJSONObject(i)).getString("name"));
            CheckBox checkBox = (CheckBox) inflater.inflate(R.layout.checkbox, null);
            checkBox.setText("" +(checkboxes.getJSONObject(i)).getString("name"));
            checkBox.setTag("" + (checkboxes.getJSONObject(i)).getLong("id"));

            checkboxesContainer.addView(checkBox);
        }
    }

    public ArrayList<Integer> getIntrestsSelected() {
        LinearLayout checkboxesContainer = (LinearLayout) findViewById(R.id.checkboxes_container);
        ArrayList<Integer> values = new ArrayList<>();

        for(int i = 0; i < checkboxesContainer.getChildCount(); i++) {
            CheckBox currentCheckbox = ((CheckBox)checkboxesContainer.getChildAt(i));
            if(currentCheckbox.isChecked())
                values.add(Integer.parseInt(currentCheckbox.getTag().toString()));
        }

        return values;
    }

    public void showRegisterError() {
        login.setError("Błędne dane");
        View firstLoadingScreen = findViewById(R.id.first_card_loading);
        firstLoadingScreen.setVisibility(View.GONE);
    }

    class RegisterFirstStepAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("First step", "Register");

            String login = strings[0];
            String password = strings[1];

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View firstLoadingScreen = findViewById(R.id.first_card_loading);
                    firstLoadingScreen.setVisibility(View.VISIBLE);
                }
            });

            String response = Requests.createUser(login, password);

            if(response == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRegisterError();
                    }
                });
                return null;
            }

            editor.putString("login", login);
            editor.putString("password", password);
            editor.apply();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSecondCard();
                }
            });

            return null;
        }


    }

    class DownloadInterestsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("Second step", "Register");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View secondLoadingScreen = findViewById(R.id.second_card_loading);
                    secondLoadingScreen.setVisibility(View.VISIBLE);
                }
            });

            String login = preferences.getString("login", "");
            String password = preferences.getString("password", "");
            Requests requests = new Requests(login, password);
            String interestsResponse = requests.getAllInterest();
            Log.d("All interests", interestsResponse);

            try {
                final JSONArray interestsJSON = new JSONArray(interestsResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setupSecondCardCheckboxes(interestsJSON);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    class SendInterestsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View secondLoadingScreen = findViewById(R.id.second_card_loading);
                    secondLoadingScreen.setVisibility(View.VISIBLE);
                    View firstLoadingScreen = findViewById(R.id.first_card_loading);
                    firstLoadingScreen.setVisibility(View.VISIBLE);
                }
            });

            String login = preferences.getString("login", "");
            String password = preferences.getString("password", "");
            Requests requests = new Requests(login, password);

            int[] idsInt = new int[getIntrestsSelected().size()];
            for(int i = 0; i < getIntrestsSelected().size(); i++) {
                idsInt[i] = getIntrestsSelected().get(i);
            }

            String addInterestsResponse = requests.addInterests(idsInt);
            Log.d("Add interests", addInterestsResponse);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return null;
        }
    }
}

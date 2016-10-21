package pl.marcinwroblewski.e_miasto.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import pl.marcinwroblewski.e_miasto.R;
import pl.marcinwroblewski.e_miasto.Requests;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        login = (EditText)findViewById(R.id.login_et);
        password = (EditText)findViewById(R.id.password_et);
        View loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                loginAsyncTask.execute(
                        login.getText().toString(),
                        password.getText().toString()
                );
            }
        });
    }

    public void showLoadingIndicator() {
        View loadingIndicator = findViewById(R.id.loading_screen);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    public void showLoginError() {
        View loadingIndicator = findViewById(R.id.loading_screen);
        loadingIndicator.setVisibility(View.GONE);
        login.setError("Nieprawidłowe dane");
    }

    class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            String login = strings[0];
            String password = strings[1];

            Requests requests = new Requests(login, password);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoadingIndicator();
                }
            });

            String response = requests.getCurrentUser();

            if(response == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoginError();
                    }
                });
                return false;
            }

            Log.d("Login", "" + response);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return true;



        }
    }
}

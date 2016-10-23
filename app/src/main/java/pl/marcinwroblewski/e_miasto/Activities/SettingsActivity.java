package pl.marcinwroblewski.e_miasto.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import pl.marcinwroblewski.e_miasto.R;

public class SettingsActivity extends AppCompatActivity {

    private AppCompatCheckBox notificationsCheckBox;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = preferences.edit();


        notificationsCheckBox = (AppCompatCheckBox) findViewById(R.id.notifications_checkbox);
        notificationsCheckBox.setChecked(preferences.getBoolean("notifications", true));
        notificationsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("notifications", b);
            }
        });

        View loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("login");
                editor.remove("password");
                editor.apply();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        setCurrentUser(preferences.getString("login", "☺"));
    }

    void setCurrentUser(String currentUser) {
        TextView currentUserTV = (TextView) findViewById(R.id.current_user);
        currentUserTV.setText(currentUser);
    }

}

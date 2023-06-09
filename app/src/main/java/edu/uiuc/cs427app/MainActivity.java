package edu.uiuc.cs427app;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private final String SHOW_DETAILS = "WEATHER";
    private final String MAP_BUTTON_TEXT = "MAP";

    private String username;
    private UserProvider userProvider;
    private int check = 0;

    // Sets up the MainActivity and constructs the UserProvider
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        userProvider = new UserProvider(this, username);
        userProvider.initializeTheme(userProvider, this);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name)+'-'+username);

        Set<String> cities = userProvider.getCities();
        for (String city : cities) {
            createCityLayout(city);
        }
    }

    // onClick handler for the MainActivity
    // Handles "Add A Location" button and all "Show Details" buttons for every city
    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.buttonAddLocation) {
            intent = new Intent(this, AddLocationActivity.class);
            intent.putExtra("username", username);
        } else if (view.getId() == R.id.buttonSignOff) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            String buttonText = ((TextView)view).getText().toString();
            String city = (String)view.getTag();
            if (buttonText.equals(SHOW_DETAILS)) {
                intent = new Intent(this, DetailsActivity.class);
            } else {
                intent = new Intent(this, MapActivity.class);
            }
            intent.putExtra("city", city);
            intent.putExtra("username", username);
        }
        startActivity(intent);
    }

    // Creates and formats horizontal linear layout to house the city text and "Show Details" button
    private void createCityLayout(String city) {
        LinearLayout childLayout = new LinearLayout(this);
        childLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView text = createCityTextView(userProvider.getCityById(city).getCityName());
        childLayout.addView(text);

        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1));
        childLayout.addView(spacer);

        Button mapButton = createMapButton(city);
        childLayout.addView(mapButton);

        Button showDetailsButton = createShowDetailsButton(city);
        childLayout.addView(showDetailsButton);

        LinearLayout parentLayout = findViewById(R.id.citiesLayout);
        parentLayout.addView(childLayout);
    }

    // Creates the "Show Details" button with proper formatting
    private Button createShowDetailsButton(String city) {
        Button showDetailsButton = new MaterialButton(this);
        showDetailsButton.setText(SHOW_DETAILS);
        showDetailsButton.setOnClickListener(this);
        showDetailsButton.setTag(city);
        showDetailsButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        return showDetailsButton;
    }

    // Creates the text which display the city name with proper formatting
    private TextView createCityTextView(String city) {
        TextView text = new MaterialTextView(this);
        text.setText(city);
        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        return text;
    }

    // Creates button for application
    private Button createMapButton(String city) {
        Button mapButton = new MaterialButton(this);
        mapButton.setText(MAP_BUTTON_TEXT);
        mapButton.setOnClickListener(this);
        mapButton.setTag(city);
        mapButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        return mapButton;
    }
}


package com.example.ubinone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ubinone.R;
import com.example.ubinone.databinding.ActivityMainBinding;
import com.example.ubinone.utils.HTMLRequester;
import com.example.ubinone.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final Object token = new Object();
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private HTMLRequester htmlRequester = null;
    private TextView nav_class;
    private SharedPreferences sharedPrefs = null;
    private SharedPreferences.Editor editor = null;
    private String sharedClass;
    private FloatingActionButton actionButton;
    private String TAG = "MainActivity";

    public TextView getNav_class() {
        return nav_class;
    }

    public Object getToken() {
        return token;
    }

    public void setSharedClass(String sharedClass) {
        this.sharedClass = sharedClass;
    }

    public HTMLRequester getHtmlRequester() {
        return htmlRequester;
    }

    public void setHtmlRequester(HTMLRequester htmlRequester) {
        this.htmlRequester = htmlRequester;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPrefs.edit();
        sharedClass = sharedPrefs.getString("sharedClass", "None");

        String appLang = sharedPrefs.getString("sharedLang", Locale.getDefault().getLanguage());
        editor.putString("sharedLang", appLang).commit();
        Utils.setLocale(this, appLang);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionButton = findViewById(R.id.app_bar_main).findViewById(R.id.fab);
        TooltipCompat.setTooltipText(actionButton, "Send feedback");

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "No implemented yet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        nav_class = navigationView.getHeaderView(0).findViewById(R.id.shared_class);
        nav_class.setText(sharedClass);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_cremi, R.id.nav_date, R.id.nav_rooms).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(item -> {
            settingsAction();
            return true;
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void settingsAction() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
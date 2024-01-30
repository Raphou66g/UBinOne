package com.example.ubinone.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;
import com.example.ubinone.databinding.ActivitySettingsBinding;
import com.example.ubinone.recyclers.settings.RecyclerSettingsAdapter;
import com.example.ubinone.recyclers.settings.RecyclerSettingsItem;
import com.example.ubinone.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    List<RecyclerSettingsItem> items = new ArrayList<>();
    private ActivitySettingsBinding binding;
    private String TAG = "SettingsActivity";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String sharedLang;
    private int selected;
    private RecyclerView recyclerView;

    public static void restart(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);

        mainIntent.setPackage(context.getPackageName());
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPrefs.edit();
        sharedLang = sharedPrefs.getString("sharedLang", "None");

        String appLang = sharedPrefs.getString("sharedLang", Locale.getDefault().getLanguage());
        editor.putString("sharedLang", sharedLang).apply();
        Utils.setLocale(this, appLang);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarSettings.toolbar);
        setupActionBar();

        recyclerView = findViewById(R.id.recycler_settings);
        populateRecycler();

        Log.i(TAG, "end:" + Locale.getDefault().getLanguage());
    }

    private void populateRecycler() {
        items.add(new RecyclerSettingsItem(getString(R.string.language), createAndInitializeSpinnerLanguage()));
        items.add(new RecyclerSettingsItem(getString(R.string.reset_preferences), createAndInitializeButtonReset()));


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerSettingsAdapter(this, items));
    }

    private Button createAndInitializeButtonReset() {
        Button button = new Button(this);
        int color = Color.parseColor("#ff0000");
        button.setTextColor(Utils.invertedColor(color));
        button.setText(R.string.reset);
        button.setBackground(AppCompatResources.getDrawable(this, R.drawable.reset_button));
        button.setPaddingRelative(10, 0, 10, 0);

        button.setOnClickListener(view -> {
            Toast.makeText(this, getString(R.string.hold_to_reset), Toast.LENGTH_SHORT).show();
        });
        button.setOnLongClickListener(v -> {
            alertReset();
            return true;
        });

        return button;
    }

    private void alertReset() {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(getString(R.string.reset));
        alertBox.setMessage(R.string.do_you_really_want_to_reset);

        SpannableString spanString = new SpannableString(getString(R.string.apply));
        spanString.setSpan(new ForegroundColorSpan(Color.RED), 0, spanString.length(), 0); //fix the color to white

        alertBox.setPositiveButton(spanString, (dialog, which) -> {
            editor.clear().commit();
            restart(getApplicationContext());
        });
        alertBox.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            super.getOnBackPressedDispatcher();
        });

        AlertDialog dialog = alertBox.create();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setOnShowListener(dialog1 -> findViewById(R.id.settings_layout).getForeground().setTint(Color.parseColor("#66000000")));
        dialog.setOnDismissListener(dialog1 -> findViewById(R.id.settings_layout).getForeground().setTint(Color.parseColor("#00000000")));
        dialog.show();
    }

    private Spinner createAndInitializeSpinnerLanguage() {
        Spinner spinner = new Spinner(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lang, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(sharedLang.equals(Locale.FRANCE.getLanguage()) ? 1 : 0);
        selected = spinner.getSelectedItemPosition();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinnerItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return spinner;
    }

    private void handleSpinnerItemSelected(int position) {
        if (position != selected) {
            sharedLang = position == 0 ? Locale.ENGLISH.getLanguage() : Locale.FRANCE.getLanguage();
            editor.putString("sharedLang", sharedLang);
            editor.commit();
            restart(getApplicationContext());
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}

package com.example.ubinone.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;
import com.example.ubinone.activities.MainActivity;
import com.example.ubinone.autocomplete.AutoCompleteAdapter;
import com.example.ubinone.databinding.FragmentHomeBinding;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaAdapter;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaBase;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaEmpty;
import com.example.ubinone.utils.HTMLRequester;
import com.example.ubinone.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class HomeFragment extends Fragment {

    private final Object htmlToken = new Object();
    private final Semaphore semaphore = new Semaphore(0);
    private String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HTMLRequester htmlRequester;
    private AutoCompleteTextView autoComplete;
    private RecyclerView recyclerView;
    private MainActivity activity;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String sharedClass;
    private TextView day;

    private void htmlInit() throws IOException {
        if (activity.getHtmlRequester() == null) {
            htmlRequester = new HTMLRequester(requireContext());
            activity.setHtmlRequester(htmlRequester);
            htmlRequester.requestData();
        } else {
            htmlRequester = activity.getHtmlRequester();
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = (MainActivity) getActivity();

        recyclerView = root.findViewById(R.id.recycler);
        day = root.findViewById(R.id.day);

        sharedPrefs = requireActivity().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPrefs.edit();
        sharedClass = sharedPrefs.getString("sharedClass", "None");

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                htmlInit();
                autoComplete = root.findViewById(R.id.auto_text);
                activity.runOnUiThread(this::setupAutoComplete);
                semaphore.acquire();
                htmlRequester.requestForClass(sharedClass, null);
                activity.runOnUiThread(this::updateUI);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return root;
    }

    private void updateUI() {
        recyclerView.removeAllViews();

        List<RecyclerAgendaBase> items = Utils.resultInterpreter(htmlRequester);

        if (Objects.equals(items.size(), 0)) {
            items.add(new RecyclerAgendaEmpty(getString(R.string.no_course_today)));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerAgendaAdapter(requireContext(), items));

        day.setText(htmlRequester.getDay());
    }

    private void setupAutoComplete() {
        ArrayAdapter<String> adapter = new AutoCompleteAdapter(requireContext(), android.R.layout.simple_list_item_1, htmlRequester.getClasses());

        autoComplete.setAdapter(adapter);
        if (!Objects.equals(sharedClass, "None")) {
            autoComplete.setText(sharedClass);
            clearFocus();
        }
        autoComplete.setOnItemClickListener((parent, view, position, id) -> {
            clearFocus();
            sharedClass = adapter.getItem(position);
            sharedClass = sharedClass == null ? "None" : sharedClass;
            editor.putString("sharedClass", sharedClass);
            editor.commit();
            activity.getNav_class().setText(sharedClass);
            Toast.makeText(requireContext(), R.string.class_picked, Toast.LENGTH_SHORT).show();
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    htmlRequester.requestForClass(sharedClass, null);
                    activity.runOnUiThread(this::updateUI);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        autoComplete.setSelectAllOnFocus(true);

        semaphore.release();

    }

    private void clearFocus() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);
        autoComplete.clearFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
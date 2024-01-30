package com.example.ubinone.ui.rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ubinone.activities.MainActivity;
import com.example.ubinone.databinding.FragmentRoomsBinding;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaBase;
import com.example.ubinone.utils.HTMLRequester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class RoomsFragment extends Fragment {

    private static final String TAG = "RoomsFragment";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private MainActivity activity;
    private HTMLRequester htmlRequester;
    private FragmentRoomsBinding binding;
    private String[] ids = new String[]{"a9", "a22", "a29", " a28 salle "};
    private Map<String, Map<String, List<RecyclerAgendaBase>>> rooms;
    private Semaphore init = new Semaphore(0);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRoomsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = (MainActivity) getActivity();
        htmlRequester = activity.getHtmlRequester();

        separateBatRooms();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                //await separation
                init.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //TODO after separation

            rooms.forEach((bat, places) -> {

            });
        });

        return root;
    }

    private void separateBatRooms() {
        Executors.newSingleThreadExecutor().execute(() -> {
            rooms = new HashMap<>();

            Arrays.stream(ids).iterator().forEachRemaining(id -> {
                if (id.startsWith(" ")) {
                    rooms.put("CREMI", htmlRequester.getRooms().stream().filter(s -> s.toLowerCase().contains(id)).collect(Collectors.toMap(s -> s, s -> new ArrayList<>())));
                } else {
                    rooms.put(id.toUpperCase(), htmlRequester.getRooms().stream().filter(s -> s.toLowerCase().startsWith(id)).collect(Collectors.toMap(s -> s, s -> new ArrayList<>())));
                }
            });

            init.release();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
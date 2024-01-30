package com.example.ubinone.ui.cremi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;
import com.example.ubinone.activities.MainActivity;
import com.example.ubinone.databinding.FragmentCremiBinding;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaBase;
import com.example.ubinone.recyclers.cremi.RecyclerCremiAdapter;
import com.example.ubinone.recyclers.cremi.RecyclerCremiItem;
import com.example.ubinone.utils.HTMLRequester;
import com.example.ubinone.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class CremiFragment extends Fragment {
    private String TAG = "CremiFragment";

    private MainActivity activity;
    private HTMLRequester htmlRequester;
    private List<String> roomsAll;
    private List<String> rooms0;
    private List<String> rooms1;
    private List<String> rooms2;
    private Semaphore writer = new Semaphore(1), client = new Semaphore(0), ui = new Semaphore(0);
    private FragmentCremiBinding binding;

    private List<RecyclerCremiItem> recyclerViewList;
    private RecyclerView recycler;
    private ArrayList<RadioButton> listOfRadioButtons;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCremiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = (MainActivity) getActivity();

        htmlRequester = activity.getHtmlRequester();

        roomsAll = htmlRequester.getRooms().stream().filter(str -> str.toLowerCase().contains("a28 salle")).collect(Collectors.toList());
        rooms0 = roomsAll.stream().filter(str -> str.contains("00")).collect(Collectors.toList());
        rooms1 = roomsAll.stream().filter(str -> str.contains("10")).collect(Collectors.toList());
        rooms2 = roomsAll.stream().filter(str -> str.contains("20")).collect(Collectors.toList());

        recycler = root.findViewById(R.id.recycler_cremi);

        RadioGroup group = root.findViewById(R.id.btn_group);
        int count = group.getChildCount();
        listOfRadioButtons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View o = group.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton) o);
            }
        }

        group.setOnCheckedChangeListener((group1, checkedId) -> {

            listOfRadioButtons.forEach(btn -> btn.setEnabled(false));

            List<String> toUse;
            if (checkedId == R.id.btn_overall) {
                toUse = roomsAll;
            } else if (checkedId == R.id.btn_floor0) {
                toUse = rooms0;
            } else if (checkedId == R.id.btn_floor1) {
                toUse = rooms1;
            } else {
                toUse = rooms2;
            }

            generateRecyclers(toUse);

            updateRecycler();

        });

        return root;
    }

    private void generateRecyclers(List<String> toUse) {
        Executors.newSingleThreadExecutor().execute(() -> {
            recyclerViewList = new ArrayList<>();

            toUse.forEach(str -> {

                try {
                    writer.acquire();
                    Log.i(TAG, str);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        htmlRequester.requestForRoom(str);
                        client.release();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                activity.runOnUiThread(() -> {
                    try {
                        client.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    List<RecyclerAgendaBase> items = Utils.resultInterpreter(htmlRequester);
                    Log.i(TAG, items.toString());
                    String[] name = str.split(" A28 ");
                    recyclerViewList.add(new RecyclerCremiItem(name[name.length - 1].trim(), items));

                    //here -> animation
//                    recycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//                    recycler.setAdapter(new RecyclerCremiAdapter(requireContext(), recyclerViewList));

                    writer.release();
                });
            });

            ui.release();
        });
    }

    private void updateRecycler() {
        recycler.removeAllViews();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ui.acquire();
                activity.runOnUiThread(() -> {
                    recycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                    recycler.setAdapter(new RecyclerCremiAdapter(requireContext(), recyclerViewList));
                    listOfRadioButtons.forEach(btn -> btn.setEnabled(true));
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
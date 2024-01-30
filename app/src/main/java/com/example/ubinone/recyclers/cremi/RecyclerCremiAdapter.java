package com.example.ubinone.recyclers.cremi;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaAdapter;

import java.util.List;

public class RecyclerCremiAdapter extends RecyclerView.Adapter<RecyclerCremiHolder> {
    private Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<RecyclerCremiItem> items;

    public RecyclerCremiAdapter(Context context, List<RecyclerCremiItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerCremiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerCremiHolder(LayoutInflater.from(context).inflate(R.layout.cremi_sub, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCremiHolder holder, int position) {
        RecyclerCremiItem cremiItem = items.get(position);

        holder.name.setText(cremiItem.getRoomName());
        holder.name.setGravity(Gravity.CENTER);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(cremiItem.getRecyclerItemList().size());

        RecyclerAgendaAdapter adapter = new RecyclerAgendaAdapter(context, cremiItem.getRecyclerItemList());

        holder.childRecycler.setLayoutManager(layoutManager);
        holder.childRecycler.setAdapter(adapter);
        holder.childRecycler.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

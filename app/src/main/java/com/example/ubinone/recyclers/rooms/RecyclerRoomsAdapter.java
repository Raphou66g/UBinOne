package com.example.ubinone.recyclers.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

import java.util.List;

public class RecyclerRoomsAdapter extends RecyclerView.Adapter<RecyclerRoomsHolder> {
    Context context;
    List<RecyclerRoomsItem> items;

    RecyclerView.RecycledViewPool pool;

    public RecyclerRoomsAdapter(Context context, List<RecyclerRoomsItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerRoomsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerRoomsHolder(LayoutInflater.from(context).inflate(R.layout.rooms_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerRoomsHolder holder, int position) {
        holder.name.setText(items.get(position).getBat());
        holder.gridView.addView(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

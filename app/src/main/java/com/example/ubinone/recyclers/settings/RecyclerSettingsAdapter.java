package com.example.ubinone.recyclers.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

import java.util.List;

public class RecyclerSettingsAdapter extends RecyclerView.Adapter<RecyclerSettingsHolder> {
    Context context;
    List<RecyclerSettingsItem> items;

    public RecyclerSettingsAdapter(Context context, List<RecyclerSettingsItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerSettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerSettingsHolder(LayoutInflater.from(context).inflate(R.layout.tab_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSettingsHolder holder, int position) {
        holder.desc.setText(items.get(position).getDescription());
        holder.action.addView(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

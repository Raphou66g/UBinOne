package com.example.ubinone.recyclers.rooms;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

public class RecyclerRoomsHolder extends RecyclerView.ViewHolder {

    TextView name;
    GridView gridView;

    public RecyclerRoomsHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.room_name);
        gridView = itemView.findViewById(R.id.room_table);
    }
}

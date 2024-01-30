package com.example.ubinone.recyclers.cremi;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

public class RecyclerCremiHolder extends RecyclerView.ViewHolder {

    TextView name;
    RecyclerView childRecycler;

    public RecyclerCremiHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_room);
        childRecycler = itemView.findViewById(R.id.child_recycler);
    }
}

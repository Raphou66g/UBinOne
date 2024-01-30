package com.example.ubinone.recyclers.agenda;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

public class RecyclerAgendaHolder extends RecyclerView.ViewHolder {

    TextView datetime;
    TextView content;
    View container;

    public RecyclerAgendaHolder(@NonNull View itemView) {
        super(itemView);
        datetime = itemView.findViewById(R.id.overview_tab_datetime);
        content = itemView.findViewById(R.id.overview_tab_content);
        container = itemView;
    }
}

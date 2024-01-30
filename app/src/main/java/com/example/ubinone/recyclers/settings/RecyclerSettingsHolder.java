package com.example.ubinone.recyclers.settings;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

public class RecyclerSettingsHolder extends RecyclerView.ViewHolder {

    TextView desc;
    FrameLayout action;

    public RecyclerSettingsHolder(@NonNull View itemView) {
        super(itemView);
        desc = itemView.findViewById(R.id.description_settings);
        action = itemView.findViewById(R.id.frame_settings);
    }
}

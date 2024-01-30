package com.example.ubinone.recyclers.settings;

import android.view.View;

public class RecyclerSettingsItem {
    String description;
    View content;

    public RecyclerSettingsItem(String description, View content) {
        this.description = description;
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public View getContent() {
        return content;
    }

    public void setContent(View content) {
        this.content = content;
    }
}

package com.example.ubinone.recyclers.rooms;

import android.view.View;

public class RecyclerRoomsItem {
    String bat;
    View content;

    public RecyclerRoomsItem(String bat, View content) {
        this.bat = bat;
        this.content = content;
    }

    public String getBat() {
        return bat;
    }

    public void setBat(String bat) {
        this.bat = bat;
    }

    public View getContent() {
        return content;
    }

    public void setContent(View content) {
        this.content = content;
    }
}

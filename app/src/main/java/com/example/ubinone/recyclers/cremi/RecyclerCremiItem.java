package com.example.ubinone.recyclers.cremi;

import com.example.ubinone.recyclers.agenda.RecyclerAgendaBase;

import java.util.List;

public class RecyclerCremiItem {

    private String roomName;
    private List<RecyclerAgendaBase> recyclerItemList;

    public RecyclerCremiItem(String roomName, List<RecyclerAgendaBase> recyclerItemList) {
        this.roomName = roomName;
        this.recyclerItemList = recyclerItemList;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<RecyclerAgendaBase> getRecyclerItemList() {
        return recyclerItemList;
    }

    public void setRecyclerItemList(List<RecyclerAgendaBase> recyclerItemList) {
        this.recyclerItemList = recyclerItemList;
    }
}

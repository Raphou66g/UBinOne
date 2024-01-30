package com.example.ubinone.recyclers.agenda;

import com.example.ubinone.utils.Utils;

public class RecyclerAgendaItem implements RecyclerAgendaBase {
    String datetime;
    String start, end;
    boolean allDay;
    String content;
    String color;

    public RecyclerAgendaItem(String start, String end, boolean allDay, String content, String color) {
        this.start = start.replace("T", " ");
        this.end = end.replace("T", " ");
        this.allDay = allDay;
        this.content = content;
        this.color = color;
        setDatetime(Utils.getDateTime(this.start, this.end));
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

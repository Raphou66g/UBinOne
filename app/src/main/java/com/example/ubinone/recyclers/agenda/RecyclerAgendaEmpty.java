package com.example.ubinone.recyclers.agenda;

public class RecyclerAgendaEmpty implements RecyclerAgendaBase {
    String content;

    public RecyclerAgendaEmpty(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

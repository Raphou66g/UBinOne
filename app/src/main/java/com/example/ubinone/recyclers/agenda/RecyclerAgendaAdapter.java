package com.example.ubinone.recyclers.agenda;

import static com.example.ubinone.utils.Utils.invertedColor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubinone.R;

import java.util.List;

public class RecyclerAgendaAdapter extends RecyclerView.Adapter<RecyclerAgendaHolder> {
    Context context;
    List<RecyclerAgendaBase> items;

    public RecyclerAgendaAdapter(Context context, List<RecyclerAgendaBase> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerAgendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (items.get(0) instanceof RecyclerAgendaItem) {
            return new RecyclerAgendaHolder(LayoutInflater.from(context).inflate(R.layout.tab_overview, parent, false));
        } else {
            return new RecyclerAgendaHolder(LayoutInflater.from(context).inflate(R.layout.overview_empty, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAgendaHolder holder, int position) {
        if (items.get(position) instanceof RecyclerAgendaItem) {
            SpannableString content = new SpannableString(((RecyclerAgendaItem) items.get(position)).getDatetime());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            holder.datetime.setText(content);
            holder.datetime.setTypeface(null, Typeface.BOLD);

            int color = Color.parseColor(((RecyclerAgendaItem) items.get(position)).getColor());
            holder.container.setBackgroundColor(color);

            int inv = invertedColor(color);
            holder.datetime.setTextColor(inv);
            holder.content.setTextColor(inv);
        }
        holder.content.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

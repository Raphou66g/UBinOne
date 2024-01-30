package com.example.ubinone.autocomplete;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    List<String> allCodes;
    List<String> originalCodes;
    StringFilter filter;

    public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.allCodes = this.originalCodes = objects;
    }

    public int getCount() {
        return allCodes == null ? 0 : allCodes.size();
    }

    public String getItem(int position) {
        return allCodes == null ? "None" : allCodes.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new StringFilter();
    }

    private class StringFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<String> list = originalCodes;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<>(count);
            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allCodes = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }

}

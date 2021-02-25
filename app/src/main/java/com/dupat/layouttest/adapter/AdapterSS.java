package com.dupat.layouttest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dupat.layouttest.R;

import java.util.List;

public class AdapterSS extends RecyclerView.Adapter<AdapterSS.ViewHolder> {

    Context ctx;
    int[] list;

    public AdapterSS(Context ctx, int[] list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterSS.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_ss,parent,false);
        return new AdapterSS.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSS.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

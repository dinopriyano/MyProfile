package com.dupat.layouttest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dupat.layouttest.DetailPortfolio;
import com.dupat.layouttest.R;
import com.dupat.layouttest.model.ModelPortfolio;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPortfolio extends RecyclerView.Adapter<AdapterPortfolio.ViewHolder> {

    List<ModelPortfolio> list;
    Context ctx;

    public AdapterPortfolio(List<ModelPortfolio> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public AdapterPortfolio.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_portfolio,parent,false);
        return new AdapterPortfolio.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterPortfolio.ViewHolder holder, int position) {
        final ModelPortfolio model = list.get(position);
        holder.descApps.setText(model.getDesc());
        holder.nameApps.setText(model.getNama());
        if(model.getIcon() == R.drawable.ic_android)
            holder.iconApps.setPadding(20,20,20,20);

        Picasso.get().load(model.getIcon()).into(holder.iconApps);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, DetailPortfolio.class);
                intent.putExtra("name",model.getNama());
                intent.putExtra("icon",model.getIcon());
                intent.putExtra("desc",model.getDesc());
                intent.putExtra("ss",model.getSs());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconApps;
        TextView nameApps,descApps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconApps = itemView.findViewById(R.id.iconApps);
            nameApps = itemView.findViewById(R.id.nameApps);
            descApps = itemView.findViewById(R.id.descApps);
        }
    }
}

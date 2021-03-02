package com.dupat.layouttest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dupat.layouttest.DisplayContactActivity;
import com.dupat.layouttest.R;
import com.dupat.layouttest.model.ContactModel;

import java.util.List;

public class ContactAdapter extends BaseAdapter {

    List<ContactModel> list;
    Context ctx;

    public ContactAdapter(List<ContactModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ContactModel model = list.get(i);

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.item_list_contact,viewGroup,false);
        }

        TextView txtName = view.findViewById(R.id.txtName);
        txtName.setText(model.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id_To_Search = model.getId();

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(ctx, DisplayContactActivity.class);

                intent.putExtras(dataBundle);
                ctx.startActivity(intent);
            }
        });

        return view;
    }
}

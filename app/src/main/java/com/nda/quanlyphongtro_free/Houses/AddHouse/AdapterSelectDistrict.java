package com.nda.quanlyphongtro_free.Houses.AddHouse;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.R;

import java.util.List;

public class AdapterSelectDistrict extends RecyclerView.Adapter<AdapterSelectDistrict.HolderAddHouse> {
    Context context;
    List<String> stringList;
    Dialog dialog;
    TextView txt_selectQuanHuyen;

    public AdapterSelectDistrict(Context context, List<String> stringList, Dialog dialog,
                                 TextView txt_selectQuanHuyen) {
        this.context = context;
        this.stringList = stringList;
        this.dialog = dialog;
        this.txt_selectQuanHuyen = txt_selectQuanHuyen;
    }

    @NonNull
    @Override
    public HolderAddHouse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simple_text, parent, false);
        return new HolderAddHouse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAddHouse holder, int position) {
        String str = stringList.get(position);

        holder.txt_simpleStringItem.setText(str);
        holder.txt_simpleStringItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_selectQuanHuyen.setText(str);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class HolderAddHouse extends RecyclerView.ViewHolder {
        TextView txt_simpleStringItem;

        public HolderAddHouse(@NonNull View itemView) {
            super(itemView);

            txt_simpleStringItem = itemView.findViewById(R.id.txt_simpleStringItem);
        }
    }
}

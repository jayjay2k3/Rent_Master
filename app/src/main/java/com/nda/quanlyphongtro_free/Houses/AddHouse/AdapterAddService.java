package com.nda.quanlyphongtro_free.Houses.AddHouse;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterAddService extends RecyclerView.Adapter<AdapterAddService.HolderAddHouse> {
    AddHouse context;
    List<Service> serviceList;
    List<Service> checkedServiceList;

    public AdapterAddService(AddHouse context, List<Service> serviceList, List<Service> checkedServiceList) {
        this.context = context;
        this.serviceList = serviceList;
        this.checkedServiceList = checkedServiceList;
    }

    @NonNull
    @Override
    public HolderAddHouse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_services_add_house, parent, false);
        return new HolderAddHouse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAddHouse holder, int position) {
        Service service = serviceList.get(position);

        holder.txtServicesName.setText(service.getName());


        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(service.getPrice());
        holder.txtServicesCost.setText(formatter.format(cost) + " đ/" + service.getUnit());

        // Check if the item is checked or not, if yes, check box
        // will be ticked
        for (Service checkedService : checkedServiceList)
        {
            if (checkedService.getId().equals(service.getId()))
            {
                holder.checkBox_service.setChecked(true);
            }
        }

        holder.checkBox_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox_service.isChecked())
                {
                    checkedServiceList.add(service);
                }
                else {
                    checkedServiceList.remove(service);

                    for (Service checkedService : checkedServiceList)
                    {
                        if (checkedService.getId().equals(service.getId()))
                        {
                            holder.checkBox_service.setChecked(true);
                            Toast.makeText(context, "Warning : Hãy xóa dịch vụ này ở ngoài !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                context.showCheckedServices();

            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class HolderAddHouse extends RecyclerView.ViewHolder {
        CheckBox checkBox_service;
        TextView txtServicesName, txtServicesCost;

        public HolderAddHouse(@NonNull View itemView) {
            super(itemView);

            checkBox_service = itemView.findViewById(R.id.checkBox_service);
            txtServicesName  = itemView.findViewById(R.id.txtServicesName);
            txtServicesCost  = itemView.findViewById(R.id.txtServicesCost);

        }
    }
}

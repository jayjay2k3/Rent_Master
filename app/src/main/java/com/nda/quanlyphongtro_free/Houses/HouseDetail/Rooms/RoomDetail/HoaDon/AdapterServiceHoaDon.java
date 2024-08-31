package com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.AddRoom.AddRoom;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterServiceHoaDon extends RecyclerView.Adapter<AdapterServiceHoaDon.HolderAddHoaDon> {
    Context context;
    List<Service> serviceList;
    List<String> strServiceThanhTien;


    public AdapterServiceHoaDon(Context context, List<Service> serviceList, List<String> strServiceThanhTien) {
        this.context = context;
        this.serviceList = serviceList;
        this.strServiceThanhTien = strServiceThanhTien;
    }

    @NonNull
    @Override
    public AdapterServiceHoaDon.HolderAddHoaDon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_services_add_hoa_don, parent,false);
        return new AdapterServiceHoaDon.HolderAddHoaDon(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterServiceHoaDon.HolderAddHoaDon holder, int position) {
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

        String str = service.getName() + " " + formatter.format(cost) + " đ/" + service.getUnit() + "  :  0 đ";
        strServiceThanhTien.add(str);

        String serviceId = service.getId();
        String splitServiceId[] = serviceId.split("_");
        imageAdap(splitServiceId[0], holder.img_service);

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class HolderAddHoaDon extends RecyclerView.ViewHolder {
        TextView txtServicesName, txtServicesCost;
        ImageView img_service;

        public HolderAddHoaDon(@NonNull View itemView) {
            super(itemView);

            img_service = itemView.findViewById(R.id.img_service);

            txtServicesName = itemView.findViewById(R.id.txtServicesName);
            txtServicesCost = itemView.findViewById(R.id.txtServicesCost);


        }
    }

    private void imageAdap(String signal,  ImageView imgShow) {
        // Set default image
        imgShow.setImageResource(R.drawable.ic_options);

        // Check and assign between image and signal
        if (signal.equals("1"))
        {
            imgShow.setImageResource(R.drawable.ic_electricity);

        }
        if (signal.equals("2"))
        {
            imgShow.setImageResource(R.drawable.ic_water);

        }

        if (signal.equals("3"))
        {
            imgShow.setImageResource(R.drawable.ic_wifi);

        }
        if (signal.equals("4"))
        {
            imgShow.setImageResource(R.drawable.ic_security);

        }
        if (signal.equals("5"))
        {
            imgShow.setImageResource(R.drawable.ic_parking_space);

        }

        if (signal.equals("6"))
        {
            imgShow.setImageResource(R.drawable.ic_sanitation);

        }
        if (signal.equals("7"))
        {
            imgShow.setImageResource(R.drawable.ic_trash);

        }

    }

}

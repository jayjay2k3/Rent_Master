package com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterServiceOfRoom extends RecyclerView.Adapter<AdapterServiceOfRoom.HolderAddHouse> {
    RoomDetailSystem context;
    List<Service> serviceList;

    public AdapterServiceOfRoom(RoomDetailSystem context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public HolderAddHouse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_services_room_system, parent, false);
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

        String serviceId = service.getId();
        String splitServiceId[] = serviceId.split("_");
        imageAdap(splitServiceId[0], holder.img_service);

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class HolderAddHouse extends RecyclerView.ViewHolder {
        ImageView img_service;
        TextView txtServicesName, txtServicesCost;

        public HolderAddHouse(@NonNull View itemView) {
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

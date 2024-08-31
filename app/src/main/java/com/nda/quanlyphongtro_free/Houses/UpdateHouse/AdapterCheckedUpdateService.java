package com.nda.quanlyphongtro_free.Houses.UpdateHouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.Houses.AddHouse.AddHouse;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterCheckedUpdateService extends RecyclerView.Adapter<AdapterCheckedUpdateService.HolderServices> {
    UpdateHouse context;
    List<Service> checkedServiceList;

    public AdapterCheckedUpdateService(UpdateHouse context, List<Service> checkedServiceList) {
        this.context = context;
        this.checkedServiceList = checkedServiceList;
    }

    @NonNull
    @Override
    public AdapterCheckedUpdateService.HolderServices onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checked_services, parent,false);
        return new AdapterCheckedUpdateService.HolderServices(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCheckedUpdateService.HolderServices holder, int position) {
        Service service = checkedServiceList.get(position);

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

        holder.img_removeCheckedService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedServiceList.remove(service);
                context.showCheckedServices();
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkedServiceList.size();
    }


    public class HolderServices extends RecyclerView.ViewHolder {
        LinearLayout ll_serviceItem;
        ImageView img_service, img_removeCheckedService;
        TextView txtServicesName, txtServicesCost;

        public HolderServices(@NonNull View itemView) {
            super(itemView);

            ll_serviceItem = itemView.findViewById(R.id.ll_serviceItem);

            img_service                 = itemView.findViewById(R.id.img_service);
            img_removeCheckedService    = itemView.findViewById(R.id.img_removeCheckedService);

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

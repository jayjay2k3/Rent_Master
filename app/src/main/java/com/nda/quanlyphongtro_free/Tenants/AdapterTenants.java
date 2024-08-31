package com.nda.quanlyphongtro_free.Tenants;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.Model.Tenants;
import com.nda.quanlyphongtro_free.R;

import java.util.List;

public class AdapterTenants extends RecyclerView.Adapter<AdapterTenants.HolderTenants> {
    TenantsSystem context;
    List<Tenants> tenantsList;

    public AdapterTenants(TenantsSystem context, List<Tenants> tenantsList) {
        this.context = context;
        this.tenantsList = tenantsList;
    }

    @NonNull
    @Override
    public AdapterTenants.HolderTenants onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_tenants, parent, false);
        return new AdapterTenants.HolderTenants(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTenants.HolderTenants holder, int position) {
        Tenants tenants = tenantsList.get(position);

        holder.txt_tenantName.setText(tenants.gettName());
        holder.txt_houseName.setText(tenants.gettRentHouse());
        holder.txt_roomName.setText(tenants.gettRentRoom());
        holder.txt_tenantPhonenumber.setText(tenants.gettPhoneNumber());

        holder.txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeCallTenant(tenants.gettPhoneNumber());
            }
        });

        holder.txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.dialogConfirmDeleteTenant(tenants);
            }
        });
    }


    @Override
    public int getItemCount() {
        return tenantsList.size();
    }

    public class HolderTenants extends RecyclerView.ViewHolder {
        TextView txt_tenantName, txt_houseName, txt_roomName, txt_tenantPhonenumber;
        TextView txt_call, txt_update, txt_delete;

        public HolderTenants(@NonNull View itemView) {
            super(itemView);

            txt_tenantName = itemView.findViewById(R.id.txt_tenantName);
            txt_houseName = itemView.findViewById(R.id.txt_houseName);
            txt_roomName = itemView.findViewById(R.id.txt_roomName);
            txt_tenantPhonenumber = itemView.findViewById(R.id.txt_tenantPhonenumber);

            txt_call = itemView.findViewById(R.id.txt_call);
            txt_update = itemView.findViewById(R.id.txt_update);
            txt_delete = itemView.findViewById(R.id.txt_delete);

        }
    }

    private void executeCallTenant(String phoneNumber) {
        if (phoneNumber.isEmpty())
        {
            Toast.makeText(context, "Error : Số điện thoại bị bỏ trống", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
            else
            {
                String s = "tel:" + phoneNumber;
                context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(s)));
            }

        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "phone_number"));
    }

}

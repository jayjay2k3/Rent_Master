package com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail;

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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nda.quanlyphongtro_free.Model.HoaDon;
import com.nda.quanlyphongtro_free.Model.Tenants;
import com.nda.quanlyphongtro_free.R;

import java.util.List;

public class AdapterTenants extends RecyclerView.Adapter<AdapterTenants.HolderTenants> {
    RoomDetailSystem context;
    List<Tenants> tenantsList;

    public AdapterTenants(RoomDetailSystem context, List<Tenants> tenantsList) {
        this.context = context;
        this.tenantsList = tenantsList;
    }

    @NonNull
    @Override
    public AdapterTenants.HolderTenants onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tenants, parent, false);
        return new AdapterTenants.HolderTenants(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTenants.HolderTenants holder, int position) {
        Tenants tenants = tenantsList.get(position);

        holder.txt_tenantName.setText(tenants.gettName());
        holder.txt_tenantPhonenumber.setText(tenants.gettPhoneNumber());
        holder.txt_tenantRentHouseAndRoom.setText(tenants.gettRentHouse() + ", " + tenants.gettRentRoom());

        holder.cv_tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetTenantOption(tenants);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tenantsList.size();
    }

    public class HolderTenants extends RecyclerView.ViewHolder {
        CardView cv_tenant;
        TextView txt_tenantName, txt_tenantPhonenumber, txt_tenantRentHouseAndRoom;

        public HolderTenants(@NonNull View itemView) {
            super(itemView);

            cv_tenant = itemView.findViewById(R.id.cv_tenant);
            txt_tenantName        = itemView.findViewById(R.id.txt_tenantName);
            txt_tenantPhonenumber = itemView.findViewById(R.id.txt_tenantPhonenumber);
            txt_tenantRentHouseAndRoom  = itemView.findViewById(R.id.txt_tenantRentHouseAndRoom);

        }
    }

    private void bottomSheetTenantOption(Tenants tenants) {
        View view = context.getLayoutInflater().inflate(R.layout.bottomsheet_tenant_option,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        TextView txt_callTenant = view.findViewById(R.id.txt_callTenant);
        TextView txt_editTenant = view.findViewById(R.id.txt_editTenant);
        TextView txt_deleteTenant = view.findViewById(R.id.txt_deleteTenant);

        CardView cv_closeBottomSheet = view.findViewById(R.id.cv_closeBottomSheet);

        txt_callTenant.setText("Gọi điện (" + tenants.gettPhoneNumber() + " )" );
        txt_callTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeCallTenant(tenants.gettPhoneNumber());
            }
        });

        txt_editTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.editTenant(tenants, bottomSheetDialog);
            }
        });

        txt_deleteTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.dialogConfirmDeleteTenant(tenants, bottomSheetDialog);
            }
        });

        cv_closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

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

package com.nda.quanlyphongtro_free.Tenants;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.RoomDetailSystem;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Tenants;
import com.nda.quanlyphongtro_free.R;

import java.util.ArrayList;
import java.util.List;

public class TenantsSystem extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    ImageView   img_back ;

    RecyclerView rcv_tenants;
    List<Tenants> tenantsList = new ArrayList<>();
    AdapterTenants adapterTenants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tenants);
        initUI();
        init();
        setUpRCV();

    }
    private void setUpRCV() {
        adapterTenants = new AdapterTenants(this, tenantsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        rcv_tenants.setLayoutManager(linearLayoutManager);
        rcv_tenants.setAdapter(adapterTenants);

        displayTenants();

    }


    private void displayTenants()
    {
        tenantsList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Tenants tenants = dataSnapshot.getValue(Tenants.class);
                    tenantsList.add(tenants);
                }

                adapterTenants.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("tenants").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);



    }


    public void dialogConfirmDeleteTenant(Tenants tenants) {
        Dialog dialog = new Dialog(TenantsSystem.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CardView cv_delete = dialog.findViewById(R.id.cv_delete);
        CardView cv_cancel = dialog.findViewById(R.id.cv_cancel);

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("tenants").child(firebaseUser.getUid()).child(tenants.getId()).removeValue();

                displayTenants();
                dialog.dismiss();
            }
        });

        cv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    private void backToMain() {
        startActivity(new Intent(TenantsSystem.this, MainActivity.class));
        TenantsSystem.this.finish();
    }

    private void initUI() {
        img_back =  findViewById(R.id.img_back);

        rcv_tenants = findViewById(R.id.rcv_tenants);
    }

    @Override
    public void onBackPressed() {
        backToMain();
        super.onBackPressed();
    }
}
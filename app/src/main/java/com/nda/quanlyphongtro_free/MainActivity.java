package com.nda.quanlyphongtro_free;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HousesSystem;
import com.nda.quanlyphongtro_free.JoinRoom.JoinRoomSystem;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.Model.Tenants;
import com.nda.quanlyphongtro_free.Model.Users;
import com.nda.quanlyphongtro_free.Note.NoteManagement;
import com.nda.quanlyphongtro_free.Services.ServicesSystem;
import com.nda.quanlyphongtro_free.Tenants.TenantsSystem;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    CardView cvNhaTro, cvDichVu,cv_quanLyTenants, cv_note, cv_joinRoom;

    ImageView img_vipAccount, img_logout;
    TextView txt_userName,txt_accountType, txt_totalHouse,txt_totalTenants,txt_totalServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initiate();

    }
    private void initiate() {
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginSignUpActivity.class));
                finishAffinity();
            }
        });

        cvNhaTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HousesSystem.class));
            }
        });

        cvDichVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicesSystem.class));
            }
        });

        cv_quanLyTenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TenantsSystem.class));
            }
        });

        cv_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteManagement.class));
            }
        });

        cv_joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, JoinRoomSystem.class));
            }
        });

    }


    private void initUI() {
        cvNhaTro    =  findViewById(R.id.cvNhaTro);
        cvDichVu    =  findViewById(R.id.cvDichVu);
        cv_quanLyTenants    =  findViewById(R.id.cv_quanLyTenants);
        cv_note = findViewById(R.id.cv_note);
        cv_joinRoom = findViewById(R.id.cv_joinRoom);

        txt_userName = findViewById(R.id.txt_userName);
        txt_accountType = findViewById(R.id.txt_accountType);

        txt_totalHouse   = findViewById(R.id.txt_totalHouse);
        txt_totalTenants = findViewById(R.id.txt_totalTenants);
        txt_totalServices   =  findViewById(R.id.txt_totalServices);

        img_vipAccount = findViewById(R.id.img_vipAccount);
        img_logout = findViewById(R.id.img_logout);
    }


    @Override
    protected void onStart() {

        // Get information of user
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                txt_userName.setText(users.getName());
                txt_accountType.setText(users.getAccountType());

                if (users.getAccountType().equals("VIP"))
                {
                    img_vipAccount.setVisibility(View.VISIBLE);
                } else {
                    img_vipAccount.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("users").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);

        // Count number of houses
        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Houses> housesList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Houses houses = dataSnapshot.getValue(Houses.class);
                    housesList.add(houses);
                }
                txt_totalHouse.setText(String.valueOf(housesList.size()));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query2 = myRef.child("houses").child(firebaseUser.getUid());
        query2.addListenerForSingleValueEvent(valueEventListener2);

        // Count number of tenants
        ValueEventListener valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Tenants> tenantsList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Tenants tenants = dataSnapshot.getValue(Tenants.class);
                    tenantsList.add(tenants);
                }
                txt_totalTenants.setText(String.valueOf(tenantsList.size()));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query3 = myRef.child("tenants").child(firebaseUser.getUid());
        query3.addListenerForSingleValueEvent(valueEventListener3);

        // Count number of tenants
        ValueEventListener valueEventListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Service> serviceList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Service service = dataSnapshot.getValue(Service.class);
                    serviceList.add(service);
                }
                txt_totalServices.setText(String.valueOf(serviceList.size()));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query4 = myRef.child("services").child(firebaseUser.getUid());
        query4.addListenerForSingleValueEvent(valueEventListener4);


        super.onStart();
    }
}
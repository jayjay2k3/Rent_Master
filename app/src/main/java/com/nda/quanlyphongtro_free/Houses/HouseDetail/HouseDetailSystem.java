package com.nda.quanlyphongtro_free.Houses.HouseDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.UpdateHouse.UpdateHouse;
import com.nda.quanlyphongtro_free.Houses.HousesSystem;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.AddRoom.AddRoom;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.Model.Tenants;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HouseDetailSystem extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    ShimmerFrameLayout shimmer_view_container;

    ImageView imgBack,img_addRoom, img_editHouse;
    TextView txt_houseName, txt_numberOfRoomsInHouse;

    LinearLayout ll_danhSachPhong, ll_chiTietNha, ll_showDanhSachPhong,
            ll_showChiTietNha, ll_optionHouse;
    TextView txt_bgColor1,txt_bgColor2;
    androidx.appcompat.widget.SearchView searchView_searchRoom;
    Houses houses;

    RecyclerView rcv_rooms;

    List<Rooms> roomsList = new ArrayList<>();
    AdapterRoom roomAdapter;

    TextView txt_houseLocation, txt_numberOfRooms, txt_numberOfTenants, txt_numberOfFloors, txt_feeRooms,
            txt_goMoCua, txt_goDongCua, txt_showBaoTrcNgayChuyen, txt_description, txt_note;
    Button btn_deleteHouse;

    List<Service> serviceList = new ArrayList<>();
    AdapterServiceOfHouse adapterServiceInHouse;
    RecyclerView rcv_servicesHouseDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail_system);

        initUI();

        init();
        setupRCV();
    }



    private void setupRCV() {
        roomAdapter = new AdapterRoom(this,roomsList, houses);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        rcv_rooms.setLayoutManager(linearLayoutManager);
        rcv_rooms.setAdapter(roomAdapter);
        displayRooms();
    }

    private void init() {
        houses = getIntent().getParcelableExtra("Data_House_Parcelable");
        txt_houseName.setText(houses.gethName());



        img_addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseDetailSystem.this, AddRoom.class);

                intent.putExtra("Data_House_Parcelable", houses);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHouseSystem();
            }
        });

        ll_danhSachPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll_danhSachPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showDanhSachPhong.setVisibility(View.VISIBLE);
                ll_showChiTietNha.setVisibility(View.GONE);
                searchView_searchRoom.setVisibility(View.VISIBLE);
                txt_bgColor1.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor2.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
        });
        ll_chiTietNha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showChiTietNha.setVisibility(View.VISIBLE);
                ll_showDanhSachPhong.setVisibility(View.GONE);
                searchView_searchRoom.setVisibility(View.GONE);
                txt_bgColor2.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


        btn_deleteHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteHouse();
            }
        });

        img_editHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseDetailSystem.this, UpdateHouse.class);

                intent.putExtra("Data_House_Parcelable", houses);
                startActivity(intent);
            }
        });
    }



    private void displayRooms()
    {
        roomsList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Rooms rooms = dataSnapshot.getValue(Rooms.class);
                    roomsList.add(rooms);
                }

                relatedHouseDetail();

                txt_numberOfRoomsInHouse.setText("Danh sách phòng (" + roomsList.size() + ")");
                roomAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("rooms").child(firebaseUser.getUid()).child(houses.gethId());
        query.addListenerForSingleValueEvent(valueEventListener);



    }


    /***************************
     *
     *
     * (Related) Display house detail
     *
     *
     *************************** */
    private void relatedHouseDetail() {
        txt_houseLocation.setText(houses.gethAddress() + ", " + houses.gethTinhThanhPho() + ", " + houses.gethQuanHuyen());

        txt_numberOfRooms.setText(roomsList.size() + "");

        countTenants();

        txt_numberOfFloors.setText(houses.gethFloorsNumber());

        if (houses.gethFee().equals(""))
        {
            txt_feeRooms.setText("0 đ");
        } else {
            /**
             * Format cost lấy về từ firebase
             * theo định dạng money
             * */
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            Double cost = Double.parseDouble(houses.gethFee());
            txt_feeRooms.setText(formatter.format(cost) + " đ");
        }


        if (houses.gethOpenTime().equals(""))
        {
            txt_goMoCua.setText("-:-");
        } else {
            txt_goMoCua.setText(houses.gethOpenTime());
        }
        if (houses.gethCloseTime().equals(""))
        {
            txt_goDongCua.setText("-:-");
        } else {
            txt_goDongCua.setText(houses.gethCloseTime());
        }

        if (houses.gethBaoSoNgayChuyen().equals(""))
        {
            txt_showBaoTrcNgayChuyen.setText("- ngày");
        } else {
            txt_showBaoTrcNgayChuyen.setText(houses.gethBaoSoNgayChuyen() + " ngày");
        }



        txt_description.setText(houses.gethDescription());
        txt_note.setText(houses.gethNote());

        adapterServiceInHouse = new AdapterServiceOfHouse(this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_servicesHouseDetail.setLayoutManager(linearLayoutManager);
        rcv_servicesHouseDetail.setAdapter(adapterServiceInHouse);

        displayServices();


    }
    private void displayServices() {
        serviceList.clear();

        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Service service = dataSnapshot.getValue(Service.class);

                        serviceList.add(service);
                    }

                    // When get data successfully, hide the shimmer and show all function field
                    searchView_searchRoom.setVisibility(View.VISIBLE);
                    rcv_rooms.setVisibility(View.VISIBLE);
                    img_addRoom.setVisibility(View.VISIBLE);
                    img_editHouse.setVisibility(View.VISIBLE);
                    ll_optionHouse.setVisibility(View.VISIBLE);
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmerAnimation();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("houses").child(firebaseUser.getUid()).child(houses.gethId()).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            startActivity(new Intent(HouseDetailSystem.this, MainActivity.class));
            HouseDetailSystem.this.finish();

            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }

    }


    private void countTenants()
    {
        List<Tenants> tenantsList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Tenants tenants = dataSnapshot.getValue(Tenants.class);
                    tenantsList.add(tenants);
                }

                txt_numberOfTenants.setText(tenantsList.size() + "");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("tenants").child(firebaseUser.getUid()).orderByChild("rentHouseId").equalTo(houses.gethId());
        query.addListenerForSingleValueEvent(valueEventListener);
    }


    public void dialogDeleteHouse() {
        Dialog dialog = new Dialog(HouseDetailSystem.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CardView cv_delete = dialog.findViewById(R.id.cv_delete);
        CardView cv_cancel = dialog.findViewById(R.id.cv_cancel);

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Tenants tenants = dataSnapshot.getValue(Tenants.class);
                            myRef.child("tenants").child(firebaseUser.getUid()).child(tenants.getId())
                                    .removeValue();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
                Query query = myRef.child("tenants").child(firebaseUser.getUid())
                        .orderByChild("rentHouseId").equalTo(houses.gethId());
                query.addListenerForSingleValueEvent(valueEventListener);

                myRef.child("contracts").child(firebaseUser.getUid()).child(houses.gethId()).removeValue();

                myRef.child("receipt").child(firebaseUser.getUid()).child(houses.gethId()).removeValue();

                myRef.child("rooms").child(firebaseUser.getUid()).child(houses.gethId()).removeValue();

                myRef.child("tenants").child(firebaseUser.getUid()).child(houses.gethId()).removeValue();

                myRef.child("houses").child(firebaseUser.getUid()).child(houses.gethId()).removeValue();

                dialog.dismiss();

                backToHouseSystem();
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



    /***************************
     *
     *
     * (Related) Tenant
     *
     *
     *************************** */
    public void countTenants(Rooms rooms, TextView txtShowNumTenantsWithLimit)
    {
        List<Tenants> tenantsList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Tenants tenants = dataSnapshot.getValue(Tenants.class);
                    tenantsList.add(tenants);
                }
                if (Integer.parseInt(rooms.getrLimitTenants()) < tenantsList.size())
                {
                    txtShowNumTenantsWithLimit.setText("Số người : " + tenantsList.size() +
                            "/" + rooms.getrLimitTenants() + " (Số lượng vượt giới hạn) ");
                }
                else {
                    txtShowNumTenantsWithLimit.setText("Số người : " + tenantsList.size() + "/" + rooms.getrLimitTenants());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("tenants").child(firebaseUser.getUid()).orderByChild("rentRoomId").equalTo(rooms.getId());
        query.addListenerForSingleValueEvent(valueEventListener);



    }



    private void initUI() {
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        imgBack         =  findViewById(R.id.imgBack);
        img_addRoom     =  findViewById(R.id.img_addRoom);
        img_editHouse   = findViewById(R.id.img_editHouse);

        rcv_rooms     =  findViewById(R.id.rcv_rooms);

        txt_houseName               =  findViewById(R.id.txt_houseName);
        txt_numberOfRoomsInHouse    = findViewById(R.id.txt_numberOfRoomsInHouse);

        ll_danhSachPhong  =  findViewById(R.id.ll_danhSachPhong);
        ll_chiTietNha     =  findViewById(R.id.ll_chiTietNha);
        txt_bgColor1      =  findViewById(R.id.txt_bgColor1);
        txt_bgColor2      =  findViewById(R.id.txt_bgColor2);
        ll_showDanhSachPhong  =  findViewById(R.id.ll_showDanhSachPhong);
        ll_showChiTietNha     =  findViewById(R.id.ll_showChiTietNha);
        ll_optionHouse        = findViewById(R.id.ll_optionHouse);

        searchView_searchRoom = findViewById(R.id.searchView_searchRoom);

        txt_houseLocation     =  findViewById(R.id.txt_houseLocation);
        txt_numberOfRooms     =  findViewById(R.id.txt_numberOfRooms);
        txt_numberOfTenants     =  findViewById(R.id.txt_numberOfTenants);
        txt_numberOfFloors     =  findViewById(R.id.txt_numberOfFloors);
        txt_feeRooms     =  findViewById(R.id.txt_feeRooms);
        txt_goMoCua     =  findViewById(R.id.txt_goMoCua);
        txt_goDongCua     =  findViewById(R.id.txt_goDongCua);
        txt_showBaoTrcNgayChuyen     =  findViewById(R.id.txt_showBaoTrcNgayChuyen);
        txt_description     =  findViewById(R.id.txt_description);
        txt_note     =  findViewById(R.id.txt_note);

        rcv_servicesHouseDetail     =  findViewById(R.id.rcv_servicesHouseDetail);

        btn_deleteHouse = findViewById(R.id.btn_deleteHouse);
    }


    private void backToHouseSystem() {
        startActivity(new Intent(HouseDetailSystem.this, HousesSystem.class));
        HouseDetailSystem.this.finish();

    }
    @Override
    public void onBackPressed() {
        backToHouseSystem();

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        // Hide all function field and show shimmer effect
        searchView_searchRoom.setVisibility(View.GONE);
        rcv_rooms.setVisibility(View.GONE);
        img_addRoom.setVisibility(View.GONE);
        img_editHouse.setVisibility(View.GONE);
        ll_optionHouse.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        //displayServices();

        super.onStart();

    }
}
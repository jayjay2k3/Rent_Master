package com.nda.quanlyphongtro_free.JoinRoom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.AdapterRoom;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.AdapterServiceOfHouse;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.AddRoom.AddRoom;
import com.nda.quanlyphongtro_free.Houses.HousesSystem;
import com.nda.quanlyphongtro_free.Houses.UpdateHouse.UpdateHouse;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.JoinRoom;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.Model.Tenants;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinRoomSystem extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    ShimmerFrameLayout shimmer_view_container;

    ImageView imgBack, img_joinRoom;
   
    androidx.appcompat.widget.SearchView searchView_searchJoinedRoom;


    ProgressDialog progressDialog;

    RecyclerView rcv_joinedRooms;
    List<JoinRoom> joinRoomList = new ArrayList<>();
    AdapterJoinRoom adapterJoinRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room_system);

        initUI();

        setUpRCV();
        init();
    }

    private void setUpRCV() {
        adapterJoinRoom = new AdapterJoinRoom(JoinRoomSystem.this, joinRoomList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        rcv_joinedRooms.setLayoutManager(linearLayoutManager);
        rcv_joinedRooms.setAdapter(adapterJoinRoom);

        displayJoinRoom();
    }


    private void init() {
        progressDialog.setMessage("Đang truy vấn !");

        img_joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogJoinRoom();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });
    }

    private void dialogJoinRoom() {
        Dialog dialog = new Dialog(JoinRoomSystem.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_join_room);

        TextInputEditText textInputEdt_joinId = dialog.findViewById(R.id.textInputEdt_joinId);
        Button btn_joinRoom = dialog.findViewById(R.id.btn_joinRoom);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btn_joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String joinRoomId = textInputEdt_joinId.getText().toString().trim();
                if (joinRoomId.equals(""))
                {
                    Toast.makeText(JoinRoomSystem.this, "Error : Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] splitJoinRoomId = joinRoomId.split("_splitHere_");
                String houseId = splitJoinRoomId[0];
                String roomId = splitJoinRoomId[1];
                String ownerUserId = splitJoinRoomId[2];

                if (firebaseUser.getUid().equals(ownerUserId))
                {
                    Toast.makeText(JoinRoomSystem.this, "Error : Không thể tham gia phòng của chính bạn", Toast.LENGTH_SHORT).show();
                    return;
                }

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Rooms room = snapshot.getValue(Rooms.class);
                            if (room != null)
                            {
                                // House and room available in the system
                                // then add some data (such as entered houseId, roomId, ownerUserId)
                                // to current user
                                Log.d("RDetailJoin", room.getId() + "\n" + room.getrName());

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                                String currentDateAndTime = sdf.format(new Date());

                                String joinRoomId = "joinRoom_" + currentDateAndTime + "_" + firebaseUser.getUid();

                                JoinRoom joinRoom = new JoinRoom(joinRoomId, ownerUserId, houseId, roomId);

                                // Add joinRoom to Database
                                myRef.child("joinRooms").child(firebaseUser.getUid()).child(joinRoomId).setValue(joinRoom);

                                displayJoinRoom();

                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(JoinRoomSystem.this, "Error : Phòng không tồn tại hoặc sai Mã Phòng", Toast.LENGTH_SHORT).show();
                            }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
                Query query = myRef.child("rooms").child(ownerUserId).child(houseId).child(roomId);
                query.addListenerForSingleValueEvent(valueEventListener);


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void displayJoinRoom() {
        joinRoomList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    JoinRoom joinRoom = dataSnapshot.getValue(JoinRoom.class); // Snap Key here is City
                    joinRoomList.add(joinRoom);
                }
                adapterJoinRoom.notifyDataSetChanged();

                // When get data successfully, hide the shimmer and show all function field
                searchView_searchJoinedRoom.setVisibility(View.VISIBLE);
                rcv_joinedRooms.setVisibility(View.VISIBLE);
                img_joinRoom.setVisibility(View.VISIBLE);
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("joinRooms").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);

    }


    public void getInformationOfJoinedRoom(JoinRoom joinRoom, TextView txt_joinRoomName,
                                                 TextView txt_joinRoomFloor, TextView txt_joinRoomFee)
    {
        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Rooms rooms = snapshot.getValue(Rooms.class);

                    txt_joinRoomName.setText(rooms.getrName());
                    txt_joinRoomFloor.setText("Tầng : " + rooms.getrFloorNumber());

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    Double cost = Double.parseDouble(rooms.getrPrice());
                    txt_joinRoomFee.setText("Giá Phòng : " + formatter.format(cost));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms")
                    .child(joinRoom.getOwnerUserId())
                    .child(joinRoom.getHouseId())
                    .child(joinRoom.getRoomId());
            query.addListenerForSingleValueEvent(valueEventListener);
        }catch (Exception e)
        {
            Toast.makeText(this, "Error : Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
            backToMain();
        }



    }


    private void initUI() {
        progressDialog = new ProgressDialog(JoinRoomSystem.this);

        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        imgBack         =  findViewById(R.id.imgBack);
        img_joinRoom = findViewById(R.id.img_joinRoom);

        rcv_joinedRooms = findViewById(R.id.rcv_joinedRooms);

        searchView_searchJoinedRoom = findViewById(R.id.searchView_searchJoinedRoom);
    }


    private void backToMain() {
        startActivity(new Intent(JoinRoomSystem.this, MainActivity.class));
        JoinRoomSystem.this.finish();

    }
    @Override
    public void onBackPressed() {
        backToMain();

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        // Hide all function field and show shimmer effect
//        searchView_searchJoinedRoom.setVisibility(View.GONE);
//        rcv_joinedRooms.setVisibility(View.GONE);
//        img_joinRoom.setVisibility(View.GONE);
//
//        shimmer_view_container.setVisibility(View.VISIBLE);
//        shimmer_view_container.startShimmerAnimation();

        //displayServices();

        super.onStart();

    }


}
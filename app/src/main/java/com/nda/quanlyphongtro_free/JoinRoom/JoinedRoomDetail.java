package com.nda.quanlyphongtro_free.JoinRoom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.HouseDetailSystem;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.AdapterServiceOfRoom;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.AdapterTenants;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon.AddHoaDon;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon.UpdateHoaDon;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.RoomDetailSystem;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.Tenants.AddTenant;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.Tenants.UpdateTenant;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.UpdateRoom.UpdateRoom;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Contract;
import com.nda.quanlyphongtro_free.Model.HoaDon;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinedRoomDetail extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    ImageView imgBack;
    JoinRoom joinRoom;
    String ownerUserId,joinedHouseId, joinedRoomId;

    TextView txt_joinedRoomName;

    CardView cv_contact;


    LinearLayout  ll_chiTietPhong, ll_showRoomDetail, ll_hoaDon,ll_showHoaDon, ll_optionRooms;
    TextView txt_bgColor2, txt_bgColor3;

    TextView txt_roomFee, txt_area, txt_floorNumber, txt_numberOfBedRooms, txt_numberOfLivingRooms,
            txt_limitTenants, txt_deposits;
    TextView txt_genderMale, txt_genderFemale, txt_genderOther;
    TextView txt_description, txt_noteForTenants, txt_roomHouseID;

    Button btn_deleteRoom;

    List<Service> serviceList = new ArrayList<>();
    AdapterService adapterService;
    RecyclerView rcv_servicesJoinedRoom;

    List<HoaDon> hoaDonList = new ArrayList<>();
    RecyclerView rcv_hoaDon;
    com.nda.quanlyphongtro_free.JoinRoom.AdapterHoaDon adapterHoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_room_detail);

        initUI();

        init();

    }


    private void init() {
        joinRoom = getIntent().getParcelableExtra("Data_JoinedRoom_Parcelable");

        ownerUserId = joinRoom.getOwnerUserId();
        joinedHouseId = joinRoom.getHouseId();
        joinedRoomId = joinRoom.getRoomId();

        displayDataOfJoinedRoomDetail();

        displayHoaDon();

        ll_chiTietPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showRoomDetail.setVisibility(View.VISIBLE);
                ll_showHoaDon.setVisibility(View.GONE);

                txt_bgColor2.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor3.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        ll_hoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showHoaDon.setVisibility(View.VISIBLE);
                ll_showRoomDetail.setVisibility(View.GONE);

                txt_bgColor3.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor2.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


        btn_deleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteRoom();
            }
        });

        cv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeContract();
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToJoinRoomSystem();
            }
        });


    }
    /***************************
     *
     *
     * (Related) Contract
     *
     *
     *************************** */
    private void executeContract() {
        // Get services first
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

                    // Check if current room has contract or not
                    myRef.child("contracts").child(ownerUserId).child(joinedHouseId).child(joinedRoomId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Contract contract = snapshot.getValue(Contract.class);

                                    if (snapshot.getValue() == null)
                                    {
                                        // Dont have Contract
                                        Toast.makeText(JoinedRoomDetail.this, "Warning : Phòng chưa có hợp đồng !",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        // Contract already existed
                                        bottomSheetContract(contract);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms").child(ownerUserId)
                    .child(joinedHouseId).child(joinedRoomId).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            backToJoinRoomSystem();

            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();
        }

    }

    private void bottomSheetContract(Contract contract)
    {
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_contract,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(JoinedRoomDetail.this);
        bottomSheetDialog.setContentView(view);

        ImageView img_editContract;

        TextView txt_houseName, txt_roomName, txt_thoiHan, txt_daiDienNguoiThue,
                txt_ngayTinhTien,txt_kiThanhToan, txt_roomFee, txt_deposits,
                txt_camKetSoNguoiThue;

        CardView cv_closeBottomSheet;

        RecyclerView rcv_servicesContract;

        img_editContract = view.findViewById(R.id.img_editContract);
        txt_houseName = view.findViewById(R.id.txt_houseName);
        txt_roomName = view.findViewById(R.id.txt_roomName);
        txt_thoiHan = view.findViewById(R.id.txt_thoiHan);
        txt_daiDienNguoiThue = view.findViewById(R.id.txt_daiDienNguoiThue);
        txt_ngayTinhTien = view.findViewById(R.id.txt_ngayTinhTien);
        txt_kiThanhToan = view.findViewById(R.id.txt_kiThanhToan);
        txt_roomFee = view.findViewById(R.id.txt_roomFee);
        txt_deposits = view.findViewById(R.id.txt_deposits);
        txt_camKetSoNguoiThue = view.findViewById(R.id.txt_camKetSoNguoiThue);

        cv_closeBottomSheet = view.findViewById(R.id.cv_closeBottomSheet);

        rcv_servicesContract = view.findViewById(R.id.rcv_servicesContract);
        List<Service> serviceListContract = new ArrayList<>();

        img_editContract.setVisibility(View.GONE);
        txt_houseName.setText(contract.getRentHouse());
        txt_roomName.setText(contract.getRentRoom());
        txt_thoiHan.setText("Thời hạn : " + contract.getFromDate() + " đến " + contract.getToDate());
        txt_daiDienNguoiThue.setText("Đại diện : " + contract.getDaiDienNguoiThue());
        txt_ngayTinhTien.setText(contract.getNgayBatDauTinhTien());
        txt_kiThanhToan.setText(contract.getKyThanhToanTienPhong());

        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(contract.getTienPhong());
        txt_roomFee.setText(formatter.format(cost) + " đ");
        Double cost2 = Double.parseDouble(contract.getTienCoc());
        txt_deposits.setText(formatter.format(cost2) + " đ");

        txt_camKetSoNguoiThue.setText(contract.getCamKetNguoiThue());


        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Service service = dataSnapshot.getValue(Service.class);

                        serviceListContract.add(service);
                    }

                    adapterService = new AdapterService(JoinedRoomDetail.this, serviceListContract);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                            RecyclerView.HORIZONTAL,false);
                    rcv_servicesContract.setLayoutManager(linearLayoutManager);
                    rcv_servicesContract.setAdapter(adapterService);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };

            Query query = myRef.child("contracts").child(ownerUserId)
                    .child(joinedHouseId).child(joinedRoomId).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            backToJoinRoomSystem();
            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }



        cv_closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    /***************************
     *
     *
     * (Related) Detail of Joined Room
     *
     *
     *************************** */
    private void displayDataOfJoinedRoomDetail() {
        try {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Rooms rooms = snapshot.getValue(Rooms.class);

                    txt_joinedRoomName.setText("Phòng tham gia : " + rooms.getrName());
                    txt_floorNumber.setText(rooms.getrFloorNumber());

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    Double cost = Double.parseDouble(rooms.getrPrice());
                    txt_roomFee.setText(formatter.format(cost) + " đ");

                    Double cost2 = Double.parseDouble(rooms.getrDeposit());
                    txt_deposits.setText(formatter.format(cost2) + " đ");

                    txt_area.setText(rooms.getrArea() + "m2");
                    txt_numberOfBedRooms.setText(rooms.getrBedRoomNumber());
                    txt_numberOfLivingRooms.setText(rooms.getrLivingRoomNumber());
                    txt_limitTenants.setText(rooms.getrLimitTenants());
                    txt_description.setText(rooms.getrDescription());
                    txt_noteForTenants.setText(rooms.getrNoteToTenant());


                    String selectedGender = rooms.getrGender();
                    String[] splitGender = selectedGender.split(",");
                    for (int i = 0 ; i < splitGender.length; i ++)
                    {
                        if (splitGender[i].equals("Nam"))
                        {
                            txt_genderMale.setBackgroundColor(Color.parseColor("#11C618"));

                        }
                        else if (splitGender[i].equals("Nữ"))
                        {
                            txt_genderFemale.setBackgroundColor(Color.parseColor("#11C618"));

                        }
                        else if (splitGender[i].equals("Khác"))
                        {
                            txt_genderOther.setBackgroundColor(Color.parseColor("#11C618"));

                        }
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms")
                    .child(ownerUserId)
                    .child(joinedHouseId)
                    .child(joinedRoomId);
            query.addListenerForSingleValueEvent(valueEventListener);
        }catch (Exception e)
        {
            Toast.makeText(this, "Error : Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
            backToJoinRoomSystem();
        }

        adapterService = new AdapterService(this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_servicesJoinedRoom.setLayoutManager(linearLayoutManager);
        rcv_servicesJoinedRoom.setAdapter(adapterService);
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
                    adapterService.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms")
                    .child(ownerUserId)
                    .child(joinedHouseId)
                    .child(joinedRoomId)
                    .child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            backToJoinRoomSystem();

            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }

    }


    private void dialogDeleteRoom() {
        Dialog dialog = new Dialog(JoinedRoomDetail.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CardView cv_delete = dialog.findViewById(R.id.cv_delete);
        CardView cv_cancel = dialog.findViewById(R.id.cv_cancel);

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("joinRooms").child(firebaseUser.getUid()).child(joinRoom.getjId()).removeValue();


                dialog.dismiss();

                backToJoinRoomSystem();
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
     * (Related) Hoa Don
     *
     *
     *************************** */
    private void displayHoaDon() {
        adapterHoaDon = new AdapterHoaDon(this,hoaDonList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        rcv_hoaDon.setLayoutManager(linearLayoutManager);
        rcv_hoaDon.setAdapter(adapterHoaDon);

        hoaDonList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                    hoaDonList.add(0,hoaDon);
                }

                adapterHoaDon.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("receipt").child(ownerUserId).child(joinedHouseId).child(joinedRoomId);
        query.addListenerForSingleValueEvent(valueEventListener);

    }


    private void backToJoinRoomSystem()
    {
        Intent intent = new Intent(JoinedRoomDetail.this, JoinRoomSystem.class);

        startActivity(intent);

        JoinedRoomDetail.this.finish();

    }

    private void initUI() {
        imgBack         =  findViewById(R.id.imgBack);

        cv_contact      = findViewById(R.id.cv_contact);

        rcv_servicesJoinedRoom = findViewById(R.id.rcv_servicesJoinedRoom);
        rcv_hoaDon = findViewById(R.id.rcv_hoaDon);

        txt_joinedRoomName        =  findViewById(R.id.txt_joinedRoomName);
        txt_roomHouseID     = findViewById(R.id.txt_roomHouseID);

        txt_bgColor2        =  findViewById(R.id.txt_bgColor2);
        txt_bgColor3        = findViewById(R.id.txt_bgColor3);

        ll_optionRooms      = findViewById(R.id.ll_optionRooms);
        ll_chiTietPhong     =  findViewById(R.id.ll_chiTietPhong);
        ll_hoaDon           = findViewById(R.id.ll_hoaDon);
        ll_showRoomDetail   =  findViewById(R.id.ll_showRoomDetail);
        ll_showHoaDon       = findViewById(R.id.ll_showHoaDon);

        txt_roomFee     =  findViewById(R.id.txt_roomFee);
        txt_area     =  findViewById(R.id.txt_area);
        txt_floorNumber     =  findViewById(R.id.txt_floorNumber);
        txt_numberOfBedRooms     =  findViewById(R.id.txt_numberOfBedRooms);
        txt_numberOfLivingRooms     =  findViewById(R.id.txt_numberOfLivingRooms);
        txt_limitTenants     =  findViewById(R.id.txt_limitTenants);
        txt_deposits     =  findViewById(R.id.txt_deposits);

        txt_genderMale     =  findViewById(R.id.txt_genderMale);
        txt_genderFemale     =  findViewById(R.id.txt_genderFemale);
        txt_genderOther     =  findViewById(R.id.txt_genderOther);

        txt_description     =  findViewById(R.id.txt_description);
        txt_noteForTenants     =  findViewById(R.id.txt_noteForTenants);


        btn_deleteRoom = findViewById(R.id.btn_deleteRoom);
    }


    @Override
    public void onBackPressed() {
        backToJoinRoomSystem();

        super.onBackPressed();
    }

}
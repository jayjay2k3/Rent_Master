package com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.UpdateRoom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.AddRoom.AdapterAddService;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.RoomDetailSystem;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.Tenants.AddTenant;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateRoom extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    Houses houses;
    Rooms rooms;

    ImageView imgBack, img_addRoom;
    TextView txt_genderMale, txt_genderFemale, txt_genderOther, txt_addRoomTitle;
    boolean isMale = false, isFemale = false, isOtherGender = false;

    TextInputEditText textInputEdt_getTenPhong, textInputEdt_getPhiThuePhong, textInputEdt_getSoTang;
    EditText edt_soPhongNgu, edt_soPhongKhach, edt_dienTich, edt_goiHanNguoiThue, edt_tienCoc,
            edt_description,edt_luuYChoNguoiThue;
    String gender = "";

    List<Service> serviceList = new ArrayList<>();
    List<Service> checkedServiceList = new ArrayList<>();
    AdapterServiceUpdateRoom adapterServiceUpdateRoom;
    RecyclerView rcv_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);

        initUI();
        init();
    }
    private void init()
    {
        houses = getIntent().getParcelableExtra("Data_House_Parcelable");
        rooms = getIntent().getParcelableExtra("Data_Room_Parcelable");

        txt_addRoomTitle.setText("Cập nhật phòng của nhà ( " + houses.gethName() + " )");
        textInputEdt_getTenPhong.setText(rooms.getrName());

        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(rooms.getrPrice());
        textInputEdt_getPhiThuePhong.setText(formatter.format(cost));

        Double cost2 = Double.parseDouble(rooms.getrDeposit());
        edt_tienCoc.setText(formatter.format(cost2));

        Double cost3 = Double.parseDouble(rooms.getrArea());
        edt_dienTich.setText(formatter.format(cost3));

        Double cost4 = Double.parseDouble(rooms.getrLimitTenants());
        edt_goiHanNguoiThue.setText(formatter.format(cost4));

        textInputEdt_getSoTang.setText(rooms.getrFloorNumber());
        edt_soPhongNgu.setText(rooms.getrBedRoomNumber());
        edt_soPhongKhach.setText(rooms.getrLivingRoomNumber());
        edt_description.setText(rooms.getrDescription());
        edt_luuYChoNguoiThue.setText(rooms.getrNoteToTenant());


        String currGenders = rooms.getrGender();
        String[] genderSplit = currGenders.split(",");
        for (String str : genderSplit)
        {
            if (str.equals("Nam"))
            {
                txt_genderMale.setBackgroundColor(Color.parseColor("#11C618"));
                gender += "Nam,";
                isMale = true;
            }
            else if (str.equals("Nữ"))
            {
                txt_genderFemale.setBackgroundColor(Color.parseColor("#11C618"));
                gender += "Nữ,";
                isFemale = true;
            }
            else if (str.equals("Khác"))
            {
                txt_genderOther.setBackgroundColor(Color.parseColor("#11C618"));
                gender += "Khác,";
                isOtherGender = true;
            }
        }


        formatMoneyType(edt_tienCoc);
        formatMoneyType(edt_dienTich);
        formatMoneyType(edt_goiHanNguoiThue);
        formatMoneyType(textInputEdt_getPhiThuePhong);

        img_addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeUpdateRoom();
            }
        });

        selectGender();

        setupRCVforServices();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRoom();
            }
        });
    }

    private void setupRCVforServices() {
        adapterServiceUpdateRoom = new AdapterServiceUpdateRoom(this, serviceList, checkedServiceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdateRoom.this,RecyclerView.VERTICAL,false);
        rcv_services.setLayoutManager(linearLayoutManager);
        rcv_services.setAdapter(adapterServiceUpdateRoom);

        displayServices();
    }

    private void displayServices() {
        serviceList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Service service = dataSnapshot.getValue(Service.class);

                    serviceList.add(service);
                }

                /**
                 * Set height of RCV to total amount of items
                 * **/
                int countItemsList = serviceList.size();

                if (countItemsList > 0)
                {
                    LinearLayout.LayoutParams params = new
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    params.height=countItemsList * 200; // Note : Height = 75dp ( in XML ) equal to Height = 250 ( in Java Code )

                    rcv_services.setLayoutParams(params);
                }

                adapterServiceUpdateRoom.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("houses").child(firebaseUser.getUid()).child(houses.gethId()).child("serviceList");
        query.addListenerForSingleValueEvent(valueEventListener);
    }


    private void executeUpdateRoom() {
        String rName = textInputEdt_getTenPhong.getText().toString().trim();
        String rFee = textInputEdt_getPhiThuePhong.getText().toString().trim();
        String rFloorNumber = textInputEdt_getSoTang.getText().toString().trim();

        String rBedRoomNumber = edt_soPhongNgu.getText().toString().trim();
        String rLivingRoomNumber = edt_soPhongKhach.getText().toString().trim();
        String rArea = edt_dienTich.getText().toString().trim();
        String rLimitTenants = edt_goiHanNguoiThue.getText().toString().trim();
        String rDeposits = edt_tienCoc.getText().toString().trim();

        String rDescription = edt_description.getText().toString().trim();
        String rNoteToTenants = edt_luuYChoNguoiThue.getText().toString().trim();

        if (rName.equals(""))
        {
            Toast.makeText(this, "Error : Nhập tên phòng", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (rFee.equals(""))
        {
            Toast.makeText(this, "Error : Nhập tên phí thuê phòng", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (rFloorNumber.equals(""))
        {
            Toast.makeText(this, "Error : Nhập số tầng của phòng", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (rBedRoomNumber.equals(""))
        {
            Toast.makeText(this, "Error : Nhập số phòng ngủ", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (rLivingRoomNumber.equals(""))
        {
            Toast.makeText(this, "Error : Nhập số phòng khách", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (rArea.equals(""))
        {
            Toast.makeText(this, "Error : Nhập diện tích phòng", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (rLimitTenants.equals(""))
        {
            Toast.makeText(this, "Error : Nhập giới hạn số người thuê phòng", Toast.LENGTH_SHORT).show();
            return;

        }
        else if (checkedServiceList.size() <= 0 )
        {
            Toast.makeText(this, "Error : Vui lòng chọn dịch vụ cho phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * Chuyển Money Type về integer để insert vào database
         * và thực hiện tính toán.
         * */
        if (rFee.contains(","))
            rFee = rFee.replaceAll(",","");
        if (rDeposits.contains(","))
            rDeposits = rDeposits.replaceAll(",","");
        if (rLimitTenants.contains(","))
            rLimitTenants = rLimitTenants.replaceAll(",","");


        Rooms updateRoom = new Rooms(rooms.getId(), rName, rFee, rFloorNumber,rBedRoomNumber, rLivingRoomNumber,
                rArea, rLimitTenants, rDeposits, gender, checkedServiceList, rDescription, rNoteToTenants
                );

        myRef.child("rooms").child(firebaseUser.getUid()).child(houses.gethId())
                .child(rooms.getId()).setValue(updateRoom);

        Toast.makeText(this, "Cập nhật phòng Thành Công !", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(UpdateRoom.this, HouseDetailSystem.class);
        intent.putExtra("Data_House_Parcelable", houses);
        startActivity(intent);
        UpdateRoom.this.finish();
    }


    public void formatMoneyType(EditText edtCostInput)
    {
        edtCostInput.addTextChangedListener( new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void afterTextChanged(Editable s) {
                edtCostInput.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    double doubleVal;

                    /**
                     * Kiếm tra xem data users nhập vào đã chứa "," chưa ?
                     * Nếu có thì sẽ thay thế = ""
                     * */
                    if (originalString.contains(","))
                        originalString = originalString.replaceAll(",","");

                    doubleVal = Double.parseDouble(originalString);


                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(doubleVal);

                    //setting text after format to EditText
                    edtCostInput.setText(formattedString);
                    edtCostInput.setSelection(edtCostInput.getText().length());

                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }

                edtCostInput.addTextChangedListener(this);

            }
        });
    }


    private void selectGender() {
        txt_genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMale)
                {
                    txt_genderMale.setBackgroundColor(Color.parseColor("#11C618"));
                    gender += "Nam,";
                    isMale = true;
                } else {
                    txt_genderMale.setBackgroundColor(Color.parseColor("#555555"));
                    gender = gender.replaceAll("Nam,", "");
                    isMale = false;
                }
            }
        });
        txt_genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFemale)
                {
                    txt_genderFemale.setBackgroundColor(Color.parseColor("#11C618"));
                    gender += "Nữ,";
                    isFemale = true;

                } else {
                    txt_genderFemale.setBackgroundColor(Color.parseColor("#555555"));
                    gender = gender.replaceAll("Nữ,", "");
                    isFemale = false;

                }
            }
        });
        txt_genderOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOtherGender)
                {
                    txt_genderOther.setBackgroundColor(Color.parseColor("#11C618"));
                    gender += "Khác,";
                    isOtherGender = true;

                } else {
                    txt_genderOther.setBackgroundColor(Color.parseColor("#555555"));
                    gender = gender.replaceAll("Khác,", "");
                    isOtherGender = false;

                }
            }
        });
    }

    private void backToRoom() {
        Intent intent = new Intent(UpdateRoom.this, RoomDetailSystem.class);
        intent.putExtra("Data_RoomOfHouse_Parcelable", houses);
        intent.putExtra("Data_Room_Parcelable", rooms);

        startActivity(intent);

        UpdateRoom.this.finish();
    }

    private void initUI() {
        imgBack     = findViewById(R.id.imgBack);
        img_addRoom = findViewById(R.id.img_addRoom);

        textInputEdt_getTenPhong = findViewById(R.id.textInputEdt_getTenPhong);
        textInputEdt_getPhiThuePhong = findViewById(R.id.textInputEdt_getPhiThuePhong);
        textInputEdt_getSoTang = findViewById(R.id.textInputEdt_getSoTang);

        txt_genderMale   = findViewById(R.id.txt_genderMale);
        txt_genderFemale = findViewById(R.id.txt_genderFemale);
        txt_genderOther  = findViewById(R.id.txt_genderOther);

        txt_addRoomTitle    = findViewById(R.id.txt_addRoomTitle);

        edt_soPhongNgu          = findViewById(R.id.edt_soPhongNgu);
        edt_soPhongKhach        = findViewById(R.id.edt_soPhongKhach);
        edt_dienTich            = findViewById(R.id.edt_dienTich);
        edt_goiHanNguoiThue     = findViewById(R.id.edt_goiHanNguoiThue);
        edt_tienCoc             = findViewById(R.id.edt_tienCoc);
        edt_description         = findViewById(R.id.edt_description);
        edt_luuYChoNguoiThue    = findViewById(R.id.edt_luuYChoNguoiThue);

        rcv_services    = findViewById(R.id.rcv_services);
    }



    @Override
    public void onBackPressed() {
        backToRoom();

        super.onBackPressed();
    }
}
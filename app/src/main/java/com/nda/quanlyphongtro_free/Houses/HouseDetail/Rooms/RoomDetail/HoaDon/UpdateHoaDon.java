package com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.RoomDetailSystem;
import com.nda.quanlyphongtro_free.Model.HoaDon;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateHoaDon extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    Houses houses;
    Rooms rooms;
    HoaDon hoaDon;

    ImageView imgBack;
    Button btn_tongHop;

    TextView txt_hoaDonDate, txt_showRentHouse, txt_showRentRoom, txt_selectNgayThanhToan,
            txt_selectHanThanhToan,txt_showFeeRoom;

    EditText edt_note;

    List<Service> serviceList = new ArrayList<>();
    List<String> strServiceThanhTien = new ArrayList<>(); // Chỉ dùng cho Add Hóa Đơn Activity
    RecyclerView rcv_services;
    AdapterServiceHoaDon adapterServiceHoaDon;


    boolean daThanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hoadon);

        initUI();
        init();
        setUpServiceRCV();

    }

    private void init()
    {
        houses = getIntent().getParcelableExtra("Data_House_Parcelable");
        rooms  = getIntent().getParcelableExtra("Data_Room_Parcelable");
        hoaDon  = getIntent().getParcelableExtra("Data_HoaDon_Parcelable");

        daThanhToan = hoaDon.isDaThanhToan();

        txt_hoaDonDate.setText(hoaDon.getHoaDonThang());
        txt_showRentHouse.setText(hoaDon.getRentHouse());
        txt_showRentRoom.setText(hoaDon.getRentRoom());

        txt_selectNgayThanhToan.setText(hoaDon.getNgayThanhToan());
        txt_selectHanThanhToan.setText(hoaDon.getHanThanhToan());

        edt_note.setText(hoaDon.getNote());

        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(hoaDon.getRoomFee());
        txt_showFeeRoom.setText(formatter.format(cost) + " đ");


        txt_hoaDonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_hoaDonDate);
            }
        });
        txt_selectNgayThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectNgayThanhToan);
            }
        });
        txt_selectHanThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectHanThanhToan);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRoomDetail();
            }
        });

        btn_tongHop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogXacNhanHoaDon();
            }
        });

    }


    /*******************************************************
     *
     * (Related to) Date
     *
     ******************************************************* */
    private void datePicker(TextView showPickTime)
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        // Implement date picker to get user's choice date
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateHoaDon.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int Myear, int Mmonth, int MdayOfMonth) {
                String FinalDate = (MdayOfMonth + "/" + (Mmonth + 1) + "/" + (Myear) );

                showPickTime.setText(FinalDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }


    private void dialogXacNhanHoaDon()
    {
        Dialog dialog = new Dialog(UpdateHoaDon.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_hoadon);

        TextView txt_roomFee = dialog.findViewById(R.id.txt_roomFee);
        EditText edt_roomServices = dialog.findViewById(R.id.edt_roomServices);
        EditText edt_sumServiceFee = dialog.findViewById(R.id.edt_sumServiceFee);

        TextView txt_daThanhToan   = dialog.findViewById(R.id.txt_daThanhToan);
        TextView txt_chuaThanhToan = dialog.findViewById(R.id.txt_chuaThanhToan);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        formatMoneyType(edt_sumServiceFee);

        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(hoaDon.getRoomFee());
        txt_roomFee.setText(formatter.format(cost) + " đ");

        Double cost2 = Double.parseDouble(hoaDon.getTotalServiceFee());
        edt_sumServiceFee.setText(formatter.format(cost2));

        edt_roomServices.setText(hoaDon.getNoteServices());

        if (daThanhToan == true)
        {
            txt_daThanhToan.setBackgroundColor(Color.parseColor("#4CAF50"));
            txt_chuaThanhToan.setBackgroundColor(Color.parseColor("#FFFFFF"));

            txt_daThanhToan.setTextColor(Color.parseColor("#FFFFFF"));
            txt_chuaThanhToan.setTextColor(Color.parseColor("#000000"));

        } else {
            txt_chuaThanhToan.setBackgroundColor(Color.parseColor("#4CAF50"));
            txt_daThanhToan.setBackgroundColor(Color.parseColor("#FFFFFF"));

            txt_chuaThanhToan.setTextColor(Color.parseColor("#FFFFFF"));
            txt_daThanhToan.setTextColor(Color.parseColor("#000000"));
        }

        executeSelectThanhToan(txt_daThanhToan, txt_chuaThanhToan);



        String hoaDonThang = txt_hoaDonDate.getText().toString().trim();
        String rentHouse = txt_showRentHouse.getText().toString().trim();
        String rentRoom = txt_showRentRoom.getText().toString().trim();
        String ngayThanhToan = txt_selectNgayThanhToan.getText().toString().trim();
        String hanThanhToan = txt_selectHanThanhToan.getText().toString().trim();
        String feeRoom = txt_showFeeRoom.getText().toString().trim();
        String note = edt_note.getText().toString().trim();


        btnAdd.setText("Cập Nhập");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSumServiceFee = edt_sumServiceFee.getText().toString().trim();
                String noteRoomServices = edt_roomServices.getText().toString().trim();

                String roomFee = rooms.getrPrice();
;
                if (strSumServiceFee.equals(""))
                {
                    strSumServiceFee = "0";
                }
                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (strSumServiceFee.contains(","))
                    strSumServiceFee = strSumServiceFee.replaceAll(",","");
                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (roomFee.contains(","))
                    roomFee = roomFee.replaceAll(",","");


                HoaDon hoaDonUpdate = new HoaDon(hoaDon.getId(), hoaDonThang,rentHouse, rentRoom, ngayThanhToan
                        , hanThanhToan , roomFee, note, strSumServiceFee,noteRoomServices, daThanhToan );

                myRef.child("receipt").child(firebaseUser.getUid()).child(houses.gethId())
                        .child(rooms.getId()).child(hoaDon.getId()).setValue(hoaDonUpdate);

                Toast.makeText(UpdateHoaDon.this, "Cập nhập hóa đơn Thành Công !", Toast.LENGTH_SHORT).show();

                backToRoomDetail();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void executeSelectThanhToan(TextView txt_daThanhToan, TextView txt_chuaThanhToan) {
        txt_daThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daThanhToan = true;

                txt_daThanhToan.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_chuaThanhToan.setBackgroundColor(Color.parseColor("#FFFFFF"));

                txt_daThanhToan.setTextColor(Color.parseColor("#FFFFFF"));
                txt_chuaThanhToan.setTextColor(Color.parseColor("#000000"));

            }
        });
        txt_chuaThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daThanhToan = false;

                txt_chuaThanhToan.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_daThanhToan.setBackgroundColor(Color.parseColor("#FFFFFF"));

                txt_chuaThanhToan.setTextColor(Color.parseColor("#FFFFFF"));
                txt_daThanhToan.setTextColor(Color.parseColor("#000000"));
            }
        });
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
    private void setUpServiceRCV() {
        adapterServiceHoaDon = new AdapterServiceHoaDon(this,serviceList, strServiceThanhTien);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdateHoaDon.this,
                RecyclerView.HORIZONTAL,false);
        rcv_services.setLayoutManager(linearLayoutManager);
        rcv_services.setAdapter(adapterServiceHoaDon);

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

                adapterServiceHoaDon.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("rooms").child(firebaseUser.getUid())
                .child(houses.gethId()).child(rooms.getId()).child("serviceList");
        query.addListenerForSingleValueEvent(valueEventListener);

    }

    private void initUI() {
        imgBack     = findViewById(R.id.imgBack);

        btn_tongHop   = findViewById(R.id.btn_tongHop);

        txt_hoaDonDate              = findViewById(R.id.txt_hoaDonDate);
        txt_showRentHouse           = findViewById(R.id.txt_showRentHouse);
        txt_showRentRoom            = findViewById(R.id.txt_showRentRoom);
        txt_selectNgayThanhToan     = findViewById(R.id.txt_selectNgayThanhToan);
        txt_selectHanThanhToan      = findViewById(R.id.txt_selectHanThanhToan);
        txt_showFeeRoom             = findViewById(R.id.txt_showFeeRoom);

        rcv_services = findViewById(R.id.rcv_services);

        edt_note   = findViewById(R.id.edt_note);

    }

    private void backToRoomDetail() {
        Intent intent = new Intent(UpdateHoaDon.this, RoomDetailSystem.class);
        intent.putExtra("Data_RoomOfHouse_Parcelable", houses);
        intent.putExtra("Data_Room_Parcelable", rooms);

        startActivity(intent);

        UpdateHoaDon.this.finish();
    }


    @Override
    public void onBackPressed() {
        backToRoomDetail();

        super.onBackPressed();
    }
}
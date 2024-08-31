package com.nda.quanlyphongtro_free.Houses.AddHouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nda.quanlyphongtro_free.Houses.HousesSystem;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;
import com.nda.quanlyphongtro_free.Services.ServicesSystem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddHouse extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();;


    ImageView imgBack, img_addHouse, img_addServices;
    TextInputEditText textInputEdt_getTenNha, textInputEdt_getSoTang, textInputEdt_getPhiThueNha,
            textInputEdt_getMoTa, textInputEdt_getDiaChi, textInputEdt_getBaoTruocNgayChuyen,
            textInputEdt_getGhiChu;
    String tenNha, soTang, phiThueNha, moTa, diaChi, baoTruocNgayChuyen, ghiChu;

    TextView txt_selectThanhPho, txt_selectQuanHuyen;
    String strTinhThanhPho, strQuanHuyen;

    TextView txt_selectGioMoCua, txt_selectGioDongCua;
    String strGioMoCua, strGioDongCua;

    RecyclerView rcv_services;
    List<Service> serviceList = new ArrayList<>();
    List<Service> checkedServiceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        initUI();
        init();
    }
    private void init()
    {
        formatMoneyType(textInputEdt_getPhiThueNha);

        img_addHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeAddHouse();
            }
        });
        img_addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddServices();
            }
        });

        executeSelectCityState();

        txt_selectGioMoCua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerPicker(txt_selectGioMoCua);
            }
        });
        txt_selectGioDongCua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerPicker(txt_selectGioDongCua);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddHouse.this, HousesSystem.class));
                AddHouse.this.finish();
            }
        });
    }


    private void executeAddHouse() {
        tenNha =  textInputEdt_getTenNha.getText().toString().trim();
        soTang =  textInputEdt_getSoTang.getText().toString().trim();
        phiThueNha =  textInputEdt_getPhiThueNha.getText().toString().trim();
        moTa =  textInputEdt_getMoTa.getText().toString().trim();
        diaChi =  textInputEdt_getDiaChi.getText().toString().trim();
        baoTruocNgayChuyen =  textInputEdt_getBaoTruocNgayChuyen.getText().toString().trim();
        ghiChu =  textInputEdt_getGhiChu.getText().toString().trim();

        strTinhThanhPho =  txt_selectThanhPho.getText().toString().trim();
        strQuanHuyen =  txt_selectQuanHuyen.getText().toString().trim();
        strGioMoCua =  txt_selectGioMoCua.getText().toString().trim();
        strGioDongCua =  txt_selectGioDongCua.getText().toString().trim();

        if (tenNha.equals(""))
        {
            Toast.makeText(this, "Error : Điền tên nhà !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(soTang.equals(""))
        {
            Toast.makeText(this, "Error : Điền số tầng !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(Integer.parseInt(soTang) <=0)
        {
            Toast.makeText(this, "Error : Số tầng không hợp lệ !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(diaChi.equals(""))
        {
            Toast.makeText(this, "Error : Điền địa Chỉ !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(strTinhThanhPho.equals(""))
        {
            Toast.makeText(this, "Error : Chọn thành Tỉnh/Thành Phố !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(strQuanHuyen.equals(""))
        {
            Toast.makeText(this, "Error : Chọn Quận/Huyện !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(checkedServiceList.size() <= 0)
        {
            Toast.makeText(this, "Error : Chọn dịch vụ !", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * Chuyển Money Type về integer để insert vào database
         * và thực hiện tính toán.
         * */
        if (phiThueNha.contains(","))
            phiThueNha = phiThueNha.replaceAll(",","");


        // Get current Datetime
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        String houseId = "house_" + currentDateAndTime + "_" + firebaseUser.getUid();

        Houses houses = new Houses(houseId, tenNha, soTang, phiThueNha, moTa, diaChi, strTinhThanhPho,
                strQuanHuyen, checkedServiceList, strGioMoCua,strGioDongCua, baoTruocNgayChuyen,
                ghiChu);

        // myRef.child("houses").child(firebaseUser.getUid()).child(strTinhThanhPho).child(houseId).setValue(houses);
        myRef.child("houses").child(firebaseUser.getUid()).child(houseId).setValue(houses);

        Toast.makeText(this, "Thêm nhà Thành Công !", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(AddHouse.this, HousesSystem.class));
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

    /*******************************************************
     *
     * (Related to) Add Services
     *
     ******************************************************* */
    private void dialogAddServices() {
        Dialog dialog = new Dialog(AddHouse.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_services_add_house);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        RecyclerView rcv_showServices = dialog.findViewById(R.id.rcv_showServices);


        AdapterAddService adapterAddService = new AdapterAddService(this, serviceList, checkedServiceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddHouse.this,RecyclerView.VERTICAL,false);
        rcv_showServices.setLayoutManager(linearLayoutManager);
        rcv_showServices.setAdapter(adapterAddService);

        displayServices(adapterAddService);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void displayServices(AdapterAddService adapterAddService) {
        serviceList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Service service =dataSnapshot.getValue(Service.class);

                    serviceList.add(service);

                }

                adapterAddService.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query query = myRef.child("services").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);
    }


    public void showCheckedServices()
    {

        AdapterCheckedService adapterCheckedService = new AdapterCheckedService(this, checkedServiceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddHouse.this,RecyclerView.HORIZONTAL,false);
        rcv_services.setLayoutManager(linearLayoutManager);
        rcv_services.setAdapter(adapterCheckedService);
    }



    /*******************************************************
     *
     * (Related to) Time Picker
     *
     ******************************************************* */
    private void timerPicker(TextView showPickTime)
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendar.set(0,0,0 ,i ,i1);
                        showPickTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, hour, minutes, true);
        timePickerDialog.show();
    }




    /*******************************************************
     *
     * (Related to) Select City State
     *
     ******************************************************* */
    private void executeSelectCityState() {
        txt_selectThanhPho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectThanhPho();
            }
        });

        txt_selectQuanHuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_selectThanhPho.getText().toString().trim().equals(""))
                {
                    Toast.makeText(AddHouse.this, "Warning : Vui lòng chọn Tỉnh/Thành Phố !", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialogSelectQuanHuyen();
                }
            }
        });
    }

    public void dialogSelectThanhPho() {
        Dialog dialog = new Dialog(AddHouse.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_select_state_city);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        androidx.appcompat.widget.SearchView  searchView_service = dialog.findViewById(R.id.searchView_service);
        RecyclerView rcv_stateCitySelection = dialog.findViewById(R.id.rcv_stateCitySelection);

        AdapterSelectCityState adapterAddHouse = new AdapterSelectCityState(this, ListTinhThanhPhoVN.listTinhThanhPho,
                dialog, txt_selectThanhPho, txt_selectQuanHuyen);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddHouse.this,RecyclerView.VERTICAL,false);
        rcv_stateCitySelection.setLayoutManager(linearLayoutManager);
        rcv_stateCitySelection.setAdapter(adapterAddHouse);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void dialogSelectQuanHuyen() {
        Dialog dialog = new Dialog(AddHouse.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_select_state_city);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        androidx.appcompat.widget.SearchView  searchView_service = dialog.findViewById(R.id.searchView_service);
        RecyclerView rcv_stateCitySelection = dialog.findViewById(R.id.rcv_stateCitySelection);
        TextView txtTitle_dialog_add_update = dialog.findViewById(R.id.txtTitle_dialog_add_update);


        txtTitle_dialog_add_update.setText("Chọn Quận/Huyện");
        AdapterSelectDistrict adapterAddHouse = null;
        String strThanhPho = txt_selectThanhPho.getText().toString().trim();

        // Set default list for adapter
        adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_HaNoi, dialog, txt_selectQuanHuyen);

        if (strThanhPho.equals("Hà Nội"))
        {
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_HaNoi, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Hà Giang")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_HaGiang, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Cao Bằng")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_CaoBang, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Bắc Kạn")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_BacKan, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Tuyên Quang")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_TuyenQuang, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Lào Cai")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_LaoCai, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Điện Biên")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_DienBien, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Lai Châu")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_LaiChau, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Sơn La")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_SonLa, dialog, txt_selectQuanHuyen);
        }
        else if (strThanhPho.equals("Yên Bái")){
            adapterAddHouse = new AdapterSelectDistrict(this, ListTinhThanhPhoVN.listQuanHuyen_YenBai, dialog, txt_selectQuanHuyen);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddHouse.this,RecyclerView.VERTICAL,false);
        rcv_stateCitySelection.setLayoutManager(linearLayoutManager);
        rcv_stateCitySelection.setAdapter(adapterAddHouse);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }



    private void initUI() {
        txt_selectThanhPho     = findViewById(R.id.txt_selectThanhPho);
        txt_selectQuanHuyen    = findViewById(R.id.txt_selectQuanHuyen);
        txt_selectGioMoCua     = findViewById(R.id.txt_selectGioMoCua);
        txt_selectGioDongCua   = findViewById(R.id.txt_selectGioDongCua);


        imgBack         = findViewById(R.id.imgBack);
        img_addHouse    = findViewById(R.id.img_addHouse);
        img_addServices = findViewById(R.id.img_addServices);

        textInputEdt_getTenNha              = findViewById(R.id.textInputEdt_getTenNha);
        textInputEdt_getSoTang              = findViewById(R.id.textInputEdt_getSoTang);
        textInputEdt_getPhiThueNha          = findViewById(R.id.textInputEdt_getPhiThueNha);
        textInputEdt_getMoTa                = findViewById(R.id.textInputEdt_getMoTa);
        textInputEdt_getDiaChi              = findViewById(R.id.textInputEdt_getDiaChi);
        textInputEdt_getBaoTruocNgayChuyen  = findViewById(R.id.textInputEdt_getBaoTruocNgayChuyen);
        textInputEdt_getGhiChu              = findViewById(R.id.textInputEdt_getGhiChu);

        rcv_services    = findViewById(R.id.rcv_services);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddHouse.this, HousesSystem.class));
        AddHouse.this.finish();
        super.onBackPressed();
    }
}
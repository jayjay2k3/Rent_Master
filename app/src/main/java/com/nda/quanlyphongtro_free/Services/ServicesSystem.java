package com.nda.quanlyphongtro_free.Services;

import android.app.Dialog;
import android.database.Cursor;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


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
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServicesSystem extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();;


    ShimmerFrameLayout shimmer_view_container;

    ImageView imgAddServices,imgBack;
    TextInputEditText textInputEdt_getServiceName,textInputEdt_getServicePrice,textInputEdt_getServiceUnit;

    RecyclerView rcv_services;
    AdapterServices adapterServices;
    List<Service> serviceList = new ArrayList<>();

    TextView txtTitleService;

    androidx.appcompat.widget.SearchView searchView_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_system);
        initUI();
        setRCV();

        init();

    }

    private void setRCV() {
        adapterServices = new AdapterServices(this, serviceList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);

        rcv_services.setLayoutManager(staggeredGridLayoutManager);
        rcv_services.setAdapter(adapterServices);

        displayServices();
    }


    private void init() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddServices();
            }
        });
    }


    public void dialogUpdateService(Service service)
    {
        Dialog dialog_update = new Dialog(ServicesSystem.this);
        dialog_update.setContentView(R.layout.dialog_add_update_services);
        dialog_update.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtTitleService = dialog_update.findViewById(R.id.txtTitleService);
        textInputEdt_getServiceName   = dialog_update.findViewById(R.id.textInputEdt_getServiceName);
        textInputEdt_getServicePrice  = dialog_update.findViewById(R.id.textInputEdt_getServicePrice);
        textInputEdt_getServiceUnit   = dialog_update.findViewById(R.id.textInputEdt_getServiceUnit);

        textInputEdt_getServiceName.setText(service.getName());
        textInputEdt_getServiceUnit.setText(service.getUnit());

        formatMoneyType(textInputEdt_getServicePrice);

        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(service.getPrice());
        textInputEdt_getServicePrice.setText(formatter.format(cost));

        txtTitleService.setText(R.string.serciesSystem_update_title);
        Button btnService   = (Button) dialog_update.findViewById(R.id.btnService);
        Button btnCancel   = (Button) dialog_update.findViewById(R.id.btnCancel);



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_update.dismiss();
            }
        });

        btnService.setText(R.string.housesSystem_update_button);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = textInputEdt_getServiceName.getText().toString().trim();
                String servicePrice = textInputEdt_getServicePrice.getText().toString().trim();
                String serviceUnit = textInputEdt_getServiceUnit.getText().toString().trim();

                if (serviceName.equals("") || servicePrice.equals("") || serviceUnit.equals(""))
                {
                    Toast.makeText(ServicesSystem.this, "Error : Điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (servicePrice.contains(","))
                    servicePrice = servicePrice.replaceAll(",","");


                Service updateService = new Service(service.getId(), serviceName, servicePrice, serviceUnit, service.isDelete());

                myRef.child("services").child(firebaseUser.getUid()).child(String.valueOf(service.getId())).setValue(updateService);

                displayServices();
                dialog_update.dismiss();
            }
        });
        dialog_update.show();


    }
    public void dialogDeleteService(Service service)
    {
        Dialog dialog_delete = new Dialog(ServicesSystem.this);
        dialog_delete.setContentView(R.layout.dialog_delete);
        dialog_delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String serviceId = service.getId() + "";
        CardView cv_delete               =  dialog_delete.findViewById(R.id.cv_delete);
        CardView cv_cancel                =  dialog_delete.findViewById(R.id.cv_cancel);
        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child("services").child(firebaseUser.getUid()).child(serviceId).removeValue();

                displayServices();
                dialog_delete.dismiss();
            }
        });
        cv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_delete.dismiss();
            }
        });
        dialog_delete.show();
    }

    private void dialogAddServices() {
        Dialog dialog = new Dialog(ServicesSystem.this);
        dialog.setContentView(R.layout.dialog_add_update_services);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textInputEdt_getServiceName   =  dialog.findViewById(R.id.textInputEdt_getServiceName);
        textInputEdt_getServicePrice   = dialog.findViewById(R.id.textInputEdt_getServicePrice);
        textInputEdt_getServiceUnit   =  dialog.findViewById(R.id.textInputEdt_getServiceUnit);


        Button btnService   = (Button) dialog.findViewById(R.id.btnService);
        Button btnCancel   = (Button) dialog.findViewById(R.id.btnCancel);

        formatMoneyType(textInputEdt_getServicePrice);


        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = textInputEdt_getServiceName.getText().toString().trim();
                String servicePrice = textInputEdt_getServicePrice.getText().toString().trim();
                String serviceUnit = textInputEdt_getServiceUnit.getText().toString().trim();

                if (serviceName.equals("") || servicePrice.equals("") || serviceUnit.equals(""))
                {
                    Toast.makeText(ServicesSystem.this, "Error : Điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (servicePrice.contains(","))
                    servicePrice = servicePrice.replaceAll(",","");

                // Get current Datetime
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());


                int currentTotalServices = serviceList.size();
                int nextServicesId = currentTotalServices + 1;
                String servicesId = nextServicesId + "_" + currentDateAndTime;

                Service service = new Service(servicesId, serviceName, servicePrice, serviceUnit, true);

                myRef.child("services").child(firebaseUser.getUid()).child(servicesId).setValue(service);

                displayServices();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();
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

    private void displayServices() {
        serviceList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Service service =dataSnapshot.getValue(Service.class);

                    serviceList.add(0,service);

                }

                adapterServices.notifyDataSetChanged();



                // When get data successfully, hide the shimmer and show all function field
                searchView_service.setVisibility(View.VISIBLE);
                rcv_services.setVisibility(View.VISIBLE);
                imgAddServices.setVisibility(View.VISIBLE);
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmerAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query query = myRef.child("services").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStart() {
        // Hide all function field and show shimmer effect
        searchView_service.setVisibility(View.GONE);
        rcv_services.setVisibility(View.GONE);
        imgAddServices.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        //displayServices();

        super.onStart();

    }

    private void initUI() {
        imgAddServices  = findViewById(R.id.imgAddServices);
        imgBack         = findViewById(R.id.imgBack);

        rcv_services        = findViewById(R.id.rcv_services);
        searchView_service  = findViewById(R.id.searchView_service);

        shimmer_view_container = findViewById(R.id.shimmer_view_container);
    }
}
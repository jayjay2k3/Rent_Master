package com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail;

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
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon.AdapterHoaDon;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon.AddHoaDon;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.HoaDon.UpdateHoaDon;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.Tenants.AddTenant;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.Tenants.UpdateTenant;
import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.UpdateRoom.UpdateRoom;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Contract;
import com.nda.quanlyphongtro_free.Model.HoaDon;
import com.nda.quanlyphongtro_free.Model.Houses;
import com.nda.quanlyphongtro_free.Model.Rooms;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.R;
import com.nda.quanlyphongtro_free.Model.Tenants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoomDetailSystem extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    ShimmerFrameLayout shimmer_view_container;

    ImageView imgBack,img_addTenants, img_editRoom, img_addHoaDon , img_copyHouseRoomID;
    TextView txt_roomName, txt_numberOfTenants;

    CardView cv_contact;

    LinearLayout ll_danhSachTenants, ll_chiTietPhong, ll_showTenants, ll_showRoomDetail, ll_optionRooms,
                    ll_hoaDon,ll_showHoaDon;
    TextView txt_bgColor1,txt_bgColor2, txt_bgColor3;
    androidx.appcompat.widget.SearchView searchView_searchTenants;

    Houses houses;
    Rooms rooms;

    RecyclerView rcv_tenants;
    List<Tenants> tenantsList = new ArrayList<>();
    AdapterTenants adapterTenants;

    TextView txt_roomFee, txt_area, txt_floorNumber, txt_numberOfBedRooms, txt_numberOfLivingRooms,
            txt_limitTenants, txt_deposits;
    TextView txt_genderMale, txt_genderFemale, txt_genderOther;
    TextView txt_description, txt_noteForTenants, txt_roomHouseID;

    Button btn_deleteRoom;

    List<Service> serviceList = new ArrayList<>();
    AdapterServiceOfRoom adapterServiceOfRoom;
    RecyclerView rcv_servicesRoomDetail;

    List<HoaDon> hoaDonList = new ArrayList<>();
    RecyclerView rcv_hoaDon;
    AdapterHoaDon adapterHoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail_system);

        initUI();

        init();
        setupTenantsRCV();
        setUpHoaDonRCV();
    }


    private void init() {
        houses = getIntent().getParcelableExtra("Data_RoomOfHouse_Parcelable");
        rooms  = getIntent().getParcelableExtra("Data_Room_Parcelable");

        txt_roomHouseID.setText(houses.gethId() + "_splitHere_" + rooms.getId()+ "_splitHere_" + firebaseUser.getUid());

        txt_roomName.setText(rooms.getrName());

        img_addTenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomDetailSystem.this, AddTenant.class);

                intent.putExtra("Data_House_Parcelable", houses);
                intent.putExtra("Data_Room_Parcelable", rooms);

                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHouseDetailSystem();
            }
        });


        ll_danhSachTenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showTenants.setVisibility(View.VISIBLE);
                ll_showRoomDetail.setVisibility(View.GONE);
                ll_showHoaDon.setVisibility(View.GONE);
                img_addHoaDon.setVisibility(View.GONE);
                searchView_searchTenants.setVisibility(View.VISIBLE);
                txt_bgColor1.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                txt_bgColor3.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
        });
        ll_chiTietPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showRoomDetail.setVisibility(View.VISIBLE);
                ll_showTenants.setVisibility(View.GONE);
                ll_showHoaDon.setVisibility(View.GONE);
                img_addHoaDon.setVisibility(View.GONE);
                searchView_searchTenants.setVisibility(View.GONE);
                txt_bgColor2.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                txt_bgColor3.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        ll_hoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_showHoaDon.setVisibility(View.VISIBLE);
                img_addHoaDon.setVisibility(View.VISIBLE);
                ll_showRoomDetail.setVisibility(View.GONE);
                ll_showTenants.setVisibility(View.GONE);
                searchView_searchTenants.setVisibility(View.GONE);
                txt_bgColor3.setBackgroundColor(Color.parseColor("#4CAF50"));
                txt_bgColor2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                txt_bgColor1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        img_editRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomDetailSystem.this, UpdateRoom.class);

                intent.putExtra("Data_House_Parcelable", houses);
                intent.putExtra("Data_Room_Parcelable", rooms);

                startActivity(intent);
            }
        });

        btn_deleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteRoom();
            }
        });

        img_addHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomDetailSystem.this, AddHoaDon.class);

                intent.putExtra("Data_House_Parcelable", houses);
                intent.putExtra("Data_Room_Parcelable", rooms);

                startActivity(intent);
            }
        });

        cv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeContract();
            }
        });

        img_copyHouseRoomID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("HouseRoomID", txt_roomHouseID.getText().toString().trim());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(RoomDetailSystem.this, "Sao chép Mã Phòng !", Toast.LENGTH_SHORT).show();
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
                    myRef.child("contracts").child(firebaseUser.getUid()).child(houses.gethId()).child(rooms.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Contract contract = snapshot.getValue(Contract.class);

                                    if (snapshot.getValue() == null)
                                    {
                                        // Dont have Contract
                                        dialogAddContract();

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
            Query query = myRef.child("rooms").child(firebaseUser.getUid())
                    .child(houses.gethId()).child(rooms.getId()).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            startActivity(new Intent(RoomDetailSystem.this, MainActivity.class));
            RoomDetailSystem.this.finish();
            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();
        }

    }

    private void dialogAddContract() {
        Dialog dialog = new Dialog(RoomDetailSystem.this);
        dialog.setContentView(R.layout.dialog_add_update_contract);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText textInputEdt_daiDienNguoiThue;
        TextView txt_showRentHouse, txt_showRentRoom, txt_camKetSoNguoiThue, txt_selectFromDate,
                txt_selectToDate, txt_selectNgayBatDauTinhTien;

        EditText edt_kiThanhToanTienPhong, edt_tienPhong, edt_tienCoc;

        RecyclerView rcv_services;

        Button btn_addContract, btn_cancelContract;

        textInputEdt_daiDienNguoiThue = dialog.findViewById(R.id.textInputEdt_daiDienNguoiThue);

        txt_showRentHouse = dialog.findViewById(R.id.txt_showRentHouse);
        txt_showRentRoom = dialog.findViewById(R.id.txt_showRentRoom);
        txt_camKetSoNguoiThue = dialog.findViewById(R.id.txt_camKetSoNguoiThue);
        txt_selectFromDate = dialog.findViewById(R.id.txt_selectFromDate);
        txt_selectToDate = dialog.findViewById(R.id.txt_selectToDate);
        txt_selectNgayBatDauTinhTien = dialog.findViewById(R.id.txt_selectNgayBatDauTinhTien);

        edt_kiThanhToanTienPhong = dialog.findViewById(R.id.edt_kiThanhToanTienPhong);
        edt_tienPhong = dialog.findViewById(R.id.edt_tienPhong);
        edt_tienCoc = dialog.findViewById(R.id.edt_tienCoc);

        btn_addContract = dialog.findViewById(R.id.btn_addContract);
        btn_cancelContract = dialog.findViewById(R.id.btn_cancelContract);

        rcv_services = dialog.findViewById(R.id.rcv_services);


        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(rooms.getrPrice());
        edt_tienPhong.setText(formatter.format(cost));

        formatMoneyType(edt_tienPhong);
        formatMoneyType(edt_tienCoc);

        setCurrentDate(txt_selectFromDate);
        setCurrentDate(txt_selectNgayBatDauTinhTien);

        txt_selectFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectFromDate);
            }
        });
        txt_selectToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectToDate);
            }
        });
        txt_selectNgayBatDauTinhTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectNgayBatDauTinhTien);
            }
        });

        txt_showRentHouse.setText(houses.gethName());
        txt_showRentRoom.setText(rooms.getrName());

        // When room has contract
        adapterServiceOfRoom = new AdapterServiceOfRoom(this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_services.setLayoutManager(linearLayoutManager);
        rcv_services.setAdapter(adapterServiceOfRoom);

        btn_addContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daiDienNguoiThue = textInputEdt_daiDienNguoiThue.getText().toString().trim();

                String rentHouse = txt_showRentHouse.getText().toString().trim();
                String rentRoom = txt_showRentRoom.getText().toString().trim();
                String camKetSoNguoiThue = txt_camKetSoNguoiThue.getText().toString().trim();
                String fromDate = txt_selectFromDate.getText().toString().trim();
                String toDate = txt_selectToDate.getText().toString().trim();
                String ngayBatDauTinhTien = txt_selectNgayBatDauTinhTien.getText().toString().trim();

                String tienPhong = edt_tienPhong.getText().toString().trim();
                String tienCoc = edt_tienCoc.getText().toString().trim();
                String kiThanhToanTienPhong = edt_kiThanhToanTienPhong.getText().toString().trim();


                if (daiDienNguoiThue.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Ghi đại diện người thuê", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(camKetSoNguoiThue.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Điền cam kết số người thuê", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(toDate.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Chọn thời hạn của hợp đồng", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(kiThanhToanTienPhong.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Kỳ thanh toán tiền phòng không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tienPhong.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Tiền phòng không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tienCoc.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Tiền cọc không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (Integer.parseInt(camKetSoNguoiThue) <= 0)
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Cam kết người thuê không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (tienPhong.contains(","))
                    tienPhong = tienPhong.replaceAll(",","");
                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (tienCoc.contains(","))
                    tienCoc = tienCoc.replaceAll(",","");

                // Get current Datetime
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());

                String contractId = "contract_" + currentDateAndTime + "_" + firebaseUser.getUid();
                Contract contract = new Contract(contractId, daiDienNguoiThue,rentHouse, rentRoom,
                        camKetSoNguoiThue, fromDate, toDate, ngayBatDauTinhTien, kiThanhToanTienPhong,tienPhong,
                        tienCoc, serviceList );

                myRef.child("contracts").child(firebaseUser.getUid()).child(houses.gethId()).child(rooms.getId()).setValue(contract);

                Toast.makeText(RoomDetailSystem.this, "Thêm hợp đồng Thành Công !", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        btn_cancelContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
    private void setCurrentDate(TextView txtCurrentDate)
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int finalMonth = month + 1;
        txtCurrentDate.setText(day + "/" + finalMonth + "/" + year);

    }
    private void datePicker(TextView showPickTime)
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        // Implement date picker to get user's choice date
        DatePickerDialog datePickerDialog = new DatePickerDialog(RoomDetailSystem.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int Myear, int Mmonth, int MdayOfMonth) {
                String FinalDate = (MdayOfMonth + "/" + (Mmonth + 1) + "/" + (Myear) );

                showPickTime.setText(FinalDate);
            }
        }, year, month, day);

        datePickerDialog.show();
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


    private void bottomSheetContract(Contract contract)
    {
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_contract,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RoomDetailSystem.this);
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

                    adapterServiceOfRoom = new AdapterServiceOfRoom(RoomDetailSystem.this, serviceListContract);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                            RecyclerView.HORIZONTAL,false);
                    rcv_servicesContract.setLayoutManager(linearLayoutManager);
                    rcv_servicesContract.setAdapter(adapterServiceOfRoom);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("contracts").child(firebaseUser.getUid())
                    .child(houses.gethId()).child(rooms.getId()).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            startActivity(new Intent(RoomDetailSystem.this, MainActivity.class));
            RoomDetailSystem.this.finish();
            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }


        img_editContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bottomSheetDialog.dismiss();

                        Contract contractForUpdate = snapshot.getValue(Contract.class);
                        dialogUpdateContract(contractForUpdate);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
                Query query = myRef.child("contracts").child(firebaseUser.getUid())
                        .child(houses.gethId()).child(rooms.getId());
                query.addListenerForSingleValueEvent(valueEventListener);

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
    private void dialogUpdateContract(Contract contractForUpdate) {
        Dialog dialog = new Dialog(RoomDetailSystem.this);
        dialog.setContentView(R.layout.dialog_add_update_contract);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText textInputEdt_daiDienNguoiThue;
        TextView txt_showRentHouse, txt_showRentRoom, txt_camKetSoNguoiThue, txt_selectFromDate,
                txt_selectToDate, txt_selectNgayBatDauTinhTien, txt_contractTitle;

        EditText edt_kiThanhToanTienPhong, edt_tienPhong, edt_tienCoc;

        RecyclerView rcv_services;

        Button btn_addContract, btn_cancelContract;

        textInputEdt_daiDienNguoiThue = dialog.findViewById(R.id.textInputEdt_daiDienNguoiThue);

        txt_contractTitle = dialog.findViewById(R.id.txt_contractTitle);
        txt_showRentHouse = dialog.findViewById(R.id.txt_showRentHouse);
        txt_showRentRoom = dialog.findViewById(R.id.txt_showRentRoom);
        txt_camKetSoNguoiThue = dialog.findViewById(R.id.txt_camKetSoNguoiThue);
        txt_selectFromDate = dialog.findViewById(R.id.txt_selectFromDate);
        txt_selectToDate = dialog.findViewById(R.id.txt_selectToDate);
        txt_selectNgayBatDauTinhTien = dialog.findViewById(R.id.txt_selectNgayBatDauTinhTien);

        edt_kiThanhToanTienPhong = dialog.findViewById(R.id.edt_kiThanhToanTienPhong);
        edt_tienPhong = dialog.findViewById(R.id.edt_tienPhong);
        edt_tienCoc = dialog.findViewById(R.id.edt_tienCoc);

        btn_addContract = dialog.findViewById(R.id.btn_addContract);
        btn_cancelContract = dialog.findViewById(R.id.btn_cancelContract);

        rcv_services = dialog.findViewById(R.id.rcv_services);


        txt_contractTitle.setText("Cập nhật hợp đồng");
        textInputEdt_daiDienNguoiThue.setText(contractForUpdate.getDaiDienNguoiThue());
        txt_showRentHouse.setText(contractForUpdate.getRentHouse());
        txt_showRentRoom.setText(contractForUpdate.getRentRoom());
        txt_camKetSoNguoiThue.setText(contractForUpdate.getCamKetNguoiThue());
        txt_selectFromDate.setText(contractForUpdate.getFromDate());
        txt_selectToDate.setText(contractForUpdate.getToDate());
        txt_selectNgayBatDauTinhTien.setText(contractForUpdate.getNgayBatDauTinhTien());
        edt_kiThanhToanTienPhong.setText(contractForUpdate.getKyThanhToanTienPhong());


        /**
         * Format cost lấy về từ firebase
         * theo định dạng money
         * */
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        Double cost = Double.parseDouble(contractForUpdate.getTienPhong());
        edt_tienPhong.setText(formatter.format(cost));

        Double cost2 = Double.parseDouble(contractForUpdate.getTienCoc());
        edt_tienCoc.setText(formatter.format(cost2));

        formatMoneyType(edt_tienPhong);
        formatMoneyType(edt_tienCoc);

        txt_selectFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectFromDate);
            }
        });
        txt_selectToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectToDate);
            }
        });
        txt_selectNgayBatDauTinhTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(txt_selectNgayBatDauTinhTien);
            }
        });

        txt_showRentHouse.setText(houses.gethName());
        txt_showRentRoom.setText(rooms.getrName());

        // When room has contract
        adapterServiceOfRoom = new AdapterServiceOfRoom(this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_services.setLayoutManager(linearLayoutManager);
        rcv_services.setAdapter(adapterServiceOfRoom);

        btn_addContract.setText("Cập Nhật");
        btn_addContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daiDienNguoiThue = textInputEdt_daiDienNguoiThue.getText().toString().trim();

                String rentHouse = txt_showRentHouse.getText().toString().trim();
                String rentRoom = txt_showRentRoom.getText().toString().trim();
                String camKetSoNguoiThue = txt_camKetSoNguoiThue.getText().toString().trim();
                String fromDate = txt_selectFromDate.getText().toString().trim();
                String toDate = txt_selectToDate.getText().toString().trim();
                String ngayBatDauTinhTien = txt_selectNgayBatDauTinhTien.getText().toString().trim();

                String tienPhong = edt_tienPhong.getText().toString().trim();
                String tienCoc = edt_tienCoc.getText().toString().trim();
                String kiThanhToanTienPhong = edt_kiThanhToanTienPhong.getText().toString().trim();


                if (daiDienNguoiThue.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Ghi đại diện người thuê", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(camKetSoNguoiThue.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Điền cam kết số người thuê", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(toDate.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Chọn thời hạn của hợp đồng", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(kiThanhToanTienPhong.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Kỳ thanh toán tiền phòng không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tienPhong.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Tiền phòng không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tienCoc.equals(""))
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Tiền cọc không được trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (Integer.parseInt(camKetSoNguoiThue) <= 0)
                {
                    Toast.makeText(RoomDetailSystem.this, "Error : Cam kết người thuê không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (tienPhong.contains(","))
                    tienPhong = tienPhong.replaceAll(",","");
                /**
                 * Chuyển Money Type về integer để insert vào database
                 * và thực hiện tính toán.
                 * */
                if (tienCoc.contains(","))
                    tienCoc = tienCoc.replaceAll(",","");


                Contract contract = new Contract(contractForUpdate.getcId(), daiDienNguoiThue,rentHouse, rentRoom,
                        camKetSoNguoiThue, fromDate, toDate, ngayBatDauTinhTien, kiThanhToanTienPhong,tienPhong,
                        tienCoc, serviceList );

                myRef.child("contracts").child(firebaseUser.getUid()).child(houses.gethId())
                        .child(rooms.getId()).setValue(contract);

                Toast.makeText(RoomDetailSystem.this, "Sửa hợp đồng Thành Công !", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        btn_cancelContract.setOnClickListener(new View.OnClickListener() {
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
    private void setUpHoaDonRCV() {
        adapterHoaDon = new AdapterHoaDon(this,hoaDonList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,false);
        rcv_hoaDon.setLayoutManager(linearLayoutManager);
        rcv_hoaDon.setAdapter(adapterHoaDon);

        displayHoaDon();
    }
    public void displayHoaDon()
    {
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
        Query query = myRef.child("receipt").child(firebaseUser.getUid()).child(houses.gethId()).child(rooms.getId());
        query.addListenerForSingleValueEvent(valueEventListener);



    }

    public void editHoaDon(HoaDon hoaDon, BottomSheetDialog bottomSheetDialog)
    {
        Intent intent = new Intent(RoomDetailSystem.this, UpdateHoaDon.class);

        intent.putExtra("Data_House_Parcelable", houses);
        intent.putExtra("Data_Room_Parcelable", rooms);
        intent.putExtra("Data_HoaDon_Parcelable", hoaDon);

        startActivity(intent);
    }

    public void dialogConfirmDeleteHoaDon(HoaDon hoaDon, BottomSheetDialog bottomSheetDialog) {
        Dialog dialog = new Dialog(RoomDetailSystem.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CardView cv_delete = dialog.findViewById(R.id.cv_delete);
        CardView cv_cancel = dialog.findViewById(R.id.cv_cancel);

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("receipt").child(firebaseUser.getUid()).child(houses.gethId()).child(rooms.getId())
                        .child(hoaDon.getId()).removeValue();

                displayHoaDon();
                dialog.dismiss();
                bottomSheetDialog.dismiss();
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
     * (Related) Tenants
     *
     *
     *************************** */
    private void setupTenantsRCV() {
        adapterTenants = new AdapterTenants(this,tenantsList);

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

                relatedRoomDetail();
                txt_numberOfTenants.setText("Người thuê (" + tenantsList.size() + ")");

                adapterTenants.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        Query query = myRef.child("tenants").child(firebaseUser.getUid()).orderByChild("rentRoomId").equalTo(rooms.getId());
        query.addListenerForSingleValueEvent(valueEventListener);



    }

    public void editTenant(Tenants tenants, BottomSheetDialog bottomSheetDialog)
    {
        Intent intent = new Intent(RoomDetailSystem.this, UpdateTenant.class);

        intent.putExtra("Data_House_Parcelable", houses);
        intent.putExtra("Data_Room_Parcelable", rooms);
        intent.putExtra("Data_Tenant_Parcelable", tenants);

        startActivity(intent);
    }

    public void dialogConfirmDeleteTenant(Tenants tenants, BottomSheetDialog bottomSheetDialog) {
        Dialog dialog = new Dialog(RoomDetailSystem.this);
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
                bottomSheetDialog.dismiss();
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
     * (Related) room
     *
     *
     *************************** */
    private void relatedRoomDetail() {

        txt_area.setText(rooms.getrArea() + " m2");

        txt_floorNumber.setText(rooms.getrFloorNumber());

        txt_numberOfBedRooms.setText(rooms.getrBedRoomNumber());
        txt_numberOfLivingRooms.setText(rooms.getrLivingRoomNumber());
        txt_limitTenants.setText(rooms.getrLimitTenants());

        if (rooms.getrPrice().equals(""))
        {
            txt_roomFee.setText("0 đ");
        } else {
            /**
             * Format cost lấy về từ firebase
             * theo định dạng money
             * */
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            Double cost = Double.parseDouble(rooms.getrPrice());
            txt_roomFee.setText(formatter.format(cost) + " đ");
        }

        if (rooms.getrDeposit().equals(""))
        {
            txt_deposits.setText("0 đ");
        } else {
            /**
             * Format cost lấy về từ firebase
             * theo định dạng money
             * */
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            Double cost = Double.parseDouble(rooms.getrDeposit());
            txt_deposits.setText(formatter.format(cost) + " đ");
        }

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


        adapterServiceOfRoom = new AdapterServiceOfRoom(this, serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL,false);
        rcv_servicesRoomDetail.setLayoutManager(linearLayoutManager);
        rcv_servicesRoomDetail.setAdapter(adapterServiceOfRoom);

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
                    searchView_searchTenants.setVisibility(View.VISIBLE);
                    rcv_tenants.setVisibility(View.VISIBLE);
                    img_addTenants.setVisibility(View.VISIBLE);
                    img_addTenants.setVisibility(View.VISIBLE);
                    cv_contact.setVisibility(View.VISIBLE);
                    img_editRoom.setVisibility(View.VISIBLE);
                    ll_optionRooms.setVisibility(View.VISIBLE);
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmerAnimation();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            Query query = myRef.child("rooms").child(firebaseUser.getUid())
                    .child(houses.gethId()).child(rooms.getId()).child("serviceList");
            query.addListenerForSingleValueEvent(valueEventListener);
        } catch (Exception e)
        {
            startActivity(new Intent(RoomDetailSystem.this, MainActivity.class));
            RoomDetailSystem.this.finish();

            Toast.makeText(this, "Warning : Kiểm tra đường truyền Internet !", Toast.LENGTH_SHORT).show();

        }

    }



    private void dialogDeleteRoom() {
        Dialog dialog = new Dialog(RoomDetailSystem.this);
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
                        .orderByChild("rentRoomId").equalTo(rooms.getId());
                query.addListenerForSingleValueEvent(valueEventListener);



                myRef.child("contracts").child(firebaseUser.getUid()).child(houses.gethId())
                        .child(rooms.getId()).removeValue();

                myRef.child("receipt").child(firebaseUser.getUid()).child(houses.gethId())
                        .child(rooms.getId()).removeValue();

                myRef.child("rooms").child(firebaseUser.getUid()).child(houses.gethId())
                        .child(rooms.getId()).removeValue();



                dialog.dismiss();
                backToHouseDetailSystem();
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






    private void backToHouseDetailSystem()
    {
        Intent intent = new Intent(RoomDetailSystem.this, HouseDetailSystem.class);

        intent.putExtra("Data_House_Parcelable", houses);
        startActivity(intent);

        RoomDetailSystem.this.finish();

    }

    private void initUI() {
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        imgBack         =  findViewById(R.id.imgBack);
        img_addTenants  =  findViewById(R.id.img_addTenants);
        img_editRoom    = findViewById(R.id.img_editRoom);
        img_addHoaDon   = findViewById(R.id.img_addHoaDon);
        img_copyHouseRoomID = findViewById(R.id.img_copyHouseRoomID);

        rcv_tenants     =  findViewById(R.id.rcv_tenants);

        cv_contact      = findViewById(R.id.cv_contact);

        txt_roomName        =  findViewById(R.id.txt_roomName);
        txt_numberOfTenants = findViewById(R.id.txt_numberOfTenants);
        txt_roomHouseID     = findViewById(R.id.txt_roomHouseID);

        ll_danhSachTenants  =  findViewById(R.id.ll_danhSachTenants);
        ll_chiTietPhong     =  findViewById(R.id.ll_chiTietPhong);
        txt_bgColor1        =  findViewById(R.id.txt_bgColor1);
        txt_bgColor2        =  findViewById(R.id.txt_bgColor2);
        txt_bgColor3        = findViewById(R.id.txt_bgColor3);
        ll_showTenants      =  findViewById(R.id.ll_showTenants);
        ll_showRoomDetail   =  findViewById(R.id.ll_showRoomDetail);
        ll_optionRooms      = findViewById(R.id.ll_optionRooms);
        ll_hoaDon           = findViewById(R.id.ll_hoaDon);
        ll_showHoaDon       = findViewById(R.id.ll_showHoaDon);

        searchView_searchTenants = findViewById(R.id.searchView_searchTenants);


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

        rcv_servicesRoomDetail     =  findViewById(R.id.rcv_servicesRoomDetail);

        btn_deleteRoom = findViewById(R.id.btn_deleteRoom);

        rcv_hoaDon = findViewById(R.id.rcv_hoaDon);
    }


    @Override
    public void onBackPressed() {
        backToHouseDetailSystem();

        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        // Hide all function field and show shimmer effect
        searchView_searchTenants.setVisibility(View.GONE);
        rcv_tenants.setVisibility(View.GONE);
        img_addTenants.setVisibility(View.GONE);
        cv_contact.setVisibility(View.GONE);
        img_editRoom.setVisibility(View.GONE);
        ll_optionRooms.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();

        //displayServices();

        super.onStart();

    }
}
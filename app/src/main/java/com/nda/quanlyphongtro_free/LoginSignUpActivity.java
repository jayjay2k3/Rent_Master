package com.nda.quanlyphongtro_free;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nda.quanlyphongtro_free.Model.Service;
import com.nda.quanlyphongtro_free.Model.Users;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginSignUpActivity extends AppCompatActivity {

    CardView cv_gotoLogin, cv_gotoSignUp;
    TextInputEditText textInputEdt_getUname, textInputEdt_getUphonenumber, textInputEdt_getEmailSignUp,
                        textInputEdt_getPasswordSignUp, textInputEdt_getRetypePasswordSignUp;

    TextInputEditText textInputEdt_getEmailLogin, textInputEdt_getPasswordLogin;

    TextView txt_gotoLogin, txt_gotoSignUp, txt_forgotPassword, txt_underLineLogin,txt_underLineSignUp ;
    LinearLayout ll_formLogin, ll_formSignUp;

    Button btn_login, btn_signup;

    private String email, password, retypePassword, message, uName, uPhoneNumber;
    private ProgressDialog progressDialog;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;

    BottomSheetDialog bottomSheetDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        getWindow().setStatusBarColor(ContextCompat.getColor(LoginSignUpActivity.this,R.color.status_bar_login));

        initUI();

        init();
    }

    private void init()
    {
        progressDialog = new ProgressDialog(this);
        String message = getString(R.string.message_inquiring);
        progressDialog.setMessage(message);

        cv_gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
            }
        });

        cv_gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignUp();
            }
        });

        txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomsheetForgotPassword();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    /***********************************************
     *
     * (Related) Sign up
     *
     *********************************************** */
    private void onClickSignUp()
    {
        uName = textInputEdt_getUname.getText().toString().trim();
        uPhoneNumber = textInputEdt_getUphonenumber.getText().toString().trim();
        email = textInputEdt_getEmailSignUp.getText().toString().trim();
        password = textInputEdt_getPasswordSignUp.getText().toString().trim();
        retypePassword = textInputEdt_getRetypePasswordSignUp.getText().toString().trim();

        if (uName.equals("") || uPhoneNumber.equals("") || email.equals("") || password.equals("") || retypePassword.equals(""))
        {
            Toast.makeText(this, "Error : Điền đủ các thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!email.contains("@") || !email.contains(".com"))
        {
            Toast.makeText(this, "Error : Sai định dạng email !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.equals(retypePassword))
        {
            Toast.makeText(this, "Error : Mật khẩu không khớp !", Toast.LENGTH_SHORT).show();
            return;

        }
        else if(password.length() <= 5)
        {
            Toast.makeText(this, "Error : Mật khẩu phải > 5 ký tự !", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                try {
                    if (task.getResult().getSignInMethods().size() == 0){
                        // email not existed
                        startSignUp(password);
                    }else {
                        // email existed
                        message = "Error : Tài Khoản Đã Tồn Tại !";
                        Toast.makeText(LoginSignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(LoginSignUpActivity.this, "Error : Đăng Kí Thất Bại !", Toast.LENGTH_SHORT).show();
                }


            }
        });

        
    }

    private void startSignUp(String password)
    {

        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            textInputEdt_getUname.setText("");
                            textInputEdt_getUphonenumber.setText("");

                            textInputEdt_getEmailSignUp.setText("");
                            textInputEdt_getPasswordSignUp.setText("");
                            textInputEdt_getRetypePasswordSignUp.setText("");

                            // Lấy thông tin của tài khoản vừa tạo
                            firebaseUser = firebaseAuth.getCurrentUser();


                            // Add information of USER to realtime firebase
                            Users users = new Users(
                                    firebaseUser.getUid(),
                                    uName,
                                    uPhoneNumber,
                                    firebaseUser.getEmail(),
                                    password,
                                    "Thường",
                                    ""
                            );
                            myRef.child("users").child(firebaseUser.getUid()).setValue(users);

                            // Get current Datetime
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                            String currentDateAndTime = sdf.format(new Date());


                            // Add information of SERVICE to realtime firebase
                            Service service1 = new Service(1 + "_" + currentDateAndTime, "Điện", "3500", "Kwh", false);
                            Service service2 = new Service(2 + "_" + currentDateAndTime, "Nước", "20000", "Tháng", false);
                            Service service3 = new Service(3 + "_" + currentDateAndTime, "Wifi", "50000", "Tháng", false);
                            Service service4 = new Service(4 + "_" + currentDateAndTime, "Bảo Vệ", "10000", "Tháng", false);
                            Service service5 = new Service(5 + "_" + currentDateAndTime, "Giữ Xe", "120000", "Tháng", false);
                            Service service6 = new Service(6 + "_" + currentDateAndTime, "Vệ Sinh Chung", "47000", "Tháng", false);
                            Service service7 = new Service(7 + "_" + currentDateAndTime, "Rác", "6000", "Tháng", false);

                            myRef.child("services").child(firebaseUser.getUid()).child("1_"+ currentDateAndTime).setValue(service1);
                            myRef.child("services").child(firebaseUser.getUid()).child("2_"+ currentDateAndTime).setValue(service2);
                            myRef.child("services").child(firebaseUser.getUid()).child("3_"+ currentDateAndTime).setValue(service3);
                            myRef.child("services").child(firebaseUser.getUid()).child("4_"+ currentDateAndTime).setValue(service4);
                            myRef.child("services").child(firebaseUser.getUid()).child("5_"+ currentDateAndTime).setValue(service5);
                            myRef.child("services").child(firebaseUser.getUid()).child("6_"+ currentDateAndTime).setValue(service6);
                            myRef.child("services").child(firebaseUser.getUid()).child("7_"+ currentDateAndTime).setValue(service7);

                            dialogSignUpSuccess();
                        }
                        else
                        {
                            Toast.makeText(LoginSignUpActivity.this, "Error : Đăng Kí Thất Bại !",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void dialogSignUpSuccess()
    {
        Dialog dialog = new Dialog(LoginSignUpActivity.this);
        dialog.setContentView(R.layout.dialog_sign_up_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        CardView cv_gotoHomePage = (CardView) dialog.findViewById(R.id.cv_gotoHomePage);
        CardView cv_gotoLogin = (CardView) dialog.findViewById(R.id.cv_gotoLogin);

        cv_gotoHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(LoginSignUpActivity.this,MainActivity.class));
                finishAffinity();
            }
        });

        cv_gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isLogin();
            }
        });

        dialog.show();
    }


    /***********************************************
     *
     * (Related ) Login
     *
     *********************************************** */
    private void onClickLogin()
    {

        email = textInputEdt_getEmailLogin.getText().toString().trim();
        password = textInputEdt_getPasswordLogin.getText().toString().trim();

        if (email.equals("") || password.equals(""))
        {
            Toast.makeText(this, "Error : Điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!email.contains("@") || !email.contains(".com"))
        {
            Toast.makeText(this, "Error : Sai định dạng email !", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length() <= 5)
        {
            Toast.makeText(this, "Error : Mật khẩu phải > 5 ký tự !", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                try {

                    if (task.getResult().getSignInMethods().size() == 0){
                        // email not existed
                        Toast.makeText(LoginSignUpActivity.this, "Error : Tài khoản không tồn tại !", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        // email existed
                        startLogin();
                    }
                }
                 catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error : Đăng nhập thất bại !", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    private void startLogin()
    {
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(LoginSignUpActivity.this, MainActivity.class));
                            finishAffinity();
                        }
                        else
                        {
                            Toast.makeText(LoginSignUpActivity.this, "Error : Tài khoản hoặc mật khẩu không chính xác !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void bottomsheetForgotPassword()
    {
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_forgot_password, null);
        bottomSheetDialog = new BottomSheetDialog(LoginSignUpActivity.this);
        bottomSheetDialog.setContentView(view);

        EditText edt_forgotEmail    = (EditText) view.findViewById(R.id.textInputEdt_forgotEmail);
        TextView txt_sentSuccess    = (TextView) view.findViewById(R.id.txt_sentSuccess);
        TextView txt_sentFailed    = (TextView) view.findViewById(R.id.txt_sentFailed);
        CardView cv_sentToForgotEmail   = (CardView) view.findViewById(R.id.cv_sentToForgotEmail);

        cv_sentToForgotEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = edt_forgotEmail.getText().toString().trim();

                if (emailAddress.equals(""))
                {
                    progressDialog.dismiss();

                    edt_forgotEmail.setError("Error : Không được trống !");

                    return;
                }
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();

                                    txt_sentFailed.setVisibility(View.GONE);
                                    txt_sentSuccess.setVisibility(View.VISIBLE);

                                }
                                else
                                {
                                    progressDialog.dismiss();

                                    txt_sentSuccess.setVisibility(View.GONE);
                                    txt_sentFailed.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });

        bottomSheetDialog.show();
    }


    private void isLogin()
    {
        txt_underLineLogin.setBackgroundColor(Color.parseColor("#000000"));
        txt_gotoLogin.setTextColor(Color.parseColor("#1E1E1E"));

        txt_underLineSignUp.setBackgroundColor((Color.parseColor("#FFFFFF")));
        txt_gotoSignUp.setTextColor(Color.parseColor("#686868"));

        ll_formLogin.setVisibility(View.VISIBLE);
        ll_formSignUp.setVisibility(View.GONE);
    }
    private void isSignUp()
    {
        txt_underLineSignUp.setBackgroundColor(Color.parseColor("#000000"));
        txt_gotoSignUp.setTextColor(Color.parseColor("#1E1E1E"));

        txt_underLineLogin.setBackgroundColor((Color.parseColor("#FFFFFF")));
        txt_gotoLogin.setTextColor(Color.parseColor("#686868"));

        ll_formSignUp.setVisibility(View.VISIBLE);
        ll_formLogin.setVisibility(View.GONE);
    }




    private void initUI()
    {

        ll_formLogin    = findViewById(R.id.ll_formLogin);
        ll_formSignUp   = findViewById(R.id.ll_formSignUp);


        txt_gotoLogin       = findViewById(R.id.txt_gotoLogin);
        txt_gotoSignUp      = findViewById(R.id.txt_gotoSignUp);
        txt_forgotPassword  = findViewById(R.id.txt_forgotPassword);
        txt_underLineLogin  = findViewById(R.id.txt_underLineLogin);
        txt_underLineSignUp = findViewById(R.id.txt_underLineSignUp);


        cv_gotoLogin           = findViewById(R.id.cv_gotoLogin);
        cv_gotoSignUp          = findViewById(R.id.cv_gotoSignUp);
        btn_login     = findViewById(R.id.btn_login);
        btn_signup    = findViewById(R.id.btn_signup);

        textInputEdt_getEmailLogin             = findViewById(R.id.textInputEdt_getEmailLogin);
        textInputEdt_getPasswordLogin          = findViewById(R.id.textInputEdt_getPasswordLogin);

        textInputEdt_getUname                  = findViewById(R.id.textInputEdt_getUname);
        textInputEdt_getUphonenumber           = findViewById(R.id.textInputEdt_getUphonenumber);
        textInputEdt_getEmailSignUp            = findViewById(R.id.textInputEdt_getEmailSignUp);
        textInputEdt_getPasswordSignUp         = findViewById(R.id.textInputEdt_getPasswordSignUp);
        textInputEdt_getRetypePasswordSignUp   = findViewById(R.id.textInputEdt_getRetypePasswordSignUp);

    }
}
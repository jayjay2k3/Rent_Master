package com.nda.quanlyphongtro_free.Note;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Note;
import com.nda.quanlyphongtro_free.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUpdateNote extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();;


    ImageView img_back, img_save;

    EditText edt_noteTitle, edt_noteContent;
    TextView txt_createdDate;

    ProgressDialog progressDialog;

    String noteTitle, noteContent;

    /**
     * Color picker variables
     * */
    LinearLayout ll_color_1, ll_color_2, ll_color_3, ll_color_4, ll_color_5;
    TextView txt_color_1, txt_color_2, txt_color_3, txt_color_4, txt_color_5;
    String backgroundColor = "#754545";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setStatusBarColor(ContextCompat.getColor(AddUpdateNote.this,R.color.status_bar_login));

        initUI();

        colorPicker();

        init();
    }
    private void init()
    {
        progressDialog = new ProgressDialog(AddUpdateNote.this);
        String message = getString(R.string.message_inquiring);
        progressDialog.setMessage(message);


        /**
         * Get Signal
         * */
        Intent intent = getIntent();
        String signal = intent.getStringExtra("signalAddUpdate");

        if (signal.equals("addNote"))
        {
            executeAddNote();
        }
        if (signal.equals("updateNote"))
        {
            executeUpdateNote();

        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToNoteManagement();
            }
        });
    }

    private void executeAddNote() {
        txt_createdDate.setVisibility(View.GONE);

        Date date = new Date();

        /**
         *
         * */
        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Get current Date
                 * */
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String currDateTime = formatter.format(date);

                /**
                 * Get noteID
                 * */
                SimpleDateFormat formatterForNoteID = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                String noteID = firebaseUser.getUid() + "_note_" + formatterForNoteID.format(date);
                Log.d("noteTest", noteID);


                noteTitle = edt_noteTitle.getText().toString().trim();
                noteContent = edt_noteContent.getText().toString().trim();

                if (noteTitle.equals("") || noteContent.equals(""))
                {
                    Toast.makeText(AddUpdateNote.this, "Error : điền đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Note note = new Note(noteID,firebaseUser.getUid(), noteTitle, noteContent,currDateTime,
                        backgroundColor);

                myRef.child("notes").child(firebaseUser.getUid()).child(noteID).setValue(note)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful())
                                {
                                    progressDialog.dismiss();

                                    backToNoteManagement();
                                }
                                else
                                {
                                    Toast.makeText(AddUpdateNote.this, "Error : Fail to Add ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    private void executeUpdateNote()
    {
        txt_createdDate.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String updateNoteID = intent.getStringExtra("available_noteID");
        String updateTitle = intent.getStringExtra("available_noteTitle");
        String updateContent = intent.getStringExtra("available_noteContent");
        String createdDate = intent.getStringExtra("available_noteCreatedDate");
        String updateBackgroundColor = intent.getStringExtra("available_noteBackgroundColor");

        edt_noteTitle.setText(updateTitle);
        edt_noteContent.setText(updateContent);
        txt_createdDate.setText(createdDate);

        /**
         * Edit available Note
         * */
        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noteTitle = edt_noteTitle.getText().toString().trim();
                noteContent = edt_noteContent.getText().toString().trim();

                if (noteTitle.equals("") || noteContent.equals(""))
                {
                    Toast.makeText(AddUpdateNote.this, "Error : điền đủ thông tin", Toast.LENGTH_SHORT).show();

                    return;
                }

                Note updateNote = new Note(updateNoteID,firebaseUser.getUid(), noteTitle, noteContent,createdDate,
                        backgroundColor);


                myRef.child("notes").child(firebaseUser.getUid()).child(updateNoteID).setValue(updateNote)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful())
                                {
                                    progressDialog.dismiss();

                                    backToNoteManagement();

                                }
                                else
                                {
                                    Toast.makeText(AddUpdateNote.this, "Error : Fail to Add ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void colorPicker()
    {
        ll_color_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_color_1.setVisibility(View.VISIBLE);
                txt_color_2.setVisibility(View.GONE);
                txt_color_3.setVisibility(View.GONE);
                txt_color_4.setVisibility(View.GONE);
                txt_color_5.setVisibility(View.GONE);
                backgroundColor = "#754545";
            }
        });
        ll_color_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_color_1.setVisibility(View.GONE);
                txt_color_2.setVisibility(View.VISIBLE);
                txt_color_3.setVisibility(View.GONE);
                txt_color_4.setVisibility(View.GONE);
                txt_color_5.setVisibility(View.GONE);
                backgroundColor = "#543291";
            }
        });
        ll_color_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_color_1.setVisibility(View.GONE);
                txt_color_2.setVisibility(View.GONE);
                txt_color_3.setVisibility(View.VISIBLE);
                txt_color_4.setVisibility(View.GONE);
                txt_color_5.setVisibility(View.GONE);
                backgroundColor = "#237327";

            }
        });
        ll_color_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_color_1.setVisibility(View.GONE);
                txt_color_2.setVisibility(View.GONE);
                txt_color_3.setVisibility(View.GONE);
                txt_color_4.setVisibility(View.VISIBLE);
                txt_color_5.setVisibility(View.GONE);
                backgroundColor = "#1B5570";

            }
        });
        ll_color_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_color_1.setVisibility(View.GONE);
                txt_color_2.setVisibility(View.GONE);
                txt_color_3.setVisibility(View.GONE);
                txt_color_4.setVisibility(View.GONE);
                txt_color_5.setVisibility(View.VISIBLE);
                backgroundColor = "#988C1E";

            }
        });
    }
    private void initUI() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_save = (ImageView) findViewById(R.id.img_save);

        edt_noteTitle   = (EditText) findViewById(R.id.edt_noteTitle);
        edt_noteContent = (EditText) findViewById(R.id.edt_noteContent);
        txt_createdDate = (TextView) findViewById(R.id.txt_createdDate);


        ll_color_1 = (LinearLayout) findViewById(R.id.ll_color_1);
        ll_color_2 = (LinearLayout) findViewById(R.id.ll_color_2);
        ll_color_3 = (LinearLayout) findViewById(R.id.ll_color_3);
        ll_color_4 = (LinearLayout) findViewById(R.id.ll_color_4);
        ll_color_5 = (LinearLayout) findViewById(R.id.ll_color_5);

        txt_color_1 = (TextView) findViewById(R.id.txt_color_1);
        txt_color_2 = (TextView) findViewById(R.id.txt_color_2);
        txt_color_3 = (TextView) findViewById(R.id.txt_color_3);
        txt_color_4 = (TextView) findViewById(R.id.txt_color_4);
        txt_color_5 = (TextView) findViewById(R.id.txt_color_5);

    }

    private void backToNoteManagement() {
        startActivity(new Intent(AddUpdateNote.this, NoteManagement.class));
        AddUpdateNote.this.finish();
    }

    @Override
    public void onBackPressed() {
        backToNoteManagement();
        super.onBackPressed();
    }
}
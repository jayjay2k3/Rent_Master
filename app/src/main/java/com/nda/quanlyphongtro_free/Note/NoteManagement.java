package com.nda.quanlyphongtro_free.Note;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.nda.quanlyphongtro_free.Houses.AddHouse.AddHouse;
import com.nda.quanlyphongtro_free.Houses.HousesSystem;
import com.nda.quanlyphongtro_free.MainActivity;
import com.nda.quanlyphongtro_free.Model.Note;
import com.nda.quanlyphongtro_free.R;

import java.util.ArrayList;
import java.util.List;

public class NoteManagement extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();;

    View view;

    ImageView img_addNote, img_back;


    ProgressDialog progressDialog;

    List<Note> noteList  = new ArrayList<>();
    AdapterNote adapterNote;
    RecyclerView rcv_note;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initUI();
        init();

        setupRecycleView();

    }


    private void init()
    {
        progressDialog = new ProgressDialog(NoteManagement.this);
        String message = getString(R.string.message_inquiring);
        progressDialog.setMessage(message);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });
        img_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteManagement.this, AddUpdateNote.class);
                intent.putExtra("signalAddUpdate", "addNote");

                startActivity(intent);
            }
        });
    }



    private void setupRecycleView()
    {
        adapterNote = new AdapterNote(this, noteList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        rcv_note.setLayoutManager(staggeredGridLayoutManager);
        rcv_note.setAdapter(adapterNote);

        displayNote();
    }



    public void displayNote()
    {
        noteList.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Note note =dataSnapshot.getValue(Note.class);

                    noteList.add(0, note);

                }

                adapterNote.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query query = myRef.child("notes").child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);

    }
    private void backToMain() {
        startActivity(new Intent(NoteManagement.this, MainActivity.class));
        NoteManagement.this.finish();
    }

    private void initUI()
    {
        rcv_note = findViewById(R.id.rcv_note);

        img_addNote = findViewById(R.id.img_addNote);
        img_back = findViewById(R.id.img_back);
    }

    @Override
    public void onBackPressed() {
        backToMain();
        super.onBackPressed();
    }
}
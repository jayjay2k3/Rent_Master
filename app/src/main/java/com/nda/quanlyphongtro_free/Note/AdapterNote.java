package com.nda.quanlyphongtro_free.Note;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.nda.quanlyphongtro_free.Model.Note;
import com.nda.quanlyphongtro_free.R;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.HolderNote> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef  = database.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();;

    NoteManagement context;
    List<Note> noteList;

    public AdapterNote(NoteManagement context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public HolderNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_note, parent,false);

        return new HolderNote(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNote holder, int position) {
        Note note = noteList.get(position);

        holder.txt_showNoteTitle.setText(note.getTitle());
        holder.txt_showNoteContent.setText(note.getContent());
        holder.txt_showNoteCreatedDate.setText(note.getCreatedDate());

        String itemColor = note.getBackgroundColor();

        holder.ll_noteItem.setBackgroundColor(Color.parseColor(itemColor));

        holder.cv_itemNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddUpdateNote.class);
                intent.putExtra("signalAddUpdate", "updateNote");
                intent.putExtra("available_noteID", note.getId());
                intent.putExtra("available_noteTitle", note.getTitle());
                intent.putExtra("available_noteContent", note.getContent());
                intent.putExtra("available_noteCreatedDate", note.getCreatedDate());
                intent.putExtra("available_noteBackgroundColor", note.getBackgroundColor());
                context.startActivity(intent);
            }
        });

        holder.cv_itemNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogDeleteNote(firebaseUser.getUid(), note.getId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class HolderNote extends RecyclerView.ViewHolder {
        CardView cv_itemNote;
        TextView txt_showNoteTitle, txt_showNoteContent, txt_showNoteCreatedDate;
        LinearLayout ll_noteItem;

        public HolderNote(@NonNull View itemView) {
            super(itemView);

            cv_itemNote = (CardView) itemView.findViewById(R.id.cv_itemNote);

            ll_noteItem = (LinearLayout) itemView.findViewById(R.id.ll_noteItem);

            txt_showNoteTitle       = (TextView) itemView.findViewById(R.id.txt_showNoteTitle);
            txt_showNoteContent     = (TextView) itemView.findViewById(R.id.txt_showNoteContent);
            txt_showNoteCreatedDate = (TextView) itemView.findViewById(R.id.txt_showNoteCreatedDate);

        }
    }

    private void dialogDeleteNote(String userID,String noteID)
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CardView cv_delete, cv_cancel;

        cv_cancel   = (CardView) dialog.findViewById(R.id.cv_cancel);
        cv_delete   = (CardView) dialog.findViewById(R.id.cv_delete);

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child("notes").child(userID).child(noteID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    context.displayNote();

                                    dialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(context, "Error : Fail to Delete !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
        cv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

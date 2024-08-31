package com.nda.quanlyphongtro_free.JoinRoom;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nda.quanlyphongtro_free.Houses.HouseDetail.Rooms.RoomDetail.RoomDetailSystem;
import com.nda.quanlyphongtro_free.Model.JoinRoom;
import com.nda.quanlyphongtro_free.R;

import java.util.List;

public class AdapterJoinRoom extends RecyclerView.Adapter<AdapterJoinRoom.HolderJoinRoom> {
    JoinRoomSystem context;
    List<JoinRoom> joinRoomList;

    public AdapterJoinRoom(JoinRoomSystem context, List<JoinRoom> joinRoomList) {
        this.context = context;
        this.joinRoomList = joinRoomList;
    }

    @NonNull
    @Override
    public AdapterJoinRoom.HolderJoinRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_joined_rooms, parent, false);
        return new AdapterJoinRoom.HolderJoinRoom(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterJoinRoom.HolderJoinRoom holder, int position) {
        JoinRoom joinRoom = joinRoomList.get(position);

        context.getInformationOfJoinedRoom(joinRoom, holder.txt_joinRoomName,
                holder.txt_joinRoomFloor, holder.txt_joinRoomFee);

        holder.cv_joinRoomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JoinedRoomDetail.class);

                intent.putExtra("Data_JoinedRoom_Parcelable", joinRoom);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return joinRoomList.size();
    }

    public class HolderJoinRoom extends RecyclerView.ViewHolder {
        CardView cv_joinRoomItem;
        TextView txt_joinRoomName, txt_joinRoomFloor, txt_joinRoomFee;
        public HolderJoinRoom(@NonNull View itemView) {
            super(itemView);

            cv_joinRoomItem = itemView.findViewById(R.id.cv_joinRoomItem);
            txt_joinRoomName = itemView.findViewById(R.id.txt_joinRoomName);
            txt_joinRoomFloor = itemView.findViewById(R.id.txt_joinRoomFloor);
            txt_joinRoomFee = itemView.findViewById(R.id.txt_joinRoomFee);


        }
    }
}

package com.nda.quanlyphongtro_free.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class HoaDon implements Parcelable {
    private String id;
    private String hoaDonThang, rentHouse, rentRoom, ngayThanhToan, hanThanhToan, roomFee,
             note, totalServiceFee, noteServices;

    private boolean daThanhToan = false;

    public HoaDon(){}

    public HoaDon(String id, String hoaDonThang, String rentHouse, String rentRoom, String ngayThanhToan,
                  String hanThanhToan, String roomFee, String note, String totalServiceFee, String noteServices,
                  boolean daThanhToan) {
        this.id = id;
        this.hoaDonThang = hoaDonThang;
        this.rentHouse = rentHouse;
        this.rentRoom = rentRoom;
        this.ngayThanhToan = ngayThanhToan;
        this.hanThanhToan = hanThanhToan;
        this.roomFee = roomFee;
        this.note = note;
        this.totalServiceFee = totalServiceFee;
        this.noteServices = noteServices;
        this.daThanhToan = daThanhToan;
    }

    protected HoaDon(Parcel in) {
        id = in.readString();
        hoaDonThang = in.readString();
        rentHouse = in.readString();
        rentRoom = in.readString();
        ngayThanhToan = in.readString();
        hanThanhToan = in.readString();
        roomFee = in.readString();
        note = in.readString();
        totalServiceFee = in.readString();
        noteServices = in.readString();
        daThanhToan = in.readByte() != 0;
    }

    public static final Creator<HoaDon> CREATOR = new Creator<HoaDon>() {
        @Override
        public HoaDon createFromParcel(Parcel in) {
            return new HoaDon(in);
        }

        @Override
        public HoaDon[] newArray(int size) {
            return new HoaDon[size];
        }
    };

    public String getId() {
        return id;
    }


    public String getHoaDonThang() {
        return hoaDonThang;
    }

    public void setHoaDonThang(String hoaDonThang) {
        this.hoaDonThang = hoaDonThang;
    }

    public String getRentHouse() {
        return rentHouse;
    }

    public void setRentHouse(String rentHouse) {
        this.rentHouse = rentHouse;
    }

    public String getRentRoom() {
        return rentRoom;
    }

    public void setRentRoom(String rentRoom) {
        this.rentRoom = rentRoom;
    }

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getHanThanhToan() {
        return hanThanhToan;
    }

    public void setHanThanhToan(String hanThanhToan) {
        this.hanThanhToan = hanThanhToan;
    }

    public String getRoomFee() {
        return roomFee;
    }

    public void setRoomFee(String roomFee) {
        this.roomFee = roomFee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTotalServiceFee() {
        return totalServiceFee;
    }

    public void setTotalServiceFee(String totalServiceFee) {
        this.totalServiceFee = totalServiceFee;
    }

    public String getNoteServices() {
        return noteServices;
    }

    public void setNoteServices(String noteServices) {
        this.noteServices = noteServices;
    }

    public boolean isDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(hoaDonThang);
        parcel.writeString(rentHouse);
        parcel.writeString(rentRoom);
        parcel.writeString(ngayThanhToan);
        parcel.writeString(hanThanhToan);
        parcel.writeString(roomFee);
        parcel.writeString(note);
        parcel.writeString(totalServiceFee);
        parcel.writeString(noteServices);
        parcel.writeByte((byte) (daThanhToan ? 1 : 0));
    }
}

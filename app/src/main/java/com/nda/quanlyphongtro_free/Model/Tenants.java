package com.nda.quanlyphongtro_free.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Tenants implements Parcelable {
    private String id, rentHouseId, rentRoomId;
    private String tName, tPhoneNumber, tRentHouse, tRentRoom, tEmail,
            tDob, tNoiSinh, tSoCMND, tNgayCapCMND, tNoiCapCMND;

    public Tenants(){}
    public Tenants(String id, String rentHouseId, String rentRoomId, String tName, String tPhoneNumber,
                   String tRentHouse, String tRentRoom,
                   String tEmail, String tDob, String tNoiSinh, String tSoCMND, String tNgayCapCMND,
                   String tNoiCapCMND) {
        this.id = id;
        this.rentHouseId = rentHouseId;
        this.rentRoomId = rentRoomId;
        this.tName = tName;
        this.tPhoneNumber = tPhoneNumber;
        this.tRentHouse = tRentHouse;
        this.tRentRoom = tRentRoom;
        this.tEmail = tEmail;
        this.tDob = tDob;
        this.tNoiSinh = tNoiSinh;
        this.tSoCMND = tSoCMND;
        this.tNgayCapCMND = tNgayCapCMND;
        this.tNoiCapCMND = tNoiCapCMND;
    }

    protected Tenants(Parcel in) {
        id = in.readString();
        rentHouseId = in.readString();
        rentRoomId = in.readString();
        tName = in.readString();
        tPhoneNumber = in.readString();
        tRentHouse = in.readString();
        tRentRoom = in.readString();
        tEmail = in.readString();
        tDob = in.readString();
        tNoiSinh = in.readString();
        tSoCMND = in.readString();
        tNgayCapCMND = in.readString();
        tNoiCapCMND = in.readString();
    }

    public static final Creator<Tenants> CREATOR = new Creator<Tenants>() {
        @Override
        public Tenants createFromParcel(Parcel in) {
            return new Tenants(in);
        }

        @Override
        public Tenants[] newArray(int size) {
            return new Tenants[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getRentHouseId() {
        return rentHouseId;
    }


    public String getRentRoomId() {
        return rentRoomId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettPhoneNumber() {
        return tPhoneNumber;
    }

    public void settPhoneNumber(String tPhoneNumber) {
        this.tPhoneNumber = tPhoneNumber;
    }

    public String gettRentHouse() {
        return tRentHouse;
    }

    public void settRentHouse(String tRentHouse) {
        this.tRentHouse = tRentHouse;
    }

    public String gettRentRoom() {
        return tRentRoom;
    }

    public void settRentRoom(String tRentRoom) {
        this.tRentRoom = tRentRoom;
    }

    public String gettEmail() {
        return tEmail;
    }

    public void settEmail(String tEmail) {
        this.tEmail = tEmail;
    }

    public String gettDob() {
        return tDob;
    }

    public void settDob(String tDob) {
        this.tDob = tDob;
    }

    public String gettNoiSinh() {
        return tNoiSinh;
    }

    public void settNoiSinh(String tNoiSinh) {
        this.tNoiSinh = tNoiSinh;
    }

    public String gettSoCMND() {
        return tSoCMND;
    }

    public void settSoCMND(String tSoCMND) {
        this.tSoCMND = tSoCMND;
    }

    public String gettNgayCapCMND() {
        return tNgayCapCMND;
    }

    public void settNgayCapCMND(String tNgayCapCMND) {
        this.tNgayCapCMND = tNgayCapCMND;
    }

    public String gettNoiCapCMND() {
        return tNoiCapCMND;
    }

    public void settNoiCapCMND(String tNoiCapCMND) {
        this.tNoiCapCMND = tNoiCapCMND;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(rentHouseId);
        parcel.writeString(rentRoomId);
        parcel.writeString(tName);
        parcel.writeString(tPhoneNumber);
        parcel.writeString(tRentHouse);
        parcel.writeString(tRentRoom);
        parcel.writeString(tEmail);
        parcel.writeString(tDob);
        parcel.writeString(tNoiSinh);
        parcel.writeString(tSoCMND);
        parcel.writeString(tNgayCapCMND);
        parcel.writeString(tNoiCapCMND);
    }
}

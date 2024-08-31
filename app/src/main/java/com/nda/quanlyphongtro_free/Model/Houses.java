package com.nda.quanlyphongtro_free.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Houses implements Parcelable {
    private String hId;
    private String hName, hFloorsNumber, hFee, hDescription, hAddress, hTinhThanhPho,
            hQuanHuyen,hOpenTime, hCloseTime, hBaoSoNgayChuyen, hNote;

    private List<Service> serviceList;

    public Houses(){}
    public Houses(String hId, String hName, String hFloorsNumber, String hFee, String hDescription,
                  String hAddress, String hTinhThanhPho, String hQuanHuyen, List<Service> serviceList,
                  String hOpenTime, String hCloseTime, String hBaoSoNgayChuyen, String hNote) {
        this.hId = hId;
        this.hName = hName;
        this.hFloorsNumber = hFloorsNumber;
        this.hFee = hFee;
        this.hDescription = hDescription;
        this.hAddress = hAddress;
        this.hTinhThanhPho = hTinhThanhPho;
        this.hQuanHuyen = hQuanHuyen;
        this.hOpenTime = hOpenTime;
        this.hCloseTime = hCloseTime;
        this.hBaoSoNgayChuyen = hBaoSoNgayChuyen;
        this.hNote = hNote;
        this.serviceList = serviceList;
    }

    // Related to Parcelable
    protected Houses(Parcel in) {
        hId = in.readString();
        hName = in.readString();
        hFloorsNumber = in.readString();
        hFee = in.readString();
        hDescription = in.readString();
        hAddress = in.readString();
        hTinhThanhPho = in.readString();
        hQuanHuyen = in.readString();
        hOpenTime = in.readString();
        hCloseTime = in.readString();
        hBaoSoNgayChuyen = in.readString();
        hNote = in.readString();
    }

    public static final Creator<Houses> CREATOR = new Creator<Houses>() {
        @Override
        public Houses createFromParcel(Parcel in) {
            return new Houses(in);
        }

        @Override
        public Houses[] newArray(int size) {
            return new Houses[size];
        }
    };

    public String gethId() {
        return hId;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String gethFloorsNumber() {
        return hFloorsNumber;
    }

    public void sethFloorsNumber(String hFloorsNumber) {
        this.hFloorsNumber = hFloorsNumber;
    }

    public String gethFee() {
        return hFee;
    }

    public void sethFee(String hFee) {
        this.hFee = hFee;
    }

    public String gethDescription() {
        return hDescription;
    }

    public void sethDescription(String hDescription) {
        this.hDescription = hDescription;
    }

    public String gethAddress() {
        return hAddress;
    }

    public void sethAddress(String hAddress) {
        this.hAddress = hAddress;
    }

    public String gethTinhThanhPho() {
        return hTinhThanhPho;
    }

    public void sethTinhThanhPho(String hTinhThanhPho) {
        this.hTinhThanhPho = hTinhThanhPho;
    }

    public String gethQuanHuyen() {
        return hQuanHuyen;
    }

    public void sethQuanHuyen(String hQuanHuyen) {
        this.hQuanHuyen = hQuanHuyen;
    }

    public String gethOpenTime() {
        return hOpenTime;
    }

    public void sethOpenTime(String hOpenTime) {
        this.hOpenTime = hOpenTime;
    }

    public String gethCloseTime() {
        return hCloseTime;
    }

    public void sethCloseTime(String hCloseTime) {
        this.hCloseTime = hCloseTime;
    }

    public String gethBaoSoNgayChuyen() {
        return hBaoSoNgayChuyen;
    }

    public void sethBaoSoNgayChuyen(String hBaoSoNgayChuyen) {
        this.hBaoSoNgayChuyen = hBaoSoNgayChuyen;
    }

    public String gethNote() {
        return hNote;
    }

    public void sethNote(String hNote) {
        this.hNote = hNote;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }


    // Related to Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hId);
        parcel.writeString(hName);
        parcel.writeString(hFloorsNumber);
        parcel.writeString(hFee);
        parcel.writeString(hDescription);
        parcel.writeString(hAddress);
        parcel.writeString(hTinhThanhPho);
        parcel.writeString(hQuanHuyen);
        parcel.writeString(hOpenTime);
        parcel.writeString(hCloseTime);
        parcel.writeString(hBaoSoNgayChuyen);
        parcel.writeString(hNote);
    }
}

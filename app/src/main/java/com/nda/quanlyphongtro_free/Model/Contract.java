package com.nda.quanlyphongtro_free.Model;

import java.util.List;

public class Contract {
    private String cId;
    private String daiDienNguoiThue, rentHouse, rentRoom, camKetNguoiThue, fromDate,
            toDate, ngayBatDauTinhTien, kyThanhToanTienPhong, tienPhong, tienCoc;
    private List<Service> serviceList;

    public Contract(){}
    public Contract(String cId, String daiDienNguoiThue, String rentHouse, String rentRoom,
                    String camKetNguoiThue, String fromDate, String toDate, String ngayBatDauTinhTien,
                    String kyThanhToanTienPhong, String tienPhong, String tienCoc, List<Service> serviceList) {
        this.cId = cId;
        this.daiDienNguoiThue = daiDienNguoiThue;
        this.rentHouse = rentHouse;
        this.rentRoom = rentRoom;
        this.camKetNguoiThue = camKetNguoiThue;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ngayBatDauTinhTien = ngayBatDauTinhTien;
        this.kyThanhToanTienPhong = kyThanhToanTienPhong;
        this.tienPhong = tienPhong;
        this.tienCoc = tienCoc;
        this.serviceList = serviceList;
    }

    public String getcId() {
        return cId;
    }


    public String getDaiDienNguoiThue() {
        return daiDienNguoiThue;
    }

    public void setDaiDienNguoiThue(String daiDienNguoiThue) {
        this.daiDienNguoiThue = daiDienNguoiThue;
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

    public String getCamKetNguoiThue() {
        return camKetNguoiThue;
    }

    public void setCamKetNguoiThue(String camKetNguoiThue) {
        this.camKetNguoiThue = camKetNguoiThue;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getNgayBatDauTinhTien() {
        return ngayBatDauTinhTien;
    }

    public void setNgayBatDauTinhTien(String ngayBatDauTinhTien) {
        this.ngayBatDauTinhTien = ngayBatDauTinhTien;
    }

    public String getKyThanhToanTienPhong() {
        return kyThanhToanTienPhong;
    }

    public void setKyThanhToanTienPhong(String kyThanhToanTienPhong) {
        this.kyThanhToanTienPhong = kyThanhToanTienPhong;
    }

    public String getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(String tienPhong) {
        this.tienPhong = tienPhong;
    }

    public String getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(String tienCoc) {
        this.tienCoc = tienCoc;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }
}

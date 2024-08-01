/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Phong {
    @CsvBindByPosition(position = 0)
    @SerializedName("maP")
    @Expose
    private String maP;
    @CsvBindByPosition(position = 1)
    @SerializedName("Khach_hang")
    @Expose
    private Khach_hang KH;
    @CsvBindByPosition(position = 2)
    @SerializedName("tang")
    @Expose
    private int tang;
    @CsvBindByPosition(position = 3)
    @SerializedName("giaPhong")
    @Expose
    private double giaPhong;
    @CsvBindByPosition(position = 4)
    @SerializedName("ngayDatPhong")
    @Expose
    private LocalDate ngayDatPhong;
    @CsvBindByPosition(position = 5)
    @SerializedName("ngayTraPhong")
    @Expose
    private LocalDate ngayTraPhong;
    @CsvBindByPosition(position = 6)
    @SerializedName("trangThai")
    @Expose
    private String trangThai;
    @CsvBindByPosition(position = 7)
    @SerializedName("sucChua")
    @Expose
    private int sucChua;

    public Phong(String maP, Khach_hang maKH, int tang, double giaPhong, LocalDate ngayDatPhong, LocalDate ngayTraPhong, String trangThai, int sucChua) {
        this.maP = maP;
        this.KH = maKH;
        this.tang = tang;
        this.giaPhong = giaPhong;
        this.ngayDatPhong = ngayDatPhong;
        this.ngayTraPhong = ngayTraPhong;
        this.trangThai = trangThai;
        this.sucChua = sucChua;
    }

    public Phong() {
    }

    public String getMaP() {
        return maP;
    }

    public void setMaP(String maP) {
        this.maP = maP;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }

    public LocalDate getNgayDatPhong() {
        return ngayDatPhong;
    }

    public void setNgayDatPhong(LocalDate ngayDatPhong) {
        this.ngayDatPhong = ngayDatPhong;
    }

    public LocalDate getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void setNgayTraPhong(LocalDate ngayTraPhong) {
        this.ngayTraPhong = ngayTraPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    @Override
    public String toString() {
        return "Phong{" + "maP=" + maP + ", maKH=" + KH.getMaKH() + ", tang=" + tang + ", giaPhong=" + giaPhong + ", ngayDatPhong=" + ngayDatPhong + ", ngayTraPhong=" + ngayTraPhong + ", trangThai=" + trangThai + ", sucChua=" + sucChua + '}';
    }

    public Khach_hang getKH() {
        return KH;
    }

    public void setKH(Khach_hang KH) {
        this.KH = KH;
    }

}

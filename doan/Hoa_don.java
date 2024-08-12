/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opencsv.bean.CsvBindByPosition;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Hoa_don {
    @CsvBindByPosition(position = 0)
    @SerializedName("maHD")
    @Expose
    private String maHD;
    @CsvBindByPosition(position = 1)
    @SerializedName("maKH")
    @Expose
    private String maKH;
    @CsvBindByPosition(position = 2)
    @SerializedName("giaTien")
    @Expose
    private double giaTien;
    @CsvBindByPosition(position = 3)
    @SerializedName("ngayThanhToan")
    @Expose
    private LocalDate ngayThanhToan;

    public Hoa_don() {
    }

    public Hoa_don(String maHD, String maKH, double giaTien, LocalDate ngayThanhToan) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.giaTien = giaTien;
        this.ngayThanhToan = ngayThanhToan;
    }

    @Override
    public String toString() {
        return "Hoa_don{" + "maHD=" + maHD + ", maKH=" + maKH + ", giaTien=" + giaTien + ", ngayThanhToan=" + ngayThanhToan + '}';
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public LocalDate getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDate ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opencsv.bean.CsvBindByPosition;
import javax.swing.text.Position;

/**
 *
 * @author Admin
 */
public class Khach_hang {

    @SerializedName("maKH")
    @Expose
    private String maKH;
    @SerializedName("sdt")
    @Expose
    private String sdt;
    @SerializedName("diaChi")
    @Expose
    private String diaChi;
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("Hoa_don")
    @Expose
    private Hoa_don hd;


    public Khach_hang() {
    }
    

    public Khach_hang(String maKH, String sdt, String diaChi, String ten, Hoa_don hd) {
        this.maKH = maKH;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.ten = ten;
        this.hd = hd;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return "Khach_hang{" + "maKH=" + maKH + ", sdt=" + sdt + ", diaChi=" + diaChi + ", ten=" + ten + ", hd=" + hd + '}';
    }

    

    public Hoa_don getHd() {
        return hd;
    }

    public void setHd(Hoa_don hd) {
        this.hd = hd;
    }

}

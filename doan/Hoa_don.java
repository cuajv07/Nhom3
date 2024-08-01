/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Hoa_don {

    private String maHD;
    private Khach_hang KH;
    private double giaTien;

    public Hoa_don() {
    }

    public Hoa_don(String maHD, Khach_hang KH, double giaTien) {
        this.maHD = maHD;
        this.KH = KH;
        this.giaTien = giaTien;
    }

    @Override
    public String toString() {
        return "Hoa_don{" + "maHD=" + maHD + ", KH=" + KH + ", giaTien=" + giaTien + '}';
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

    public Khach_hang getKH() {
        return KH;
    }

    public void setKH(Khach_hang KH) {
        this.KH = KH;
    }

}

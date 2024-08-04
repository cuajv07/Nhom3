/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToolBar;

/**
 *
 * @author Admin
 */
public class QuanLiHoaDon {

    static final String curr__dir = System.getProperty("user.dir");
    static final String separator = File.separator;
    static final String JSON_PATH_READ_HD = curr__dir + separator + "Data_Phong" + separator + "Phong.json";
    static final String JSON_PATH_WRITE_HD = curr__dir + separator + "Data_HoaDon" + separator + "Hoadon.json";
    static final String CSV_PATH_WRITE_HD = curr__dir + separator + "Data_HoaDon" + separator + "HoaDon.csv";
    static ArrayList<Hoa_don> list_data = new ArrayList<>();

    static void set_list_HD() throws FileNotFoundException {
        JsonParser jsp = new JsonParser();
        FileReader fr = new FileReader(JSON_PATH_READ_HD);
        JsonArray Phong = (JsonArray) jsp.parse(fr);
        FileWriter fw = null;
        try {
            fw = new FileWriter(JSON_PATH_WRITE_HD);
            for (int i = 0; i < Phong.size(); i++) {
                JsonObject Phong_con = (JsonObject) Phong.get(i);
                JsonObject KH = (JsonObject) Phong_con.getAsJsonObject("Khach_hang");
                JsonObject HD = (JsonObject) KH.getAsJsonObject("Hoa_don");
                Hoa_don e = new Hoa_don();
                e.setMaHD(String.valueOf(HD.get("maHD")));
                e.setMaKH(String.valueOf(KH.get("maKH")));
                String date_tra = String.valueOf(Phong_con.get("ngayTraPhong")).replace("\"", "");
                String date_dat = String.valueOf(Phong_con.get("ngayDatPhong")).replace("\"", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                if (date_dat.compareTo("null") != 0 && date_tra.compareTo("null") != 0) {
                    LocalDate ld_tra = LocalDate.parse(date_tra, formatter);
                    LocalDate ld_dat = LocalDate.parse(date_dat, formatter);
                    e.setNgayThanhToan(ld_tra);
                    int soNgay = ld_tra.getDayOfMonth() - ld_dat.getDayOfMonth();
                    if (Integer.parseInt(String.valueOf(Phong_con.get("sucChua"))) == 2) {
                        e.setGiaTien(soNgay * 200000.0);
                    }
                    if (Integer.parseInt(String.valueOf(Phong_con.get("sucChua"))) == 3) {
                        e.setGiaTien(soNgay * 300000.0);
                    }
                    if (Integer.parseInt(String.valueOf(Phong_con.get("sucChua"))) == 4) {
                        e.setGiaTien(soNgay * 400000.0);
                    }

                } else {
                    e.setNgayThanhToan(null);
                    e.setGiaTien(0);
                }
                list_data.add(e);

            }
           
        } catch (IOException ex) {
            Logger.getLogger(QuanLiHoaDon.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        set_list_HD();
        System.out.println(list_data.size());
    }

}

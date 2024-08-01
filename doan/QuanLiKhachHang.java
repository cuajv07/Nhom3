/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class QuanLiKhachHang {

    static final String curr_dir = System.getProperty("user.dir");
    static final String separ = File.separator;
    static final String JSON_PATH_READ_KH = curr_dir + separ + "Data_Phong" + separ + "Phong.json";
    static final String CSV_WRITE_PHONG = curr_dir + separ + "Data_Phong" + separ + "write.csv";
    static final String JSON_PATH_WR_KH = curr_dir + separ + "Data_KH" + separ + "Khach_hang.json";
    static final String WRITE_CSV_KH = curr_dir + separ + "Data_KH" + separ + "KH.csv";
    static ArrayList<Khach_hang> list_KH = new ArrayList<>();
    static ArrayList<Phong> list_Phong = new ArrayList<>();
    static QuanLiPhong qlp = new QuanLiPhong();
    static Scanner sc = new Scanner(System.in);
    
   

    static void read_Json_KH(String path, List<Khach_hang> list) {
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Khach_hang>>() {
            }.getType();
            List<Khach_hang> list_dm = gson.fromJson(fr, type);
            list.clear();
            list.addAll(list_dm);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();

            } catch (IOException ex) {
                Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    static void write_Json_KH( String path_write,List<Khach_hang> list) throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(path_write);
            Gson gson = new Gson();
            gson.toJson(list, fw);
        } catch (IOException ex) {
            Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            fw.flush();
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    static void add_KH() throws IOException {
        System.out.println("Bạn muốn thêm khách hàng vào phòng nào: ");
        int maphong = sc.nextInt();
        sc.nextLine();
        qlp.read_Json(JSON_PATH_READ_KH, list_Phong);
        for(Phong e : list_Phong) {
            if(Integer.parseInt(e.getMaP()) == maphong) {
                
                e.getKH().setMaKH(sc.nextLine());
                e.getKH().setTen(sc.nextLine());
                e.getKH().setSdt(sc.nextLine());
                e.getKH().setDiaChi(sc.nextLine());
                
            }
        }
        qlp.write_json(JSON_PATH_READ_KH, list_Phong);
        qlp.write_csv(JSON_PATH_READ_KH, CSV_WRITE_PHONG);
        
       
        
        
        
    }
    
    

    public static void main(String[] args) throws IOException {
        System.out.println(curr_dir);
        read_Json_KH(JSON_PATH_READ_KH, list_KH);
        write_Json_KH(JSON_PATH_WR_KH, list_KH);
        qlp.read_Json(JSON_PATH_READ_KH, list_Phong);
        System.out.println(list_Phong.get(0).toString());
        add_KH();
        
        
    }
}

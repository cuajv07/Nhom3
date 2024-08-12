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
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
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
    static ArrayList<Hoa_don> list_Hoadon = new ArrayList<>();
    static QuanLiHoaDon qlhd = new QuanLiHoaDon();
    static QuanLiPhong qlp = new QuanLiPhong();
    static Scanner sc = new Scanner(System.in);

    static void set_list_KH(String path_Json_read_KH, List<Khach_hang> list) throws FileNotFoundException, IOException {
        JsonParser jsp = new JsonParser();
        FileReader fr = new FileReader(path_Json_read_KH);
        Object obj = jsp.parse(fr);
        JsonArray Phong = (JsonArray) obj;

        for (int i = 0; i < Phong.size(); i++) {
            JsonObject Phong_con = (JsonObject) Phong.get(i);
            JsonObject K_H = (JsonObject) Phong_con.getAsJsonObject("Khach_hang");
            Khach_hang e = new Khach_hang();
            e.setMaKH(String.valueOf(K_H.get("maKH")).replace("\"", ""));
            e.setTen(String.valueOf(K_H.get("ten")).replace("\"", ""));
            e.setSdt(String.valueOf(K_H.get("sdt")));
            e.setDiaChi(String.valueOf(K_H.get("diaChi")).replace("\"", ""));
            list.add(e);

        }
        fr.close();

    }

    static void read_Json_KH(String path, List<Khach_hang> list) {
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
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

    static void write_Json_KH_noParse(String path_write, List<Khach_hang> list) throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(path_write);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
            gson.toJson(list, fw);
        } catch (IOException ex) {
            Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.flush();
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static void write_csv_KH(String path_write_csv_KH, List<Khach_hang> list) throws FileNotFoundException, IOException {

        String header = "Ma khach hang,Ten,So dien thoai,Dia Chi";
        FileWriter fw = null;
        try {
            fw = new FileWriter(path_write_csv_KH);
            CSVWriter write = new CSVWriter(fw, CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            write.writeNext(header.split(","));
            for (Khach_hang e : list) {
                write.writeNext(new String[]{e.getMaKH(), e.getTen(), e.getSdt(), e.getDiaChi()});

            }
        } catch (IOException ex) {
            Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.flush();
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(QuanLiKhachHang.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    static void add_KH(String path_json_read_kh, List<Phong> list_Phong, List<Khach_hang> list_KH) throws IOException {
        qlp.read_Json(JSON_PATH_READ_KH, list_Phong);        
        int maphong = 0;
        do {
            try {
                System.out.println("Bạn muốn thêm khách hàng vào phòng nào: ");
                maphong = sc.nextInt();

                sc.nextLine();

                for (Phong e : list_Phong) {
                    if (Integer.parseInt(e.getMaP()) == maphong) {
                        System.out.println("Nhập mã khách hàng: ");
                        e.getKH().setMaKH(sc.nextLine());
                        System.out.println("Nhập tên khách hàng: ");
                        e.getKH().setTen(sc.nextLine());
                        System.out.println("Nhập số điện thoại: ");
                        String phoneNumber = "";
                        while (true) {
                            phoneNumber = sc.nextLine();
                            if (phoneNumber.matches("\\d+")) {
                                e.getKH().setSdt(phoneNumber);
                                break;
                            }
                            
                            System.out.println("Số điện thoại chỉ chứa chữ số ! Vui lòng nhập lại.");
                            
                            

                        }
                       
                        System.out.println("Nhập địa chỉ: ");
                        e.getKH().setDiaChi(sc.nextLine());

                    }
                }
                list_KH.clear();
                qlp.write_json(path_json_read_kh, list_Phong);
                qlp.write_csv(path_json_read_kh, CSV_WRITE_PHONG);
                set_list_KH(path_json_read_kh, list_KH);
                write_Json_KH_noParse(JSON_PATH_WR_KH, list_KH);
                write_csv_KH(WRITE_CSV_KH, list_KH);
                

            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                sc.nextLine();
            }
            if (maphong > 510 || maphong < 101) {
                System.out.println("Không tồn tại phòng này ! Vui lòng nhập lại.");

            }

        } while (maphong > 511 || maphong < 101);
    }

    static void delete_KH() throws IOException {
        qlp.read_Json(JSON_PATH_READ_KH, list_Phong);
        
        int maP = 0;
        System.out.println("Bạn muốn xoá khách hàng ở phòng nào: ");
        do {
            try {
                maP = sc.nextInt();
                sc.nextLine();

                for (Phong e : list_Phong) {
                    if (Integer.parseInt(e.getMaP()) == maP) {
                        Khach_hang kh = new Khach_hang();
                        Hoa_don hd = new Hoa_don();
                        e.setKH(kh);
                        e.getKH().setHd(hd);
                    }
                }
                list_KH.clear();
                qlhd.list_Hoadon.clear();
                qlp.write_json(JSON_PATH_READ_KH, list_Phong);
                qlhd.set_list_HD(JSON_PATH_READ_KH, qlhd.JSON_PATH_WRITE_HD, qlhd.list_Hoadon);
                qlhd.write_csv_HD(qlhd.CSV_PATH_WRITE_HD, qlhd.list_Hoadon);
                qlp.write_csv(JSON_PATH_READ_KH, CSV_WRITE_PHONG);
                set_list_KH(JSON_PATH_READ_KH, list_KH);

                write_csv_KH(WRITE_CSV_KH, list_KH);
            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
            }
            if (maP > 510 && maP < 101) {
                System.out.println("Không tồn tại phòng này ! Vui lòng nhập lại.");
            }

        } while (maP > 510 && maP < 101);

    }

    static void cus_inforKH() throws IOException {
        qlp.read_Json(JSON_PATH_READ_KH, list_Phong);
        String maKH = "";
        boolean validInt = false;
        while (validInt != true) {
            try {
                System.out.println("Nhập mã khách hàng muốn sửa: ");
                 maKH = sc.nextLine();

                for (int i = 0; i < list_Phong.size(); i++) {
                    if (list_Phong.get(i).getKH().getMaKH() != null) {
                        if (list_Phong.get(i).getKH().getMaKH().compareTo(maKH.trim()) == 0) {
                            list_KH.clear();
                            validInt = true;
                            int choice = 0;
                            do {
                                try {
                                    System.out.println("Sửa thông tin khách hàng: ");
                                    System.out.println("1. Sửa số điện thoại ");
                                    System.out.println("2. Sửa địa chỉ");
                                    System.out.println("3. Sửa tên");
                                    choice = sc.nextInt();
                                    sc.nextLine();
                                    switch (choice) {
                                        case 1:
                                            String phoneNumber = "";
                                            while (true) {
                                                System.out.println("Nhập số điện thoại: ");
                                                phoneNumber = sc.nextLine();
                                                if (phoneNumber.matches("\\d+")) {
                                                    list_Phong.get(i).getKH().setSdt(phoneNumber);
                                                    break;
                                                }
                                                System.out.println("Số điện thoại chỉ chứa chữ số ! Vui lòng nhập lại.");

                                            }
                                            break;
                                        case 2:
                                            System.out.println("Nhập địa chỉ : ");
                                            String addr = sc.nextLine();
                                            list_Phong.get(i).getKH().setDiaChi(addr);
                                            break;
                                        case 3:
                                            System.out.println("Nhập tên : ");
                                            String name = sc.nextLine();
                                            list_Phong.get(i).getKH().setTen(name);
                                            break;

                                    }
                                    qlp.write_json(JSON_PATH_READ_KH, list_Phong);
                                    qlp.write_csv(JSON_PATH_READ_KH, CSV_WRITE_PHONG);

                                    set_list_KH(JSON_PATH_READ_KH, list_KH);
                                    write_Json_KH_noParse(JSON_PATH_WR_KH, list_KH);
                                    write_csv_KH(WRITE_CSV_KH, list_KH);

                                } catch (InputMismatchException e1) {
                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                }
                                if (choice < 1 || choice > 3) {
                                    System.out.println("Không có chức năng này ! Vui lòng nhập lại.");
                                }
                            } while (choice < 1 || choice > 3);
                            break;

                        }
                    }
                }
                if(validInt == false) {
                    System.out.println("Không tìm thấy mã khách hàng! Vui lòng nhập lại");
                }
            } catch (InputMismatchException e) {
                System.out.println("Định dạng không lợp lệ ! Vui lòng nhập lại.");
            }
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println(curr_dir);
        qlp.read_Json(JSON_PATH_READ_KH, list_Phong);
        set_list_KH(JSON_PATH_READ_KH, list_KH);
        
        int choice = 0;
        do {
            while (choice != 4) {
                System.out.println("1. Thêm khách hàng");
                System.out.println("2. Xoá khách hàng");
                System.out.println("3. Sửa thông tin khách hàng");
                System.out.println("4. Thoát");
                try {
                    choice = sc.nextInt();
                    sc.nextLine();
                    switch (choice) {
                        case 1:
                            add_KH(JSON_PATH_READ_KH, list_Phong, list_KH);
                            break;
                        case 2:
                            delete_KH();
                            break;
                        case 3:
                            cus_inforKH();
                            break;
                        case 4:
                            System.exit(0);
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                    sc.nextLine();
                }
                if (choice < 1 || choice > 4) {
                    System.out.println("Không tồn tại chức năng này ! Vui lòng nhập lại.");
                }
            }
        } while (choice < 1 || choice > 4);

//        add_KH(JSON_PATH_READ_KH, list_Phong, list_KH);
//        
//        for(int i = 0;i<list_KH.size();i++) {
//            System.out.println(list_KH.get(i).toString());
//        }
    }
}

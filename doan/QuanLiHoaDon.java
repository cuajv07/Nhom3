/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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
    static final String CSV_Filter_HD = curr__dir + separator + "Data_HoaDon" + separator + "filterHD.csv";
    static ArrayList<Hoa_don> list_Hoadon = new ArrayList<>();
    static ArrayList<Phong> list_Phong = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static QuanLiPhong qlp = new QuanLiPhong();
    static QuanLiKhachHang qlkh = new QuanLiKhachHang();
    static ArrayList<Hoa_don> list_filter = new ArrayList<>();

    static void set_list_HD(String json_read, String json_write, List<Hoa_don> list) throws FileNotFoundException, IOException {
        JsonParser jsp = new JsonParser();
        FileReader fr = new FileReader(json_read);
        Object obj = jsp.parse(fr);
        JsonArray Phong = (JsonArray) obj;
        FileWriter fw = null;
        try {
            fw = new FileWriter(json_write);
            for (int i = 0; i < Phong.size(); i++) {
                JsonObject Phong_con = (JsonObject) Phong.get(i);
                JsonObject KH = (JsonObject) Phong_con.getAsJsonObject("Khach_hang");
                JsonObject HD = (JsonObject) KH.getAsJsonObject("Hoa_don");
                Hoa_don e = new Hoa_don();
                e.setMaHD(String.valueOf(HD.get("maHD")));
                e.setMaKH(String.valueOf(KH.get("maKH")).replace("\"", ""));
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
                list.add(e);

            }

            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
            gson.toJson(list, fw);

        } catch (IOException ex) {
            Logger.getLogger(QuanLiHoaDon.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            fr.close();
            try {
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(QuanLiHoaDon.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    static void write_csv_HD(String csv_write, List<Hoa_don> list) {
        FileWriter fw = null;
        String header = "Ma hoa don, Ma khach hang, Gia tien, Ngay thanh toan";
        try {
            fw = new FileWriter(csv_write);
            CSVWriter write = new CSVWriter(fw, CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            write.writeNext(header.split(","));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (Hoa_don e : list) {
                String date;
                if (e.getNgayThanhToan() != null) {
                    date = e.getNgayThanhToan().format(formatter);
                } else {
                    date = null;
                }
                write.writeNext(new String[]{e.getMaHD(),
                    e.getMaKH(),
                    String.valueOf(e.getGiaTien()),
                    date});
            }

        } catch (IOException ex) {
            Logger.getLogger(QuanLiHoaDon.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(QuanLiHoaDon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static void add_HD() throws IOException {
        
        
        qlp.read_Json(JSON_PATH_READ_HD, list_Phong);
        System.out.println("Nhập mã khách hàng muốn thêm hoá đơn: ");
        String maKH = sc.nextLine();
        for (Phong e : list_Phong) {
            if (e.getKH().getMaKH() != null) {
                if (e.getKH().getMaKH().compareTo(maKH) == 0) {
                    e.getKH().getHd().setMaKH(maKH);
                    String date = String.valueOf(e.getNgayTraPhong()).replace("-", "");
                    String maHD = maKH.concat(date).replace("\"", "");
                    e.getKH().getHd().setMaHD(maHD);
                    e.getKH().getHd().setNgayThanhToan(e.getNgayTraPhong());
                    if ((e.getNgayTraPhong() != null) && (e.getNgayDatPhong() != null)) {
                        int soNgay = e.getNgayTraPhong().getDayOfMonth() - e.getNgayDatPhong().getDayOfMonth();
                        e.getKH().getHd().setGiaTien(soNgay * e.getGiaPhong());
                    }
                    qlp.write_json(JSON_PATH_READ_HD, list_Phong);
                    list_Hoadon.clear();
                    set_list_HD(JSON_PATH_READ_HD, JSON_PATH_WRITE_HD, list_Hoadon);
                    write_csv_HD(CSV_PATH_WRITE_HD, list_Hoadon);
                    qlkh.list_KH.clear();
                    qlkh.set_list_KH(JSON_PATH_READ_HD, qlkh.list_KH);
                    qlkh.write_Json_KH_noParse(qlkh.JSON_PATH_WR_KH, qlkh.list_KH);
                    qlkh.write_csv_KH(qlkh.WRITE_CSV_KH, qlkh.list_KH);
                    return;

                }

            }
        }
        System.out.println("Mã khách hàng không tìm thấy. Vui lòng thêm khách hàng trước khi thêm hoá đơn.");

    }

    static void delete_HD() throws IOException {
        qlp.read_Json(JSON_PATH_READ_HD, list_Phong);
        System.out.println("Nhập mã khách hàng muốn xoá hoá đơn: ");
        String maKH = sc.nextLine();
        for (Phong e : list_Phong) {
            if (e.getKH().getMaKH() != null) {
                if (e.getKH().getMaKH().compareTo(maKH) == 0) {
                    Khach_hang kh = new Khach_hang();
                    Hoa_don hd = new Hoa_don();
                    Phong ph = new Phong();
                    hd.setGiaTien(0);
                    hd.setNgayThanhToan(null);
                    e.setNgayDatPhong(null);
                    e.setNgayTraPhong(null);
                    e.setTrangThai("trong");
                    e.setKH(kh);
                    e.getKH().setHd(hd);
                }
            }
        }
        qlp.write_json(JSON_PATH_READ_HD, list_Phong);
        qlp.write_csv(JSON_PATH_READ_HD, qlp.CSV_PATH_WRITE);
        qlkh.list_KH.clear();
        qlkh.set_list_KH(JSON_PATH_READ_HD, qlkh.list_KH);
        qlkh.write_Json_KH_noParse(qlkh.JSON_PATH_WR_KH, qlkh.list_KH);
        qlkh.write_csv_KH(qlkh.WRITE_CSV_KH, qlkh.list_KH);
        list_Hoadon.clear();
        set_list_HD(JSON_PATH_READ_HD, JSON_PATH_WRITE_HD, list_Hoadon);
        write_csv_HD(CSV_PATH_WRITE_HD, list_Hoadon);

    }

    static void filter_datePay() {
        list_filter.clear();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Nhập ngày thanh toán cần lọc: ");
        String day_month = sc.nextLine();
        String[] day_month_split = day_month.split(" ");
        for (Hoa_don e : list_Hoadon) {
            if (e.getNgayThanhToan() != null) {
                String[] date_df = df.format(e.getNgayThanhToan()).split("-");
                String date1 = "";
                for (int i = 0; i < day_month_split.length - 1; i++) {

                    if (Integer.parseInt(date_df[i + 1]) == Integer.parseInt(day_month_split[i + 1])) {
                        if (Integer.parseInt(date_df[i]) == Integer.parseInt(day_month_split[i])) {
                            list_filter.add(e);
                            break;

                        }
                    }

                }

            }

        }
        if (list_filter.size() == 0) {
            System.out.println("Không tìm thấy ngày tháng nhập vào ");
        }
        write_csv_HD(CSV_Filter_HD, list_filter);

    }

    static void tinh_doanh_thu() {

        int month = 0;
        boolean validMonth = false;
        do {
            while (validMonth != true) {
                try {
                    System.out.println("Nhập tháng tính doanh thu");
                    month = sc.nextInt();
                    validMonth = true;
                    double doanh_thu = 0;
                    for(Hoa_don e : list_Hoadon) {
                        if(e.getNgayThanhToan() != null) {
                            if(e.getNgayThanhToan().getMonthValue() == month) {
                                doanh_thu = doanh_thu + e.getGiaTien();
                            }
                        }
                    }
                    if(month>0 && month <13) {
                        System.out.printf("Doanh thu của khách sạn tháng %d là %.2f",month,doanh_thu);
                        System.out.println("");
                    }
                    
                    
                } catch (InputMismatchException e) {
                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại. ");
                    sc.nextLine();
                }
            }
            if(month < 1 || month > 12) {
                System.out.println("Không tồn tại tháng " + month);
                validMonth = false;
            }
            

        } while (month < 1 || month > 12);

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(curr__dir);
        set_list_HD(JSON_PATH_READ_HD, JSON_PATH_WRITE_HD, list_Hoadon);
        
        int choice = 0;
        do{
            while(choice != 5) {
                System.out.println("1. Thêm hoá đơn");
                System.out.println("2. Xoá hoá đơn");
                System.out.println("3. Lọc ngày tháng thanh toán");
                System.out.println("4. Tính doanh thu theo tháng");
                System.out.println("5. Thoát chương trình");
                try {
                    
                    choice = sc.nextInt();
                    sc.nextLine();
                    switch (choice) {
                        case 1:
                            add_HD();
                            break;
                        case 2:
                            delete_HD();
                            break;
                            
                        case 3:
                            filter_datePay();
                            break;
                        case 4:
                            tinh_doanh_thu();
                            break;
                        case 5:
                            System.exit(0);
                            break;
                    }
                }catch(InputMismatchException e) {
                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại. ");
                }
            }
            if(choice < 1 || choice > 4) {
                System.out.println("Chức năng không tồn tại ! Vui lòng nhập lại. ");
            }
            
        }while(choice < 1 || choice > 4);

  
        

    }

}

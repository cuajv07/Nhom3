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
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
public class QuanLiPhong {

    public QuanLiPhong() {
    }

    static final String curr_dir = System.getProperty("user.dir");
    static final String separator = File.separator;
    static final String JSON_PATH_READ = curr_dir + separator + "Data_Phong" + separator + "Phong.json";
    static final String JSON_PATH_WRITE = curr_dir + separator + "Data_Phong" + separator + "write.json";
    static final String CSV_PATH_WRITE = curr_dir + separator + "Data_Phong" + separator + "write.csv";
    static final String CSV_PATH_Filter = curr_dir + separator + "Data_Phong" + separator + "filter.csv";

    static ArrayList<Phong> listdata = new ArrayList<>();
    static ArrayList<Phong> list_filter = new ArrayList<>();
    static ArrayList<Phong> list_filter_deatail = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
// đọc file json gốc lưu vào 1 ArrayList

    public static void read_Json(String pathRead, List<Phong> list1) {
        FileReader fr = null;
        try {
            fr = new FileReader(pathRead);

            //gson không có những tích hợp tương ứng cho Java.time nên cần phải tạo 1 class LocalDateTypeAdapter để gson có thể đọc được định dạng ngày tháng từ 1 đối tượng
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
            Type type = new TypeToken<Collection<Phong>>() {
            }.getType();
            List<Phong> list2 = gson.fromJson(fr, type);
            list1.clear();
            list1.addAll(list2);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
// cập nhật và lưu file json gốc

    static void write_json(String pathFile, List<Phong> list) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(pathFile);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
            gson.toJson(list, fw);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
// ghi file json sang file csv

    static void write_csv(String pathRead, String PathWrite) throws FileNotFoundException, IOException {
        //tạo ra parse để dùng chuyển đổi từ file json sang 1 đối tượng
        JsonParser jsp = new JsonParser();
        FileReader fr = new FileReader(pathRead);
        // tạo 1 đối tượng từ cả 1 file json
        Object obj = jsp.parse(fr);
        // định dạng lại đối tượng đó theo kiểu Json. Nếu là collection thì Json Array
        // nếu chỉ là 1 đối tượng thì chỉ là Json Object
        JsonArray Phong = (JsonArray) obj;

        FileWriter fw = null;
        String header = "Ma Phong,Ma Khach Hang,Tang,Gia Phong,Ngay Dat Phong,Ngay Tra Phong,Trang Thai,Suc Chua";
        try {
            fw = new FileWriter(PathWrite);
            CSVWriter write = new CSVWriter(fw, CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            //tạo ra các cột có tên như ở header bằng cách bỏ dấu phẩy
            write.writeNext(header.split(","));
            for (int i = 0; i < Phong.size(); i++) {
                //đưa các phần tử trong JsonArray về các đối tượng để lấy thông tin
                JsonObject idv_Phong = (JsonObject) Phong.get(i);
                // trong các đối tượng của JsonArray có các đối tượng nhỏ hơn thì lại phải ép kiểu
                JsonObject Khach_hang = (JsonObject) idv_Phong.getAsJsonObject("Khach_hang");

                write.writeNext(new String[]{String.valueOf(idv_Phong.get("maP")).replace("\"", ""),
                    String.valueOf(Khach_hang.get("maKH")).replace("\"", ""),
                    String.valueOf(idv_Phong.get("tang")).replace("\"", ""),
                    String.valueOf(idv_Phong.get("giaPhong")).replace("\"", ""),
                    String.valueOf(idv_Phong.get("ngayDatPhong")).replace("\"", ""),
                    String.valueOf(idv_Phong.get("ngayTraPhong")).replace("\"", ""),
                    String.valueOf(idv_Phong.get("trangThai")).replace("\"", ""),
                    String.valueOf(idv_Phong.get("sucChua")).replace("\"", "")});
            }
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.close();
            fr.close();
        }

    }

    static void cus_trangThai(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {
        int n = 0;
        do {
            System.out.println("Sửa đổi trạng thái: ");
            System.out.println("1. Trống");
            System.out.println("2. Đã đặt trước");
            System.out.println("3. Đang sử dụng");

            try {
                n = sc.nextInt();
                switch (n) {
                    case 1:
                        list.get(i).setTrangThai("trong");
                        break;
                    case 2:
                        list.get(i).setTrangThai("da dat truoc");
                        break;
                    case 3:
                        list.get(i).setTrangThai("dang su dung");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại");
                sc.nextLine();
            }
            if (n > 3 || n < 1) {
                System.out.println("Không tồn tại chức năng này ! Vui lòng nhập lại.");
            }

        } while (n > 3 || n < 1);
        write_json(path_save_json, list);
        write_csv(path_save_json, path_write_csv);
    }

    static void delete_ngayTraPhong(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {
        list.get(i).setNgayTraPhong(null);
        write_json(path_save_json, list);
        write_csv(path_save_json, path_write_csv);
    }

    static void delete_ngayDatPhong(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {
        list.get(i).setNgayDatPhong(null);
        write_json(path_save_json, list);
        write_csv(path_save_json, path_write_csv);
    }

    static void update_ngayTraPhong(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {
        boolean validInput = false;
        int date = 0;
        int month = 0;
        int year = 0;
        while (validInput != true) {
            System.out.println("Nhập ngày trả phòng: ");

            try {
                date = sc.nextInt();
                month = sc.nextInt();
                year = sc.nextInt();
                LocalDate upd = LocalDate.of(year, month, date);
                validInput = true;
                list.get(i).setNgayTraPhong(upd);
                write_json(path_save_json, list);
                write_csv(path_save_json, path_write_csv);
            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                sc.nextLine();
            } catch (DateTimeException e) {
                System.out.println("Ngày, tháng, năm nhập vào không hợp lệ ! Vui lòng nhập lại.");
                sc.nextLine();
            }

        }

    }

    static void update_ngayDatPhong(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {

        boolean validInput = false;
        int date = 0;
        int month = 0;
        int year = 0;
        while (validInput != true) {
            System.out.println("Nhập ngày đặt phòng: ");

            try {
                date = sc.nextInt();
                month = sc.nextInt();
                year = sc.nextInt();
                LocalDate upd = LocalDate.of(year, month, date);
                validInput = true;
                list.get(i).setNgayDatPhong(upd);
                write_json(path_save_json, list);
                write_csv(path_save_json, path_write_csv);
            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại");
                sc.nextLine();
            } catch (DateTimeException e) {
                System.out.println("Ngày, tháng, năm nhập vào không hợp lệ ! Vui lòng nhập lại.");
                sc.nextLine();
            }

        }

    }

    static void filter_trangThai(List<Phong> list_write, List<Phong> list_read) throws IOException {
        int caseeee = 0;
        do {
            System.out.println("1. Lọc trạng thái trống");
            System.out.println("2. Lọc trạng thái đã đặt trước");
            System.out.println("3. Lọc trạng thái đang sử dụng");

            try {
                caseeee = sc.nextInt();

                switch (caseeee) {
                    case 1:

                        for (Phong e : list_read) {
                            if (e.getTrangThai().compareTo("trong") == 0) {
                                list_write.add(e);

                            }
                        }
                        // đọc từ list_write sang file json write
                        write_json(JSON_PATH_WRITE, list_write);
                        // đọc file json write sang file csv 
                        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
                        list_write.clear();
                        break;
                    case 2:

                        for (Phong e : list_read) {
                            if (e.getTrangThai().compareTo("da dat truoc") == 0) {
                                list_write.add(e);

                            }
                        }
                        write_json(JSON_PATH_WRITE, list_write);
                        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
                        list_write.clear();
                        break;
                    case 3:

                        for (Phong e : list_read) {
                            if (e.getTrangThai().compareTo("dang su dung") == 0) {
                                list_write.add(e);

                            }
                        }
                        write_json(JSON_PATH_WRITE, list_write);
                        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
                        list_write.clear();
                        break;

                }

            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                sc.nextLine();
            }
            if (caseeee < 1 || caseeee > 3) {
                System.out.println("Không tồn tại chức năng này ! Vui lòng nhập lại.");
            }

        } while (caseeee < 1 || caseeee > 3);
    }

    static void filter_sucChua(List<Phong> list_write, List<Phong> list_read, List<Phong> filter_dt) throws IOException {
        int caseeee = 0;
        do {
            System.out.println("1. Lọc phòng có sức chứa 2 người");
            System.out.println("2. Lọc phòng có sức chứa 3 người");
            System.out.println("3. Lọc phòng có sức chứa 4 người");

            int choice = 0;
            try {
                caseeee = sc.nextInt();

                switch (caseeee) {
                    case 1:

                        for (Phong e : list_read) {
                            if (e.getSucChua() == 2) {
                                list_write.add(e);
                            }
                        }
                        do {
                            System.out.println("Bạn có muốn lọc chi tiết không ?");
                            System.out.println("1. Có");
                            System.out.println("2. Không");
                            try {
                                choice = sc.nextInt();
                                switch (choice) {
                                    case 1:
                                        filter_trangThai(filter_dt, list_write);
                                        list_write.clear();
                                        break;
                                    case 2:
                                        write_json(JSON_PATH_WRITE, list_write);
                                        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
                                        list_write.clear();
                                        break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                sc.nextLine();
                            }
                            if (choice < 1 || choice > 2) {
                                System.out.println("Không có chức năng này ! Vui lòng nhập lại.");
                            }
                        } while (choice < 1 || choice > 2);
                        break;

                    case 2:
                        for (Phong e : list_read) {
                            if (e.getSucChua() == 3) {
                                list_write.add(e);
                            }
                        }

                        do {
                            System.out.println("Bạn có muốn lọc chi tiết không ?");
                            System.out.println("1. Có");
                            System.out.println("2. Không");
                            try {
                                choice = sc.nextInt();
                                switch (choice) {
                                    case 1:
                                        filter_trangThai(filter_dt, list_write);
                                        list_write.clear();
                                        break;
                                    case 2:
                                        write_json(JSON_PATH_WRITE, list_write);
                                        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
                                        list_write.clear();
                                        break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                sc.nextLine();
                            }
                            if (choice < 1 || choice > 2) {
                                System.out.println("Không có chức năng này ! Vui lòng nhập lại.");
                            }
                        } while (choice < 1 || choice > 2);
                        break;
                    case 3:
                        for (Phong e : list_read) {
                            if (e.getSucChua() == 4) {
                                list_write.add(e);
                            }
                        }

                        do {
                            System.out.println("Bạn có muốn lọc chi tiết không ?");
                            System.out.println("1. Có");
                            System.out.println("2. Không");
                            try {
                                choice = sc.nextInt();
                                switch (choice) {
                                    case 1:
                                        filter_trangThai(filter_dt, list_write);
                                        list_write.clear();
                                        break;
                                    case 2:
                                        write_json(JSON_PATH_WRITE, list_write);
                                        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
                                        list_write.clear();
                                        break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                sc.nextLine();
                            }
                            if (choice < 1 || choice > 2) {
                                System.out.println("Không có chức năng này ! Vui lòng nhập lại.");
                            }
                        } while (choice < 1 || choice > 2);
                        break;

                }

            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                sc.nextLine();
            }
            if (caseeee > 3 || caseeee < 1) {
                System.out.println("Không có chức năng này ! Vui lòng nhập lại.");
            }
        } while (caseeee > 3 || caseeee < 1);
    }

    static void filter_locNgayDat(List<Phong> list_write, List<Phong> list_read) throws IOException {
        list_write.clear();
        int day = 0;
        boolean validInput = false;
        while (validInput != true) {
            try {
                System.out.println("Nhập ngày đặt phòng: ");
                day = sc.nextInt();
                validInput = true;

                for (Phong e : list_read) {
                    if (e.getNgayDatPhong() != null) {
                        if (e.getNgayDatPhong().getDayOfMonth() == day) {
                            list_write.add(e);
                        }
                    }

                }
                write_json(JSON_PATH_WRITE, list_write);
                write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);

            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
            }

        }
    }

    static void filter_locNgayTra(List<Phong> list_write, List<Phong> list_read) throws IOException {
        list_write.clear();
        int day = 0;
        boolean validInput = false;
        while (validInput != true) {

            try {
                System.out.println("Nhập ngày trả phòng: ");
                day = sc.nextInt();
                validInput = true;

                for (Phong e : list_read) {
                    if (e.getNgayTraPhong() != null) {
                        if (e.getNgayTraPhong().getDayOfMonth() == day) {
                            list_write.add(e);
                        }
                    }

                }
                write_json(JSON_PATH_WRITE, list_write);
                write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);

            } catch (InputMismatchException e) {
                System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
            }

        }
    }

    public static void thuc_hien() throws IOException, InputMismatchException {
        int case_quan_li = 0;

        int maPhong = 0;
        boolean validInput = false;

        read_Json(JSON_PATH_READ, listdata);
        do {
            while (case_quan_li != 5) {
                System.out.println("1. Sửa đổi trạng thái");
                System.out.println("2. Cập nhật ngày đặt phòng");
                System.out.println("3. Cập nhật ngày trả phòng");
                System.out.println("4. Lọc");
                System.out.println("5. Thoát chương trình");

                try {
                    case_quan_li = sc.nextInt();
                    sc.nextLine();
                    switch (case_quan_li) {
                        case 1:

                            while (validInput != true) {

                                System.out.println("Nhập mã phòng cần sửa: ");
                                try {
                                    maPhong = sc.nextInt();
                                    if (maPhong > 100 && maPhong <= 510) {
                                        validInput = true;
                                        for (int i = 0; i < listdata.size(); i++) {
                                            if (Integer.parseInt(listdata.get(i).getMaP()) == maPhong) {
                                                cus_trangThai(listdata, i, JSON_PATH_READ, CSV_PATH_WRITE);
                                                break;
                                            }
                                        }
                                    }
                                    if (maPhong < 101 || maPhong > 510) {
                                        System.out.println("Không tồn tại phòng " + maPhong);
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                    sc.nextLine();
                                }

                            }
                            break;
                        case 2:
                            int case1 = 0;
                            boolean validInput_Ud = false;

                            do {

                                try {
                                    System.out.println("1. Xoá ngày đặt phòng");
                                    System.out.println("2. Cập nhật");
                                    case1 = sc.nextInt();
                                    sc.nextLine();
                                    switch (case1) {
                                        case 1:
                                            while (validInput_Ud != true) {

                                                
                                                try {
                                                    System.out.println("Nhập mã phòng cần xoá: ");
                                                    maPhong = sc.nextInt();
                                                    if (maPhong > 100 && maPhong <= 510) {
                                                        validInput_Ud = true;
                                                        for (int i = 0; i < listdata.size(); i++) {
                                                            if (Integer.parseInt(listdata.get(i).getMaP()) == maPhong) {
                                                                delete_ngayDatPhong(listdata, i, JSON_PATH_READ, CSV_PATH_WRITE);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (maPhong < 101 || maPhong > 510) {
                                                        System.out.println("Không tồn tại phòng " + maPhong);
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                                    sc.nextLine();
                                                }

                                            }
                                            break;

                                        case 2:
                                            while (validInput_Ud != true) {

                                                
                                                try {
                                                    System.out.println("Nhập mã phòng cần update: ");
                                                    maPhong = sc.nextInt();
                                                    if (maPhong > 100 && maPhong <= 510) {
                                                        validInput_Ud = true;
                                                        for (int i = 0; i < listdata.size(); i++) {
                                                            if (Integer.parseInt(listdata.get(i).getMaP()) == maPhong) {
                                                                update_ngayDatPhong(listdata, i, JSON_PATH_READ, CSV_PATH_WRITE);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (maPhong < 101 || maPhong > 510) {
                                                        System.out.println("Không tồn tại phòng " + maPhong);
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                                    sc.nextLine();
                                                }

                                            }
                                            break;

                                    }

                                } catch (InputMismatchException e) {
                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                    sc.nextLine();
                                }
                                if (case1 < 1 || case1 > 2) {
                                    System.out.println("Không tồn tại chức năng này ! Vui lòng nhập lại.");
                                }

                            } while (case1 < 1 || case1 > 2);

                            break;

                        case 3:
                            int case2 = 0;
                            boolean validInput_Ud1 = false;

                            do {

                                try {
                                    System.out.println("1. Xoá ngày trả phòng");
                                    System.out.println("2. Cập nhật");
                                    case2 = sc.nextInt();
                                    switch (case2) {
                                        case 1:
                                            while (validInput_Ud1 != true) {

                                                
                                                try {
                                                    System.out.println("Nhập mã phòng cần xoá: ");
                                                    maPhong = sc.nextInt();
                                                    if (maPhong > 100 && maPhong <= 510) {
                                                        validInput_Ud1 = true;
                                                        for (int i = 0; i < listdata.size(); i++) {
                                                            if (Integer.parseInt(listdata.get(i).getMaP()) == maPhong) {
                                                                delete_ngayTraPhong(listdata, i, JSON_PATH_READ, CSV_PATH_WRITE);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (maPhong < 101 || maPhong > 510) {
                                                        System.out.println("Không tồn tại phòng " + maPhong);
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                                    sc.nextLine();
                                                }

                                            }
                                            break;

                                        case 2:
                                            while (validInput_Ud1 != true) {

                                                System.out.println("Nhập mã phòng cần update: ");
                                                try {
                                                    maPhong = sc.nextInt();
                                                    if (maPhong > 100 && maPhong <= 510) {
                                                        validInput_Ud1 = true;
                                                        for (int i = 0; i < listdata.size(); i++) {
                                                            if (Integer.parseInt(listdata.get(i).getMaP()) == maPhong) {
                                                                update_ngayTraPhong(listdata, i, JSON_PATH_READ, CSV_PATH_WRITE);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (maPhong < 101 || maPhong > 510) {
                                                        System.out.println("Không tồn tại phòng " + maPhong);
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                                    sc.nextLine();
                                                }

                                            }
                                            break;

                                    }

                                } catch (InputMismatchException e) {
                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại.");
                                    sc.nextLine();
                                }
                                if (case2 < 1 || case2 > 2) {
                                    System.out.println("Không tồn tại chức năng này ! Vui lòng nhập lại.");
                                   
                                }

                            } while (case2 < 1 || case2 > 2);

                            break;

                        case 4:
                            int caseee = 0;
                            do {
                                System.out.println("1. Lọc trạng thái");
                                System.out.println("2. Lọc sức chứa");
                                System.out.println("3. Lọc ngày đặt");
                                System.out.println("4. Lọc ngày trả");
                                try {
                                    caseee = sc.nextInt();
                                    switch (caseee) {
                                        case 1:
                                            filter_trangThai(list_filter, listdata);
                                            break;
                                        case 2:
                                            filter_sucChua(list_filter, listdata, list_filter_deatail);
                                            break;
                                        case 3:
                                            filter_locNgayDat(list_filter, listdata);
                                            break;
                                        case 4:
                                            filter_locNgayTra(list_filter, listdata);
                                            break;
                                    }

                                } catch (InputMismatchException e) {
                                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại. ");
                                    sc.nextLine();
                                }

                            } while (caseee < 1 || caseee > 4);
                            break;
                        case 5:
                            System.exit(0);
                            break;

                    }

                } catch (InputMismatchException e) {
                    System.out.println("Định dạng không hợp lệ ! Vui lòng nhập lại. ");
                    sc.nextLine();
                }
                if (case_quan_li < 1 || case_quan_li > 5) {
                    System.out.println("Không tồn tại chức năng này ! Vui lòng nhập lại.");
                    sc.nextLine();
                }

            }
        } while (case_quan_li < 1 || case_quan_li > 5);
    }

    public static void main(String[] args) throws IOException {
        thuc_hien();
    }

}

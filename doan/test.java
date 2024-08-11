package com.mycompany.doan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class test {

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

    static void read_Json(String pathRead, List<Phong> list1) {
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
        JsonParser jsp = new JsonParser();
        FileReader fr = new FileReader(pathRead);
        Object obj = jsp.parse(fr);
        JsonArray Phong = (JsonArray) obj;

        FileWriter fw = null;
        String header = "Ma Phong,Ma Khach Hang,Tang,Gia Phong,Ngay Dat Phong,Ngay Tra Phong,Trang Thai,Suc Chua";
        try {
            fw = new FileWriter(PathWrite);
            CSVWriter write = new CSVWriter(fw, CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            write.writeNext(header.split(","));
            for (int i = 0; i < Phong.size(); i++) {
                //đưa các phần tử trong JsonArray về các đối tượng để lấy thông tin
                JsonObject idv_Phong = (JsonObject) Phong.get(i);
                // trong các đối tượng của JsonArray có các đối tượng nhỏ hơn thì lại phải ép kiểu
                JsonObject Khach_hang = (JsonObject) idv_Phong.getAsJsonObject("Khach_hang");
                JsonObject Hoa_don = (JsonObject) idv_Phong.getAsJsonObject("Hoa_don");

                write.writeNext(new String[]{String.valueOf(idv_Phong.get("maP")),
                    String.valueOf(Khach_hang.get("maKH")),
                    String.valueOf(idv_Phong.get("tang")),
                    String.valueOf(idv_Phong.get("giaPhong")),
                    String.valueOf(idv_Phong.get("ngayDatPhong")),
                    String.valueOf(idv_Phong.get("ngayTraPhong")),
                    String.valueOf(idv_Phong.get("trangThai")),
                    String.valueOf(idv_Phong.get("sucChua"))});
            }
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.close();
            fr.close();
        }

    }

    static void cus_trangThai(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {

        System.out.println("Sửa đổi trạng thái: ");
        System.out.println("1. Trống");
        System.out.println("2. Đã đặt trước");
        System.out.println("3. Đang sử dụng");
        System.out.println();

        int n = sc.nextInt();

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
        write_json(path_save_json, list);
        write_csv(path_save_json, path_write_csv);
    }

    static void update_ngayTraPhong(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {

        System.out.println("Nhập ngày trả phòng: ");
        LocalDate upd = LocalDate.of(sc.nextInt(), sc.nextInt(), sc.nextInt());
        list.get(i).setNgayTraPhong(upd);
        write_json(path_save_json, list);
        write_csv(path_save_json, path_write_csv);
    }

    static void update_ngayDatPhong(List<Phong> list, int i, String path_save_json, String path_write_csv) throws IOException {

        System.out.println("Nhập ngày đặt phòng: ");
        LocalDate upd = LocalDate.of(sc.nextInt(), sc.nextInt(), sc.nextInt());
        list.get(i).setNgayDatPhong(upd);
        write_json(path_save_json, list);
        write_csv(path_save_json, path_write_csv);
    }

    static void filter_trangThai(List<Phong> list_write, List<Phong> list_read) throws IOException {
        System.out.println("1. Lọc trạng thái trống");
        System.out.println("2. Lọc trạng thái đã đặt trước");
        System.out.println("3. Lọc trạng thái đang sử dụng");

        int caseeee = sc.nextInt();

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

    }

    static void filter_sucChua(List<Phong> list_write, List<Phong> list_read, List<Phong> filter_dt) throws IOException {
        System.out.println("1. Lọc phòng có sức chứa 2 người");
        System.out.println("2. Lọc phòng có sức chứa 3 người");
        System.out.println("3. Lọc phòng có sức chứa 4 người");
        int caseeee = sc.nextInt();
        int choice;

        switch (caseeee) {
            case 1:

                for (Phong e : list_read) {
                    if (e.getSucChua() == 2) {
                        list_write.add(e);
                    }
                }

                System.out.println("Bạn có muốn lọc chi tiết không ?");
                System.out.println("1. Có");
                System.out.println("2. Không");
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
                break;

            case 2:
                for (Phong e : list_read) {
                    if (e.getSucChua() == 3) {
                        list_write.add(e);
                    }
                }

                System.out.println("Bạn có muốn lọc chi tiết không ?");
                System.out.println("1. Có");
                System.out.println("2. Không");
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
                break;
            case 3:
                for (Phong e : list_read) {
                    if (e.getSucChua() == 4) {
                        list_write.add(e);
                    }
                }

                System.out.println("Bạn có muốn lọc chi tiết không ?");
                System.out.println("1. Có");
                System.out.println("2. Không");
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
                break;

        }

    }

    static void filter_locNgayDat(List<Phong> list_write, List<Phong> list_read) throws IOException {
        System.out.println("Nhập ngày đặt phòng: ");
        int day = sc.nextInt();
        for (Phong e : list_read) {
            if(e.getNgayDatPhong() != null) {
                if(e.getNgayDatPhong().getDayOfMonth() == day) {
                    list_write.add(e);
                }
            }
            
        }
        write_json(JSON_PATH_WRITE, list_write);
        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
    }
    static void filter_locNgayTra(List<Phong> list_write,List<Phong> list_read) throws IOException {
        System.out.println("Nhập ngày trả phòng: ");
        int day = sc.nextInt();
        for(Phong e : list_read) {
            if(e.getNgayTraPhong() != null) {
                if(e.getNgayTraPhong().getDayOfMonth() == day) {
                    list_write.add(e);
                }
            }
        }
        write_json(JSON_PATH_WRITE, list_write);
        write_csv(JSON_PATH_WRITE, CSV_PATH_Filter);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(curr_dir);

        read_Json(JSON_PATH_READ, listdata);
//        System.out.println(listdata.size());
//        LocalDate date = LocalDate.of(2024, 8, 20);
//        for (Phong e : listdata) {
//            if (Integer.parseInt(e.getMaP()) % 2 == 0) {
//                e.setNgayDatPhong(date);
//            }
//        }
//        write_json(JSON_PATH_WRITE, listdata);
//        write_csv(JSON_PATH_WRITE, CSV_PATH_WRITE);
        
        

//        String date = String.valueOf(listdata.get(0).getNgayDatPhong());
//           listdata.get(0).setTrangThai(null);
//           System.out.println(listdata.get(0));
//           System.out.println(listdata.size());
//           
//           LocalDate date = LocalDate.of(2024, 6, 6);
//           listdata.get(1).setNgayDatPhong(null);
//           System.out.println(listdata.get(1));
    }

    // in the old days
}

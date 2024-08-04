/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class test2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  
        String phoneNumber = "";  

        while (true) {  
            System.out.print("Nhập số điện thoại: ");  
            phoneNumber = scanner.nextLine();  

            // Kiểm tra xem chuỗi chỉ chứa chữ số  
            if (phoneNumber.matches("\\d+")) {  
                System.out.println("Số điện thoại bạn đã nhập: " + phoneNumber);  
                break; // Thoát khỏi vòng lặp nếu nhập hợp lệ  
            } else {  
                System.out.println("Vui lòng nhập số điện thoại hợp lệ (chỉ chứa chữ số).");  
            }  
        }  

        scanner.close(); 
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lsp;

// Mengimpor semua kelas yang ada dalam paket java.sql, 
// yang menyediakan API untuk berinteraksi dengan database melalui JDBC (Java Database Connectivity).
import java.sql.*;

// Mengimpor semua kelas yang ada dalam paket javax.swing, 
// yang menyediakan berbagai komponen untuk membangun antarmuka pengguna grafis (GUI) dalam Java.
import javax.swing.*;

/**
 *
 * @author ICT
 */
public class koneksi {
    // Mendeklarasikan variabel Connection yang digunakan untuk menyimpan koneksi ke database
    Connection conn;

    // Metode statis untuk membuka koneksi ke database
    public static Connection BukaKoneksi() {
        try {
            // Memuat driver JDBC untuk MySQL. 
            // Driver ini memungkinkan aplikasi Java berkomunikasi dengan database MySQL.
            Class.forName("com.mysql.jdbc.Driver");

            // Membuka koneksi ke database MySQL dengan URL, username, dan password yang diberikan.
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db_lsp", "root", "");

            // Mengembalikan objek koneksi jika berhasil tersambung ke database
            return conn;
        } catch (Exception e) {
            // Menangani kesalahan jika terjadi kegagalan dalam memuat driver atau membuka koneksi
            JOptionPane.showMessageDialog(null, e);
            
            // Mengembalikan nilai null jika koneksi gagal dibuka
            return null;
        }
    } 
}
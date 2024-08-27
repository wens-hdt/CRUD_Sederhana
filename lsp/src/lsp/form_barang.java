/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package lsp;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ICT
 */
public class form_barang extends javax.swing.JFrame {
    
    Connection conn = koneksi.BukaKoneksi();
    public Statement st;
    public ResultSet rs;

    /**
     * Creates new form form_barang
     */
    public form_barang() {
        initComponents();
        autonumber();
        tampil();
        setLocationRelativeTo(this);
    }
    
    public void autonumber() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // SQL untuk mendapatkan nilai maksimum dari kolom id_barang di tabel tbl_barang.
            String sql = "SELECT max(id_barang) FROM tbl_barang";
            pst = conn.prepareStatement(sql);

            // Eksekusi query dan simpan hasilnya dalam ResultSet.
            rs = pst.executeQuery();

            if (rs.next()) { // Jika hasil query ditemukan
                int a = rs.getInt(1); // Mendapatkan nilai maksimum id_barang
                if (a == 0) { // Jika tidak ada data dalam tabel
                    txt_idBarang.setText("0001");
                } else {
                    // Jika ada data, tambahkan 1 ke nilai maksimum dan format menjadi 4 digit.
                    txt_idBarang.setText(String.format("%04d", a + 1));
                }
            }
        } catch (SQLException e) {
            // Menampilkan pesan kesalahan jika terjadi SQLException
            JOptionPane.showMessageDialog(null, "Gagal menggenerate ID Barang: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menutup resource: " + e.getMessage());
            }
        }
    }
    
    private void bersih(){
        txt_namaBarang.setText(null);
        cmb_kategori.setSelectedIndex(0);
        txt_qty.setText(null);
        txt_namaBarang.requestFocus();
        autonumber();
    }
    
    private void tampil() {
        // Membuat model tabel dengan kolom yang sesuai
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("ID Barang");
        tbl.addColumn("Nama Barang");
        tbl.addColumn("Kategori Barang");
        tbl.addColumn("Jumlah Barang");

        // Mengaitkan model dengan JTable
        tbl_barang.setModel(tbl);

        ResultSet rs = null;
        Statement st = null;

        try {
            // Membuat statement untuk mengeksekusi query
            st = conn.createStatement();

            // Query untuk mengambil semua data dari tabel tbl_barang dan mengurutkannya berdasarkan id_barang
            String query = "SELECT * FROM tbl_barang ORDER BY id_barang";
            rs = st.executeQuery(query);

            // Menambahkan setiap baris data yang diambil ke dalam model tabel
            while (rs.next()) {
                tbl.addRow(new Object[]{
                    rs.getString("id_barang"), // Mengambil ID Barang
                    rs.getString("nama_barang"), // Mengambil Nama Barang
                    rs.getString("kategori_barang"), // Mengambil Kategori Barang
                    rs.getString("jumlah_barang") // Mengambil Jumlah Barang
                });
            }

            // Mengaitkan model yang sudah diisi dengan JTable
            tbl_barang.setModel(tbl);

        } catch (SQLException e) {
            // Menangani kesalahan SQL dan menampilkan pesan kesalahan
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
        } finally {
            // Menutup ResultSet dan Statement untuk mencegah kebocoran resource
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menutup resource: " + e.getMessage());
            }
        }
    }
    
    private void simpan() {
        // Validasi input
        if (txt_idBarang.getText().isEmpty() ||
            txt_namaBarang.getText().isEmpty() ||
            cmb_kategori.getSelectedIndex() == 0 ||
            txt_qty.getText().isEmpty()) {

            JOptionPane.showMessageDialog(rootPane, "Isi Data dengan Lengkap!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
            PreparedStatement pst = null;

            try {
                // Menyiapkan query dengan menggunakan PreparedStatement
                String query = "INSERT INTO tbl_barang (id_barang, nama_barang, kategori_barang, jumlah_barang) VALUES (?, ?, ?, ?)";
                pst = conn.prepareStatement(query);

                // Mengisi nilai ke dalam query
                pst.setString(1, txt_idBarang.getText());
                pst.setString(2, txt_namaBarang.getText());
                pst.setInt(3, cmb_kategori.getSelectedIndex());
                pst.setInt(4, Integer.parseInt(txt_qty.getText()));

                // Eksekusi query untuk menyimpan data
                int rowsAffected = pst.executeUpdate();

                // Memberikan konfirmasi kepada pengguna jika data berhasil disimpan
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Data Barang " + txt_namaBarang.getText() + " Berhasil disimpan.");
                }

            } catch (SQLException e) {
                // Menampilkan pesan kesalahan SQL
                JOptionPane.showMessageDialog(null, "Keterangan Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Menutup PreparedStatement
                try {
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Gagal menutup resource: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Memanggil metode tambahan setelah penyimpanan data
            autonumber(); // Mengenerate nomor ID Barang berikutnya
            tampil(); // Menampilkan data terbaru di tabel
            bersih(); // Membersihkan input form
        }
    }
    
    private void ubah() {
        PreparedStatement pst = null;
        try {
            // Menyiapkan query SQL dengan menggunakan placeholder untuk nilai yang akan diubah
            String query = "UPDATE tbl_barang SET "
                         + "nama_barang = ?, "
                         + "kategori_barang = ?, "
                         + "jumlah_barang = ? "
                         + "WHERE id_barang = ?";

            // Membuat PreparedStatement untuk query di atas
            pst = conn.prepareStatement(query);

            // Mengisi nilai ke dalam placeholder yang ada pada query
            pst.setString(1, txt_namaBarang.getText());
            pst.setInt(2, cmb_kategori.getSelectedIndex());
            pst.setInt(3, Integer.parseInt(txt_qty.getText()));
            pst.setString(4, txt_idBarang.getText());

            // Menjalankan eksekusi query untuk mengubah data
            int rowsAffected = pst.executeUpdate();

            // Memberikan konfirmasi kepada pengguna jika data berhasil diubah
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data Barang " + txt_namaBarang.getText() + " Berhasil diubah.");
            } else {
                JOptionPane.showMessageDialog(null, "Data Barang tidak ditemukan atau tidak ada perubahan.");
            }

        } catch (SQLException e) {
            // Menangani kesalahan SQL dan menampilkan pesan kesalahan
            JOptionPane.showMessageDialog(null, "Keterangan Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Menutup PreparedStatement untuk mencegah kebocoran resource
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menutup resource: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Memanggil metode tambahan setelah penyimpanan data
        autonumber(); // Mengenerate nomor ID Barang berikutnya
        tampil(); // Menampilkan data terbaru di tabel
        bersih(); // Membersihkan input form
    }
    
    private void hapus() {
        // Menampilkan dialog konfirmasi untuk memastikan pengguna ingin menghapus data
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda Yakin Akan Menghapus Data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Jika pengguna memilih "Yes", lanjutkan dengan penghapusan data
        if (confirm == JOptionPane.YES_OPTION) {
            PreparedStatement pst = null;
            try {
                // Menyiapkan query SQL untuk menghapus data berdasarkan id_barang
                String query = "DELETE FROM tbl_barang WHERE id_barang = ?";
                pst = conn.prepareStatement(query);

                // Mengisi nilai placeholder dengan id_barang dari form
                pst.setString(1, txt_idBarang.getText());

                // Eksekusi query untuk menghapus data
                int rowsAffected = pst.executeUpdate();

                // Memberikan konfirmasi jika data berhasil dihapus
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Data Barang Berhasil dihapus");
                } else {
                    JOptionPane.showMessageDialog(null, "Data Barang tidak ditemukan atau sudah dihapus.");
                }

            } catch (SQLException e) {
                // Menangani kesalahan SQL dan menampilkan pesan kesalahan
                JOptionPane.showMessageDialog(null, "Keterangan Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Menutup PreparedStatement untuk mencegah kebocoran resource
                try {
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Gagal menutup resource: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Memanggil metode tambahan setelah penghapusan data
            autonumber(); // Mengenerate nomor ID Barang berikutnya
            tampil(); // Menampilkan data terbaru di tabel
            bersih(); // Membersihkan input form
        }
    }




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_qty = new javax.swing.JTextField();
        txt_idBarang = new javax.swing.JTextField();
        cmb_kategori = new javax.swing.JComboBox<>();
        txt_namaBarang = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_barang = new javax.swing.JTable();
        btn_close = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_refresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Swis721 BlkCn BT", 2, 18)); // NOI18N
        jLabel1.setText("DATA BARANG");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 130, -1));

        jLabel3.setText("Qty");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 80, -1));

        jLabel4.setText("ID Barang");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 80, -1));

        jLabel5.setText("Nama");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 80, -1));

        jLabel6.setText("Kategori");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 80, -1));
        getContentPane().add(txt_qty, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 430, -1));
        getContentPane().add(txt_idBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 430, -1));

        cmb_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Kategori Barang ", "Elektonik ", "Meubeler ", "ATK " }));
        getContentPane().add(cmb_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 430, -1));
        getContentPane().add(txt_namaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 430, -1));

        tbl_barang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_barangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_barang);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 510, 160));

        btn_close.setText("Close");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        getContentPane().add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 340, 80, -1));

        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        getContentPane().add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 80, -1));

        btn_update.setText("Update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        getContentPane().add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 80, -1));

        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        getContentPane().add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, 80, -1));

        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        getContentPane().add(btn_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 340, 80, -1));

        setSize(new java.awt.Dimension(560, 415));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

    private void tbl_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_barangMouseClicked
        // TODO add your handling code here:
        try {
            // Mendapatkan indeks baris yang diklik pada tabel berdasarkan lokasi klik mouse.
            int row = tbl_barang.rowAtPoint(evt.getPoint());

            // Mengambil data dari setiap kolom di baris yang diklik.
            String id = tbl_barang.getValueAt(row, 0).toString();      // Mengambil data dari kolom pertama (ID Barang).
            String name = tbl_barang.getValueAt(row, 1).toString();    // Mengambil data dari kolom kedua (Nama Barang).
            String category = tbl_barang.getValueAt(row, 2).toString(); // Mengambil data dari kolom ketiga (Kategori Barang).
            String qty = tbl_barang.getValueAt(row, 3).toString();     // Mengambil data dari kolom keempat (Jumlah Barang).

            // Mengisi form input dengan data yang diambil dari tabel.
            txt_idBarang.setText(id);          // Mengisi field ID Barang dengan data yang diambil.
            txt_namaBarang.setText(name);      // Mengisi field Nama Barang dengan data yang diambil.
            cmb_kategori.setSelectedItem(category); // Mengisi combobox kategori dengan data yang diambil.
            txt_qty.setText(qty);              // Mengisi field Jumlah Barang dengan data yang diambil.

        } catch (Exception e) {
            // Menampilkan pesan kesalahan jika terjadi error saat mengambil atau menampilkan data.
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_tbl_barangMouseClicked

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        // TODO add your handling code here:
        simpan(); //memanggil private void simpan
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        // TODO add your handling code here:
        ubah(); //memanggil private void ubah
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // TODO add your handling code here:
        hapus(); //memanggil private void hapus
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        // TODO add your handling code here:
        bersih(); //memanggil private void bersih
    }//GEN-LAST:event_btn_refreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(form_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(form_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(form_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(form_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new form_barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cmb_kategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_barang;
    private javax.swing.JTextField txt_idBarang;
    private javax.swing.JTextField txt_namaBarang;
    private javax.swing.JTextField txt_qty;
    // End of variables declaration//GEN-END:variables
}

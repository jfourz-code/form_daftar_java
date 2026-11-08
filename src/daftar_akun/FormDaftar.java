/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daftar_akun;

/**
 *
 * @author junsuy
 */


import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;                               // [TAMBAH]
import java.sql.PreparedStatement;                        // [TAMBAH]
import java.sql.ResultSet;                                // [TAMBAH]
import java.sql.SQLException;                             // [TAMBAH]
import java.sql.SQLIntegrityConstraintViolationException; // [TAMBAH]
import javax.swing.table.DefaultTableModel;               // [TAMBAH]


public class FormDaftar extends JFrame {
    private JTextField txtNama;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnDaftar;
    private JButton btnHapusSemua;          // [TAMBAH]
    private JTable tblAkun;                 // [TAMBAH]
    private DefaultTableModel model;        // [TAMBAH]
    
    public FormDaftar() {
        initUI();
        tampilData();                       // [TAMBAH] isi tabel saat form dibuka
    }
    
    
    private void initUI() {
        setTitle("Daftar Akun");
        setSize(520, 480);                  // [UBAH] sebelumnya (380, 250)
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // judul
        JLabel labelJudul = new JLabel("DAFTAR AKUN", SwingConstants.CENTER);
        labelJudul.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelJudul, BorderLayout.NORTH);

        // form (3 baris x 2 kolom)
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        txtNama = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        panelForm.add(new JLabel("Nama:"));
        panelForm.add(txtNama);
        panelForm.add(new JLabel("Email:"));
        panelForm.add(txtEmail);
        panelForm.add(new JLabel("Password:"));
        panelForm.add(txtPassword);
        // [HAPUS] add(panelForm, BorderLayout.CENTER);
       
       
        // tombol Daftar
        JPanel panelTombol = new JPanel(new FlowLayout());
        btnDaftar = new JButton("Daftar");
        btnHapusSemua = new JButton("Hapus Semua");   // [TAMBAH]
        panelTombol.add(btnDaftar);
        panelTombol.add(btnHapusSemua);               // [TAMBAH]
        // [HAPUS] add(panelTombol, BorderLayout.SOUTH);

        // [TAMBAH] bagian atas = form + tombol
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        
        
        // [TAMBAH] tabel data akun
        model = new DefaultTableModel(new Object[]{"ID", "Nama", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false;
            }
        };
        
        tblAkun = new JTable(model);
        JScrollPane scrollTabel = new JScrollPane(tblAkun);
        scrollTabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        // [TAMBAH] gabungkan ke bagian tengah jendela
        JPanel panelTengah = new JPanel(new BorderLayout(8, 8));
        panelTengah.add(panelAtas, BorderLayout.NORTH);
        panelTengah.add(scrollTabel, BorderLayout.CENTER);
        add(panelTengah, BorderLayout.CENTER);

        // Aksi tombol
        btnDaftar.addActionListener(e -> aksiDaftar());          // tidak berubah
        btnHapusSemua.addActionListener(e -> aksiHapusSemua());  // [TAMBAH] 
    }
    
    // [TAMBAH] mengosongkan isian form
    private void bersihkan() {
        txtNama.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtNama.requestFocus();
    }
   
    
    // [TAMBAH] memeriksa isian form sebelum dikirim ke database
    private boolean isianValid() {
        String nama = txtNama.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom wajib diisi",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Format email tidak valid",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }


    // [TAMBAH] READ: membaca data dari database lalu menampilkannya di tabel
    private void tampilData() {
        model.setRowCount(0);

        String sql = "SELECT id, nama, email FROM akun";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal membaca data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    // [UBAH] CREATE: sebelumnya hanya menampilkan pesan "Daftar akun berhasil"
    private void aksiDaftar() {
        if (!isianValid()) {
            return;
        }

        String sql = "INSERT INTO akun (nama, email, password) VALUES (?, ?, ?)";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtNama.getText().trim());
            ps.setString(2, txtEmail.getText().trim());
            ps.setString(3, new String(txtPassword.getPassword()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Daftar akun berhasil",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            bersihkan();
            tampilData();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Email sudah terdaftar",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // [TAMBAH] DELETE: menghapus seluruh data akun
    private void aksiHapusSemua() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Seluruh data akun akan dihapus. Lanjutkan?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (pilihan != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM akun";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int jumlah = ps.executeUpdate();

            JOptionPane.showMessageDialog(this, jumlah + " data berhasil dihapus",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            tampilData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

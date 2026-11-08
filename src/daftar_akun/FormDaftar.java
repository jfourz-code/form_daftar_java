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

public class FormDaftar extends JFrame {
    private JTextField txtNama;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnDaftar;
    
    public FormDaftar() {
        initUI();
    }
    
    
    private void initUI() {
        setTitle("Daftar Akun");
        setSize(380, 250);
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
        add(panelForm, BorderLayout.CENTER);

        // tombol Daftar
        JPanel panelTombol = new JPanel(new FlowLayout());
        btnDaftar = new JButton("Daftar");
        panelTombol.add(btnDaftar);
        add(panelTombol, BorderLayout.SOUTH);

        // Aksi tombol
        btnDaftar.addActionListener(e -> aksiDaftar());
    }
    
    private void aksiDaftar() {
        JOptionPane.showMessageDialog(this, "Daftar akun berhasil",
        "Info", JOptionPane.INFORMATION_MESSAGE);
    }

}

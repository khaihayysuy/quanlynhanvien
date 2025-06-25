package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.TaiKhoanDao;
import java.awt.*;

public class DangNhap extends JFrame {
    private JComboBox<String> cbVaiTro;
    private JTextField tfTenDangNhap;
    private JPasswordField pfMatKhau;
    private JButton btnDangNhap, btnThoat;

    public DangNhap() {
        setTitle("Đăng Nhập Hệ Thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 350);  // Tăng kích thước giao diện
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(241, 245, 249));

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));  // Font to hơn
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 15, 15));
        panelForm.setBackground(new Color(241, 245, 249));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lbVaiTro = new JLabel("Vai trò:");
        lbVaiTro.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cbVaiTro = new JComboBox<>(new String[]{"Admin", "Quản lý", "Nhân viên"});
        cbVaiTro.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lbTen = new JLabel("Tên đăng nhập:");
        lbTen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tfTenDangNhap = new JTextField();
        tfTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lbMatKhau = new JLabel("Mật khẩu:");
        lbMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pfMatKhau = new JPasswordField();
        pfMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        panelForm.add(lbVaiTro); panelForm.add(cbVaiTro);
        panelForm.add(lbTen); panelForm.add(tfTenDangNhap);
        panelForm.add(lbMatKhau); panelForm.add(pfMatKhau);

        btnDangNhap = new JButton("Đăng Nhập");
        btnThoat = new JButton("Thoát");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangNhap.setBackground(new Color(34, 197, 94));
        btnDangNhap.setForeground(Color.WHITE);
        btnThoat.setBackground(new Color(239, 68, 68));
        btnThoat.setForeground(Color.WHITE);

        panelForm.add(btnDangNhap); panelForm.add(btnThoat);
        add(panelForm, BorderLayout.CENTER);

        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        btnThoat.addActionListener(e -> System.exit(0));
    }

    private void xuLyDangNhap() {
        String tenDangNhap = tfTenDangNhap.getText().trim();
        String matKhau = new String(pfMatKhau.getPassword()).trim();
        String vaiTroChon = cbVaiTro.getSelectedItem().toString().trim();

        TaiKhoanDao dao = new TaiKhoanDao();

        if (dao.kiemTraDangNhap(tenDangNhap, matKhau, vaiTroChon)) {
            int maNhanVien = dao.layMaNhanVienTuTenDangNhap(tenDangNhap);
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

            HeThongQuanLyNhanSu app = new HeThongQuanLyNhanSu(vaiTroChon, maNhanVien);
            app.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản, mật khẩu hoặc vai trò!", "Đăng Nhập Thất Bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DangNhap login = new DangNhap();
            login.setVisible(true);
        });
    }
}
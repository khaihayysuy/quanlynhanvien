package quanlynhansu.gui;

import javax.swing.*;

import java.awt.*;

public class HeThongQuanLyNhanSu extends JFrame {
    private JPanel sidebar;
    private JPanel mainPanel;
    private String vaiTro;
    private int maNhanVienDangNhap;

    public HeThongQuanLyNhanSu(String vaiTro, int maNhanVienDangNhap) {
        this.vaiTro = vaiTro;
        this.maNhanVienDangNhap = maNhanVienDangNhap;

        setTitle("Hệ Thống Quản Lý Nhân Sự - " + vaiTro);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        taoSidebar();
        taoMainPanel();

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void taoSidebar() {
        sidebar = new JPanel();
        sidebar.setBackground(new Color(24, 33, 60));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 10, 30, 10));

        sidebar.add(titleLabel);

        String[] menuItems;
        if (vaiTro.equals("Admin")) {
            menuItems = new String[]{
                "Quản lý Nhân viên", "Quản lý Phòng Ban", "Quản lý Hồ sơ",
                "Quản lý Chấm công", "Quản lý Lương", "Quản lý Nghỉ phép",
                "Báo cáo & Thống kê", "Quản lý tài khoản", "Đăng xuất"
            };
        } else if (vaiTro.equals("Quản lý")) {
            menuItems = new String[]{
                "Quản lý Nhân viên", "Quản lý Lương", "Quản lý Chấm công",
                "Quản lý Nghỉ phép", "Báo cáo & Thống kê", "Đăng xuất"
            };
        } else {
            menuItems = new String[]{
                "Xem Hồ sơ", "Xem Bảng lương", "Xin Nghỉ phép", "Chấm Công", "Đăng xuất"
            };
        }

        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(220, 45));
            button.setFocusPainted(false);
            button.setBackground(new Color(24, 33, 60));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            button.setHorizontalAlignment(SwingConstants.LEFT);

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(38, 50, 74));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(24, 33, 60));
                }
            });

            button.addActionListener(e -> xuLyMenuClick(item));

            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }
    }

    private void taoMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Chào mừng bạn: " + vaiTro);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
    }

    private void xuLyMenuClick(String item) {
        mainPanel.removeAll();

        if (item.contains("Đăng xuất")) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Đăng Xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    DangNhap login = new DangNhap();
                    login.setVisible(true);
                });
            }
            return;
        }

        switch (item) {
            case "Quản lý Nhân viên":
                mainPanel.add(new QuanLyNhanVienPanel(), BorderLayout.CENTER);
                break;
            case "Quản lý Phòng Ban":
                mainPanel.add(new QuanLyPhongBanPanel(), BorderLayout.CENTER);
                break;
            case "Quản lý Hồ sơ":
                mainPanel.add(new QuanLyHoSoPanel(), BorderLayout.CENTER);
                break;
            case "Quản lý Chấm công":
                mainPanel.add(new QuanLyChamCongPanel(), BorderLayout.CENTER);
                break;
            case "Quản lý Lương":
                mainPanel.add(new QuanLyLuongPanel(), BorderLayout.CENTER);
                break;
            case "Quản lý Nghỉ phép":
                mainPanel.add(new QuanLyNghiPhepPanel(), BorderLayout.CENTER);
                break;
            case "Báo cáo & Thống kê":
                mainPanel.add(new QuanLyBaoCaoThongKePanel(), BorderLayout.CENTER);
                break;
            case "Quản lý tài khoản":
                mainPanel.add(new QuanLyTaiKhoanPanel(), BorderLayout.CENTER);
                break;
            case "Xem Hồ sơ":
                mainPanel.add(new XemHoSoPanel(maNhanVienDangNhap), BorderLayout.CENTER);
                break;
            case "Xem Bảng lương":
                mainPanel.add(new XemLuongPanel(maNhanVienDangNhap), BorderLayout.CENTER);
                break;
            case "Xin Nghỉ phép":
                mainPanel.add(new XinNghiPanel(maNhanVienDangNhap), BorderLayout.CENTER);
                break;
            case "Chấm Công":
                mainPanel.add(new ChamCongNhanVienPanel(maNhanVienDangNhap), BorderLayout.CENTER);
                break;
            default:
                JLabel label = new JLabel("Bạn đang ở: " + item);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(label, BorderLayout.CENTER);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
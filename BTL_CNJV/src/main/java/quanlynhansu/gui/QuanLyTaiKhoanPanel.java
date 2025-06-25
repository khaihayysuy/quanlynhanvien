package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.TaiKhoanTSX;
import quanlynhansu.dto.TaiKhoan;
import quanlynhansu.databaseconnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyTaiKhoanPanel extends JPanel {

    // Bảng hiển thị danh sách tài khoản
    private JTable table;

    // Model chứa dữ liệu cho bảng
    private DefaultTableModel model;

    // DAO xử lý nghiệp vụ tài khoản
    private TaiKhoanTSX dao = new TaiKhoanTSX();

    // Ô tìm kiếm theo mã nhân viên
    private JTextField tfSearchMaNV;

    // Constructor - thiết lập giao diện và tải dữ liệu ban đầu
    public QuanLyTaiKhoanPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();        // Khởi tạo UI
        loadData(null);  // Tải toàn bộ dữ liệu ban đầu
    }

    // Khởi tạo giao diện quản lý tài khoản
    private void initUI() {
        // Tiêu đề
        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Panel tìm kiếm tài khoản theo mã nhân viên
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        tfSearchMaNV = new JTextField(10);
        JButton btnSearch = new JButton("Tìm");

        searchPanel.add(new JLabel("Mã NV:"));
        searchPanel.add(tfSearchMaNV);
        searchPanel.add(btnSearch);

        // Gán sự kiện tìm kiếm
        btnSearch.addActionListener(e -> {
            String maNV = tfSearchMaNV.getText().trim();
            loadData(maNV.isEmpty() ? null : maNV);  // Nếu rỗng thì load all
        });

        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Cấu hình bảng và model
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Tên đăng nhập", "Mật khẩu", "Vai trò", "Mã nhân viên"});

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Màu xen kẽ dòng
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                } else {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        });

        // Thêm bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Tạo các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");

        // Cài đặt style cho nút
        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnRefresh}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(new Dimension(120, 35));
        }

        // Thêm nút vào panel
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.SOUTH);

        // Gán sự kiện cho các nút
        btnAdd.addActionListener(e -> themTaiKhoan());
        btnEdit.addActionListener(e -> suaTaiKhoan());
        btnDelete.addActionListener(e -> xoaTaiKhoan());
        btnRefresh.addActionListener(e -> loadData(null));
    }

    // Tải danh sách tài khoản (có thể lọc theo mã nhân viên)
    private void loadData(String maNVFilter) {
        model.setRowCount(0);  // Xóa dữ liệu cũ
        List<TaiKhoan> danhSach = dao.layDanhSachTaiKhoan();  // Gọi DAO
        for (TaiKhoan tk : danhSach) {
            if (maNVFilter != null && !String.valueOf(tk.getMaNhanVien()).equals(maNVFilter)) continue;
            model.addRow(new Object[]{
                tk.getId(),
                tk.getTenDangNhap(),
                tk.getMatKhau(),
                tk.getVaiTro(),
                tk.getMaNhanVien()
            });
        }
    }

    // Thêm tài khoản mới
    private void themTaiKhoan() {
        JTextField tfTen = new JTextField();
        JTextField tfMatKhau = new JTextField();
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});
        JTextField tfMaNhanVien = new JTextField();

        // Tạo form nhập liệu
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 200));
        panel.add(new JLabel("Tên đăng nhập:")); panel.add(tfTen);
        panel.add(new JLabel("Mật khẩu:")); panel.add(tfMatKhau);
        panel.add(new JLabel("Vai trò:")); panel.add(cbVaiTro);
        panel.add(new JLabel("Mã nhân viên:")); panel.add(tfMaNhanVien);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Tài Khoản", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Tạo đối tượng tài khoản
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(tfTen.getText());
                tk.setMatKhau(tfMatKhau.getText());
                tk.setVaiTro(cbVaiTro.getSelectedItem().toString());
                tk.setMaNhanVien(Integer.parseInt(tfMaNhanVien.getText()));

                // Gọi DAO để thêm vào DB
                if (dao.themTaiKhoan(tk)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Sửa tài khoản đã chọn
    private void suaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn tài khoản cần sửa!");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        JTextField tfMatKhau = new JTextField(model.getValueAt(selectedRow, 2).toString());
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});
        cbVaiTro.setSelectedItem(model.getValueAt(selectedRow, 3).toString());

        // Tạo form sửa
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setPreferredSize(new Dimension(350, 100));
        panel.add(new JLabel("Mật khẩu:")); panel.add(tfMatKhau);
        panel.add(new JLabel("Vai trò:")); panel.add(cbVaiTro);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Tài Khoản", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            TaiKhoan tk = new TaiKhoan();
            tk.setId(id);
            tk.setMatKhau(tfMatKhau.getText());
            tk.setVaiTro(cbVaiTro.getSelectedItem().toString());

            if (dao.capNhatTaiKhoan(tk)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    // Xóa tài khoản
    private void xoaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn tài khoản cần xóa!");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.xoaTaiKhoan(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    // Truy vấn danh sách nhân viên chưa có tài khoản
    private List<Integer> layDanhSachNhanVienChuaCoTaiKhoan() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT ma_nhan_vien FROM NhanVien WHERE ma_nhan_vien NOT IN (SELECT ma_nhan_vien FROM TaiKhoan WHERE ma_nhan_vien IS NOT NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("ma_nhan_vien"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

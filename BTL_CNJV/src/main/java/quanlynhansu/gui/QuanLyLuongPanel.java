package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.LuongDao;
import quanlynhansu.dto.Luong;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyLuongPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private LuongDao dao = new LuongDao();
    private JTextField tfSearchMaNV;

    public QuanLyLuongPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();
        loadData(null);
    }

    private void initUI() {
        JLabel title = new JLabel("QUẢN LÝ LƯƠNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        tfSearchMaNV = new JTextField(10);
        JButton btnSearch = new JButton("Tìm");

        searchPanel.add(new JLabel("Mã NV:"));
        searchPanel.add(tfSearchMaNV);
        searchPanel.add(btnSearch);

        btnSearch.addActionListener(e -> {
            String maNV = tfSearchMaNV.getText().trim();
            loadData(maNV.isEmpty() ? null : maNV);
        });

        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Mã nhân viên", "Tháng", "Năm", "Lương cơ bản", "Phụ cấp", "Thưởng", "Khấu trừ", "Tổng lương"});

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

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

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnRefresh}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(new Dimension(120, 35));
        }

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> themLuong());
        btnEdit.addActionListener(e -> suaLuong());
        btnDelete.addActionListener(e -> xoaLuong());
        btnRefresh.addActionListener(e -> loadData(null));
    }

    private void loadData(String maNVFilter) {
        model.setRowCount(0);
        List<Luong> danhSach = dao.layDanhSachLuong();
        for (Luong luong : danhSach) {
            if (maNVFilter != null && !String.valueOf(luong.getMaNhanVien()).equals(maNVFilter)) continue;

            model.addRow(new Object[]{
                luong.getMaLuong(),
                luong.getMaNhanVien(),
                luong.getThang(),
                luong.getNam(),
                luong.getLuongCoBan(),
                luong.getPhuCap(),
                luong.getThuong(),
                luong.getKhauTru(),
                luong.getTongLuong()
            });
        }
    }

    private void themLuong() {
        JTextField tfMaNhanVien = new JTextField();
        JTextField tfThang = new JTextField();
        JTextField tfNam = new JTextField();
        JTextField tfLuongCoBan = new JTextField();
        JTextField tfPhuCap = new JTextField();
        JTextField tfThuong = new JTextField();
        JTextField tfKhauTru = new JTextField();

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 280));
        panel.add(new JLabel("Mã nhân viên:")); panel.add(tfMaNhanVien);
        panel.add(new JLabel("Tháng:")); panel.add(tfThang);
        panel.add(new JLabel("Năm:")); panel.add(tfNam);
        panel.add(new JLabel("Lương cơ bản:")); panel.add(tfLuongCoBan);
        panel.add(new JLabel("Phụ cấp:")); panel.add(tfPhuCap);
        panel.add(new JLabel("Thưởng:")); panel.add(tfThuong);
        panel.add(new JLabel("Khấu trừ:")); panel.add(tfKhauTru);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Lương", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int maNhanVien = Integer.parseInt(tfMaNhanVien.getText());
                int thang = Integer.parseInt(tfThang.getText());
                int nam = Integer.parseInt(tfNam.getText());
                double luongCoBan = Double.parseDouble(tfLuongCoBan.getText());
                double phuCap = Double.parseDouble(tfPhuCap.getText());
                double thuong = Double.parseDouble(tfThuong.getText());
                double khauTru = Double.parseDouble(tfKhauTru.getText());

                Luong luong = new Luong();
                luong.setMaNhanVien(maNhanVien);
                luong.setThang(thang);
                luong.setNam(nam);
                luong.setLuongCoBan(luongCoBan);
                luong.setPhuCap(phuCap);
                luong.setThuong(thuong);
                luong.setKhauTru(khauTru);
                luong.tinhTongLuong();

                if (dao.themLuong(luong)) {
                    JOptionPane.showMessageDialog(this, "Thêm lương thành công!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm lương thất bại!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        }
    }

    private void suaLuong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn lương cần sửa!");
            return;
        }

        int maLuong = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        int maNhanVien = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
        int thang = Integer.parseInt(model.getValueAt(selectedRow, 2).toString());
        int nam = Integer.parseInt(model.getValueAt(selectedRow, 3).toString());
        JTextField tfThang = new JTextField(String.valueOf(thang));
        JTextField tfNam = new JTextField(String.valueOf(nam));
        JTextField tfLuongCoBan = new JTextField(model.getValueAt(selectedRow, 4).toString());
        JTextField tfPhuCap = new JTextField(model.getValueAt(selectedRow, 5).toString());
        JTextField tfThuong = new JTextField(model.getValueAt(selectedRow, 6).toString());
        JTextField tfKhauTru = new JTextField(model.getValueAt(selectedRow, 7).toString());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 300));
        panel.add(new JLabel("Mã nhân viên:")); panel.add(new JLabel(String.valueOf(maNhanVien)));
        panel.add(new JLabel("Tháng:")); panel.add(tfThang);
        panel.add(new JLabel("Năm:")); panel.add(tfNam);
        panel.add(new JLabel("Lương cơ bản:")); panel.add(tfLuongCoBan);
        panel.add(new JLabel("Phụ cấp:")); panel.add(tfPhuCap);
        panel.add(new JLabel("Thưởng:")); panel.add(tfThuong);
        panel.add(new JLabel("Khấu trừ:")); panel.add(tfKhauTru);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Lương", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int thangNew = Integer.parseInt(tfThang.getText());
                int namNew = Integer.parseInt(tfNam.getText());
                double luongCoBan = Double.parseDouble(tfLuongCoBan.getText());
                double phuCap = Double.parseDouble(tfPhuCap.getText());
                double thuong = Double.parseDouble(tfThuong.getText());
                double khauTru = Double.parseDouble(tfKhauTru.getText());

                Luong luong = new Luong();
                luong.setMaLuong(maLuong);
                luong.setMaNhanVien(maNhanVien);
                luong.setThang(thangNew);
                luong.setNam(namNew);
                luong.setLuongCoBan(luongCoBan);
                luong.setPhuCap(phuCap);
                luong.setThuong(thuong);
                luong.setKhauTru(khauTru);
                luong.tinhTongLuong();

                if (dao.capNhatLuong(luong)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật lương thành công!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật lương thất bại!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        }
    }

    private void xoaLuong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn lương cần xóa!");
            return;
        }

        int maNhanVien = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
        int thang = Integer.parseInt(model.getValueAt(selectedRow, 2).toString());
        int nam = Integer.parseInt(model.getValueAt(selectedRow, 3).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.xoaLuong(maNhanVien, thang, nam)) {
                JOptionPane.showMessageDialog(this, "Xóa lương thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa lương thất bại!");
            }
        }
    }
}
package quanlynhansu.gui;

import java.sql.Connection;
import quanlynhansu.dao.HoSoDao;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.HoSo;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

public class QuanLyHoSoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private HoSoDao dao = new HoSoDao();
    private JTextField tfTimMaNV;

    public QuanLyHoSoPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();
        loadData(null); // load toàn bộ ban đầu
    }

    private void initUI() {
        JLabel title = new JLabel("QUẢN LÝ HỒ SƠ", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Thanh tìm kiếm theo mã NV
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        tfTimMaNV = new JTextField(10);
        JButton btnTim = new JButton("Tìm");

        searchPanel.add(new JLabel("Mã nhân viên:"));
        searchPanel.add(tfTimMaNV);
        searchPanel.add(btnTim);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        btnTim.addActionListener(e -> {
            String keyword = tfTimMaNV.getText().trim();
            loadData(keyword.isEmpty() ? null : keyword);
        });

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã hồ sơ", "Mã NV", "Loại hồ sơ", "Tên tài liệu", "Đường dẫn file", "Ngày upload"});

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
        JButton btnOpenFile = new JButton("Mở file");

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnRefresh, btnOpenFile}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(new Dimension(120, 35));
        }

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnOpenFile);

        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> themHoSo());
        btnEdit.addActionListener(e -> suaHoSo());
        btnDelete.addActionListener(e -> xoaHoSo());
        btnRefresh.addActionListener(e -> loadData(null));
        btnOpenFile.addActionListener(e -> moFile());
    }

    private void loadData(String maNVFilter) {
        model.setRowCount(0);
        List<HoSo> list = dao.layDanhSachHoSo();
        for (HoSo hs : list) {
            String maNVStr = String.valueOf(hs.getMaNhanVien());
            if (maNVFilter == null || maNVStr.equals(maNVFilter)) {
                model.addRow(new Object[]{
                        hs.getMaHoSo(),
                        hs.getMaNhanVien(),
                        hs.getLoaiHoSo(),
                        hs.getTenTaiLieu(),
                        hs.getDuongDanFile(),
                        hs.getNgayUpload()
                });
            }
        }
    }

    private void suaHoSo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn hồ sơ cần sửa!");
            return;
        }

        int maHoSo = (int) model.getValueAt(selectedRow, 0);
        JTextField tfLoaiHoSo = new JTextField(model.getValueAt(selectedRow, 2).toString());
        JTextField tfTenTaiLieu = new JTextField(model.getValueAt(selectedRow, 3).toString());
        JTextField tfDuongDan = new JTextField(model.getValueAt(selectedRow, 4).toString());
        JButton btnChonFile = new JButton("...");

        btnChonFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                tfDuongDan.setText(selectedFile.getAbsolutePath());
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setPreferredSize(new Dimension(500, 130));
        panel.add(new JLabel("Loại Hồ Sơ:")); panel.add(tfLoaiHoSo);
        panel.add(new JLabel("Tên Tài Liệu:")); panel.add(tfTenTaiLieu);
        panel.add(new JLabel("Đường Dẫn File:"));
        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.add(tfDuongDan, BorderLayout.CENTER);
        filePanel.add(btnChonFile, BorderLayout.EAST);
        panel.add(filePanel);

        int confirm = JOptionPane.showConfirmDialog(this, panel, "Sửa Hồ Sơ", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            HoSo hs = new HoSo();
            hs.setMaHoSo(maHoSo);
            hs.setLoaiHoSo(tfLoaiHoSo.getText());
            hs.setTenTaiLieu(tfTenTaiLieu.getText());
            hs.setDuongDanFile(tfDuongDan.getText());

            if (dao.capNhatHoSo(hs)) {
                JOptionPane.showMessageDialog(this, "Sửa hồ sơ thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    private void themHoSo() {
        JTextField tfMaNhanVien = new JTextField();
        JTextField tfLoaiHoSo = new JTextField();
        JTextField tfTenTaiLieu = new JTextField();

        JButton btnChonFile = new JButton("Chọn File");
        JLabel lblPath = new JLabel("Chưa chọn file");

        btnChonFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                lblPath.setText(selectedFile.getAbsolutePath());
            }
        });

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setPreferredSize(new Dimension(500, 220));
        panel.add(new JLabel("Mã Nhân Viên:")); panel.add(tfMaNhanVien);
        panel.add(new JLabel("Loại Hồ Sơ:")); panel.add(tfLoaiHoSo);
        panel.add(new JLabel("Tên Tài Liệu:")); panel.add(tfTenTaiLieu);
        panel.add(new JLabel("Chọn File:")); panel.add(btnChonFile);
        panel.add(new JLabel("Đường Dẫn File:")); panel.add(lblPath);

        int confirm = JOptionPane.showConfirmDialog(this, panel, "Thêm Hồ Sơ", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                File fileSrc = new File(lblPath.getText());
                if (!fileSrc.exists()) {
                    JOptionPane.showMessageDialog(this, "File không tồn tại!");
                    return;
                }

                File uploadFolder = new File("uploads");
                if (!uploadFolder.exists()) uploadFolder.mkdirs();

                File fileDest = new File(uploadFolder, fileSrc.getName());
                Files.copy(fileSrc.toPath(), fileDest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                HoSo hs = new HoSo();
                hs.setMaNhanVien(Integer.parseInt(tfMaNhanVien.getText()));
                hs.setLoaiHoSo(tfLoaiHoSo.getText());
                hs.setTenTaiLieu(tfTenTaiLieu.getText());
                hs.setDuongDanFile("uploads/" + fileSrc.getName());
                hs.setNgayUpload(new Date());

                if (dao.themHoSo(hs)) {
                    JOptionPane.showMessageDialog(this, "Thêm hồ sơ thành công!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm hồ sơ thất bại!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void xoaHoSo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn hồ sơ cần xóa!");
            return;
        }

        int maHoSo = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hồ sơ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM HoSo WHERE ma_ho_so = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, maHoSo);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa hồ sơ thành công!");
                loadData(null);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void moFile() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn hồ sơ cần mở file!");
            return;
        }

        String duongDan = model.getValueAt(selectedRow, 4).toString();
        try {
            File file = new File(duongDan);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this, "File không tồn tại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở file: " + e.getMessage());
        }
    }
}

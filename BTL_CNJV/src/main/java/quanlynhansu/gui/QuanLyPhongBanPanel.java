package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.PhongBanDao;
import quanlynhansu.dto.PhongBan;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyPhongBanPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private PhongBanDao dao = new PhongBanDao();

    public QuanLyPhongBanPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();
        loadData(null); // load toàn bộ
    }

    private void initUI() {
        JLabel title = new JLabel("QUẢN LÝ PHÒNG BAN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JTextField tfTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tìm");

        searchPanel.add(new JLabel("Tên phòng ban:"));
        searchPanel.add(tfTimKiem);
        searchPanel.add(btnTim);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        btnTim.addActionListener(e -> {
            String keyword = tfTimKiem.getText().trim();
            loadData(keyword.isEmpty() ? null : keyword);
        });

        // Bảng
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã phòng ban", "Tên phòng ban"});

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

        btnAdd.addActionListener(e -> themPhongBan());
        btnEdit.addActionListener(e -> suaPhongBan());
        btnDelete.addActionListener(e -> xoaPhongBan());
        btnRefresh.addActionListener(e -> loadData(null));
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        List<PhongBan> danhSach = dao.layDanhSachPhongBan();
        for (PhongBan pb : danhSach) {
            if (keyword == null || pb.getTenPhongBan().toLowerCase().contains(keyword.toLowerCase())) {
                model.addRow(new Object[]{
                        pb.getMaPhongBan(),
                        pb.getTenPhongBan()
                });
            }
        }
    }

    private void themPhongBan() {
        JTextField tfTen = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 60));
        panel.add(new JLabel("Tên phòng ban:"));
        panel.add(tfTen);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Phòng Ban", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            PhongBan pb = new PhongBan();
            pb.setTenPhongBan(tfTen.getText());

            if (dao.themPhongBan(pb)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        }
    }

    private void suaPhongBan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phòng ban cần sửa!");
            return;
        }

        int maPB = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        JTextField tfTen = new JTextField(model.getValueAt(selectedRow, 1).toString());

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 60));
        panel.add(new JLabel("Tên phòng ban:"));
        panel.add(tfTen);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Phòng Ban", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            PhongBan pb = new PhongBan();
            pb.setMaPhongBan(maPB);
            pb.setTenPhongBan(tfTen.getText());

            if (dao.capNhatPhongBan(pb)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    private void xoaPhongBan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn phòng ban cần xóa!");
            return;
        }

        int maPB = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.xoaPhongBan(maPB)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
}

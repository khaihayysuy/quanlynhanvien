package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.ChamCongDao;
import quanlynhansu.dto.ChamCong;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuanLyChamCongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ChamCongDao chamCongDao = new ChamCongDao();
    private JTextField tfSearchMaNV, tfSearchNgay;

    public QuanLyChamCongPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();
        loadData(null, null);
    }

    private void initUI() {
        JLabel title = new JLabel("QUẢN LÝ CHẤM CÔNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        tfSearchMaNV = new JTextField(10);
        tfSearchNgay = new JTextField(10);
        JButton btnSearch = new JButton("Tìm");

        searchPanel.add(new JLabel("Mã NV:"));
        searchPanel.add(tfSearchMaNV);
        searchPanel.add(new JLabel("Ngày:"));
        searchPanel.add(tfSearchNgay);
        searchPanel.add(btnSearch);

        btnSearch.addActionListener(e -> {
            String maNV = tfSearchMaNV.getText().trim();
            String ngay = tfSearchNgay.getText().trim();
            loadData(maNV.isEmpty() ? null : maNV, ngay.isEmpty() ? null : ngay);
        });

        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        model = new DefaultTableModel(new Object[]{
                "Mã Chấm Công", "Mã NV", "Ngày", "Giờ Vào", "Giờ Ra", "Giờ Làm", "Giờ Làm Thêm", "Trạng Thái"
        }, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
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

        JButton btnAdd = new JButton("Thêm mới");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnRefresh}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(new Dimension(130, 35));
        }

        btnAdd.addActionListener(e -> themChamCong());
        btnEdit.addActionListener(e -> suaChamCong());
        btnDelete.addActionListener(e -> xoaChamCong());
        btnRefresh.addActionListener(e -> loadData(null, null));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData(String maNVFilter, String ngayFilter) {
        model.setRowCount(0);
        List<ChamCong> list = chamCongDao.layDanhSachChamCong();
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy");

        for (ChamCong cc : list) {
            String maNVStr = String.valueOf(cc.getMaNhanVien());
            String ngayStr = dateFmt.format(cc.getNgayChamCong());

            if ((maNVFilter != null && !maNVStr.equals(maNVFilter)) ||
                    (ngayFilter != null && !ngayStr.equals(ngayFilter))) {
                continue;
            }

            String gioVaoStr = (cc.getGioVao() != null && cc.getGioVao().length() >= 8)
                    ? cc.getGioVao().substring(0, 8) : "";

            String gioRaStr = (cc.getGioRa() != null && cc.getGioRa().length() >= 8)
                    ? cc.getGioRa().substring(0, 8) : "";

            double gioLam = 0.0;
            try {
                if (cc.getGioVao() != null && cc.getGioRa() != null) {
                    LocalTime gioVao = LocalTime.parse(cc.getGioVao().substring(0, 8), timeFmt);
                    LocalTime gioRa = LocalTime.parse(cc.getGioRa().substring(0, 8), timeFmt);
                    gioLam = Duration.between(gioVao, gioRa).toMinutes() / 60.0;
                }
            } catch (Exception ignored) {}

            model.addRow(new Object[]{
                    cc.getMaChamCong(),
                    cc.getMaNhanVien(),
                    ngayStr,
                    gioVaoStr,
                    gioRaStr,
                    String.format("%.2f", gioLam),
                    String.format("%.2f", cc.getGioLamThem()),
                    cc.getTrangThai()
            });
        }
    }

    private void themChamCong() {
        JTextField tfMaNV = new JTextField();
        JTextField tfGioVao = new JTextField();
        JTextField tfGioRa = new JTextField();
        JTextField tfTrangThai = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Mã nhân viên:"));
        panel.add(tfMaNV);
        panel.add(new JLabel("Giờ vào (HH:mm:ss):"));
        panel.add(tfGioVao);
        panel.add(new JLabel("Giờ ra (HH:mm:ss):"));
        panel.add(tfGioRa);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(tfTrangThai);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Chấm Công", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int maNV = Integer.parseInt(tfMaNV.getText().trim());
                String gioVao = tfGioVao.getText().trim();
                String gioRa = tfGioRa.getText().trim();
                String trangThai = tfTrangThai.getText().trim();

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime vao = LocalTime.parse(gioVao, fmt);
                LocalTime ra = LocalTime.parse(gioRa, fmt);
                double gioLam = Duration.between(vao, ra).toMinutes() / 60.0;
                double gioThem = gioLam > 8.0 ? gioLam - 8.0 : 0.0;

                ChamCong cc = new ChamCong(0, maNV, new java.util.Date(), gioVao, gioRa, gioThem, trangThai);
                if (chamCongDao.themChamCong(cc)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadData(null, null);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        }
    }

    private void suaChamCong() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!");
            return;
        }

        JTextField tfMaNV = new JTextField(table.getValueAt(selected, 1).toString());
        JTextField tfGioVao = new JTextField(table.getValueAt(selected, 3).toString());
        JTextField tfGioRa = new JTextField(table.getValueAt(selected, 4).toString());
        JTextField tfTrangThai = new JTextField(table.getValueAt(selected, 7).toString());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Mã nhân viên:"));
        panel.add(tfMaNV);
        panel.add(new JLabel("Giờ vào:"));
        panel.add(tfGioVao);
        panel.add(new JLabel("Giờ ra:"));
        panel.add(tfGioRa);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(tfTrangThai);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Chấm Công", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int maChamCong = (int) table.getValueAt(selected, 0);
                int maNV = Integer.parseInt(tfMaNV.getText().trim());
                String gioVao = tfGioVao.getText().trim();
                String gioRa = tfGioRa.getText().trim();
                String trangThai = tfTrangThai.getText().trim();

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime vao = LocalTime.parse(gioVao, fmt);
                LocalTime ra = LocalTime.parse(gioRa, fmt);
                double gioLam = Duration.between(vao, ra).toMinutes() / 60.0;
                double gioThem = gioLam > 8.0 ? gioLam - 8.0 : 0.0;

                ChamCong cc = new ChamCong(maChamCong, maNV, new java.util.Date(), gioVao, gioRa, gioThem, trangThai);
                if (chamCongDao.suaChamCong(cc)) {
                    JOptionPane.showMessageDialog(this, "Sửa thành công!");
                    loadData(null, null);
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!");
            }
        }
    }

    private void xoaChamCong() {
        int selected = table.getSelectedRow();
        if (selected >= 0) {
            int maChamCong = (int) table.getValueAt(selected, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (chamCongDao.xoaChamCong(maChamCong)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadData(null, null);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
        }
    }
}

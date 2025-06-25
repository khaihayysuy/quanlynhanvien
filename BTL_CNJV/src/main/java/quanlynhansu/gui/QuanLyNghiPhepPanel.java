package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.NghiPhepDao;
import quanlynhansu.dto.NghiPhep;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyNghiPhepPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private NghiPhepDao dao = new NghiPhepDao();
    private JTextField tfSearchMaNV;

    public QuanLyNghiPhepPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();
        loadData(null);
    }

    private void initUI() {
        JLabel title = new JLabel("QUẢN LÝ NGHỈ PHÉP", JLabel.CENTER);
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
        model.setColumnIdentifiers(new Object[]{
            "Mã nghỉ phép", "Mã nhân viên", "Loại nghỉ phép", "Ngày bắt đầu",
            "Ngày kết thúc", "Số ngày", "Lý do", "Trạng thái"
        });

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

        JButton btnApprove = new JButton("Duyệt đơn");
        JButton btnReject = new JButton("Từ chối đơn");

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        btnApprove.setFont(btnFont);
        btnReject.setFont(btnFont);
        btnApprove.setPreferredSize(new Dimension(140, 35));
        btnReject.setPreferredSize(new Dimension(140, 35));

        buttonPanel.add(btnApprove);
        buttonPanel.add(btnReject);

        add(buttonPanel, BorderLayout.SOUTH);

        btnApprove.addActionListener(e -> duyetNghiPhep());
        btnReject.addActionListener(e -> tuChoiNghiPhep());
    }

    private void loadData(String maNVFilter) {
        model.setRowCount(0);
        List<NghiPhep> danhSach = dao.layDanhSachNghiPhep();
        for (NghiPhep nghiPhep : danhSach) {
            if (maNVFilter != null && !String.valueOf(nghiPhep.getMaNhanVien()).equals(maNVFilter)) continue;

            model.addRow(new Object[]{
                nghiPhep.getMaNghiPhep(),
                nghiPhep.getMaNhanVien(),
                nghiPhep.getLoaiNghiPhep(),
                nghiPhep.getNgayBatDau(),
                nghiPhep.getNgayKetThuc(),
                nghiPhep.getSoNgay(),
                nghiPhep.getLyDo(),
                nghiPhep.getTrangThai()
            });
        }
    }

    private void duyetNghiPhep() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn nghỉ phép để duyệt!");
            return;
        }
        int maNghiPhep = (int) model.getValueAt(selectedRow, 0);
        dao.capNhatTrangThai(maNghiPhep, "Đã duyệt");
        loadData(null);
    }

    private void tuChoiNghiPhep() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn nghỉ phép để từ chối!");
            return;
        }
        int maNghiPhep = (int) model.getValueAt(selectedRow, 0);
        dao.capNhatTrangThai(maNghiPhep, "Đã từ chối");
        loadData(null);
    }
}

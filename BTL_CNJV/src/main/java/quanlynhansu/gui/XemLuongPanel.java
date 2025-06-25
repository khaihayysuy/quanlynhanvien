package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class XemLuongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private int maNhanVien;

    public XemLuongPanel(int maNhanVien) {
        this.maNhanVien = maNhanVien;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();
        loadData();
    }

    private void initUI() {
        JLabel title = new JLabel("BẢNG LƯƠNG CỦA BẠN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 37, 41));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "Tháng", "Năm", "Lương CB", "Phụ cấp", "Thưởng", "Khấu trừ", "Tổng lương"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Dòng xen kẽ màu
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
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT thang, nam, luong_co_ban, phu_cap, thuong, khau_tru, " +
                         "(luong_co_ban + phu_cap + thuong - khau_tru) AS tong_luong " +
                         "FROM Luong WHERE ma_nhan_vien = ? ORDER BY nam DESC, thang DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("thang"),
                    rs.getInt("nam"),
                    rs.getDouble("luong_co_ban"),
                    rs.getDouble("phu_cap"),
                    rs.getDouble("thuong"),
                    rs.getDouble("khau_tru"),
                    rs.getDouble("tong_luong")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

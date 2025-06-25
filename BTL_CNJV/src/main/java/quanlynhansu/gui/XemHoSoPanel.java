package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class XemHoSoPanel extends JPanel {
    private int maNhanVien;

    public XemHoSoPanel(int maNhanVien) {
        this.maNhanVien = maNhanVien;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        loadData();
    }

    private void addInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setBackground(new Color(245, 245, 250));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setPreferredSize(new Dimension(160, 30));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        row.setAlignmentX(LEFT_ALIGNMENT);
        add(Box.createVerticalStrut(8));  // spacing giữa các dòng
        add(row);
    }

    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, ma_phong_ban, trang_thai FROM NhanVien WHERE ma_nhan_vien = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String ngaySinh = "";
                Date dob = rs.getDate("ngay_sinh");
                if (dob != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    ngaySinh = sdf.format(dob);
                }

                addInfoRow("Họ tên:", rs.getString("ho_ten"));
                addInfoRow("Ngày sinh:", ngaySinh);
                addInfoRow("Giới tính:", rs.getString("gioi_tinh"));
                addInfoRow("Số điện thoại:", rs.getString("so_dien_thoai"));
                addInfoRow("Mã phòng ban:", String.valueOf(rs.getInt("ma_phong_ban")));
                addInfoRow("Trạng thái:", rs.getString("trang_thai"));
            } else {
                addInfoRow("Thông báo:", "Không tìm thấy hồ sơ!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            addInfoRow("Lỗi:", "Lỗi kết nối cơ sở dữ liệu!");
        }
    }
}

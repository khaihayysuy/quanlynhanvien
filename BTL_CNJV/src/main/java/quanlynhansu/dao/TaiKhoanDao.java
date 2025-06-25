package quanlynhansu.dao;

import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.TaiKhoan;

public class TaiKhoanDao {
    
    public boolean kiemTraDangNhap(String username, String password, String vaiTro) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ? AND vaiTro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, vaiTro);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String layVaiTro(String username) {
        String sql = "SELECT vaiTro FROM TaiKhoan WHERE tenDangNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("vaiTro");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Thêm mới:
    public int layMaNhanVienTuTenDangNhap(String tenDangNhap) {
        String sql = "SELECT ma_nhan_vien FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ma_nhan_vien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // nếu không tìm thấy
    }
}

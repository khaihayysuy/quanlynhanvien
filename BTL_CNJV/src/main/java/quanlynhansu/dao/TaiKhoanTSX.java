package quanlynhansu.dao;


import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.TaiKhoan;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanTSX {

    public List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getInt("id"),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        rs.getInt("ma_nhan_vien")
                );
                danhSach.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro, ma_nhan_vien) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro());
            ps.setInt(4, tk.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET matKhau=?, vaiTro=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.getVaiTro());
            ps.setInt(3, tk.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaTaiKhoan(int id) {
        String sql = "DELETE FROM TaiKhoan WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

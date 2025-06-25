package quanlynhansu.dao;

import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.NhanVien;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDao {

    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getInt("ma_nhan_vien"),
                        rs.getString("ho_ten"),
                        rs.getString("ngay_sinh"),
                        rs.getString("gioi_tinh"),
                        rs.getString("so_dien_thoai"),
                        rs.getInt("ma_phong_ban")
                );
                danhSach.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, ma_phong_ban) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getNgaySinh());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSoDienThoai());
            ps.setInt(5, nv.getMaPhongBan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET ho_ten=?, ngay_sinh=?, gioi_tinh=?, so_dien_thoai=?, ma_phong_ban=? WHERE ma_nhan_vien=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getNgaySinh());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSoDienThoai());
            ps.setInt(5, nv.getMaPhongBan());
            ps.setInt(6, nv.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaNhanVien(int maNhanVien) {
        String sql = "DELETE FROM NhanVien WHERE ma_nhan_vien = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNhanVien);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package quanlynhansu.dao;

import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.Luong;
import java.util.ArrayList;
import java.util.List;

public class LuongDao {

    // Thêm lương
    public boolean themLuong(Luong luong) {
        String sql = "INSERT INTO Luong (ma_nhan_vien, thang, nam, luong_co_ban, phu_cap, thuong, khau_tru, tong_luong) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, luong.getMaNhanVien());
            ps.setInt(2, luong.getThang());
            ps.setInt(3, luong.getNam());
            ps.setDouble(4, luong.getLuongCoBan());
            ps.setDouble(5, luong.getPhuCap());
            ps.setDouble(6, luong.getThuong());
            ps.setDouble(7, luong.getKhauTru());
            ps.setDouble(8, luong.getTongLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Cập nhật lương theo ma_luong
    public boolean capNhatLuong(Luong luong) {
        String sql = "UPDATE Luong SET ma_nhan_vien=?, thang=?, nam=?, luong_co_ban=?, phu_cap=?, thuong=?, khau_tru=?, tong_luong=? WHERE ma_luong=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, luong.getMaNhanVien());
            ps.setInt(2, luong.getThang());
            ps.setInt(3, luong.getNam());
            ps.setDouble(4, luong.getLuongCoBan());
            ps.setDouble(5, luong.getPhuCap());
            ps.setDouble(6, luong.getThuong());
            ps.setDouble(7, luong.getKhauTru());
            ps.setDouble(8, luong.getTongLuong());
            ps.setInt(9, luong.getMaLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa lương theo ma_nhan_vien + thang + năm
    public boolean xoaLuong(int maNhanVien, int thang, int nam) {
        String sql = "DELETE FROM Luong WHERE ma_nhan_vien=? AND thang=? AND nam=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNhanVien);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách lương
    public List<Luong> layDanhSachLuong() {
        List<Luong> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM Luong";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Luong luong = new Luong();
                luong.setMaLuong(rs.getInt("ma_luong"));
                luong.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                luong.setThang(rs.getInt("thang"));
                luong.setNam(rs.getInt("nam"));
                luong.setLuongCoBan(rs.getDouble("luong_co_ban"));
                luong.setPhuCap(rs.getDouble("phu_cap"));
                luong.setThuong(rs.getDouble("thuong"));
                luong.setKhauTru(rs.getDouble("khau_tru"));
                luong.setTongLuong(rs.getDouble("tong_luong"));
                danhSach.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}

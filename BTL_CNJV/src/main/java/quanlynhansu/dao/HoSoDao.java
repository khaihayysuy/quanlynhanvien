package quanlynhansu.dao;

import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.HoSo;
import java.util.ArrayList;
import java.util.List;

public class HoSoDao {
    public boolean themHoSo(HoSo hs) {
        String sql = "INSERT INTO HoSo(ma_nhan_vien, loai_ho_so, ten_tai_lieu, duong_dan_file, ngay_upload) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hs.getMaNhanVien());
            ps.setString(2, hs.getLoaiHoSo());
            ps.setString(3, hs.getTenTaiLieu());
            ps.setString(4, hs.getDuongDanFile());
            ps.setDate(5, new java.sql.Date(hs.getNgayUpload().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatHoSo(HoSo hs) {
        String sql = "UPDATE HoSo SET loai_ho_so = ?, ten_tai_lieu = ?, duong_dan_file = ? WHERE ma_ho_so = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hs.getLoaiHoSo());
            ps.setString(2, hs.getTenTaiLieu());
            ps.setString(3, hs.getDuongDanFile());
            ps.setInt(4, hs.getMaHoSo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HoSo> layDanhSachHoSo() {
        List<HoSo> list = new ArrayList<>();
        String sql = "SELECT * FROM HoSo";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HoSo hs = new HoSo();
                hs.setMaHoSo(rs.getInt("ma_ho_so"));
                hs.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                hs.setLoaiHoSo(rs.getString("loai_ho_so"));
                hs.setTenTaiLieu(rs.getString("ten_tai_lieu"));
                hs.setDuongDanFile(rs.getString("duong_dan_file"));
                hs.setNgayUpload(rs.getDate("ngay_upload"));
                list.add(hs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}        

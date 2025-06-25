package quanlynhansu.dao;


import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.NghiPhep;
import java.util.ArrayList;
import java.util.List;

public class NghiPhepDao {

    // Lấy danh sách nghỉ phép
    public List<NghiPhep> layDanhSachNghiPhep() {
        List<NghiPhep> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM NghiPhep";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NghiPhep nghiPhep = new NghiPhep();
                nghiPhep.setMaNghiPhep(rs.getInt("ma_nghi_phep"));
                nghiPhep.setMaNhanVien(rs.getInt("ma_nhan_vien"));
                nghiPhep.setLoaiNghiPhep(rs.getString("loai_nghi_phep"));
                nghiPhep.setNgayBatDau(rs.getDate("ngay_bat_dau"));
                nghiPhep.setNgayKetThuc(rs.getDate("ngay_ket_thuc"));
                nghiPhep.setSoNgay(rs.getInt("so_ngay"));
                nghiPhep.setLyDo(rs.getString("ly_do"));
                nghiPhep.setTrangThai(rs.getString("trang_thai"));
                danhSach.add(nghiPhep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    // Thêm đơn nghỉ phép
    public boolean themNghiPhep(NghiPhep nghiPhep) {
        String sql = "INSERT INTO NghiPhep (ma_nhan_vien, loai_nghi_phep, ngay_bat_dau, ngay_ket_thuc, so_ngay, ly_do, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nghiPhep.getMaNhanVien());
            ps.setString(2, nghiPhep.getLoaiNghiPhep());
            ps.setDate(3, new java.sql.Date(nghiPhep.getNgayBatDau().getTime()));
            ps.setDate(4, new java.sql.Date(nghiPhep.getNgayKetThuc().getTime()));
            ps.setDouble(5, nghiPhep.getSoNgay());
            ps.setString(6, nghiPhep.getLyDo());
            ps.setString(7, nghiPhep.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật trạng thái nghỉ phép
    public boolean capNhatTrangThai(int maNghiPhep, String trangThai) {
        String sql = "UPDATE NghiPhep SET trang_thai = ? WHERE ma_nghi_phep = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            ps.setInt(2, maNghiPhep);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

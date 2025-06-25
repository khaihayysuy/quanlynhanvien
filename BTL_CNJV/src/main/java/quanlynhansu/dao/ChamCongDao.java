package quanlynhansu.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.ChamCong;


public class ChamCongDao {

    // Thêm chấm công mới
    public boolean themChamCong(ChamCong chamCong) {
        String sql = "INSERT INTO ChamCong (ma_nhan_vien, ngay_cham_cong, gio_vao, gio_ra, gio_lam_them, trang_thai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chamCong.getMaNhanVien());
            ps.setDate(2, new java.sql.Date(chamCong.getNgayChamCong().getTime()));
            ps.setString(3, chamCong.getGioVao());
            ps.setString(4, chamCong.getGioRa());
            ps.setDouble(5, chamCong.getGioLamThem());
            ps.setString(6, chamCong.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy toàn bộ danh sách chấm công
    public List<ChamCong> layDanhSachChamCong() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT * FROM ChamCong ORDER BY ngay_cham_cong DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ChamCong chamCong = new ChamCong(
                        rs.getInt("ma_cham_cong"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getDate("ngay_cham_cong"),
                        rs.getString("gio_vao"),
                        rs.getString("gio_ra"),
                        rs.getDouble("gio_lam_them"),
                        rs.getString("trang_thai")
                );
                list.add(chamCong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy danh sách chấm công theo mã nhân viên
    public List<ChamCong> layDanhSachChamCongTheoNV(int maNhanVien) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT * FROM ChamCong WHERE ma_nhan_vien = ? ORDER BY ngay_cham_cong DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChamCong chamCong = new ChamCong(
                        rs.getInt("ma_cham_cong"),
                        rs.getInt("ma_nhan_vien"),
                        rs.getDate("ngay_cham_cong"),
                        rs.getString("gio_vao"),
                        rs.getString("gio_ra"),
                        rs.getDouble("gio_lam_them"),
                        rs.getString("trang_thai")
                );
                list.add(chamCong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Sửa thông tin chấm công
    public boolean suaChamCong(ChamCong chamCong) {
        String sql = "UPDATE ChamCong SET gio_vao = ?, gio_ra = ?, gio_lam_them = ?, trang_thai = ? WHERE ma_cham_cong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, chamCong.getGioVao());
            ps.setString(2, chamCong.getGioRa());
            ps.setDouble(3, chamCong.getGioLamThem());
            ps.setString(4, chamCong.getTrangThai());
            ps.setInt(5, chamCong.getMaChamCong());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xóa chấm công theo mã
    public boolean xoaChamCong(int maChamCong) {
        String sql = "DELETE FROM ChamCong WHERE ma_cham_cong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maChamCong);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật giờ vào hoặc giờ ra nếu đã tồn tại bản ghi, nếu chưa thì insert mới
    public boolean capNhatGio(int maNhanVien, java.sql.Date ngay, String gio, String loai) {
        String column = loai.equalsIgnoreCase("vao") ? "gio_vao" : "gio_ra";

        String sqlCheck = "SELECT * FROM ChamCong WHERE ma_nhan_vien = ? AND ngay_cham_cong = ?";
        String sqlInsert = "INSERT INTO ChamCong (ma_nhan_vien, ngay_cham_cong, gio_vao, gio_ra, gio_lam_them, trang_thai) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE ChamCong SET " + column + " = ? WHERE ma_nhan_vien = ? AND ngay_cham_cong = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, maNhanVien);
            psCheck.setDate(2, ngay);

            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                // ✅ Ngăn chấm giờ ra nếu chưa có giờ vào
                if (loai.equalsIgnoreCase("ra")) {
                    String gioVaoDb = rs.getString("gio_vao");
                    if (gioVaoDb == null || gioVaoDb.isEmpty()) {
                        return false;
                    }
                }

                // ✅ Cập nhật giờ
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                psUpdate.setString(1, gio);
                psUpdate.setInt(2, maNhanVien);
                psUpdate.setDate(3, ngay);
                return psUpdate.executeUpdate() > 0;
            } else {
                // ✅ Chưa có → thêm mới
                PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                psInsert.setInt(1, maNhanVien);
                psInsert.setDate(2, ngay);
                if (loai.equalsIgnoreCase("vao")) {
                    psInsert.setString(3, gio);  // giờ vào
                    psInsert.setNull(4, java.sql.Types.VARCHAR); // giờ ra
                } else {
                    psInsert.setNull(3, java.sql.Types.VARCHAR); // giờ vào
                    psInsert.setString(4, gio);  // giờ ra
                }
                psInsert.setDouble(5, 0.0); // giờ làm thêm
                psInsert.setString(6, "Đang làm");
                return psInsert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

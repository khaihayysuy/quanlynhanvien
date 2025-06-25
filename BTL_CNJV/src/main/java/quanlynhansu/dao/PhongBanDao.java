package quanlynhansu.dao;

import java.sql.*;
import quanlynhansu.databaseconnection.DatabaseConnection;
import quanlynhansu.dto.PhongBan;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDao {

    public List<PhongBan> layDanhSachPhongBan() {
        List<PhongBan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhongBan pb = new PhongBan(
                        rs.getInt("ma_phong_ban"),
                        rs.getString("ten_phong_ban")
                );
                danhSach.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public boolean themPhongBan(PhongBan pb) {
        String sql = "INSERT INTO PhongBan (ten_phong_ban) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pb.getTenPhongBan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatPhongBan(PhongBan pb) {
        String sql = "UPDATE PhongBan SET ten_phong_ban=? WHERE ma_phong_ban=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pb.getTenPhongBan());
            ps.setInt(2, pb.getMaPhongBan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaPhongBan(int maPhongBan) {
        String sql = "DELETE FROM PhongBan WHERE ma_phong_ban=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhongBan);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

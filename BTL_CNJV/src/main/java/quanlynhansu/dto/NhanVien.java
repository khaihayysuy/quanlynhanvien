package quanlynhansu.dto;

public class NhanVien {
    private int maNhanVien;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private int maPhongBan; // phòng ban ID (integer nhé)

    public NhanVien() {}

    public NhanVien(int maNhanVien, String hoTen, String ngaySinh, String gioiTinh, String soDienThoai, int maPhongBan) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.maPhongBan = maPhongBan;
    }

    // Getter và Setter

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(int maPhongBan) {
        this.maPhongBan = maPhongBan;
    }
}

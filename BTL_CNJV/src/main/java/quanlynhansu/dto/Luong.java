package quanlynhansu.dto;

public class Luong {
    private int maLuong;
    private int maNhanVien;
    private int thang;
    private int nam;
    private double luongCoBan;
    private double phuCap;
    private double thuong;
    private double khauTru;
    private double tongLuong;

    public Luong() {}

    // Getters và Setters
    public int getMaLuong() {
        return maLuong;
    }

    public void setMaLuong(int maLuong) {
        this.maLuong = maLuong;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public double getPhuCap() {
        return phuCap;
    }

    public void setPhuCap(double phuCap) {
        this.phuCap = phuCap;
    }

    public double getThuong() {
        return thuong;
    }

    public void setThuong(double thuong) {
        this.thuong = thuong;
    }

    public double getKhauTru() {
        return khauTru;
    }

    public void setKhauTru(double khauTru) {
        this.khauTru = khauTru;
    }

    public double getTongLuong() {
        return tongLuong;
    }

    public void setTongLuong(double tongLuong) {
        this.tongLuong = tongLuong;
    }

    // Tính tổng lương
    public void tinhTongLuong() {
        this.tongLuong = this.luongCoBan + this.phuCap + this.thuong - this.khauTru;
    }
}

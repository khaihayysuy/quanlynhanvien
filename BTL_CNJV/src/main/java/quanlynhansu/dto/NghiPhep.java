package quanlynhansu.dto;

import java.util.Date;

public class NghiPhep {
    private int maNghiPhep;
    private int maNhanVien;
    private String loaiNghiPhep;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private double soNgay;  // Đổi thành double
    private String lyDo;
    private String trangThai;

    // Getters và Setters
    public int getMaNghiPhep() {
        return maNghiPhep;
    }

    public void setMaNghiPhep(int maNghiPhep) {
        this.maNghiPhep = maNghiPhep;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getLoaiNghiPhep() {
        return loaiNghiPhep;
    }

    public void setLoaiNghiPhep(String loaiNghiPhep) {
        this.loaiNghiPhep = loaiNghiPhep;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getSoNgay() {  // Trả về kiểu double
        return soNgay;
    }

    public void setSoNgay(double soNgay) {  // Nhận vào kiểu double
        this.soNgay = soNgay;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

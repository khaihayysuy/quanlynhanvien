package quanlynhansu.dto;

import java.util.Date;

public class HoSo {
    private int maHoSo;
    private int maNhanVien;
    private String loaiHoSo;
    private String tenTaiLieu;
    private String duongDanFile;
    private Date ngayUpload;

    // Getters & Setters
    public int getMaHoSo() { return maHoSo; }
    public void setMaHoSo(int maHoSo) { this.maHoSo = maHoSo; }

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getLoaiHoSo() { return loaiHoSo; }
    public void setLoaiHoSo(String loaiHoSo) { this.loaiHoSo = loaiHoSo; }

    public String getTenTaiLieu() { return tenTaiLieu; }
    public void setTenTaiLieu(String tenTaiLieu) { this.tenTaiLieu = tenTaiLieu; }

    public String getDuongDanFile() { return duongDanFile; }
    public void setDuongDanFile(String duongDanFile) { this.duongDanFile = duongDanFile; }

    public Date getNgayUpload() { return ngayUpload; }
    public void setNgayUpload(Date ngayUpload) { this.ngayUpload = ngayUpload; }
}
package quanlynhansu.dto;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

public class ChamCong {
    private int maChamCong;
    private int maNhanVien;
    private Date ngayChamCong;
    private String gioVao;
    private String gioRa;
    private double gioLamThem;
    private String trangThai;

    public ChamCong(int maChamCong, int maNhanVien, Date ngayChamCong, String gioVao, String gioRa, double gioLamThem, String trangThai) {
        this.maChamCong = maChamCong;
        this.maNhanVien = maNhanVien;
        this.ngayChamCong = ngayChamCong;
        this.gioVao = gioVao;
        this.gioRa = gioRa;
        this.gioLamThem = gioLamThem;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaChamCong() {
        return maChamCong;
    }

    public void setMaChamCong(int maChamCong) {
        this.maChamCong = maChamCong;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayChamCong() {
        return ngayChamCong;
    }

    public void setNgayChamCong(Date ngayChamCong) {
        this.ngayChamCong = ngayChamCong;
    }

    public String getGioVao() {
        return gioVao;
    }

    public void setGioVao(String gioVao) {
        this.gioVao = gioVao;
    }

    public String getGioRa() {
        return gioRa;
    }

    public void setGioRa(String gioRa) {
        this.gioRa = gioRa;
    }

    public double getGioLamThem() {
        return gioLamThem;
    }

    public void setGioLamThem(double gioLamThem) {
        this.gioLamThem = gioLamThem;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    
    public double tinhGioLam() {
        try {
            if (gioVao != null && gioRa != null && gioVao.length() >= 5 && gioRa.length() >= 5) {
                LocalTime vao = LocalTime.parse(gioVao);
                LocalTime ra = LocalTime.parse(gioRa);
                long minutes = Duration.between(vao, ra).toMinutes();
                return minutes > 0 ? minutes / 60.0 : 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}

package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.ChamCongDao;
import quanlynhansu.dto.ChamCong;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChamCongNhanVienPanel extends JPanel {
    private JLabel lbNgay, lbGioVao, lbGioRa;
    private JButton btnChamGioVao, btnChamGioRa;
    private JTable table;
    private DefaultTableModel model;
    private ChamCongDao chamCongDao = new ChamCongDao();
    private int maNhanVien;

    public ChamCongNhanVienPanel(int maNhanVien) {
        this.maNhanVien = maNhanVien;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);

        lbNgay = new JLabel("Ngày hôm nay: " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        lbGioVao = new JLabel("Giờ vào: Chưa chấm");
        lbGioRa = new JLabel("Giờ ra: Chưa chấm");

        lbNgay.setFont(labelFont);
        lbGioVao.setFont(labelFont);
        lbGioRa.setFont(labelFont);

        infoPanel.add(lbNgay);
        infoPanel.add(lbGioVao);
        infoPanel.add(lbGioRa);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnChamGioVao = new JButton("Chấm Giờ Vào");
        btnChamGioRa = new JButton("Chấm Giờ Ra");

        btnChamGioVao.setFont(buttonFont);
        btnChamGioRa.setFont(buttonFont);
        btnChamGioVao.setBackground(new Color(54, 209, 153));
        btnChamGioRa.setBackground(new Color(54, 144, 209));
        btnChamGioVao.setForeground(Color.WHITE);
        btnChamGioRa.setForeground(Color.WHITE);

        buttonPanel.add(btnChamGioVao);
        buttonPanel.add(btnChamGioRa);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Bảng lịch sử chấm công
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Ngày", "Giờ vào", "Giờ ra", "Giờ làm", "Giờ làm thêm", "Trạng thái"});

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnChamGioVao.addActionListener(e -> chamGio("vao"));
        btnChamGioRa.addActionListener(e -> chamGio("ra"));

        loadLichSuChamCong();
    }

    private void chamGio(String type) {
        String gioHienTai = new SimpleDateFormat("HH:mm:ss").format(new Date());
        java.sql.Date ngay = new java.sql.Date(new Date().getTime());

        // Kiểm tra xem đã có bản ghi hôm nay chưa
        List<ChamCong> list = chamCongDao.layDanhSachChamCongTheoNV(maNhanVien);
        ChamCong today = null;
        for (ChamCong cc : list) {
            String ngayDB = new SimpleDateFormat("dd-MM-yyyy").format(cc.getNgayChamCong());
            String ngayNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            if (ngayDB.equals(ngayNow)) {
                today = cc;
                break;
            }
        }

        // Tránh chấm lại giờ vào hoặc giờ ra nếu đã có
        if (type.equals("vao")) {
            if (today != null && today.getGioVao() != null) {
                JOptionPane.showMessageDialog(this, "Hôm nay bạn đã chấm giờ vào rồi!");
                return;
            }
        } else {
            if (today != null && today.getGioRa() != null) {
                JOptionPane.showMessageDialog(this, "Hôm nay bạn đã chấm giờ ra rồi!");
                return;
            }
        }

        // Cập nhật giờ vào/ra
        boolean result = chamCongDao.capNhatGio(maNhanVien, ngay, gioHienTai, type);

        if (result) {
            if (type.equals("vao")) {
                lbGioVao.setText("Giờ vào: " + gioHienTai);
                btnChamGioVao.setEnabled(false);
            } else {
                lbGioRa.setText("Giờ ra: " + gioHienTai);
                btnChamGioRa.setEnabled(false);

                // Sau khi có giờ ra, tính giờ làm
                ChamCong updated = chamCongDao.layDanhSachChamCongTheoNV(maNhanVien)
                        .stream().filter(c -> new SimpleDateFormat("dd-MM-yyyy").format(c.getNgayChamCong())
                        .equals(new SimpleDateFormat("dd-MM-yyyy").format(new Date())))
                        .findFirst().orElse(null);

                if (updated != null && updated.getGioVao() != null && updated.getGioRa() != null) {
                    double gioLam = updated.tinhGioLam();
                    double gioLamThem = gioLam > 8.0 ? gioLam - 8.0 : 0.0;

                    updated.setGioLamThem(gioLamThem);
                    updated.setTrangThai("Đã về");

                    chamCongDao.suaChamCong(updated);
                }
            }

            JOptionPane.showMessageDialog(this, "Chấm công thành công!");
            loadLichSuChamCong();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi chấm công!");
        }
    }

    private void loadLichSuChamCong() {
        model.setRowCount(0);
        List<ChamCong> list = chamCongDao.layDanhSachChamCongTheoNV(maNhanVien);
        SimpleDateFormat dfNgay = new SimpleDateFormat("dd-MM-yyyy");
        for (ChamCong cc : list) {
            String ngay = dfNgay.format(cc.getNgayChamCong());
            String vao = (cc.getGioVao() != null && cc.getGioVao().length() >= 8)
                    ? cc.getGioVao().substring(0, 8) : "";

            String ra = (cc.getGioRa() != null && cc.getGioRa().length() >= 8)
                    ? cc.getGioRa().substring(0, 8) : "";

            String gioLam = String.format("%.2f", cc.tinhGioLam());
            String gioThem = String.format("%.2f", cc.getGioLamThem());
            model.addRow(new Object[]{ngay, vao, ra, gioLam, gioThem, cc.getTrangThai()});
        }
    }
}
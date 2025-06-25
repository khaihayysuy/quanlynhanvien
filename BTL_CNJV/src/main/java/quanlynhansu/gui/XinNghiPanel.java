package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.NghiPhepDao;
import quanlynhansu.dto.NghiPhep;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class XinNghiPanel extends JPanel {
    private JTextField tfLoaiNghi, tfNgayBatDau, tfNgayKetThuc, tfLyDo;
    private JButton btnGuiDon;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTrangThaiDon;
    private NghiPhepDao dao = new NghiPhepDao();
    private int maNhanVien;

    public XinNghiPanel(int maNhanVien) {
        this.maNhanVien = maNhanVien;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("📋 XIN NGHỈ PHÉP", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        tfLoaiNghi = new JTextField();
        tfNgayBatDau = new JTextField();
        tfNgayKetThuc = new JTextField();
        tfLyDo = new JTextField();
        btnGuiDon = new JButton("Gửi đơn xin nghỉ");
        lblTrangThaiDon = new JLabel("");

        formPanel.add(new JLabel("Loại nghỉ phép:")); formPanel.add(tfLoaiNghi);
        formPanel.add(new JLabel("Ngày bắt đầu:")); formPanel.add(tfNgayBatDau);
        formPanel.add(new JLabel("Ngày kết thúc:")); formPanel.add(tfNgayKetThuc);
        formPanel.add(new JLabel("Lý do nghỉ:")); formPanel.add(tfLyDo);
        formPanel.add(new JLabel("Trạng thái đơn:")); formPanel.add(lblTrangThaiDon);
        formPanel.add(new JLabel("")); formPanel.add(btnGuiDon);

        add(formPanel, BorderLayout.NORTH);

        // Bảng hiển thị lịch sử nghỉ phép
        model = new DefaultTableModel(new Object[]{"Loại", "Từ ngày", "Đến ngày", "Số ngày", "Lý do", "Trạng thái"}, 0);
        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnGuiDon.addActionListener(e -> guiDonXinNghi());
        loadLichSu();
    }

    private void guiDonXinNghi() {
        try {
            if (tfLoaiNghi.getText().isEmpty() || tfLyDo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date ngayBD, ngayKT;
            try {
                ngayBD = sdf.parse(tfNgayBatDau.getText());
                ngayKT = sdf.parse(tfNgayKetThuc.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! (dd-MM-yyyy)");
                return;
            }

            long diff = ngayKT.getTime() - ngayBD.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diff) + 1;

            if (days <= 0) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            }

            NghiPhep np = new NghiPhep();
            np.setMaNhanVien(maNhanVien);
            np.setLoaiNghiPhep(tfLoaiNghi.getText());
            np.setNgayBatDau(ngayBD);
            np.setNgayKetThuc(ngayKT);
            np.setSoNgay((double) days);
            np.setLyDo(tfLyDo.getText());
            np.setTrangThai("Chờ duyệt");

            if (dao.themNghiPhep(np)) {
                JOptionPane.showMessageDialog(this, "Gửi đơn thành công!");
                lblTrangThaiDon.setText("Trạng thái: " + np.getTrangThai());
                tfLoaiNghi.setText(""); tfNgayBatDau.setText(""); tfNgayKetThuc.setText(""); tfLyDo.setText("");
                loadLichSu();
            } else {
                JOptionPane.showMessageDialog(this, "Gửi đơn thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xử lý dữ liệu!");
        }
    }

    private void loadLichSu() {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<NghiPhep> ds = dao.layDanhSachNghiPhep();
        for (NghiPhep np : ds) {
            if (np.getMaNhanVien() == maNhanVien) {
                model.addRow(new Object[]{
                        np.getLoaiNghiPhep(),
                        sdf.format(np.getNgayBatDau()),
                        sdf.format(np.getNgayKetThuc()),
                        np.getSoNgay(),
                        np.getLyDo(),
                        np.getTrangThai()
                });
            }
        }
    }
}
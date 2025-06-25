package quanlynhansu.gui;

import javax.swing.*;
import quanlynhansu.dao.NhanVienDao;
import quanlynhansu.dto.NhanVien;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class QuanLyNhanVienPanel extends JPanel {

    // Bảng hiển thị danh sách nhân viên
    private JTable table;

    // Model quản lý dữ liệu cho bảng
    private DefaultTableModel model;

    // DAO để truy xuất dữ liệu từ CSDL
    private NhanVienDao dao = new NhanVienDao();

    // Định dạng ngày nhập và hiển thị
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("MM-dd-yyyy");

    // Constructor
    public QuanLyNhanVienPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        initUI();     // Tạo giao diện
        loadData();   // Nạp dữ liệu ban đầu
    }

    // Hàm tạo các thành phần giao diện
    private void initUI() {
        // Tiêu đề chính
        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JTextField tfTimMaNV = new JTextField(10); // Ô nhập mã NV
        JButton btnTim = new JButton("Tìm");       // Nút tìm kiếm

        searchPanel.add(new JLabel("Mã NV:"));
        searchPanel.add(tfTimMaNV);
        searchPanel.add(btnTim);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Xử lý khi nhấn nút "Tìm"
        btnTim.addActionListener(e -> {
            String input = tfTimMaNV.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int maNV = Integer.parseInt(input);
                    loadDataTheoMaNV(maNV); // Tìm theo mã NV
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Mã NV phải là số!");
                }
            } else {
                loadData(); // Nếu để trống, tải toàn bộ
            }
        });

        // Cấu hình cột của bảng
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Mã NV", "Tên NV", "Ngày sinh", "Giới tính", "SĐT", "Mã phòng ban"});

        // Tạo bảng hiển thị
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Tô màu xen kẽ dòng trong bảng
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                } else {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        for (JButton btn : new JButton[]{btnAdd, btnEdit, btnDelete, btnRefresh}) {
            btn.setFont(btnFont);
            btn.setPreferredSize(new Dimension(120, 35));
        }

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);

        // Gán chức năng cho các nút
        btnAdd.addActionListener(e -> themNhanVien());
        btnEdit.addActionListener(e -> suaNhanVien());
        btnDelete.addActionListener(e -> xoaNhanVien());
        btnRefresh.addActionListener(e -> loadData());
    }

    // Tải toàn bộ danh sách nhân viên từ DB
    private void loadData() {
        model.setRowCount(0); // Xóa dữ liệu cũ
        List<NhanVien> danhSach = dao.layDanhSachNhanVien();
        for (NhanVien nv : danhSach) {
            String ngaySinhStr = nv.getNgaySinh();
            try {
                Date ngay = inputFormat.parse(ngaySinhStr);
                ngaySinhStr = displayFormat.format(ngay); // Đổi định dạng hiển thị
            } catch (ParseException ignored) {
                ngaySinhStr = nv.getNgaySinh(); // Giữ nguyên nếu lỗi
            }

            // Thêm hàng vào bảng
            model.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getHoTen(),
                    ngaySinhStr,
                    nv.getGioiTinh(),
                    nv.getSoDienThoai(),
                    nv.getMaPhongBan()
            });
        }
    }

    // Tải dữ liệu nhân viên theo mã
    private void loadDataTheoMaNV(int maNV) {
        model.setRowCount(0);
        List<NhanVien> danhSach = dao.layDanhSachNhanVien();
        for (NhanVien nv : danhSach) {
            if (nv.getMaNhanVien() == maNV) {
                String ngaySinhStr = nv.getNgaySinh();
                try {
                    Date ngay = inputFormat.parse(ngaySinhStr);
                    ngaySinhStr = displayFormat.format(ngay);
                } catch (ParseException ignored) {}

                model.addRow(new Object[]{
                        nv.getMaNhanVien(),
                        nv.getHoTen(),
                        ngaySinhStr,
                        nv.getGioiTinh(),
                        nv.getSoDienThoai(),
                        nv.getMaPhongBan()
                });
                return; // Dừng khi đã tìm thấy
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên với mã " + maNV);
    }

 // Hàm xử lý thêm nhân viên mới
    private void themNhanVien() {
        JTextField tfTen = new JTextField();
        JTextField tfNgaySinh = new JTextField();
        JTextField tfGioiTinh = new JTextField();
        JTextField tfSDT = new JTextField();
        JTextField tfPhongBan = new JTextField();

        // Form nhập liệu
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 220));
        panel.add(new JLabel("Tên NV:"));
        panel.add(tfTen);
        panel.add(new JLabel("Ngày sinh (dd-MM-yyyy):"));
        panel.add(tfNgaySinh);
        panel.add(new JLabel("Giới tính:"));
        panel.add(tfGioiTinh);
        panel.add(new JLabel("SĐT:"));
        panel.add(tfSDT);
        panel.add(new JLabel("Mã phòng ban:"));
        panel.add(tfPhongBan);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Nhân Viên", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Kiểm tra các trường nhập liệu có bị bỏ trống không
            if (tfTen.getText().trim().isEmpty() ||
                tfNgaySinh.getText().trim().isEmpty() ||
                tfGioiTinh.getText().trim().isEmpty() ||
                tfSDT.getText().trim().isEmpty() ||
                tfPhongBan.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return; // Dừng lại nếu thiếu dữ liệu
            }

            // Tạo đối tượng nhân viên từ dữ liệu nhập
            NhanVien nv = new NhanVien();
            nv.setHoTen(tfTen.getText());
            nv.setNgaySinh(tfNgaySinh.getText());
            nv.setGioiTinh(tfGioiTinh.getText());
            nv.setSoDienThoai(tfSDT.getText());
            nv.setMaPhongBan(Integer.parseInt(tfPhongBan.getText()));

            if (dao.themNhanVien(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData(); // Tải lại bảng sau khi thêm
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        }
    }


    // Hàm xử lý sửa thông tin nhân viên
    private void suaNhanVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên cần sửa!");
            return;
        }

        // Lấy thông tin từ bảng
        int maNV = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        JTextField tfTen = new JTextField(model.getValueAt(selectedRow, 1).toString());
        JTextField tfNgaySinh = new JTextField(model.getValueAt(selectedRow, 2).toString());
        JTextField tfGioiTinh = new JTextField(model.getValueAt(selectedRow, 3).toString());
        JTextField tfSDT = new JTextField(model.getValueAt(selectedRow, 4).toString());
        JTextField tfPhongBan = new JTextField(model.getValueAt(selectedRow, 5).toString());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setPreferredSize(new Dimension(400, 220));
        panel.add(new JLabel("Tên NV:"));
        panel.add(tfTen);
        panel.add(new JLabel("Ngày sinh (dd-MM-yyyy):"));
        panel.add(tfNgaySinh);
        panel.add(new JLabel("Giới tính:"));
        panel.add(tfGioiTinh);
        panel.add(new JLabel("SĐT:"));
        panel.add(tfSDT);
        panel.add(new JLabel("Mã phòng ban:"));
        panel.add(tfPhongBan);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Nhân Viên", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(maNV);
            nv.setHoTen(tfTen.getText());
            nv.setNgaySinh(tfNgaySinh.getText());
            nv.setGioiTinh(tfGioiTinh.getText());
            nv.setSoDienThoai(tfSDT.getText());
            nv.setMaPhongBan(Integer.parseInt(tfPhongBan.getText()));

            if (dao.capNhatNhanVien(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    // Hàm xử lý xóa nhân viên
    private void xoaNhanVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên cần xóa!");
            return;
        }

        int maNV = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.xoaNhanVien(maNV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
}

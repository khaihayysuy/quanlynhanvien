package quanlynhansu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.sql.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import quanlynhansu.databaseconnection.DatabaseConnection;

public class QuanLyBaoCaoThongKePanel extends JPanel {

    static {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createCard(String text, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 15, 5, 15),
            BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        textLabel.setForeground(new Color(30, 41, 59));
        panel.add(textLabel, BorderLayout.CENTER);

        return panel;
    }

    public QuanLyBaoCaoThongKePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("BÁO CÁO THỐNG KÊ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(25, 35, 50));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JPanel cardNV = createCard("Tổng số nhân viên đang làm việc: ...", new Color(224, 255, 255));
        JPanel cardNP = createCard("Tổng số đơn nghỉ phép: ...", new Color(255, 248, 220));
        JPanel cardLuong = createCard("Tổng số bảng lương: ...", new Color(250, 240, 230));
        JPanel cardLuongThang = createCard("Bảng lương tháng này: ...", new Color(245, 245, 255));

        JLabel[] labelArray = {
            (JLabel) cardNV.getComponent(0),
            (JLabel) cardNP.getComponent(0),
            (JLabel) cardLuong.getComponent(0),
            (JLabel) cardLuongThang.getComponent(0)
        };

        contentPanel.add(cardNV);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(cardNP);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(cardLuong);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(cardLuongThang);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(contentPanel, BorderLayout.NORTH);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();

            ResultSet rsNV = stmt.executeQuery("SELECT COUNT(*) FROM NhanVien WHERE trang_thai = 'Dang lam viec'");
            if (rsNV.next()) {
                int count = rsNV.getInt(1);
                labelArray[0].setText("Tổng số nhân viên đang làm việc: " + count);
                dataset.addValue(count, "Số lượng", "Nhân viên");
            }

            ResultSet rsNP = stmt.executeQuery("SELECT COUNT(*) FROM NghiPhep");
            if (rsNP.next()) {
                int count = rsNP.getInt(1);
                labelArray[1].setText("Tổng số đơn nghỉ phép: " + count);
                dataset.addValue(count, "Số lượng", "Đơn nghỉ phép");
            }

            ResultSet rsLuong = stmt.executeQuery("SELECT COUNT(*) FROM Luong");
            if (rsLuong.next()) {
                int count = rsLuong.getInt(1);
                labelArray[2].setText("Tổng số bảng lương: " + count);
                dataset.addValue(count, "Số lượng", "Tổng bảng lương");
            }

            ResultSet rsLuongThang = stmt.executeQuery("SELECT COUNT(*) FROM Luong WHERE thang = MONTH(GETDATE()) AND nam = YEAR(GETDATE())");
            if (rsLuongThang.next()) {
                int count = rsLuongThang.getInt(1);
                labelArray[3].setText("Bảng lương tháng này: " + count);
                dataset.addValue(count, "Số lượng", "Bảng lương tháng này");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            "Thống kê tổng quan",
            "Danh mục",
            "Số lượng",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189));

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 300));
        chartPanel.setBackground(Color.WHITE);

        centerPanel.add(chartPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JButton btnExportExcel = new JButton("In thống kê ra Excel");
        btnExportExcel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExportExcel.setBackground(new Color(30, 144, 255));
        btnExportExcel.setForeground(Color.WHITE);
        btnExportExcel.setFocusPainted(false);
        btnExportExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportExcel.setPreferredSize(new Dimension(220, 45));
        btnExportExcel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnExportExcel.setContentAreaFilled(true);
        btnExportExcel.setOpaque(true);
        btnExportExcel.setBorderPainted(false);

        btnExportExcel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnExportExcel.setBackground(new Color(25, 135, 255));
            }

            public void mouseExited(MouseEvent evt) {
                btnExportExcel.setBackground(new Color(30, 144, 255));
            }
        });

        btnExportExcel.addActionListener(e -> exportToExcel(labelArray));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(btnExportExcel);

        add(buttonWrapper, BorderLayout.SOUTH); // Đưa nút xuống dưới cùng
    }

    private void exportToExcel(JLabel[] labelArray) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("ThongKe");

                String[] headers = {
                    "Tổng số nhân viên đang làm việc",
                    "Tổng số đơn nghỉ phép",
                    "Tổng số bảng lương",
                    "Bảng lương tháng này"
                };

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                Row dataRow = sheet.createRow(1);
                for (int i = 0; i < labelArray.length; i++) {
                    String text = labelArray[i].getText();
                    String value = text.replaceAll("[^0-9]", "");
                    try {
                        int number = Integer.parseInt(value);
                        dataRow.createCell(i).setCellValue(number);
                    } catch (NumberFormatException ex) {
                        dataRow.createCell(i).setCellValue("N/A");
                    }
                }

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(this, "Đã in thống kê ra Excel!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

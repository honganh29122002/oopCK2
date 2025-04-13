package model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class HocVienDAO {
	
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String USER = "oop";
    private static final String PASS = "123";

    public List<HocVien> getAllHocVien() {
        List<HocVien> list = new ArrayList<>();
        
        try {
            // Load driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Tạo kết nối
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT MA_HV, TEN_HV, NGAY_SINH, EMAIL FROM HOCVIEN")) {

                while (rs.next()) {
                    HocVien hv = new HocVien();
                    hv.setMaHV(rs.getString("MA_HV"));
                    hv.setTenHV(rs.getString("TEN_HV"));
                    hv.setNgaySinh(rs.getDate("NGAY_SINH"));
                    hv.setEmail(rs.getString("EMAIL"));
                    list.add(hv);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver JDBC Oracle");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database");
            e.printStackTrace();
        }
        return list;
    }
    public boolean deleteHocVien(String maHV) {
        String sql = "DELETE FROM HOCVIEN WHERE MA_HV = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHV);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
      
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Mới thêm
    public void themHocVien(HocVien hv) {
        String sql = "INSERT INTO HOCVIEN (MA_HV, TEN_HV, NGAY_SINH, EMAIL) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, hv.getMaHV());
        	pstmt.setString(2, hv.getTenHV());
        	pstmt.setDate(3, new java.sql.Date(hv.getNgaySinh().getTime()));//ep kieu
        	pstmt.setString(4, hv.getEmail());
        	pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    //Tim hoc vien
    public HocVien timHocVienTheoMa(String maHV) {
        String sql = "SELECT MA_HV, TEN_HV, NGAY_SINH, EMAIL FROM HOCVIEN WHERE MA_HV = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHV);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HocVien hv = new HocVien();
                    hv.setMaHV(rs.getString("MA_HV"));
                    hv.setTenHV(rs.getString("TEN_HV"));
                    hv.setNgaySinh(rs.getDate("NGAY_SINH"));
                    hv.setEmail(rs.getString("EMAIL"));
                    return hv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }
    public void updateHocVien(HocVien hv) {
        String sql = "UPDATE HOCVIEN SET TEN_HV = ?, NGAY_SINH = ?, EMAIL = ? WHERE MA_HV = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hv.getTenHV());
            pstmt.setDate(2, new java.sql.Date(hv.getNgaySinh().getTime()));
            pstmt.setString(3, hv.getEmail());
            pstmt.setString(4, hv.getMaHV());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
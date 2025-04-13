package model;

import java.util.Date;

public class HocVien {
    private String maHV;
    private String tenHV;
    private Date ngaySinh;
    private String email;

    public HocVien() {
    }

    public HocVien(String maHV, String tenHV, Date ngaySinh, String email) {
        this.maHV = maHV;
        this.tenHV = tenHV;
        this.ngaySinh = ngaySinh;
        this.email = email;
    }

    // Getter và Setter
    public String getMaHV() {
        return maHV;
    }

    public void setMaHV(String maHV) {
        this.maHV = maHV;
    }

    public String getTenHV() {
        return tenHV;
    }

    public void setTenHV(String tenHV) {
        this.tenHV = tenHV;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
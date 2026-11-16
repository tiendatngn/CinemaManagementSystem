package model;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Lớp thực thể Movie9 - Đại diện cho Bộ phim
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Movie9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String ten;
    private int thoiLuong;
    private String theLoai;
    private Date khoiChieu;
    private String moTa;
    private String anhPoster;

    public Movie9() {
    }

    public Movie9(int id, String ten, int thoiLuong, String theLoai, Date khoiChieu, String moTa, String anhPoster) {
        this.id = id;
        this.ten = ten;
        this.thoiLuong = thoiLuong;
        this.theLoai = theLoai;
        this.khoiChieu = khoiChieu;
        this.moTa = moTa;
        this.anhPoster = anhPoster;
    }

    public Movie9(String ten, int thoiLuong, String theLoai, Date khoiChieu, String moTa, String anhPoster) {
        this.ten = ten;
        this.thoiLuong = thoiLuong;
        this.theLoai = theLoai;
        this.khoiChieu = khoiChieu;
        this.moTa = moTa;
        this.anhPoster = anhPoster;
    }

    public Movie9(String ten, int thoiLuong, String theLoai, Date khoiChieu, String moTa) {
        this(ten, thoiLuong, theLoai, khoiChieu, moTa, "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaPhim() {
        return String.format("MV%02d", id);
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public Date getKhoiChieu() {
        return khoiChieu;
    }

    public void setKhoiChieu(Date khoiChieu) {
        this.khoiChieu = khoiChieu;
    }

    public String getKhoiChieuFormatted() {
        if (khoiChieu == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(khoiChieu);
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnhPoster() {
        return anhPoster;
    }

    public void setAnhPoster(String anhPoster) {
        this.anhPoster = anhPoster;
    }

    @Override
    public String toString() {
        return "Movie9{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                ", thoiLuong=" + thoiLuong +
                ", theLoai='" + theLoai + '\'' +
                ", khoiChieu=" + khoiChieu +
                ", moTa='" + moTa + '\'' +
                ", anhPoster='" + anhPoster + '\'' +
                '}';
    }
}

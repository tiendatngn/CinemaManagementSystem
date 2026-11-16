package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * Lớp thực thể Showtime9 - Suất chiếu
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Showtime9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Date ngayChieu;
    private Time batDau;
    private Time ketThuc;
    private String moTa;
    private Theater9 phongChieu;
    private Movie9 phim;

    public Showtime9() {
    }

    public Showtime9(int id, Date ngayChieu, Time batDau, Time ketThuc, String moTa, Theater9 phongChieu, Movie9 phim) {
        this.id = id;
        this.ngayChieu = ngayChieu;
        this.batDau = batDau;
        this.ketThuc = ketThuc;
        this.moTa = moTa;
        this.phongChieu = phongChieu;
        this.phim = phim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(Date ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getNgayChieuFormatted() {
        if (ngayChieu == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(ngayChieu);
    }

    public Time getBatDau() {
        return batDau;
    }

    public void setBatDau(Time batDau) {
        this.batDau = batDau;
    }

    public String getBatDauFormatted() {
        if (batDau == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(batDau);
    }

    public Time getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(Time ketThuc) {
        this.ketThuc = ketThuc;
    }

    public String getKetThucFormatted() {
        if (ketThuc == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(ketThuc);
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Theater9 getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(Theater9 phongChieu) {
        this.phongChieu = phongChieu;
    }

    public Movie9 getPhim() {
        return phim;
    }

    public void setPhim(Movie9 phim) {
        this.phim = phim;
    }
}

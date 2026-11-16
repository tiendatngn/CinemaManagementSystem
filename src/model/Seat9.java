package model;

import java.io.Serializable;

/**
 * Lớp thực thể Seat9 - Ghế ngồi trong phòng chiếu
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Seat9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String ten;
    private String loai;
    private String moTa;
    private Theater9 theater;

    public Seat9() {
    }

    public Seat9(int id, String ten, String loai, String moTa) {
        this.id = id;
        this.ten = ten;
        this.loai = loai;
        this.moTa = moTa;
    }

    public Seat9(int id, String ten, String loai, String moTa, Theater9 theater) {
        this.id = id;
        this.ten = ten;
        this.loai = loai;
        this.moTa = moTa;
        this.theater = theater;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Theater9 getTheater() {
        return theater;
    }

    public void setTheater(Theater9 theater) {
        this.theater = theater;
    }
}

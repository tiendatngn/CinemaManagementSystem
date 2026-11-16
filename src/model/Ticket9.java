package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Lớp thực thể Ticket9 - Vé xem phim
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Ticket9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private float gia;
    private Timestamp tgTao;
    private String trangThai; // "Chưa đặt", "Đang giữ", "Đã bán"
    private Seat9 ghe;
    private Showtime9 suatChieu;

    public Ticket9() {
    }

    public Ticket9(int id, float gia, Timestamp tgTao, String trangThai, Seat9 ghe, Showtime9 suatChieu) {
        this.id = id;
        this.gia = gia;
        this.tgTao = tgTao;
        this.trangThai = trangThai;
        this.ghe = ghe;
        this.suatChieu = suatChieu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public Timestamp getTgTao() {
        return tgTao;
    }

    public void setTgTao(Timestamp tgTao) {
        this.tgTao = tgTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Seat9 getGhe() {
        return ghe;
    }

    public void setGhe(Seat9 ghe) {
        this.ghe = ghe;
    }

    public Showtime9 getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(Showtime9 suatChieu) {
        this.suatChieu = suatChieu;
    }
}

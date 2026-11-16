package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Lớp thực thể Theater9 - Phòng chiếu
 * Thiết kế theo Biểu đồ lớp chi tiết Pha 3
 */
public class Theater9 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String ten;
    private String loai;
    private int sucChua;
    private String moTa;
    private ArrayList<Seat9> dsGhe;

    public Theater9() {
        dsGhe = new ArrayList<>();
    }

    public Theater9(int id, String ten, String loai, int sucChua, String moTa) {
        this.id = id;
        this.ten = ten;
        this.loai = loai;
        this.sucChua = sucChua;
        this.moTa = moTa;
        this.dsGhe = new ArrayList<>();
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

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public ArrayList<Seat9> getDsGhe() {
        return dsGhe;
    }

    public void setDsGhe(ArrayList<Seat9> dsGhe) {
        this.dsGhe = dsGhe;
    }
}

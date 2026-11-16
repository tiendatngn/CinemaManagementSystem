-- ========================================================
-- Cơ sở dữ liệu: tblCinemaDB (Hệ thống quản lý rạp chiếu phim)
-- Đề tài số 13 - Môn: Phân tích & Thiết kế HTTT (PTTKHT)
-- Thiết kế CSDL chuẩn theo Báo cáo Pha 3 (Hình 3.1 & CSDL)
-- ========================================================

CREATE DATABASE IF NOT EXISTS tblCinemaDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tblCinemaDB;

-- 1. Bảng tblMember9 (Thành viên hệ thống)
CREATE TABLE IF NOT EXISTS tblMember9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hoTen VARCHAR(255) NOT NULL,
    ngaySinh DATE,
    diaChi VARCHAR(255),
    email VARCHAR(255),
    sdt VARCHAR(255),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Bảng tblStaff9 (Nhân viên - Quản lý và Bán vé)
CREATE TABLE IF NOT EXISTS tblStaff9 (
    tblMember9id INT PRIMARY KEY,
    viTri VARCHAR(255) NOT NULL,
    CONSTRAINT fk_staff_member FOREIGN KEY (tblMember9id) REFERENCES tblMember9(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Bảng tblCustomer9 (Khách hàng & Thẻ thành viên)
CREATE TABLE IF NOT EXISTS tblCustomer9 (
    tblMember9id INT PRIMARY KEY,
    maThe VARCHAR(50) UNIQUE,
    ngayPhatHanh DATE,
    diemTL INT DEFAULT 0,
    hang VARCHAR(255),
    trangThai VARCHAR(255),
    CONSTRAINT fk_customer_member FOREIGN KEY (tblMember9id) REFERENCES tblMember9(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Bảng tblMovie9 (Bộ phim)
CREATE TABLE IF NOT EXISTS tblMovie9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten VARCHAR(255) NOT NULL UNIQUE,
    thoiLuong INT NOT NULL,
    theLoai VARCHAR(255) NOT NULL,
    khoiChieu DATE NOT NULL,
    moTa VARCHAR(255),
    anhPoster VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Bảng tblTheater9 (Phòng chiếu)
CREATE TABLE IF NOT EXISTS tblTheater9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten VARCHAR(255) NOT NULL,
    loai VARCHAR(255),
    sucChua INT,
    moTa VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Bảng tblSeat9 (Ghế ngồi trong phòng chiếu)
CREATE TABLE IF NOT EXISTS tblSeat9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten VARCHAR(255) NOT NULL,
    loai VARCHAR(255),
    moTa VARCHAR(255),
    tblTheater9id INT NOT NULL,
    CONSTRAINT fk_seat_theater FOREIGN KEY (tblTheater9id) REFERENCES tblTheater9(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Bảng tblShowtime9 (Suất chiếu)
CREATE TABLE IF NOT EXISTS tblShowtime9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ngayChieu DATE NOT NULL,
    batDau TIME NOT NULL,
    ketThuc TIME NOT NULL,
    moTa VARCHAR(255),
    tblTheater9id INT NOT NULL,
    tblMovie9id INT NOT NULL,
    CONSTRAINT fk_showtime_theater FOREIGN KEY (tblTheater9id) REFERENCES tblTheater9(id) ON DELETE CASCADE,
    CONSTRAINT fk_showtime_movie FOREIGN KEY (tblMovie9id) REFERENCES tblMovie9(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Bảng tblBill9 (Hóa đơn đặt vé)
CREATE TABLE IF NOT EXISTS tblBill9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ngayLap TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tienGoc DECIMAL(12,2) NOT NULL,
    giamGia DECIMAL(12,2) DEFAULT 0,
    ptt VARCHAR(50),
    loai VARCHAR(50),
    tblStaff9id INT,
    tblCustomer9id INT,
    CONSTRAINT fk_bill_staff FOREIGN KEY (tblStaff9id) REFERENCES tblStaff9(tblMember9id) ON DELETE SET NULL,
    CONSTRAINT fk_bill_customer FOREIGN KEY (tblCustomer9id) REFERENCES tblCustomer9(tblMember9id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Bảng tblTicket9 (Vé xem phim)
CREATE TABLE IF NOT EXISTS tblTicket9 (
    id INT AUTO_INCREMENT PRIMARY KEY,
    gia DECIMAL(12,2) NOT NULL,
    tgTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trangThai VARCHAR(255),
    tblBill9id INT,
    tblSeat9id INT NOT NULL,
    tblShowtime9id INT NOT NULL,
    CONSTRAINT fk_ticket_bill FOREIGN KEY (tblBill9id) REFERENCES tblBill9(id) ON DELETE SET NULL,
    CONSTRAINT fk_ticket_seat FOREIGN KEY (tblSeat9id) REFERENCES tblSeat9(id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_showtime FOREIGN KEY (tblShowtime9id) REFERENCES tblShowtime9(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================================
-- DỮ LIỆU MẪU THEO BÁO CÁO PHA 1, 2, 3
-- ========================================================

-- 1. Thành viên & Nhân viên quản lý
INSERT INTO tblMember9 (id, hoTen, ngaySinh, diaChi, email, sdt, username, password) VALUES
(1, 'Nguyễn Văn Quản Lý', '1990-01-15', 'Hà Nội', 'quanly@cinema.com', '0912345678', 'admin', 'admin123'),
(2, 'Trần Khách Hàng', '1998-05-20', 'Hà Nội', 'khachhang@gmail.com', '0988776655', 'customer', 'customer123')
ON DUPLICATE KEY UPDATE hoTen=VALUES(hoTen);

INSERT INTO tblStaff9 (tblMember9id, viTri) VALUES
(1, 'Quản lý rạp')
ON DUPLICATE KEY UPDATE viTri=VALUES(viTri);

INSERT INTO tblCustomer9 (tblMember9id, maThe, ngayPhatHanh, diemTL, hang, trangThai) VALUES
(2, 'CARD001', '2025-01-10', 150, 'VIP', 'Hoạt động')
ON DUPLICATE KEY UPDATE maThe=VALUES(maThe), diemTL=VALUES(diemTL);

-- 2. Dữ liệu phim (Hộ linh tráng sĩ, Lên Hương, Trại Buôn Người, Người Mẹ Khác, Hope: Vùng tử địa)
INSERT INTO tblMovie9 (id, ten, thoiLuong, theLoai, khoiChieu, moTa, anhPoster) VALUES
(1, 'Hộ linh tráng sĩ', 135, 'Chính kịch, hành động', '2026-08-30', 'Độ tuổi: 13+ | Ngôn ngữ: Tiếng Việt (phụ đề) | Nội dung: Hành trình 7 tráng sĩ bảo vệ 99 quan tài của Đinh Tiên Hoàng Đế trước thế lực thù địch.', 'hlts.jpg'),
(2, 'Lên Hương', 120, 'Hài, Gay cấn', '2026-09-18', 'Độ tuổi: 16+ | Ngôn ngữ: Tiếng Việt (phụ đề) | Nội dung: Giao kèo định mệnh giữa bà chủ trại hòm ế ẩm và chàng trai cần tiền chữa bệnh cho mẹ.', 'lh.jpg'),
(3, 'Trại Buôn Người', 125, 'Hành động, Gay cấn', '2026-10-10', 'Độ tuổi: 18+ | Ngôn ngữ: Tiếng Việt | Nội dung: Cuộc giải cứu nghẹt thở tại sào huyệt buôn người xuyên quốc gia.', 'tbn.jpg'),
(4, 'Người Mẹ Khác', 115, 'Kinh dị, Bí ẩn', '2026-10-12', 'Độ tuổi: 18+ | Ngôn ngữ: Tiếng Việt | Nội dung: Bí ẩn đằng sau người mẹ kế và những hiện tượng siêu nhiên trong căn nhà cổ.', 'nmk.jpg')
ON DUPLICATE KEY UPDATE ten=VALUES(ten), anhPoster=VALUES(anhPoster);

-- 3. Phòng chiếu
INSERT INTO tblTheater9 (id, ten, loai, sucChua, moTa) VALUES
(1, 'Cinema 6', '2D Phụ đề', 80, 'Phòng chiếu tiêu chuẩn âm thanh vòm Dolby Atmos'),
(2, 'Cinema 10', '2D Lồng tiếng', 100, 'Phòng chiếu màn hình lớn IMAX'),
(3, 'Cinema 3', '3D', 70, 'Phòng chiếu 3D kỹ thuật số')
ON DUPLICATE KEY UPDATE ten=VALUES(ten);

-- 4. Ghế ngồi trong Cinema 6 (Phòng id = 1)
INSERT INTO tblSeat9 (id, ten, loai, moTa, tblTheater9id) VALUES
(1, 'A1', 'Ghế thường', 'Hàng A - Số 1', 1),
(2, 'A2', 'Ghế thường', 'Hàng A - Số 2', 1),
(3, 'F5', 'VIP', 'Hàng F - Ghế VIP 5', 1),
(4, 'F6', 'VIP', 'Hàng F - Ghế VIP 6', 1),
(5, 'F7', 'VIP', 'Hàng F - Ghế VIP 7', 1),
(6, 'F8', 'VIP', 'Hàng F - Ghế VIP 8', 1),
(7, 'H1', 'Ghế đôi', 'Hàng H - Ghế đôi Sweetbox 1', 1),
(8, 'H2', 'Ghế đôi', 'Hàng H - Ghế đôi Sweetbox 2', 1)
ON DUPLICATE KEY UPDATE ten=VALUES(ten);

-- 5. Suất chiếu
INSERT INTO tblShowtime9 (id, ngayChieu, batDau, ketThuc, moTa, tblTheater9id, tblMovie9id) VALUES
(1, '2026-10-13', '15:30:00', '17:35:00', 'Suất chiếu chiều', 1, 3),
(2, '2026-10-13', '10:00:00', '12:15:00', 'Suất chiếu sáng', 1, 1),
(3, '2026-10-13', '18:00:00', '20:15:00', 'Suất chiếu tối', 1, 2)
ON DUPLICATE KEY UPDATE moTa=VALUES(moTa);

-- 6. Vé cho Suất chiếu 1 (Phim Trại Buôn Người tại Cinema 6)
INSERT INTO tblTicket9 (id, gia, trangThai, tblBill9id, tblSeat9id, tblShowtime9id) VALUES
(1, 95500, 'Chưa đặt', NULL, 1, 1),
(2, 95500, 'Chưa đặt', NULL, 2, 1),
(3, 95500, 'Chưa đặt', NULL, 3, 1),
(4, 95500, 'Chưa đặt', NULL, 4, 1),
(5, 95500, 'Chưa đặt', NULL, 5, 1),
(6, 95500, 'Chưa đặt', NULL, 6, 1),
(7, 120000, 'Chưa đặt', NULL, 7, 1),
(8, 120000, 'Chưa đặt', NULL, 8, 1)
ON DUPLICATE KEY UPDATE gia=VALUES(gia);

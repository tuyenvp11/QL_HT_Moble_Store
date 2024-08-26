CREATE DATABASE CuaHangDienThoaiDiDong
GO
USE CuaHangDienThoaiDiDong
GO


CREATE TABLE TaiKhoan( 
 TenDangNhap NVARCHAR(50)NOT NULL,
 MatKhau NVARCHAR(50) NOT NULL,
 HoTen NVARCHAR(50) NOT NULL,
 NgayTao DATE NOT NULL
);
GO

CREATE TABLE LoaiSanPham(
 ID NVARCHAR(50) NOT NULL,
 LoaiSanPham NVARCHAR(50) PRIMARY KEY  NOT NULL
);
GO

CREATE TABLE NhaSanXuat(
 ID NVARCHAR(50) NOT NULL,
 TenNSX NVARCHAR(50)  PRIMARY KEY NOT NULL,
 DiaChi NVARCHAR(50) NOT NULL,
 Email NVARCHAR(50) NOT NULL,
 SDT NVARCHAR(50) NOT NULL,
);
GO

CREATE TABLE SanPham(
 ID NVARCHAR(50) NOT NULL,
 LoaiSanPham NVARCHAR(50) NOT NULL,
 TenSanPham NVARCHAR(50) PRIMARY KEY NOT NULL,
 NhaSanXuat NVARCHAR(50) NOT NULL,
 ThoiGianBaoHanh INT NOT NULL,
 ThoiGianBaoHanhDonVi NVARCHAR(50) NOT NULL,
 SoLuongConLai INT NOT NULL,
 DonVi NVARCHAR(50) NOT NULL,
 Gia NVARCHAR(100) NOT NULL,
 FOREIGN  KEY (LoaiSanPham) REFERENCES LoaiSanPham(LoaiSanPham),
);
GO

CREATE TABLE HoaDon(
 MaHoaDon NVARCHAR(50) PRIMARY KEY NOT NULL,
 TenSanPham NVARCHAR(50) NOT NULL,
 SoLuong INT NOT NULL,
 TongTien NVARCHAR(100) NOT NULL,
 FOREIGN KEY (TenSanPham) REFERENCES SanPham(TenSanPham)
);
GO
 
 
CREATE TABLE DoanhThu(
 ID int IDENTITY(1,1) PRIMARY KEY NOT NULL,
 TenNV NVARCHAR(100) NOT NULL,
 TenKH NVARCHAR(100) NOT NULL,
 Ngay  NVARCHAR(50) NOT NULL,
 ThoiGian  NVARCHAR(50) NOT NULL,
 TongTien NVARCHAR(100) NOT NULL,
 Tien NVARCHAR(100) NOT NULL,
 Du NVARCHAR(100) NOT NULL,
);
GO

CREATE TABLE ChucVu(
 ID NVARCHAR(50) NOT NULL,
 ChucVu NVARCHAR(50) PRIMARY KEY NOT NULL
);
GO

CREATE TABLE NhanVien(
 ChucVu NVARCHAR(50) NOT NULL,
 MaNV NVARCHAR(50) PRIMARY KEY NOT NULL,
 HoTen NVARCHAR(100) NOT NULL,
 NgaySinh DATE NOT NULL, 
 GioiTinh NVARCHAR(5) NOT NULL, 
 DiaChi NVARCHAR(50) NOT NULL, 
 SDT NVARCHAR(10) NOT NULL, 
 Email NVARCHAR(50) NOT NULL
 FOREIGN KEY (ChucVu) REFERENCES ChucVu(ChucVu)
);
GO

CREATE TABLE KhachHang(
 MaKH NVARCHAR(50) PRIMARY KEY NOT NULL,
 HoTen NVARCHAR(100) NOT NULL, 
 NgaySinh DATE NOT NULL, 
 GioiTinh NVARCHAR(5) NOT NULL, 
 DiaChi NVARCHAR(50) NOT NULL, 
 SDT NVARCHAR(10) NOT NULL, 
 Email NVARCHAR(50) NOT NULL 
);
GO


 
select * from NhanVien;
select * from KhachHang;
select * from ChucVu;
select * from LoaiSanPham;
select * from TaiKhoan;
select * from NhaSanXuat;
select * from SanPham;
select * from HoaDon; 
select * from DoanhThu;

-- Chức vụ
insert into ChucVu values ('CV01',N'Quản lý');
insert into ChucVu values ('CV02',N'Nhân viên vận chuyển');
insert into ChucVu values ('CV03',N'Nhân viên chăm sóc khách hàng');
insert into ChucVu values ('CV04',N'Nhân viên tư vấn');
-- Nhân viên
insert into NhanVien values (N'Quản lý','NV01',N'Lê viết hùng','08-12-2003','Nam',N'Thanh hóa','012','hung@2003');
insert into NhanVien values (N'Quản lý','NV02',N'Lê viết tuấn','05-11-2003','Nam',N'Thanh hóa','0123','tuan@2003');
insert into NhanVien values (N'Nhân viên vận chuyển','NV03',N'Nguyễn lan anh','04-09-2003',N'Nữ',N'Hà nam','01234','anh@2003');
insert into NhanVien values (N'Nhân viên vận chuyển','NV04',N'Nguyễn lê điệp','03-07-2003','Nam',N'Nam định','012345','diep@2003');
insert into NhanVien values (N'Nhân viên chăm sóc khách hàng','NV05',N'Nguyễn hải anh','02-08-2003','Nam',N'Như thanh','0123456','hai@2003');
insert into NhanVien values (N'Nhân viên chăm sóc khách hàng','NV06',N'Phạm văn tuyến','01-12-2003','Nam',N'Hải phòng','01234567','tuyen@2003');
insert into NhanVien values (N'Nhân viên chăm sóc khách hàng','NV07',N'Nguyễn viết duy','09-12-2003','Nam',N'Sơn la','012345678','duy@2003');
insert into NhanVien values (N'Nhân viên tư vấn','NV08',N'Bùi thị yến','09-11-2003',N'Nữ',N'Tuyên quang','0123456789','yen@2003');
insert into NhanVien values (N'Nhân viên tư vấn','NV09',N'Dư công hai','06-08-2003','Nam',N'Thanh hóa','012234','haihai@2003');
insert into NhanVien values (N'Nhân viên tư vấn','NV010',N'Nguyễn văn đức','04-03-2003','Nam',N'Nam định','0122345','duc@2003');

-- Khách hàng
insert into KhachHang values ('KH01',N'Bùi ngọc quân','02-04-2004','Nam',N'Hải phòng','023','quan@123');
insert into KhachHang values ('KH02',N'Bùi ngọc hoàng','03-04-2004','Nam',N'Điện biên','0234','hoang@123');
insert into KhachHang values ('KH03',N'Hoàng trọng đại','05-04-2004','Nam',N'Long an','02345','dai@123');
insert into KhachHang values ('KH04',N'Lưu văn vương','07-08-2004','Nam',N'Cà mau','023456','vuong@123');
insert into KhachHang values ('KH05',N'Nguyễn sỹ đức','06-12-2004','Nam',N'Huế','0234567','duc@123');
insert into KhachHang values ('KH06',N'Phạm ngọc huy','02-11-2004','Nam',N'Vũng tàu','02345678','huy@123');
insert into KhachHang values ('KH07',N'Nguyễn viết ngọc','05-12-2004','Nam',N'KomTum','023456789','ngoc@123');
insert into KhachHang values ('KH08',N'Lò văn hùng','01-11-2004','Nam',N'Lào cai','023123','hung@123');
insert into KhachHang values ('KH09',N'Lê thị hiền','02-04-2004',N'Nữ',N'Thanh hóa','0231234','hien@123');
insert into KhachHang values ('KH10',N'Bùi xuân trang','04-05-2004',N'Nữ',N'Hải phòng','02312345','trang@123');

-- Nhà sản xuất
insert into NhaSanXuat values ('IP','IPHON',N'Mỹ','my@1234','012345');
insert into NhaSanXuat values ('SS','SAMSUNG',N'Nhật','nhat@1234','0123456');
insert into NhaSanXuat values ('OP','OPPO',N'Hà Lan','halan@1234','01234567');
insert into NhaSanXuat values ('LV','LENOVO',N'Việt Nam','vietnam@1234','012345678');


-- Tài khoản
insert into TaiKhoan values ('LeVietHung','123','Le Hung','2023-08-12');

-- loại sản phẩm
insert into LoaiSanPham values('DT',N'Điện thoại di động');
insert into LoaiSanPham values ('PKDT',N'Phụ kiện điện thoại');


-- Sản phẩm
insert into SanPham values ('IP01',N'Điện thoại di động','IPHON 12','IPHON',1,N'Năm',10,N'Cái',120);
insert into SanPham values ('IP02',N'Điện thoại di động','IPHON X','IPHON',1,N'Năm',10,N'Cái',100);
insert into SanPham values ('IP03',N'Điện thoại di động','IPHON 15','IPHON',1,N'Năm',10,N'Cái',200);
insert into SanPham values ('SS01',N'Điện thoại di động','SAMSUNG S22','SAMSUNG',2,N'Năm',10,N'Cái',120);
insert into SanPham values ('SS01',N'Điện thoại di động','SAMSUNG A12','SAMSUNG',2,N'Năm',10,N'Cái',100);
insert into SanPham values ('SS01',N'Điện thoại di động','SAMSUNG S22 UTRAL','SAMSUNG',2,N'Năm',10,N'Cái',200);
insert into SanPham values ('OP01',N'Điện thoại di động','OPPO A22','OPPO',1,N'Năm',10,N'Cái',50);
insert into SanPham values ('OP02',N'Điện thoại di động','OPPO S22','OPPO',1,N'Năm',10,N'Cái',100);
insert into SanPham values ('OP03',N'Điện thoại di động','OPPO S22 UTRAL','OPPO',2,N'Năm',10,N'Cái',100);
insert into SanPham values ('LV01',N'Điện thoại di động','LENOVO A22','LENOVO',1,N'Năm',10,N'Cái',120);
insert into SanPham values ('LV02',N'Điện thoại di động','LENOVO S22 PLUS ','LENOVO',1,N'Năm',10,N'Cái',150);
insert into SanPham values ('LV03',N'Điện thoại di động','LENOVO S22 UTRAL','LENOVO',1,N'Năm',10,N'Cái',200);



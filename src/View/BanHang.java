
package View;
 
import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

class BanHang extends javax.swing.JFrame implements Runnable {
    
    private Connection conn = null;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    
    private boolean Add=false,Change=false, Pay=false;
    private String sql = "SELECT * FROM HoaDon";
    
    private Thread thread;
    private Detail detail;

    public BanHang(Detail d) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail=new Detail(d);
        lblStatus.setForeground(Color.red);
        setData();
        
        connection();
        Pays();
        Start();
        Load(sql); 
        checkBill();
    }

    private void connection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn=DriverManager.getConnection("jdbc:sqlserver://PCCUATUYEN:1433;databaseName=CuaHangDienThoaiDiDong;user=sa;password=123;integratedSecurity=false;encrypt=false;trustServerCertificate=true;");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void setData(){
        Disabled(); 
        lblName.setText(detail.getName());
        lblDate.setText(String.valueOf(new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())));
    }
    
    private void Pays() {
        lbltotalMoney.setText("0 VND");
        String sqlPay = "SELECT * FROM HoaDon";
        try {
            pst = conn.prepareStatement(sqlPay);
            rs = pst.executeQuery();
            while (rs.next()) {
                String[] s1 = rs.getString("TongTien").toString().trim().split("\\s");
                String[] s2 = lbltotalMoney.getText().split("\\s");

                DecimalFormat formatter = new DecimalFormat("###,###,###");

                double totalMoney = convertedToNumbers(s1[0]);
                if (s2.length > 1) {
                    totalMoney += convertedToNumbers(s2[0]);
                    lbltotalMoney.setText(formatter.format(totalMoney) + " " + s2[1]);
                } else {
                    lbltotalMoney.setText(formatter.format(totalMoney));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void Start(){
        if(thread==null){
            thread= new Thread(this);
            thread.start();
        }
    }
    
    private void Update(){
        lblTime.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())));
    }
    
    private void Enabled(){
        cbxClassify.setEnabled(true);
        cbxKhachHang.setEnabled(true);
        btnThemKH.setEnabled(true);
    }
    
    private void Disabled(){
        cbxClassify.setEnabled(false);
        cbxKhachHang.setEnabled(false);
        txbAmount.setEnabled(false);
        cbxProduct.setEnabled(false);
    }
    
    private void Refresh(){
        Add=false;
        Change=false;
        Pay=false;
        txbCode.setText("");
        txbPrice.setText("");
        txbAmount.setText("");
        txbIntoMoney.setText("");
        txbMoney.setText("");
        lblSurplus.setText("0 VND");
        cbxProduct.removeAllItems();
        cbxClassify.removeAllItems();
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnPrint.setEnabled(false);
        Disabled();
    }
    
    private void checkBill(){
        if(tableBill.getRowCount()==0){
            lbltotalMoney.setText("0 VND");
            txbMoney.setText("");
            lblSurplus.setText("0 VND");
            btnPay.setEnabled(false);
            txbMoney.setEnabled(false);
        }
        else {
            btnPay.setEnabled(true);
            txbMoney.setEnabled(true);
        }
    }
    
    private boolean Check() {
        boolean kq=true;
        String sqlCheck="SELECT * FROM HoaDon";
        try{
            PreparedStatement pstCheck=conn.prepareStatement(sqlCheck);
            ResultSet rsCheck=pstCheck.executeQuery();
            while(rsCheck.next()){
                if(this.txbCode.getText().equals(rsCheck.getString("MaHoaDon").toString().trim())){
                    return false;                                           
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkNull(){
        boolean kq=true;
        if(String.valueOf(this.txbCode.getText()).length()==0){
            lblStatus.setText("Bạn chưa chọn sản phẩm!");
            return false;
        }
        else if(String.valueOf(this.txbAmount.getText()).length()==0){
                lblStatus.setText("Bạn chưa nhập số lượng sản phẩm!");
                return false;
        }
        return kq;
    }
    
    private void Sucessful(){
        btnSave.setEnabled(false);
        btnThemKH.setEnabled(true);
        btnAdd.setEnabled(true);
        btnNew.setEnabled(true);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
    } 
    
    private void LoadLoaiSanPham(){
        String sql = "SELECT * FROM LoaiSanPham";
        try {
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxClassify.addItem(rs.getString("LoaiSanPham").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    
    private void LoadKhachHang(){
        String sql = "SELECT * FROM KhachHang";
        try {
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxKhachHang.addItem(rs.getString("HoTen").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    
    private void Load(String sql){
        tableBill.removeAll();
        try{
            String [] arr={"Mã Sản Phẩm","Tên Sản Phẩm","Số Lượng","Tiền"};
            DefaultTableModel model=new DefaultTableModel(arr,0);
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector=new Vector();
                vector.add(rs.getString("MaHoaDon").trim());
                vector.add(rs.getString("TenSanPham").trim());
                vector.add(rs.getInt("SoLuong"));
                vector.add(rs.getString("TongTien").trim());
                model.addRow(vector);
            }
            tableBill.setModel(model);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void consistency(){
        String sqlBill="SELECT * FROM HoaDon";
        try{
            
            PreparedStatement pstBill=conn.prepareStatement(sqlBill);
            ResultSet rsBill=pstBill.executeQuery();
            
            while(rsBill.next()){
                
                try{
                    String sqlTemp="SELECT * FROM SanPham WHERE ID ='"+rsBill.getString("MaHoaDon")+"'";
                    PreparedStatement pstTemp=conn.prepareStatement(sqlTemp);
                    ResultSet rsTemp=pstTemp.executeQuery();
                    
                    if(rsTemp.next()){
                        
                        String sqlUpdate="UPDATE SanPham SET SoLuongConLai=? WHERE ID='"+rsBill.getString("MaHoaDon").trim()+"'";
                        try{
                            pst=conn.prepareStatement(sqlUpdate);
                            pst.setInt(1, rsTemp.getInt("SoLuongConLai")-rsBill.getInt("SoLuong"));
                            pst.executeUpdate();
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }  
    }
    
    private void checkProducts(){
        String sqlCheck="SELECT SoLuongConLai FROM SanPham WHERE ID='"+txbCode.getText()+"'";
        try{
            pst=conn.prepareCall(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(rs.getInt("SoLuongConLai")==0){
                    lblStatus.setText("Sản phẩm này đã hết hàng!!");
                    btnSave.setEnabled(false);
                    txbAmount.setEnabled(false);
                }
                else{
                    lblStatus.setText("Mặt hàng này còn "+rs.getInt("SoLuongConLai")+" sản phẩm!!");
                    btnSave.setEnabled(true);
                    txbAmount.setEnabled(true);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private double convertedToNumbers(String s){
        String number="";
        String []array=s.replace(","," ").split("\\s");
        for(String i:array){
            number=number.concat(i);
        }
        return Double.parseDouble(number);
    } 
    
    private String cutChar(String arry){
        return arry.replaceAll("\\D+","");
    }
    
    private void loadPriceandClassify(String s){
        String sql = "SELECT * FROM SanPham where ID=?";
        try {
            pst=conn.prepareStatement(sql);
            pst.setString(1,String.valueOf(s ));
            rs=pst.executeQuery();
            while(rs.next()){
                cbxClassify.addItem(rs.getString("LoaiSanPham").trim());
                txbPrice.setText(rs.getString("Gia").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbxClassify = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txbCode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txbIntoMoney = new javax.swing.JTextField();
        txbAmount = new javax.swing.JTextField();
        txbPrice = new javax.swing.JTextField();
        cbxProduct = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbxKhachHang = new javax.swing.JComboBox<>();
        btnThemKH = new javax.swing.JButton();
        jlbBack = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lbltotalMoney = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txbMoney = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        lblSurplus = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBill = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setForeground(new java.awt.Color(49, 139, 130));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 1, 48)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 51, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Bán Hàng");

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("  ");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDelete.setForeground(new java.awt.Color(49, 139, 130));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 110, 50));

        btnSave.setForeground(new java.awt.Color(49, 139, 130));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setText("Lưu");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel2.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 110, 50));

        btnChange.setForeground(new java.awt.Color(49, 139, 130));
        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnChange.setText("Sửa");
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });
        jPanel2.add(btnChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 120, 50));

        btnNew.setForeground(new java.awt.Color(49, 139, 130));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/New.png"))); // NOI18N
        btnNew.setText("Hóa Đơn Mới");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel2.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 170, 40));

        btnPrint.setForeground(new java.awt.Color(49, 139, 130));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Print Sale.png"))); // NOI18N
        btnPrint.setText("Xuất Hóa Đơn");
        btnPrint.setEnabled(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jPanel2.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 170, 100));

        btnPay.setForeground(new java.awt.Color(49, 139, 130));
        btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Pay.png"))); // NOI18N
        btnPay.setText("Thanh Toán");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        jPanel2.add(btnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 180, 40));

        btnRefresh.setForeground(new java.awt.Color(49, 139, 130));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel2.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 120, 40));

        btnAdd.setForeground(new java.awt.Color(49, 139, 130));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add_28px.png"))); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 50));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Loại sản phẩm:");

        cbxClassify.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxClassifyPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Mã sản phẩm:");

        txbCode.setEnabled(false);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Tên sản phẩm:");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Giá:");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Số lượng:");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Thành Tiền:");

        txbIntoMoney.setEnabled(false);

        txbAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbAmountKeyReleased(evt);
            }
        });

        txbPrice.setEnabled(false);

        cbxProduct.setEnabled(false);
        cbxProduct.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProductPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jPanel3.setBackground(new java.awt.Color(235, 235, 235));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        lblDate.setBackground(new java.awt.Color(255, 255, 255));
        lblDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDate.setForeground(new java.awt.Color(102, 0, 0));
        lblDate.setText("Date");

        lblTime.setBackground(new java.awt.Color(255, 255, 255));
        lblTime.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(102, 0, 0));
        lblTime.setText("Time");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 0, 0));
        jLabel6.setText("Giờ:");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 0, 0));
        jLabel12.setText("Ngày:");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 0, 0));
        jLabel13.setText("Tên nhân viên:");

        lblName.setBackground(new java.awt.Color(255, 255, 255));
        lblName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblName.setForeground(new java.awt.Color(102, 0, 0));
        lblName.setText("tennhanvien");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(98, 98, 98)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Khách hàng");

        cbxKhachHang.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxKhachHangPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        btnThemKH.setForeground(new java.awt.Color(49, 139, 130));
        btnThemKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add_23px.png"))); // NOI18N
        btnThemKH.setEnabled(false);
        btnThemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbxKhachHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbxClassify, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addGap(28, 28, 28)
                                .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxProduct, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txbPrice)
                                    .addComponent(txbAmount)
                                    .addComponent(txbIntoMoney))))
                        .addGap(11, 11, 11))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 29, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txbPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txbAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txbIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jlbBack.setForeground(new java.awt.Color(51, 51, 51));
        jlbBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-home-20.png"))); // NOI18N
        jlbBack.setText("Trang chủ");
        jlbBack.setToolTipText("");
        jlbBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbBackMouseClicked(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(51, 51, 51));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Tổng Tiền:");

        lbltotalMoney.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lbltotalMoney.setForeground(new java.awt.Color(51, 51, 51));
        lbltotalMoney.setText("0 VND");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Tiền Nhận Của Khách Hàng :");

        txbMoney.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txbMoney.setForeground(new java.awt.Color(49, 139, 130));
        txbMoney.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbMoneyKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Tiền Dư Của Khách:");

        lblSurplus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblSurplus.setForeground(new java.awt.Color(51, 51, 51));
        lblSurplus.setText("0 VND");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txbMoney, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(lblSurplus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbltotalMoney, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lbltotalMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel9))
                    .addComponent(txbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblSurplus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tableBill.setForeground(new java.awt.Color(49, 139, 130));
        tableBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Tiền"
            }
        ));
        tableBill.setSelectionBackground(new java.awt.Color(235, 235, 235));
        tableBill.setSelectionForeground(new java.awt.Color(49, 139, 130));
        tableBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBillMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableBill);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jlbBack)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(lblStatus)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addProduct() {
        if(checkNull()){
            String sqlInsert="INSERT INTO HoaDon (MaHoaDon,TenSanPham,SoLuong,TongTien) VALUES(?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setString(1, String.valueOf(txbCode.getText()));
                pst.setString(2, String.valueOf(cbxProduct.getSelectedItem()));
                pst.setInt(3, Integer.parseInt(txbAmount.getText()));
                pst.setString(4, txbIntoMoney.getText());
                pst.executeUpdate();
                lblStatus.setText("Thêm sản phẩm thành công!");
                Disabled();
                Sucessful();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void changeProduct() {
        int Click=tableBill.getSelectedRow();
        TableModel model=tableBill.getModel();

        String sqlChange="UPDATE HoaDon SET SoLuong=?, TongTien=? WHERE MaHoaDon='"+model.getValueAt(Click,0).toString().trim()+"'";
        try{
            pst=conn.prepareStatement(sqlChange);
            pst.setInt(1, Integer.parseInt(this.txbAmount.getText()));
            pst.setString(2, txbIntoMoney.getText());
            pst.executeUpdate();
            Disabled();
            Sucessful();
            lblStatus.setText("Lưu thay đổi thành công!");
            Load(sql);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int lick=JOptionPane.showConfirmDialog(null,"Bạn Có Muốn Thoát Khỏi Chương Trình Hay Không?","Thông Báo",2);
        if(lick==JOptionPane.OK_OPTION){
            System.exit(0);
        }
        else{
            if(lick==JOptionPane.CANCEL_OPTION){    
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void tableBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBillMouseClicked
        cbxClassify.removeAllItems();
        cbxProduct.removeAllItems();

        int Click=tableBill.getSelectedRow();
        TableModel model=tableBill.getModel();

        txbCode.setText(model.getValueAt(Click,0).toString());
        cbxProduct.addItem(model.getValueAt(Click,1).toString());
        txbAmount.setText(model.getValueAt(Click,2).toString());
        txbIntoMoney.setText(model.getValueAt(Click,3).toString());

        loadPriceandClassify(model.getValueAt(Click,0).toString());

        btnChange.setEnabled(true);
        btnDelete.setEnabled(true);
    }//GEN-LAST:event_tableBillMouseClicked

    private void txbMoneyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbMoneyKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        if(txbMoney.getText().equals("")){
            String []s=lbltotalMoney.getText().split("\\s");
            lblSurplus.setText("0"+" "+s[1]);
        }
        else{
            txbMoney.setText(formatter.format(convertedToNumbers(txbMoney.getText())));

            String s1=txbMoney.getText();
            String[] s2=lbltotalMoney.getText().split("\\s");

            if((convertedToNumbers(s1)-convertedToNumbers(s2[0]))>=0){
                lblSurplus.setText(formatter.format((convertedToNumbers(s1)-convertedToNumbers(s2[0])))+" "+s2[1]);
                lblStatus.setText("Số tiền khách hàng đưa đã hợp lệ!");
                Pay=true;
            }
            else {

                lblSurplus.setText(formatter.format((convertedToNumbers(s1)-convertedToNumbers(s2[0])))+" "+s2[1]);
                lblStatus.setText("Số tiền khách hàng đưa nhỏ hơn tổng tiền mua hàng trong hóa đơn!");
                Pay=false;
            }
        }
    }//GEN-LAST:event_txbMoneyKeyReleased

    private void jlbBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlbBackMouseClicked
        if(this.detail.getUser().toString().toString().equals("LeVietHung")){
            TrangChu home=new TrangChu(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
        else{
            TrangChu home=new TrangChu(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
    }//GEN-LAST:event_jlbBackMouseClicked

    private void cbxProductPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxProductPopupMenuWillBecomeInvisible
        String sql = "SELECT * FROM SanPham where TenSanPham=?";
        try {
            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxProduct.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                txbCode.setText(rs.getString("ID").trim());
                txbPrice.setText(rs.getString("Gia").trim());
                txbAmount.setEnabled(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        checkProducts();
    }//GEN-LAST:event_cbxProductPopupMenuWillBecomeInvisible

    private void txbAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbAmountKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        txbAmount.setText(cutChar(txbAmount.getText()));

        if (txbAmount.getText().equals("")) {
            String[] s = txbPrice.getText().split("\\s");
            if (s.length > 1) {
                txbIntoMoney.setText("0" + " " + s[1]);
            } else {
                txbIntoMoney.setText("0");
            }
        } else {
            String sqlCheck = "SELECT SoLuongConLai FROM SanPham WHERE ID='" + txbCode.getText() + "'";
            try {
                pst = conn.prepareStatement(sqlCheck);
                rs = pst.executeQuery();

                while (rs.next()) {
                    if ((rs.getInt("SoLuongConLai") - Integer.parseInt(txbAmount.getText())) < 0) {
                        String[] s = txbPrice.getText().split("\\s");
                        if (s.length > 1) {
                            txbIntoMoney.setText("0" + " " + s[1]);
                        } else {
                            txbIntoMoney.setText("0");
                        }

                        lblStatus.setText("Số lượng sản phẩm bán không được vượt quá số lượng hàng trong kho!!");
                        btnSave.setEnabled(false);
                    } else {
                        int soluong = Integer.parseInt(txbAmount.getText().toString());
                        String[] s = txbPrice.getText().split("\\s");
                        if (s.length > 1) {
                            txbIntoMoney.setText(formatter.format(convertedToNumbers(s[0]) * soluong) + " " + s[1]);
                        } else {
                            txbIntoMoney.setText(formatter.format(convertedToNumbers(s[0]) * soluong));
                        }

                        lblStatus.setText("Số lượng sản phẩm bán hợp lệ!!");
                        btnSave.setEnabled(true);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_txbAmountKeyReleased

    private void cbxClassifyPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxClassifyPopupMenuWillBecomeInvisible
        cbxProduct.removeAllItems();
        String sql = "SELECT * FROM SanPham where LoaiSanPham=?";
        try {
            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxClassify.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxProduct.addItem(rs.getString("TenSanPham").trim());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(cbxProduct.getItemCount()==0){
            cbxProduct.setEnabled(false);
            txbAmount.setEnabled(false);
            txbCode.setText("");
            txbPrice.setText("");
            txbAmount.setText("");
            txbIntoMoney.setText("");
        }
        else cbxProduct.setEnabled(true);
    }//GEN-LAST:event_cbxClassifyPopupMenuWillBecomeInvisible

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        Refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        if(Pay==true){
            String []s=lbltotalMoney.getText().split("\\s");
            String sqlPay="INSERT INTO DoanhThu (TenNV,TenKH,Ngay,ThoiGian,TongTien,Tien,Du) VALUES(?,?,?,?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlPay);
                pst.setString(1, lblName.getText());
                pst.setString(2, String.valueOf(this.cbxKhachHang.getSelectedItem())); 
                pst.setString(3, lblDate.getText());
                pst.setString(4, lblTime.getText());
                pst.setString(5, lbltotalMoney.getText());
                pst.setString(6, txbMoney.getText()+" "+s[1]);
                pst.setString(7, lblSurplus.getText());
                pst.executeUpdate();
                lblStatus.setText("Thực hiện thanh toán thành công!"); 
                Disabled();
                Sucessful();
                consistency();

                btnPrint.setEnabled(true); 
        btnAdd.setEnabled(false);
                btnPay.setEnabled(false);
                txbMoney.setEnabled(false);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        else if(Pay==false){
            JOptionPane.showMessageDialog(null, "Bạn cần nhập số tiền khách hàng thanh toán !");
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed

        String filePath = new File("").getAbsolutePath();
        try {
            JasperReport report=JasperCompileManager.compileReport(filePath+"\\src\\Report\\HoaDon.jrxml");

            JasperPrint print=JasperFillManager.fillReport(report, null, conn);

            JasperViewer.viewReport(print,false);
        }
        catch (JRException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn tạo 1 hóa đơn bán hàng mới không?", "Thông Báo",2);
        if(Click ==JOptionPane.YES_OPTION){
            String sqlDelete="DELETE FROM HoaDon";
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.executeUpdate();
                this.lblStatus.setText("Đã tạo hóa đơn mới!");
                Load(sql);
                checkBill();
                Refresh(); 
                btnAdd.setEnabled(true);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        } 
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        Add=false;
        Change=true; 
        btnAdd.setEnabled(false);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        txbAmount.setEnabled(true);
    }//GEN-LAST:event_btnChangeActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(Add==true){
            if(Check()){
                addProduct();
            }
            else lblStatus.setText("Sản phẩm đã tồn tại trong hóa đơn");
        }else if(Change==true){
            changeProduct();
        }
        checkBill();
        Pays();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm khỏi hóa đơn hay không?", "Thông Báo",2);
        if(Click ==JOptionPane.YES_OPTION){
            String sqlDelete="DELETE FROM HoaDon WHERE MaHoaDon = ?";
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, String.valueOf(txbCode.getText()));
                pst.executeUpdate();
                this.lblStatus.setText("Xóa sản phẩm thành công!");
                Refresh();
                Load(sql);
                Sucessful();
                checkBill();
                Pays();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnThemKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKHActionPerformed
        KhachHang khachHang = new KhachHang(detail);
        this.setVisible(false);
        khachHang.setVisible(true);
    }//GEN-LAST:event_btnThemKHActionPerformed

    private void cbxKhachHangPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxKhachHangPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxKhachHangPopupMenuWillBecomeInvisible

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        Refresh();
        Add=true;
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
        Enabled();
        LoadLoaiSanPham();
        LoadKhachHang();
    }//GEN-LAST:event_btnAddActionPerformed


    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new BanHang(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnThemKH;
    private javax.swing.JComboBox<String> cbxClassify;
    private javax.swing.JComboBox<String> cbxKhachHang;
    private javax.swing.JComboBox<String> cbxProduct;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlbBack;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSurplus;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lbltotalMoney;
    private javax.swing.JTable tableBill;
    private javax.swing.JTextField txbAmount;
    private javax.swing.JTextField txbCode;
    private javax.swing.JTextField txbIntoMoney;
    private javax.swing.JTextField txbMoney;
    private javax.swing.JTextField txbPrice;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while(true){
        Update();  
            try{
                Thread.sleep(1);  
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}

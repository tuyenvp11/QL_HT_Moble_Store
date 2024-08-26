
package View;

 
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel; 

public class SanPham extends javax.swing.JFrame {

    private Connection conn = null;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    
    private String sql = "SELECT * FROM SanPham";
    private boolean Add=false,Change=false;
    
    private Detail detail;
    
    public SanPham(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connection();
        detail=new Detail(d);
        Disabled();
        Load(sql);
        lblStatus.setForeground(Color.red);
    }

    private void connection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn=DriverManager.getConnection("jdbc:sqlserver://PCCUATUYEN:1433;databaseName=CuaHangDienThoaiDiDong;user=sa;password=123;integratedSecurity=false;encrypt=false;trustServerCertificate=true;");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    private void Load(String sql){
        tableProduct.removeAll();
        try{
            String [] arr={"Mã sản phẩm","Loại sản phẩm","Tên sản phẩm","Nhà sản xuất","Thời gian bảo hành","Số lượng còn","Ðơn vị","Giá"};
            DefaultTableModel modle=new DefaultTableModel(arr,0);
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector=new Vector();
                vector.add(rs.getString("ID").trim());
                vector.add(rs.getString("LoaiSanPham").trim());
                vector.add(rs.getString("TenSanPham").trim());
                vector.add(rs.getString("NhaSanXuat").trim());
                vector.add(rs.getInt("ThoiGianBaoHanh")+" "+rs.getString("ThoiGianBaoHanhDonVi").trim());
                vector.add(rs.getInt("SoLuongConLai"));
                vector.add(rs.getString("DonVi").trim());
                vector.add(rs.getString("Gia").trim()); 
                modle.addRow(vector);
            }
            tableProduct.setModel(modle);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
     }
    
    public void LoadClassify(){
        cbxClassify.removeAllItems();
        String sql = "SELECT * FROM LoaiSanPham";
        try{
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxClassify.addItem(rs.getString("LoaiSanPham").trim());
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void LoadProducer(){
        cbxProducer.removeAllItems();
        String sql = "SELECT * FROM NhaSanXuat";
        try{
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxProducer.addItem(rs.getString("TenNSX").trim());
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void LoadSingleTime(){
        cbxTime.removeAllItems();
        this.cbxTime.addItem("Ngày");
        this.cbxTime.addItem("Tháng");
        this.cbxTime.addItem("Năm");
    }
    
    private void Enabled(){ 
        txbID.setEnabled(true);
        txbName.setEnabled(true);
        txbWarrantyPeriod.setEnabled(true);
        txbAmount.setEnabled(true);
        txbUnit.setEnabled(true);
        txbPrice.setEnabled(true);
        cbxClassify.setEnabled(true);
        cbxProducer.setEnabled(true);
        cbxTime.setEnabled(true); 
        cbxClassify.setEnabled(true);
        cbxProducer.setEnabled(true);
        cbxTime.setEnabled(true); 
        lblStatus.setText("Trạng Thái!");
    }
     
    public void Disabled(){ 
        txbID.setEnabled(false);
        txbName.setEnabled(false);
        txbWarrantyPeriod.setEnabled(false);
        txbAmount.setEnabled(false);
        txbUnit.setEnabled(false);
        txbPrice.setEnabled(false);
        cbxClassify.setEnabled(false);
        cbxProducer.setEnabled(false);
        cbxTime.setEnabled(false);  
    }
    
    public void Refresh(){
        txbID.setText("");
        txbName.setText("");
        txbWarrantyPeriod.setText("");
        txbAmount.setText("");
        txbUnit.setText("");
        txbPrice.setText("");
        btnAdd.setEnabled(true);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        Add=false;
        Change=false;  
        Disabled();
    }
    
    public boolean checkNull(){
        boolean kq=true;
        if(String.valueOf(this.txbID.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập mã cho sản phẩm!");
            return false;
        }
        if(String.valueOf(this.txbName.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập Tên sản phẩm!");
            return false;
        }
        if(String.valueOf(this.txbWarrantyPeriod.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập thời gian bảo hành!");
            return false;
        }
        if(String.valueOf(this.txbAmount.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập số lượng sản phẩm hiện có!");
            return false;
        }
        if(String.valueOf(this.txbUnit.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập đơn vị tính sản phẩm!");
            return false;
        }
        if(String.valueOf(this.txbPrice.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập giá cho sản phẩm!");
            return false;
        } 
        return kq;
    }
    
    public boolean Check(){
        boolean kq=true;
        String sqlCheck="SELECT * FROM SanPham";
        try{
            PreparedStatement pstCheck=conn.prepareStatement(sqlCheck);
            ResultSet rsCheck=pstCheck.executeQuery();
            while(rsCheck.next()){
                if(this.txbID.getText().equals(rsCheck.getString("ID").toString().trim())){
                    return false;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
    
    private void addProduct(){
        if(checkNull()){
            String sqlInsert="INSERT INTO SanPham (ID,LoaiSanPham,TenSanPham,NhaSanXuat,ThoiGianBaoHanh,ThoiGianBaoHanhDonVi,SoLuongConLai,DonVi,Gia) VALUES(?,?,?,?,?,?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setString(1, (String)txbID.getText());
                pst.setString(2, String.valueOf(this.cbxClassify.getSelectedItem()));
                pst.setString(3, (String)txbName.getText());
                pst.setString(4,String.valueOf(cbxProducer.getSelectedItem()));
                pst.setInt(5,Integer.parseInt(txbWarrantyPeriod.getText()));
                pst.setString(6, String.valueOf(this.cbxTime.getSelectedItem()));
                pst.setInt(7, Integer.parseInt(txbAmount.getText()));
                pst.setString(8, String.valueOf(this.txbUnit.getText()));
                pst.setString(9, txbPrice.getText()+" "+"VND");
                pst.executeUpdate();
                lblStatus.setText("Thêm sản phẩm thành công!");
                Disabled();
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    private void changeProduct(){
        int Click=tableProduct.getSelectedRow();
        TableModel model=tableProduct.getModel();
        
        if(checkNull()){
            String sqlChange="UPDATE SanPham SET ID=?,LoaiSanPham=?,TenSanPham=?,NhaSanXuat=?,ThoiGianBaoHanh=?,ThoiGianBaoHanhDonVi=?,SoLuongConLai=?,DonVi=?,Gia=? WHERE ID='"+model.getValueAt(Click,0).toString().trim()+"'";
            try{
                pst=conn.prepareStatement(sqlChange);
                pst.setString(1, txbID.getText());
                pst.setString(2, String.valueOf(cbxClassify.getSelectedItem()));
                pst.setString(3, txbName.getText());
                pst.setString(4,String.valueOf(cbxProducer.getSelectedItem()));
                pst.setInt(5,Integer.parseInt(txbWarrantyPeriod.getText()));
                pst.setString(6, String.valueOf(cbxTime.getSelectedItem()));
                pst.setInt(7, Integer.parseInt(txbAmount.getText()));
                pst.setString(8, txbUnit.getText());
                pst.setString(9, txbPrice.getText()+" "+"VND");
                pst.executeUpdate();
                lblStatus.setText("Lưu thay đổi thành công!");
                Disabled();
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProduct = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txbID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbxClassify = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txbName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbxProducer = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txbWarrantyPeriod = new javax.swing.JTextField();
        cbxTime = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txbAmount = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txbUnit = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txbPrice = new javax.swing.JTextField();
        jlbBack = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tableProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Loại sản phẩm", "Tên sản phẩm", "Nhà sản xuất", "Thời gian bảo hành", "Số lượng còn", "Đơn vị", "Giá"
            }
        ));
        tableProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableProduct);
        if (tableProduct.getColumnModel().getColumnCount() > 0) {
            tableProduct.getColumnModel().getColumn(0).setHeaderValue("ID");
            tableProduct.getColumnModel().getColumn(1).setHeaderValue("Loại sản phẩm");
            tableProduct.getColumnModel().getColumn(2).setHeaderValue("Tên sản phẩm");
            tableProduct.getColumnModel().getColumn(3).setHeaderValue("Nhà sản xuất");
            tableProduct.getColumnModel().getColumn(4).setHeaderValue("Thời gian bảo hành");
            tableProduct.getColumnModel().getColumn(5).setHeaderValue("Số lượng còn");
            tableProduct.getColumnModel().getColumn(6).setHeaderValue("Đơn vị");
            tableProduct.getColumnModel().getColumn(7).setHeaderValue("Giá");
        }

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("   ");
        lblStatus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel1.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 50, 50));

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add_23px.png"))); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 0, 110, 50));

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 50, 50));

        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });
        jPanel1.add(btnChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 0, 50, 50));

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 50, 50));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("SẢN PHẨM");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Mã sản phẩm:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Loại sản phẩm:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Tên sản phẩm:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("Nhà sản xuất:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel5.setText("Thời gian bảo hành:");

        txbWarrantyPeriod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbWarrantyPeriodKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("Số lượng còn:");

        txbAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbAmountKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("Đơn vị tính:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel8.setText("Giá:");

        txbPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPriceKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(129, 129, 129)
                                .addComponent(txbUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbxClassify, 0, 257, Short.MAX_VALUE)
                                    .addComponent(cbxProducer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel6)
                            .addComponent(txbAmount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txbWarrantyPeriod)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxTime, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addComponent(txbPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txbName, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                                    .addComponent(txbID))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(cbxProducer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txbID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(txbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txbUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txbPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txbAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txbWarrantyPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jLabel6)))
                .addContainerGap(54, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1079, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jlbBack))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 6, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(132, 132, 132)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jlbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(lblStatus)
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(625, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel7, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductMouseClicked
        int Click=tableProduct.getSelectedRow();
        TableModel model=tableProduct.getModel();
        cbxProducer.removeAllItems();
        cbxClassify.removeAllItems();
        cbxTime.removeAllItems();
        txbID.setText(model.getValueAt(Click,0).toString());
        cbxClassify.addItem(model.getValueAt(Click,1).toString());
        txbName.setText(model.getValueAt(Click,2).toString());
        cbxProducer.addItem(model.getValueAt(Click,3).toString());
        String []s=model.getValueAt(Click,4).toString().trim().split("\\s");
        txbWarrantyPeriod.setText(s[0]);
        cbxTime.addItem(s[1]);
        txbAmount.setText(model.getValueAt(Click,5).toString());
        txbUnit.setText(model.getValueAt(Click,6).toString());
        String []s1=model.getValueAt(Click,7).toString().split("\\s");
        txbPrice.setText(s1[0]); 
        btnChange.setEnabled(true);
        btnDelete.setEnabled(true);
    }//GEN-LAST:event_tableProductMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        Refresh();
        Add=true;
        Enabled();
        LoadClassify();
        LoadProducer();
        LoadSingleTime();
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        Add=false;
        Change=true;
        Enabled();
        LoadClassify();
        LoadProducer();
        LoadSingleTime();
        btnAdd.setEnabled(false);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnChangeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm hay không?", "Thông Báo",2);
        if(Click ==JOptionPane.YES_OPTION){
            String sqlDelete="DELETE FROM SanPham WHERE ID=? AND TenSanPham=?";
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, (String)txbID.getText());
                pst.setString(2, txbName.getText());
                pst.executeUpdate();
                lblStatus.setText("Xóa sản phẩm thành công!");
                Disabled();
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                    ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(Add==true){
            if(Check()){
                addProduct();
            }
            else lblStatus.setText("Không thể thêm sản phẩm vì mã sản phẩm bạn nhập đã tồn tại");
        }else if(Change==true){
            changeProduct();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        Refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

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

    private void txbPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbPriceKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        txbPrice.setText(cutChar(txbPrice.getText()));
        if(txbPrice.getText().equals("")){
            return;
        }
        else{
            txbPrice.setText(formatter.format(convertedToNumbers(txbPrice.getText())));
        }
    }//GEN-LAST:event_txbPriceKeyReleased

    private void txbAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbAmountKeyReleased
        txbAmount.setText(cutChar(txbAmount.getText()));
    }//GEN-LAST:event_txbAmountKeyReleased

    private void txbWarrantyPeriodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbWarrantyPeriodKeyReleased
        txbWarrantyPeriod.setText(cutChar(txbWarrantyPeriod.getText()));
    }//GEN-LAST:event_txbWarrantyPeriodKeyReleased

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new SanPham(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbxClassify;
    private javax.swing.JComboBox<String> cbxProducer;
    private javax.swing.JComboBox<String> cbxTime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlbBack;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableProduct;
    private javax.swing.JTextField txbAmount;
    private javax.swing.JTextField txbID;
    private javax.swing.JTextField txbName;
    private javax.swing.JTextField txbPrice;
    private javax.swing.JTextField txbUnit;
    private javax.swing.JTextField txbWarrantyPeriod;
    // End of variables declaration//GEN-END:variables
   
}


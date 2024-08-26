
package View;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class KhachHang extends javax.swing.JFrame {

    private Connection conn = null;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    
    private boolean Add=false,Change=false;
    private String sql = "SELECT * FROM KhachHang";
    
    private Detail detail;
    
    public KhachHang(Detail d) {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connection();
        detail=new Detail(d);
        Load(sql);
        Disabled(); 
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
    
    private void Enabled(){  
        txbName.setEnabled(true);
        txbBirthday.setEnabled(true);
        cbxSex.setEnabled(true);
        txbPhone.setEnabled(true); 
        txbAddress.setEnabled(true);
        txbEmail.setEnabled(true); 
        lblStatus.setText("Trạng Thái!");
    }
    
    private void EnabledKH(){  
        txbCode.setEnabled(true);
        txbName.setEnabled(true);
        txbBirthday.setEnabled(true);
        cbxSex.setEnabled(true);
        txbPhone.setEnabled(true); 
        txbAddress.setEnabled(true);
        txbEmail.setEnabled(true); 
        lblStatus.setText("Trạng Thái!");
    }
    
    private void Disabled(){ 
        txbCode.setEnabled(false);
        txbName.setEnabled(false);
        txbBirthday.setEnabled(false);
        cbxSex.setEnabled(false);
        txbPhone.setEnabled(false); 
        txbAddress.setEnabled(false);
        txbEmail.setEnabled(false); 
    }
    
    private void Refresh(){
        Add=false;
        Change=false;
        txbCode.setText("");
        txbName.setText("");
        ((JTextField)txbBirthday.getDateEditor().getUiComponent()).setText("");
        txbPhone.setText(""); 
        txbAddress.setText("");
        txbEmail.setText("");
        btnAdd.setEnabled(true);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false); 
        LoadGT();
        Load(sql);
        lblStatus.setText("Trạng Thái");
    }
    
    private void LoadGT(){
        cbxSex.removeAllItems();
        cbxSex.addItem("Nam");
        cbxSex.addItem("Nữ");
    }
    
    private void Load(String sql){
        try{
            String [] arr={"Mã KH","Họ Tên","Ngày Sinh","Giới Tính","Địa Chỉ","SĐT","Email"};
            DefaultTableModel modle=new DefaultTableModel(arr,0);
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector=new Vector();
                vector.add(rs.getString("MaKH").trim()); 
                vector.add(rs.getString("HoTen").trim()); 
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("NgaySinh")));
                vector.add(rs.getString("GioiTinh").trim());
                vector.add(rs.getString("DiaChi").trim());
                vector.add(rs.getString("SDT").trim());
                vector.add(rs.getString("Email").trim()); 
                modle.addRow(vector);
            }
            tableEmployees.setModel(modle);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
     
    
    private boolean checkNull(){
        boolean kq=true;
        if(String.valueOf(this.txbCode.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập mã khách hàng!");
            return false;
        }
        if(String.valueOf(this.txbName.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập họ tên khách hàng!");
            return false;
        } 
        if(String.valueOf(((JTextField)this.txbBirthday.getDateEditor().getUiComponent()).getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập năm sinh!");
            return false;
            }
        if(String.valueOf(this.txbAddress.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập địa chỉ!");
            return false;
        }
        if(String.valueOf(this.txbPhone.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập số điện thoại!");
            return false;
        }
        if(String.valueOf(this.txbEmail.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập email!");
            return false;
        } 
        return kq;
    }
    
    private void addKhachHang(){
        if(checkNull()){
            String sqlInsert="INSERT INTO KhachHang (MaKH,HoTen,NgaySinh,GioiTinh,SDT,DiaChi,Email) VALUES(?,?,?,?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert); 
                pst.setString(1, this.txbCode.getText());
                pst.setString(2, txbName.getText()); 
                pst.setDate(3,new java.sql.Date(this.txbBirthday.getDate().getTime()));
                pst.setString(4, (String)cbxSex.getSelectedItem());
                pst.setString(5, this.txbPhone.getText());
                pst.setString(6, txbAddress.getText());
                pst.setString(7, txbEmail.getText()); 
                pst.executeUpdate();
                Refresh();
                lblStatus.setText("Thêm nhân viên thành công!");
                Disabled();
                
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    private void changeKhachHang(){
        int Click=tableEmployees.getSelectedRow();
        TableModel model=tableEmployees.getModel();
        
        if(checkNull()){
                String sqlChange="UPDATE KhachHang SET HoTen=?,NgaySinh=?,GioiTinh=?,DiaChi=?,SDT=?,Email=? WHERE MaKH='"+model.getValueAt(Click,0).toString().trim()+"'";
            try{
                pst=conn.prepareStatement(sqlChange); 
                pst.setString(1, txbName.getText()); 
                pst.setDate(2,new java.sql.Date(this.txbBirthday.getDate().getTime()));
                pst.setString(3, (String)cbxSex.getSelectedItem());
                pst.setString(4, txbAddress.getText());
                pst.setString(5, this.txbPhone.getText());
                pst.setString(6, txbEmail.getText());  
                pst.executeUpdate();
                lblStatus.setText("Sửa thành công!"+model.getValueAt(Click,1).toString().trim()+"'");
//                lblStatus.setText("Thêm nhân viên thành công!");
                Disabled(); 
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
     
    
    private boolean Check(){
        boolean kq=true;
        String sqlCheck="SELECT * FROM KhachHang";
        try{
            pst=conn.prepareStatement(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(this.txbCode.getText().equals(rs.getString("MaKH").toString().trim())){
                    return false;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
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

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableEmployees = new javax.swing.JTable()
        {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lblStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txbCode = new javax.swing.JTextField();
        txbName = new javax.swing.JTextField();
        cbxSex = new javax.swing.JComboBox<>();
        txbPhone = new javax.swing.JTextField();
        txbAddress = new javax.swing.JTextField();
        txbBirthday = new com.toedter.calendar.JDateChooser();
        txbEmail = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jlbBack = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tableEmployees.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEmployeesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableEmployees);

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText(" ");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel2.setText("Mã KH:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel3.setText("Họ và tên:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel4.setText("Ngày Sinh:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel5.setText("Giới tính:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel6.setText("Địa chỉ:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel7.setText("Số ĐT:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel8.setText("Email:");

        txbCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txbName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbxSex.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txbPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txbPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPhoneKeyReleased(evt);
            }
        });

        txbAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txbBirthday.setDateFormatString("dd/MM/yyyy");

        txbEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add_23px.png"))); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txbPhone, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txbAddress, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxSex, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txbBirthday, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(txbName, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txbCode, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txbEmail))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txbBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(46, 46, 46)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("KHÁCH HÀNG");

        jlbBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-home-30.png"))); // NOI18N
        jlbBack.setText("Trang chủ");
        jlbBack.setToolTipText("");
        jlbBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbBackMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(454, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(14, 14, 14)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jlbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(515, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 6, Short.MAX_VALUE)
                    .addComponent(jlbBack)
                    .addGap(40, 40, 40)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(10, 10, 10)
                    .addComponent(lblStatus)
                    .addGap(0, 7, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEmployeesMouseClicked
        cbxSex.removeAllItems(); 
        
        int Click=tableEmployees.getSelectedRow();
        TableModel model=tableEmployees.getModel();
         
        txbCode.setText(model.getValueAt(Click,0).toString());
        txbName.setText(model.getValueAt(Click,1).toString()); 
        ((JTextField)txbBirthday.getDateEditor().getUiComponent()).setText(model.getValueAt(Click,2).toString());
        cbxSex.addItem(model.getValueAt(Click,3).toString());
        txbAddress.setText(model.getValueAt(Click,4).toString());
        txbPhone.setText(model.getValueAt(Click,5).toString());
        txbEmail.setText(model.getValueAt(Click,6).toString()); 
        
        btnDelete.setEnabled(true);
        btnChange.setEnabled(true);
    }//GEN-LAST:event_tableEmployeesMouseClicked

    private void txbPhoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbPhoneKeyReleased
        txbPhone.setText(cutChar(txbPhone.getText()));
    }//GEN-LAST:event_txbPhoneKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int lick=JOptionPane.showConfirmDialog(null,"Bạn có muốn thoát khỏi chương trình không?","Thông báo",2);
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
     TrangChu home=new TrangChu(detail);
            this.setVisible(false);
            home.setVisible(true);
    }//GEN-LAST:event_jlbBackMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(Add==true){
            if(Check()){
                addKhachHang();
            }
            else lblStatus.setText("Không thể thêm khách hàng vì mã khách hàng bạn nhập đã tồn tại");
        }else if(Change==true){
            changeKhachHang();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa khách hàng hay không?", "Thông Báo",2);
        if(Click ==JOptionPane.YES_OPTION){
            String sqlDelete="DELETE FROM KhachHang WHERE MaKH=? AND HoTen=?";//
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, txbCode.getText());
                pst.setString(2, txbName.getText());
                pst.executeUpdate();

                Disabled();
                Refresh();
                lblStatus.setText("Xóa thành công!");
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        Add=false;
        Change=true; 
        btnAdd.setEnabled(false);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        LoadGT();
        Enabled(); 
    }//GEN-LAST:event_btnChangeActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        Refresh();
        Add=true;
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
        LoadGT();
        EnabledKH(); 
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        LoadGT();
        Refresh();
        Disabled(); 
        Load(sql);
    }//GEN-LAST:event_btnRefreshActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new KhachHang(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbxSex;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlbBack;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableEmployees;
    private javax.swing.JTextField txbAddress;
    private com.toedter.calendar.JDateChooser txbBirthday;
    private javax.swing.JTextField txbCode;
    private javax.swing.JTextField txbEmail;
    private javax.swing.JTextField txbName;
    private javax.swing.JTextField txbPhone;
    // End of variables declaration//GEN-END:variables
}

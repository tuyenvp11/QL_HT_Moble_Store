
package View;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class NhaSanXuat extends javax.swing.JFrame {

    private Connection conn = null;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    
    private Detail detail;
    private boolean Add=false,Change=false;
     
    String sql2 = "SELECT * FROM NhaSanXuat"; 
    
    public NhaSanXuat(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail=new Detail(d);
        lblStatus.setForeground(Color.red);
        connection(); 
        loadNhaSanXuat(sql2); 
        DisabledNhaSanXuat();
    }

    private void connection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn=DriverManager.getConnection("jdbc:sqlserver://PCCUATUYEN:1433;databaseName=CuaHangDienThoaiDiDong;user=sa;password=123;integratedSecurity=false;encrypt=false;trustServerCertificate=true;");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadNhaSanXuat(String sql){
        tableNSX.removeAll();
        try{
            String [] arr={"mã NSX","Nhà Sản Xuất","Địa Chỉ","Số Điện Thoại","Email"};
            DefaultTableModel modle=new DefaultTableModel(arr,0);
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector=new Vector();
                vector.add(rs.getString("ID").trim());
                vector.add(rs.getString("TenNSX").trim());
                vector.add(rs.getString("DiaChi").trim());
                vector.add(rs.getString("SDT").trim());
                vector.add(rs.getString("Email").trim());
                modle.addRow(vector);
            }
            tableNSX.setModel(modle);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
     
    
    private void backHome(){
        TrangChu home=new TrangChu(detail);
        this.setVisible(false);
        home.setVisible(true);
    }
     
    private void EnabledNhaSanXuat(){
        txbIDNSX.setEnabled(true);
        txbNSX.setEnabled(true);
        txbAdress.setEnabled(true);
        txbPhone.setEnabled(true);
        txbEmail.setEnabled(true);
        lblStatus.setText("Trạng Thái!");
    }
     
    private void DisabledNhaSanXuat(){
        txbIDNSX.setEnabled(false);
        txbNSX.setEnabled(false);
        txbAdress.setEnabled(false);
        txbPhone.setEnabled(false);
        txbEmail.setEnabled(false);
    }
     
    private void Refresh(){
        Change=false;
        Add=false; 
        txbIDNSX.setText("");
        txbNSX.setText("");
        txbAdress.setText("");
        txbPhone.setText("");
        txbEmail.setText(""); 
        btnAddNSX.setEnabled(true);
        btnChangeNSX.setEnabled(false);
        btnDeleteNSX.setEnabled(false);
        btnSaveNSX.setEnabled(false); 
        DisabledNhaSanXuat();
    }
     
    private boolean CheckNhaSanXuat(){
        boolean kq=true;
        String sqlCheck="SELECT * FROM NhaSanXuat";
        try{
            pst=conn.prepareStatement(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(this.txbIDNSX.getText().equals(rs.getString("ID").toString().trim())){
                    return false;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
      
    private boolean checkNullNhaSanXuat(){
        boolean kq=true;
        if(String.valueOf(this.txbIDNSX.getText()).length()==0){
            lblStatus.setText("Bạn chưa ID cho nhà sản xuất!");
            return false;
        }
        if(String.valueOf(this.txbNSX.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập tên nhà sản xuất!");
            return false;        
        }
        if(String.valueOf(this.txbAdress.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập địa chỉ của nhà sản xuất!");
            return false;
        }
        if(String.valueOf(this.txbPhone.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập số điện thoại của nhà sản xuất!");
            return false;
        }
        if(String.valueOf(this.txbEmail.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập Email của nhà sản xuất!");
            return false;
        }
        return kq;
    }
     
    private void addNhaSanXuat(){
        if(checkNullNhaSanXuat()){
            String sqlInsert="INSERT INTO NhaSanXuat (ID,TenNSX,DiaChi,SDT,Email) VALUES(?,?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setString(1, txbIDNSX.getText());
                pst.setString(2, txbNSX.getText());
                pst.setString(3, txbAdress.getText());
                pst.setString(4, txbPhone.getText());
                pst.setString(5, txbEmail.getText());
                pst.executeUpdate();
                lblStatus.setText("Thêm nhà sản xuất thành công!");
                DisabledNhaSanXuat();
                Refresh();
                loadNhaSanXuat(sql2);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
     
    private void changedNhaSanXuat(){
        int Click=tableNSX.getSelectedRow();
        TableModel model=tableNSX.getModel();
        
        if(checkNullNhaSanXuat()){
            String sqlChange="UPDATE NhaSanXuat SET ID=?, TenNSX=?, DiaChi=?, SDT=?,Email=? WHERE ID='"+model.getValueAt(Click,0).toString().trim()+"'";;
            try{
                pst=conn.prepareStatement(sqlChange);
                pst.setString(1, txbIDNSX.getText());
                pst.setString(2, txbNSX.getText());
                pst.setString(3, txbAdress.getText());
                pst.setString(4, txbPhone.getText());
                pst.setString(5, txbEmail.getText());
                pst.executeUpdate();
                lblStatus.setText("Lưu thay đổi thành công!");
                DisabledNhaSanXuat();
                Refresh();
                loadNhaSanXuat(sql2);
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

        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txbPhone = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txbEmail = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        btnRefreshNSX = new javax.swing.JButton();
        btnAddNSX = new javax.swing.JButton();
        btnChangeNSX = new javax.swing.JButton();
        btnDeleteNSX = new javax.swing.JButton();
        btnSaveNSX = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txbIDNSX = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txbNSX = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txbAdress = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jlbBack1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableNSX = new javax.swing.JTable()
        {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Số Điện Thoại:");

        txbPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPhoneKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Email:");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        btnRefreshNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefreshNSX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshNSXMouseClicked(evt);
            }
        });
        btnRefreshNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshNSXActionPerformed(evt);
            }
        });

        btnAddNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add_23px.png"))); // NOI18N
        btnAddNSX.setText("Thêm");
        btnAddNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNSXActionPerformed(evt);
            }
        });

        btnChangeNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnChangeNSX.setEnabled(false);
        btnChangeNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeNSXActionPerformed(evt);
            }
        });

        btnDeleteNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDeleteNSX.setEnabled(false);
        btnDeleteNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteNSXActionPerformed(evt);
            }
        });

        btnSaveNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSaveNSX.setEnabled(false);
        btnSaveNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNSXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefreshNSX)
                .addGap(18, 18, 18)
                .addComponent(btnAddNSX)
                .addGap(18, 18, 18)
                .addComponent(btnChangeNSX)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteNSX)
                .addGap(18, 18, 18)
                .addComponent(btnSaveNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefreshNSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddNSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSaveNSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChangeNSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeleteNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Mã NSX:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Nhà Sản Xuất:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Địa Chỉ:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txbIDNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txbPhone))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txbNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel9))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txbAdress, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(txbEmail))))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbIDNSX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txbNSX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txbAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblStatus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText(" ");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("NHÀ SẢN XUẤT");

        jlbBack1.setForeground(new java.awt.Color(51, 51, 51));
        jlbBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-home-20.png"))); // NOI18N
        jlbBack1.setText("Trang chủ");
        jlbBack1.setToolTipText("");
        jlbBack1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbBack1MouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tableNSX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableNSX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableNSXMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableNSX);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jlbBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(365, 365, 365)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(13, 13, 13))))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(697, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlbBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblStatus)
                .addGap(19, 19, 19))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(96, 96, 96)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(43, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void txbPhoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbPhoneKeyReleased
        txbPhone.setText(cutChar(txbPhone.getText()));
    }//GEN-LAST:event_txbPhoneKeyReleased

    private void btnSaveNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNSXActionPerformed
        if(Add==true)
        if(CheckNhaSanXuat())
        addNhaSanXuat();
        else    lblStatus.setText("Mã nhà sản xuất bạn nhập đã tồn tại!");
        else{
            if(Change==true)
            changedNhaSanXuat();
        }
    }//GEN-LAST:event_btnSaveNSXActionPerformed

    private void btnDeleteNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteNSXActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa nhà sản xuất hay không?", "Thông Báo",2);
        if(Click ==JOptionPane.YES_OPTION){
            String sqlDelete="DELETE FROM NhaSanXuat WHERE ID=? AND  TenNSX=? AND DiaChi=? AND SDT=? AND Email=?";
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, txbIDNSX.getText());
                pst.setString(2, txbNSX.getText());
                pst.setString(3, txbAdress.getText());
                pst.setString(4, txbPhone.getText());
                pst.setString(5, txbEmail.getText());
                pst.executeUpdate();
                lblStatus.setText("Xóa loại nhà sản xuất thành công!");
                DisabledNhaSanXuat();
                Refresh();
                loadNhaSanXuat(sql2);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteNSXActionPerformed

    private void btnChangeNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeNSXActionPerformed
        Add=false;
        Change=true;
        btnAddNSX.setEnabled(false);
        btnChangeNSX.setEnabled(false);
        btnDeleteNSX.setEnabled(false);
        btnSaveNSX.setEnabled(true);
        EnabledNhaSanXuat();
    }//GEN-LAST:event_btnChangeNSXActionPerformed

    private void btnAddNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNSXActionPerformed
        Refresh();
        Add=true;
        btnAddNSX.setEnabled(false);
        btnSaveNSX.setEnabled(true);
        EnabledNhaSanXuat();
    }//GEN-LAST:event_btnAddNSXActionPerformed

    private void btnRefreshNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshNSXActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshNSXActionPerformed

    private void btnRefreshNSXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshNSXMouseClicked
        Refresh();
        loadNhaSanXuat(sql2);
    }//GEN-LAST:event_btnRefreshNSXMouseClicked

    private void tableNSXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableNSXMouseClicked
        int Click=tableNSX.getSelectedRow();
        TableModel model=tableNSX.getModel();

        txbIDNSX.setText(model.getValueAt(Click,0).toString());
        txbNSX.setText(model.getValueAt(Click,1).toString());
        txbAdress.setText(model.getValueAt(Click,2).toString());
        txbPhone.setText(model.getValueAt(Click,3).toString());
        txbEmail.setText(model.getValueAt(Click,4).toString());

        btnChangeNSX.setEnabled(true);
        btnDeleteNSX.setEnabled(true);
    }//GEN-LAST:event_tableNSXMouseClicked

    private void jlbBack1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlbBack1MouseClicked
        if(this.detail.getUser().toString().toString().equals("Admin")){
            TrangChu home=new TrangChu(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
        else{
            TrangChu home=new TrangChu(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
    }//GEN-LAST:event_jlbBack1MouseClicked

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NhaSanXuat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhaSanXuat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhaSanXuat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhaSanXuat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new NhaSanXuat(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNSX;
    private javax.swing.JButton btnChangeNSX;
    private javax.swing.JButton btnDeleteNSX;
    private javax.swing.JButton btnRefreshNSX;
    private javax.swing.JButton btnSaveNSX;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlbBack1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableNSX;
    private javax.swing.JTextField txbAdress;
    private javax.swing.JTextField txbEmail;
    private javax.swing.JTextField txbIDNSX;
    private javax.swing.JTextField txbNSX;
    private javax.swing.JTextField txbPhone;
    // End of variables declaration//GEN-END:variables
}

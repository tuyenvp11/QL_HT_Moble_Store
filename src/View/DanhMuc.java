
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

public class DanhMuc extends javax.swing.JFrame {

    private Connection conn = null;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    
    private Detail detail;
    private boolean Add=false,Change=false;
      
    String Classify = "SELECT * FROM LoaiSanPham";
    
    public DanhMuc(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail=new Detail(d);
        lblStatus.setForeground(Color.red);
        connection();  
        loadClassify(Classify); 
        DisabledClassify(); 
    }

    private void connection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn=DriverManager.getConnection("jdbc:sqlserver://PCCUATUYEN:1433;databaseName=CuaHangDienThoaiDiDong;user=sa;password=123;integratedSecurity=false;encrypt=false;trustServerCertificate=true;");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadClassify(String sql){
        tableClassify.removeAll();
        try{
            String [] arr={"Mã danh mục","Tên danh mục"};
            DefaultTableModel modle=new DefaultTableModel(arr,0);
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector=new Vector();
                vector.add(rs.getString("ID").trim());
                vector.add(rs.getString("LoaiSanPham").trim());
                modle.addRow(vector);
            }
            tableClassify.setModel(modle);
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
     
    private void EnabledClassify(){
        txbIDClassify.setEnabled(true);
        txbClassify.setEnabled(true);
        lblStatus.setText("Trạng Thái!");
    }
       
    private void DisabledClassify(){
        txbIDClassify.setEnabled(false);
        txbClassify.setEnabled(false);
    }
    
    private void Refresh(){
        Change=false;
        Add=false;  
        txbIDClassify.setText("");
        txbClassify.setText("");  
        btnAddClassify.setEnabled(true);
        btnChangeClassify.setEnabled(false);
        btnDeleteClassify.setEnabled(false);
        btnSaveClassify.setEnabled(false);
        DisabledClassify();  
    }
      
    private boolean CheckClassify(){
        boolean kq=true;
        String sqlCheck="SELECT * FROM LoaiSanPham";
        try{
            pst=conn.prepareStatement(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(this.txbIDClassify.getText().equals(rs.getString("ID").toString().trim())){
                    return false;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
      
    private boolean checkNullClassify(){
        boolean kq=true;
        if(String.valueOf(this.txbIDClassify.getText()).length()==0){
            lblStatus.setText("Bạn chưa ID cho loại sản phẩm!");
            return false;
        }
        if(String.valueOf(this.txbClassify.getText()).length()==0){
            lblStatus.setText("Bạn chưa nhập loại sản phẩm!");
            return false;
        }   
        return kq;
    }
      
    private void addClassify(){
        if(checkNullClassify()){
            String sqlInsert="INSERT INTO LoaiSanPham (ID,LoaiSanPham) VALUES(?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setString(1, txbIDClassify.getText());
                pst.setString(2, txbClassify.getText());
                pst.executeUpdate();
                lblStatus.setText("Thêm loại sản phẩm mới thành công!");
                DisabledClassify();
                Refresh();
                loadClassify(Classify);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
     
    private void changedClassify(){
        int Click=tableClassify.getSelectedRow();
        TableModel model=tableClassify.getModel();
        if(checkNullClassify()){
            String sqlChange="UPDATE LoaiSanPham SET ID=?, LoaiSanPham=? WHERE ID='"+model.getValueAt(Click,0).toString().trim()+"'";;
            try{
                pst=conn.prepareStatement(sqlChange);
                pst.setString(1, txbIDClassify.getText());
                pst.setString(2,txbClassify.getText() );
                pst.executeUpdate();
                lblStatus.setText("Lưu thay đổi thành công!");
                DisabledClassify();
                Refresh();
                loadClassify(Classify);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableClassify = new javax.swing.JTable();
        txbIDClassify = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txbClassify = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        btnRefreshClassify = new javax.swing.JButton();
        btnAddClassify = new javax.swing.JButton();
        btnChangeClassify = new javax.swing.JButton();
        btnDeleteClassify = new javax.swing.JButton();
        btnSaveClassify = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jlbBack1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableClassify.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableClassify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClassifyMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableClassify);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel12.setText("Mã Danh mục:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel13.setText("Tên Danh Mục");

        btnRefreshClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefreshClassify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshClassifyMouseClicked(evt);
            }
        });
        btnRefreshClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshClassifyActionPerformed(evt);
            }
        });

        btnAddClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/add_23px.png"))); // NOI18N
        btnAddClassify.setText("Thêm");
        btnAddClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddClassifyActionPerformed(evt);
            }
        });

        btnChangeClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnChangeClassify.setEnabled(false);
        btnChangeClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeClassifyActionPerformed(evt);
            }
        });

        btnDeleteClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDeleteClassify.setEnabled(false);
        btnDeleteClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteClassifyActionPerformed(evt);
            }
        });

        btnSaveClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSaveClassify.setEnabled(false);
        btnSaveClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveClassifyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(492, 492, 492)
                .addComponent(btnRefreshClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddClassify)
                .addGap(18, 18, 18)
                .addComponent(btnChangeClassify)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteClassify)
                .addGap(18, 18, 18)
                .addComponent(btnSaveClassify)
                .addGap(18, 18, 18))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefreshClassify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSaveClassify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddClassify, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteClassify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChangeClassify, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txbIDClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txbClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txbClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txbIDClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Danh mục", jPanel3);

        lblStatus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText(" ");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("DANH MỤC");

        jlbBack1.setForeground(new java.awt.Color(51, 51, 51));
        jlbBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-home-20.png"))); // NOI18N
        jlbBack1.setText("Trang chủ");
        jlbBack1.setToolTipText("");
        jlbBack1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlbBack1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jlbBack1)
                        .addGap(123, 123, 123)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus))
        );

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

    private void btnSaveClassifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveClassifyActionPerformed
        if(Add==true)
        if(CheckClassify())
        addClassify();
        else    lblStatus.setText("Mã loại sản phẩm bạn nhập đã tồn tại!");
        else{
            if(Change==true)
            changedClassify();
        }
    }//GEN-LAST:event_btnSaveClassifyActionPerformed

    private void btnDeleteClassifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteClassifyActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa loại sản phẩm hay không?", "Thông Báo",2);
        if(Click ==JOptionPane.YES_OPTION){
            String sqlDelete="DELETE FROM LoaiSanPham WHERE ID=? AND LoaiSanPham=?";
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, txbIDClassify.getText());
                pst.setString(2,txbClassify.getText() );
                pst.executeUpdate();
                lblStatus.setText("Xóa loại sản phẩm thành công!");
                DisabledClassify();
                Refresh();
                loadClassify(Classify);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteClassifyActionPerformed

    private void btnChangeClassifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeClassifyActionPerformed
        Add=false;
        Change=true;
        btnAddClassify.setEnabled(false);
        btnChangeClassify.setEnabled(false);
        btnDeleteClassify.setEnabled(false);
        btnSaveClassify.setEnabled(true);
        EnabledClassify();
    }//GEN-LAST:event_btnChangeClassifyActionPerformed

    private void btnAddClassifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddClassifyActionPerformed
        Refresh();
        Add=true;
        btnAddClassify.setEnabled(false);
        btnSaveClassify.setEnabled(true);
        EnabledClassify();
    }//GEN-LAST:event_btnAddClassifyActionPerformed

    private void btnRefreshClassifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshClassifyMouseClicked
        Refresh();
    }//GEN-LAST:event_btnRefreshClassifyMouseClicked

    private void tableClassifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClassifyMouseClicked
        int Click=tableClassify.getSelectedRow();
        TableModel model=tableClassify.getModel();

        txbIDClassify.setText(model.getValueAt(Click,0).toString());
        txbClassify.setText(model.getValueAt(Click,1).toString());

        btnChangeClassify.setEnabled(true);
        btnDeleteClassify.setEnabled(true);
    }//GEN-LAST:event_tableClassifyMouseClicked

    private void jlbBack1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlbBack1MouseClicked
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
    }//GEN-LAST:event_jlbBack1MouseClicked

    private void btnRefreshClassifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshClassifyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshClassifyActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DanhMuc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DanhMuc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DanhMuc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DanhMuc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new DanhMuc(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddClassify;
    private javax.swing.JButton btnChangeClassify;
    private javax.swing.JButton btnDeleteClassify;
    private javax.swing.JButton btnRefreshClassify;
    private javax.swing.JButton btnSaveClassify;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlbBack1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableClassify;
    private javax.swing.JTextField txbClassify;
    private javax.swing.JTextField txbIDClassify;
    // End of variables declaration//GEN-END:variables
}

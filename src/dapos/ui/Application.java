 /**
 *
 * @author Dicko
 */
package dapos.ui;

import com.mysql.jdbc.Connection;
import dapos.tools.Koneksi;
import dapos.tools.LineColumn;
import dapos.tools.LineFormatter;
import dapos.tools.UserSession;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Application extends javax.swing.JFrame {
int waktumulai =0; 
String bil;
String pay;
String shift;
public double subtotal=0;
public double totalTax=0;
public double grandtotal=0;
public int ppn=0;
public double totalDisc=0;
public double subtotalDisc=0;
public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss a";
public List itembon = new ArrayList();
public List qtybon = new ArrayList();
public List pricebon = new ArrayList();
public List discItembon = new ArrayList();
public List Totalbon = new ArrayList();
public String noInv ;
public String payAmountreceipt;
public String payMethodereceipt;
public String subtotalreceipt;
public String grandtotalreceipt;
public String taxreceipt;
public String discreceipt;
public LineFormatter lf;
public LineFormatter ShiftPrint;
public String printerreceipt;
public String printersize;
public String cutterreceipt;
public String finishdocreceipt;
public String cashdrawerreceipt;
public String pathp2;
public String sizep2;
public String cutter2;
public String finish2;
public String pathp3;
public String sizep3;
public String cutter3;
public String finish3;
public String station;
public String header1;
public String header2;
public String header3;
public String footer1;
public String footer2;
public String footer3;
public String jabatan;
public int id_security;
public String cancel;
public String pending_security;
public String opendrawer_security;
public String open_price_security;
public String device_security;

    public Application(){
        initComponents();
        
        bil = "";
        pay = "";
        shift="";
        barcode.requestFocus();
        setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
        NamaKasir.setText(UserSession.getU_name());
        try {
            hostName.setText(InetAddress.getLocalHost().getHostName());
            station = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        security();
        fillcombo();
        fillItem();
        noInvoice();
        totaltax();
        printerreceipt();
        printer2();
        printer3();
        discountFrame();
        HFreceipt();
        clock();
     MenuPanel.setVisible(false);
     
    }
    private void security(){
    Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT * FROM `security` s WHERE s.`id` = '" + UserSession.getU_security() +"'";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
            security.setText(rs.getString("nama"));
            pending_security = rs.getString("pending");
            cancel = rs.getString("cancel");
            opendrawer_security = rs.getString("open_drawer");
            open_price_security = rs.getString("open_price");
            
        }
        else {System.out.print(sql);}
        } catch(Exception e){
        System.out.print( "SELECT * FROM `security` s WHERE s.`id` = '" + UserSession.getU_security() +"'");
        }
    }
    
      private void discountFrame(){
        discPanel.setLayout(new java.awt.GridLayout(5,5,5,5));
      Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT  d.`id`, d.`nama`, d.`discAmount`, d.`discByAmount`, d.`discPersen`, d.`discPersen_amount`, d.`discItem`, d.`discSubtotal`, GROUP_CONCAT(i.`id_item`) AS item, CONCAT( IFNULL(d.`itemId_1`,0), IFNULL(d.`itemId_2`,0), IFNULL(d.`itemId_3`,0), IFNULL(d.`itemId_4`,0), IFNULL(d.`itemId_5`,0), IFNULL(d.`itemId_6`,0), IFNULL(d.`itemId_7`,0), IFNULL(d.`itemId_8`,0), IFNULL(d.`itemId_9`,0), IFNULL(d.`itemId_10`,0)) AS item2 FROM `discount` d LEFT JOIN `category` cat ON d.`categoryId1`=cat.`id` OR d.`categoryId2`=cat.`id` OR d.`categoryId3`= cat.`id` OR d.`categoryId4` = cat.`id` OR d.`categoryId5`=cat.`id` LEFT JOIN `items` i ON i.`category`=cat.`id` GROUP BY d.`id`";
        rs = db.stm.executeQuery(sql);
        while (rs.next()){
            String list = rs.getString("item2")+rs.getString("item").replace(",", "");
            char[] item = list.toCharArray();
            String discPersen_amount = rs.getString("discPersen_amount");
            String discAmount = rs.getString("discAmount");
            JButton b = new JButton(String.valueOf(rs.getString("nama")));
            b.setBackground(new java.awt.Color(128, 196, 223));
            b.setFont(new java.awt.Font("Tahoma", 0, 24));
            if(Integer.parseInt(rs.getString("discPersen"))==1 && Integer.parseInt(rs.getString("discItem"))==1){
             b.addActionListener((java.awt.event.ActionEvent e) -> {
              for(int i = 0; i < jTable1.getRowCount(); i ++){
                int a = Integer.parseInt((String)jTable1.getValueAt(i, 6));
                for (int j=0; j< item.length; j++){
                if( a == Integer.parseInt(String.valueOf(item[j])) ){
                jTable1.setValueAt(discPersen_amount, i, 8);
                    int jumlahdisc = (int) ((Double.parseDouble((String)jTable1.getValueAt(i, 8))/100)*(Double.parseDouble((String)jTable1.getValueAt(i,2))*Double.parseDouble((String)jTable1.getValueAt(i,3))));
                String disc= String.valueOf(jumlahdisc);
                 int total = (Integer.parseInt((String)jTable1.getValueAt(i,2))*Integer.parseInt((String)jTable1.getValueAt(i,3))) - Integer.parseInt((String)disc);
                jTable1.setValueAt(disc, i , 4);
                jTable1.setValueAt(String.valueOf(total), i, 5);
                
                 }
                }
                subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
                Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
                discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
                GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
                sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
                discFrame.setVisible(false);
                barcode.requestFocus();
            }
            });
            }
            if(Integer.parseInt(rs.getString("discByAmount"))==1 && Integer.parseInt(rs.getString("discItem"))==1){
             b.addActionListener((java.awt.event.ActionEvent e) -> {
              for(int i = 0; i < jTable1.getRowCount(); i ++){
                int a = Integer.parseInt((String)jTable1.getValueAt(i, 6));
                for (int j=0; j< item.length; j++){
                if( a == Integer.parseInt(String.valueOf(item[j]))  ){
                int jumlahdisc = (int) (Double.parseDouble(discAmount));
                String disc= String.valueOf(jumlahdisc);
                 int total = Integer.parseInt((String)jTable1.getValueAt(i,5)) - Integer.parseInt((String)disc);
                jTable1.setValueAt(disc, i , 4);
                jTable1.setValueAt(String.valueOf(total), i, 5);
                 }}
                subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
                Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
                discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
                GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
                sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
                discFrame.setVisible(false);
                barcode.requestFocus();
            }
            });
            }
            if(Integer.parseInt(rs.getString("discSubtotal"))==1 && Integer.parseInt(rs.getString("discPersen"))==1){
            b.addActionListener((java.awt.event.ActionEvent e) ->{
            discount.setText(NumberFormat.getNumberInstance().format(this.subTotalDisc(subtotalDisc=Double.parseDouble(discPersen_amount))));
            GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
            sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
            discFrame.setVisible(false);
            barcode.requestFocus();
            });
            }
            discPanel.add(b);
        }
        }catch(Exception e){
        System.out.println("gagal load discount");
        }
        
     }
    
      private void fillcombo(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
            sql = "select * from payment_method";
            rs = db.stm.executeQuery(sql);
            while(rs.next()){
            String nama = rs.getString("nama");
            PayMethod.addItem(nama);
        }        
    } catch(SQLException e){
        System.out.println("gagal load payment method");
    }
    
    }
      private void noInvoice(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT IF(MAX(id) IS NULL, \"0\", MAX(id) ) AS id FROM `sales_invoice`";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
        int NoInv = Integer.parseInt(rs.getString("id")) + 1;
        noInvoice.setText(String.valueOf(NoInv));
        }
        
        } catch (Exception e){
        }
      }
      private void fillsales(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String scanBarcode;
        scanBarcode = barcode.getText();
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "Select * from items i where barcode = '"+scanBarcode+"'and i.active is true and i.sales is true" ;
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
        String[] data = new String[11];
        data[0] = rs.getString("kode");
        data[1] = rs.getString("nama");
        int qty = 1;
        int harga = rs.getInt("unit_price");
        data[2] = String.valueOf(harga) ;
        data[3] = String.valueOf(qty);
        data[4] = "0";
        data[5] = String.valueOf((Integer.parseInt(data[2]) * Integer.parseInt(data[3])) - Integer.parseInt(data[4]));
        data[6] = rs.getString("id_item");
        data[7] = rs.getString("barcode");
        data[8] = "0";
        data[9] = rs.getString("unit_cost");
        data[10] = rs.getString("stocked");
        
        DefaultTableModel tbl = (DefaultTableModel) this.jTable1.getModel();
        tbl.addRow(data);
        subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
        Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
        GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
        sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
        }else{
        JOptionPane.showMessageDialog(this, "barang tidak ada");
        }
        }catch(SQLException e){
        JOptionPane.showMessageDialog(this, "barang tidak ada");
        }
        barcode.setText("");
      }
      
      private void fillItem(){
      Connection con;
        Statement stat;
        ResultSet rs;
        String scanBarcode;
        scanBarcode = barcode.getText();
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT * FROM items WHERE active= TRUE AND sales = TRUE";
        rs = db.stm.executeQuery(sql);
        while(rs.next()){
            String[] data = new String[7];
            data[0] = rs.getString("kode");
            data[1] = rs.getString("nama");
            data[2] = rs.getString("unit_price");
            data[3] = rs.getString("id_item");
            data[4] = rs.getString("barcode");
            data[5] = rs.getString("unit_cost");
            data[6] = rs.getString("stocked");
            DefaultTableModel tbl = (DefaultTableModel) this.tableItem.getModel();
        tbl.addRow(data);
        }
        
        } catch(Exception e) {
        System.out.println("gagal load item");
        }
      }
    public void printerreceipt(){
    String terminal =station;
    Connection con;
    Statement stat;
        ResultSet rs;
        String scanBarcode;
        scanBarcode = barcode.getText();
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT p.`path`, p.`size` FROM `stations` s LEFT JOIN `printers` p ON s.`printerstruk`=p.`id` WHERE s.`description`='"+station+"'";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
        printerreceipt = rs.getString("path");
        printersize = rs.getString("size");
        }
        }catch(Exception e){}
    }
    
        public void printer2(){
    String terminal =station;
          Connection con;
        Statement stat;
        ResultSet rs;
        String scanBarcode;
        scanBarcode = barcode.getText();
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT p.`path`, p.`size` FROM `stations` s LEFT JOIN `printers` p ON s.`printerreport`=p.`id` WHERE s.`description`='"+station+"'";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
        pathp2 = rs.getString("path");
        sizep2 = rs.getString("size");
        }
        }catch(Exception e){}
    }
        
    public void printer3(){
    String terminal =station;
        Connection con;
        Statement stat;
        ResultSet rs;
        String scanBarcode;
        scanBarcode = barcode.getText();
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT p.`path`, p.`size`FROM `stations` s LEFT JOIN `printers` p ON s.`printer_3`=p.`id` WHERE s.`description`='"+station+"'";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
        pathp3 = rs.getString("path");
        sizep3 = rs.getString("size");
        finish3 = rs.getString("finishdoc");
        cutter3 = rs.getString("cutter");
        }
        }catch(Exception e){}
    }
    
    public void HFreceipt(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String scanBarcode;
        scanBarcode = barcode.getText();
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "select * from receipt";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
        header1 = (rs.getString("header1") == null)? "" :rs.getString("header1") ;
        header2 = (rs.getString("header2") == null)? "" :rs.getString("header2") ;
        header3 = (rs.getString("header3") == null)? "" :rs.getString("header3") ;
        footer1 = (rs.getString("footer1") == null)? "" :rs.getString("footer1");
        footer2 = (rs.getString("footer2") == null)? "" :rs.getString("footer2");
        footer3 = (rs.getString("footer3") == null)? "" :rs.getString("footer3");
        }
        }catch(Exception e){}
    }
    
    private void clock(){
    java.util.Date skrg = new java.util.Date();
    java.text.SimpleDateFormat kal = new
    java.text.SimpleDateFormat("dd MMMMMMMM yyyy");
    new Thread(){
            @Override
            public void run(){
              while(waktumulai == 0){
                Calendar kalender = new GregorianCalendar();
                    int jam1 = kalender.get(Calendar.HOUR);
                    int menit = kalender.get(Calendar.MINUTE);
                    int detik = kalender.get(Calendar.SECOND);
                    int AM_PM = kalender.get(Calendar.AM_PM);
                    String siang_malam ="";
             if(AM_PM == 1){
                    siang_malam="PM"; 
             }else{
                    siang_malam = "AM";   
                  }
             String time =kal.format(skrg) + " - " + jam1 + ":" + menit + ":" + detik + " " + siang_malam;
             date.setText(time);               
              }  
            }
        }.start();
    
}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        qtyFrame = new javax.swing.JFrame();
        jPanel7 = new javax.swing.JPanel();
        button1 = new javax.swing.JButton();
        button3 = new javax.swing.JButton();
        button2 = new javax.swing.JButton();
        button4 = new javax.swing.JButton();
        button5 = new javax.swing.JButton();
        button6 = new javax.swing.JButton();
        button7 = new javax.swing.JButton();
        button8 = new javax.swing.JButton();
        button9 = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        button0 = new javax.swing.JButton();
        buttonBackspace = new javax.swing.JButton();
        qtyText = new javax.swing.JFormattedTextField();
        ButtonOK = new javax.swing.JButton();
        ButtonCancel = new javax.swing.JButton();
        PayFrame = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        buttonPay1 = new javax.swing.JButton();
        buttonPay3 = new javax.swing.JButton();
        buttonPay2 = new javax.swing.JButton();
        buttonPay4 = new javax.swing.JButton();
        buttonPay5 = new javax.swing.JButton();
        buttonPay6 = new javax.swing.JButton();
        buttonPay7 = new javax.swing.JButton();
        buttonPay8 = new javax.swing.JButton();
        buttonPay9 = new javax.swing.JButton();
        buttonPayClear = new javax.swing.JButton();
        buttonPay0 = new javax.swing.JButton();
        buttonPayBackspace = new javax.swing.JButton();
        payText = new javax.swing.JFormattedTextField();
        PayMethod = new javax.swing.JComboBox<>();
        ButtonPayOK = new javax.swing.JButton();
        ButtonPayCancel = new javax.swing.JButton();
        ButtonPay000 = new javax.swing.JButton();
        ButtonPay00 = new javax.swing.JButton();
        itemlist = new javax.swing.JFrame();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableItem = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        searchbox = new javax.swing.JTextField();
        buttonItemCancel = new javax.swing.JButton();
        buttonItemOK = new javax.swing.JButton();
        discFrame = new javax.swing.JFrame();
        discPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        discCancel = new javax.swing.JButton();
        changeFrame = new javax.swing.JFrame();
        change = new javax.swing.JLabel();
        changeOK = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        openingFrame = new javax.swing.JFrame();
        cancelOpen = new javax.swing.JButton();
        openOK = new javax.swing.JButton();
        openText = new javax.swing.JLabel();
        dateOpen = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        openBy = new javax.swing.JLabel();
        sudahBuka = new javax.swing.JFrame();
        sudahButton = new javax.swing.JButton();
        sudahLabel2 = new javax.swing.JLabel();
        sudahLabel = new javax.swing.JLabel();
        EODFrame = new javax.swing.JFrame();
        cancelOpen1 = new javax.swing.JButton();
        openOK1 = new javax.swing.JButton();
        openText1 = new javax.swing.JLabel();
        dateEOD = new javax.swing.JLabel();
        OpeningShiftFrame = new javax.swing.JFrame();
        jPanel13 = new javax.swing.JPanel();
        buttonopen1 = new javax.swing.JButton();
        buttonopen3 = new javax.swing.JButton();
        buttonopen2 = new javax.swing.JButton();
        buttonopen4 = new javax.swing.JButton();
        buttonopen5 = new javax.swing.JButton();
        buttonopen6 = new javax.swing.JButton();
        buttonopen7 = new javax.swing.JButton();
        buttonopen8 = new javax.swing.JButton();
        buttonopen9 = new javax.swing.JButton();
        buttonopenclear = new javax.swing.JButton();
        buttonopen0 = new javax.swing.JButton();
        buttonopenBS = new javax.swing.JButton();
        openshifttext = new javax.swing.JFormattedTextField();
        buttonopenok = new javax.swing.JButton();
        buttonopencancel = new javax.swing.JButton();
        buttonopen000 = new javax.swing.JButton();
        buttonopen00 = new javax.swing.JButton();
        errorprint = new javax.swing.JFrame();
        errorprintOK = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        openshift = new javax.swing.JFrame();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        opentextarea = new javax.swing.JTextArea();
        closeopenshift = new javax.swing.JButton();
        openshiftprint = new javax.swing.JButton();
        closingShiftFrame = new javax.swing.JFrame();
        jPanel15 = new javax.swing.JPanel();
        buttonclose1 = new javax.swing.JButton();
        buttonclose3 = new javax.swing.JButton();
        buttonclose2 = new javax.swing.JButton();
        buttonclose4 = new javax.swing.JButton();
        buttonclose5 = new javax.swing.JButton();
        buttonclose6 = new javax.swing.JButton();
        buttonclose7 = new javax.swing.JButton();
        buttonclose8 = new javax.swing.JButton();
        buttonclose9 = new javax.swing.JButton();
        buttoncloseclear = new javax.swing.JButton();
        buttonclose0 = new javax.swing.JButton();
        buttoncloseBS = new javax.swing.JButton();
        closeshifttext = new javax.swing.JFormattedTextField();
        buttoncloseok = new javax.swing.JButton();
        buttonclosecancel = new javax.swing.JButton();
        buttonclose000 = new javax.swing.JButton();
        buttonclose00 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        SalesPanel = new javax.swing.JPanel();
        noInvoice = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        GrandTotal = new javax.swing.JTextField();
        barcode = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        discount = new javax.swing.JTextField();
        Pajak = new javax.swing.JTextField();
        subTotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sisatext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        no1 = new javax.swing.JButton();
        no3 = new javax.swing.JButton();
        no2 = new javax.swing.JButton();
        no4 = new javax.swing.JButton();
        no5 = new javax.swing.JButton();
        no6 = new javax.swing.JButton();
        no7 = new javax.swing.JButton();
        no8 = new javax.swing.JButton();
        no9 = new javax.swing.JButton();
        qty = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        paytable = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        hapus = new javax.swing.JButton();
        diskon = new javax.swing.JButton();
        caribarang = new javax.swing.JButton();
        bayar = new javax.swing.JButton();
        pending = new javax.swing.JButton();
        customerName = new javax.swing.JTextField();
        lookupCustomer = new javax.swing.JButton();
        MenuPanel = new javax.swing.JPanel();
        endofday = new javax.swing.JButton();
        openingday = new javax.swing.JButton();
        OpenShift = new javax.swing.JButton();
        closingshift = new javax.swing.JButton();
        opendrawer = new javax.swing.JButton();
        PayIn = new javax.swing.JButton();
        PayOut = new javax.swing.JButton();
        pendingsls = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        salespanel = new javax.swing.JButton();
        menubutton = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        NamaKasir = new javax.swing.JLabel();
        hostName = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        security = new javax.swing.JLabel();

        qtyFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        qtyFrame.setTitle("JUMLAH");
        qtyFrame.setFocusCycleRoot(false);
        qtyFrame.setLocation(new java.awt.Point(0, 0));
        qtyFrame.setMinimumSize(new java.awt.Dimension(470, 360));
        qtyFrame.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        qtyFrame.setType(java.awt.Window.Type.UTILITY);

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 196, 223)));

        button1.setBackground(new java.awt.Color(128, 196, 223));
        button1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button1.setText("1");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button3.setBackground(new java.awt.Color(128, 204, 223));
        button3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button3.setText("3");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button2.setBackground(new java.awt.Color(128, 196, 223));
        button2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button2.setText("2");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        button4.setBackground(new java.awt.Color(128, 196, 223));
        button4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button4.setText("4");
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        button5.setBackground(new java.awt.Color(128, 196, 223));
        button5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button5.setText("5");
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        button6.setBackground(new java.awt.Color(128, 196, 223));
        button6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button6.setText("6");
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button6ActionPerformed(evt);
            }
        });

        button7.setBackground(new java.awt.Color(128, 196, 223));
        button7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button7.setText("7");
        button7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button7ActionPerformed(evt);
            }
        });

        button8.setBackground(new java.awt.Color(128, 196, 223));
        button8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button8.setText("8");
        button8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button8ActionPerformed(evt);
            }
        });

        button9.setBackground(new java.awt.Color(128, 196, 223));
        button9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button9.setText("9");
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });

        buttonClear.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonClear.setText("CLEAR");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        button0.setBackground(new java.awt.Color(128, 196, 223));
        button0.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        button0.setText("0");
        button0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button0ActionPerformed(evt);
            }
        });

        buttonBackspace.setBackground(new java.awt.Color(255, 255, 0));
        buttonBackspace.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonBackspace.setText("BACKSPACE");
        buttonBackspace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBackspaceActionPerformed(evt);
            }
        });

        qtyText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        qtyText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtyText.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        qtyText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyTextActionPerformed(evt);
            }
        });
        qtyText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtyTextKeyPressed(evt);
            }
        });

        ButtonOK.setBackground(new java.awt.Color(0, 204, 51));
        ButtonOK.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        ButtonOK.setText("OK");
        ButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonOKActionPerformed(evt);
            }
        });

        ButtonCancel.setBackground(new java.awt.Color(255, 0, 51));
        ButtonCancel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        ButtonCancel.setText("Cancel");
        ButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(239, Short.MAX_VALUE)
                .addComponent(buttonBackspace, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(qtyText)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(button8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button0, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(button6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ButtonOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ButtonCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addContainerGap()))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(256, Short.MAX_VALUE)
                .addComponent(buttonBackspace, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(qtyText, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(ButtonCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addComponent(ButtonOK, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)))
                    .addContainerGap()))
        );

        javax.swing.GroupLayout qtyFrameLayout = new javax.swing.GroupLayout(qtyFrame.getContentPane());
        qtyFrame.getContentPane().setLayout(qtyFrameLayout);
        qtyFrameLayout.setHorizontalGroup(
            qtyFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qtyFrameLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        qtyFrameLayout.setVerticalGroup(
            qtyFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qtyFrameLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        PayFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        PayFrame.setTitle("BAYAR");
        PayFrame.setBackground(new java.awt.Color(128, 196, 223));
        PayFrame.setFocusCycleRoot(false);
        PayFrame.setLocation(new java.awt.Point(0, 0));
        PayFrame.setMinimumSize(new java.awt.Dimension(480, 400));
        PayFrame.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        PayFrame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PayFrameKeyPressed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(128, 196, 223), new java.awt.Color(153, 153, 153)));

        buttonPay1.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay1.setText("1");
        buttonPay1.setBorder(null);
        buttonPay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay1ActionPerformed(evt);
            }
        });
        buttonPay1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buttonPay1KeyPressed(evt);
            }
        });

        buttonPay3.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay3.setText("3");
        buttonPay3.setBorder(null);
        buttonPay3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay3ActionPerformed(evt);
            }
        });

        buttonPay2.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay2.setText("2");
        buttonPay2.setBorder(null);
        buttonPay2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay2ActionPerformed(evt);
            }
        });

        buttonPay4.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay4.setText("4");
        buttonPay4.setBorder(null);
        buttonPay4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay4ActionPerformed(evt);
            }
        });

        buttonPay5.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay5.setText("5");
        buttonPay5.setBorder(null);
        buttonPay5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay5ActionPerformed(evt);
            }
        });

        buttonPay6.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay6.setText("6");
        buttonPay6.setBorder(null);
        buttonPay6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay6ActionPerformed(evt);
            }
        });

        buttonPay7.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay7.setText("7");
        buttonPay7.setBorder(null);
        buttonPay7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay7ActionPerformed(evt);
            }
        });

        buttonPay8.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay8.setText("8");
        buttonPay8.setBorder(null);
        buttonPay8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay8ActionPerformed(evt);
            }
        });

        buttonPay9.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay9.setText("9");
        buttonPay9.setBorder(null);
        buttonPay9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay9ActionPerformed(evt);
            }
        });

        buttonPayClear.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPayClear.setText("CLEAR");
        buttonPayClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPayClearActionPerformed(evt);
            }
        });

        buttonPay0.setBackground(new java.awt.Color(128, 196, 223));
        buttonPay0.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonPay0.setText("0");
        buttonPay0.setBorder(null);
        buttonPay0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPay0ActionPerformed(evt);
            }
        });

        buttonPayBackspace.setBackground(new java.awt.Color(255, 204, 0));
        buttonPayBackspace.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonPayBackspace.setText("BACKSPACE");
        buttonPayBackspace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPayBackspaceActionPerformed(evt);
            }
        });

        payText.setEditable(false);
        payText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        payText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        payText.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        payText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payTextActionPerformed(evt);
            }
        });
        payText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                payTextKeyPressed(evt);
            }
        });

        PayMethod.setBackground(new java.awt.Color(128, 196, 223));
        PayMethod.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        PayMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayMethodActionPerformed(evt);
            }
        });

        ButtonPayOK.setBackground(new java.awt.Color(0, 204, 102));
        ButtonPayOK.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        ButtonPayOK.setText("OK");
        ButtonPayOK.setToolTipText("");
        ButtonPayOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPayOKActionPerformed(evt);
            }
        });

        ButtonPayCancel.setBackground(new java.awt.Color(255, 0, 0));
        ButtonPayCancel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ButtonPayCancel.setText("CANCEL");
        ButtonPayCancel.setToolTipText("");
        ButtonPayCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPayCancelActionPerformed(evt);
            }
        });

        ButtonPay000.setBackground(new java.awt.Color(128, 196, 223));
        ButtonPay000.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        ButtonPay000.setText("000");
        ButtonPay000.setBorder(null);
        ButtonPay000.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPay000ActionPerformed(evt);
            }
        });

        ButtonPay00.setBackground(new java.awt.Color(128, 196, 223));
        ButtonPay00.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        ButtonPay00.setText("00");
        ButtonPay00.setBorder(null);
        ButtonPay00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPay00ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonPay7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonPay1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonPay4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonPay00, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonPay8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPay0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPay5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPay2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ButtonPay000, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(buttonPay6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonPay3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonPay9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonPayBackspace, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(buttonPayClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonPayCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonPayOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(PayMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(payText))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(payText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonPay2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPay3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPayClear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPay5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonPay4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonPay6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttonPayBackspace, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonPay8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonPay9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonPay7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ButtonPayCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonPay1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonPayOK, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(buttonPay0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonPay000, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonPay00, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PayFrameLayout = new javax.swing.GroupLayout(PayFrame.getContentPane());
        PayFrame.getContentPane().setLayout(PayFrameLayout);
        PayFrameLayout.setHorizontalGroup(
            PayFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PayFrameLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        PayFrameLayout.setVerticalGroup(
            PayFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PayFrameLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        itemlist.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        itemlist.setTitle("DAFTAR BARANG");
        itemlist.setMinimumSize(new java.awt.Dimension(777, 400));

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(128, 196, 223)));

        tableItem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tableItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Nama", "Harga", "item_id", "barcode", "unit_cost", "stocked"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableItem.setColumnSelectionAllowed(true);
        tableItem.getTableHeader().setFont(new java.awt.Font("Tahoma", 0, 18));
        tableItem.setRowHeight(30);
        jScrollPane3.setViewportView(tableItem);
        tableItem.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tableItem.getColumnModel().getColumnCount() > 0) {
            tableItem.getColumnModel().getColumn(3).setMinWidth(0);
            tableItem.getColumnModel().getColumn(3).setMaxWidth(0);
            tableItem.getColumnModel().getColumn(4).setMinWidth(0);
            tableItem.getColumnModel().getColumn(4).setMaxWidth(0);
            tableItem.getColumnModel().getColumn(5).setMinWidth(0);
            tableItem.getColumnModel().getColumn(5).setMaxWidth(0);
            tableItem.getColumnModel().getColumn(6).setMinWidth(0);
            tableItem.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("DAFTAR BARANG");

        searchbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchboxKeyReleased(evt);
            }
        });

        buttonItemCancel.setBackground(new java.awt.Color(255, 51, 51));
        buttonItemCancel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonItemCancel.setText("CANCEL");
        buttonItemCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonItemCancelActionPerformed(evt);
            }
        });

        buttonItemOK.setBackground(new java.awt.Color(128, 196, 223));
        buttonItemOK.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonItemOK.setText("OK");
        buttonItemOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonItemOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchbox)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(buttonItemCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonItemOK, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonItemCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(buttonItemOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout itemlistLayout = new javax.swing.GroupLayout(itemlist.getContentPane());
        itemlist.getContentPane().setLayout(itemlistLayout);
        itemlistLayout.setHorizontalGroup(
            itemlistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        itemlistLayout.setVerticalGroup(
            itemlistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        discFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        discFrame.setTitle("DISCOUNT");
        discFrame.setMinimumSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout discPanelLayout = new javax.swing.GroupLayout(discPanel);
        discPanel.setLayout(discPanelLayout);
        discPanelLayout.setHorizontalGroup(
            discPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        discPanelLayout.setVerticalGroup(
            discPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
        );

        discCancel.setBackground(new java.awt.Color(255, 51, 51));
        discCancel.setText("CANCEL");
        discCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(discCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(discCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout discFrameLayout = new javax.swing.GroupLayout(discFrame.getContentPane());
        discFrame.getContentPane().setLayout(discFrameLayout);
        discFrameLayout.setHorizontalGroup(
            discFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(discFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        discFrameLayout.setVerticalGroup(
            discFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(discPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        changeFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        changeFrame.setLocation(new java.awt.Point(0, 0));
        changeFrame.setMinimumSize(new java.awt.Dimension(510, 510));

        change.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        change.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        changeOK.setText("OK");
        changeOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeOKActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Kembalian");

        javax.swing.GroupLayout changeFrameLayout = new javax.swing.GroupLayout(changeFrame.getContentPane());
        changeFrame.getContentPane().setLayout(changeFrameLayout);
        changeFrameLayout.setHorizontalGroup(
            changeFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(changeOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(changeFrameLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(change, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        changeFrameLayout.setVerticalGroup(
            changeFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(change, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeOK, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        openingFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        openingFrame.setLocation(new java.awt.Point(0, 0));
        openingFrame.setLocationByPlatform(true);
        openingFrame.setMinimumSize(new java.awt.Dimension(593, 186));
        openingFrame.setSize(new java.awt.Dimension(0, 0));
        openingFrame.setType(java.awt.Window.Type.UTILITY);

        cancelOpen.setBackground(new java.awt.Color(255, 102, 102));
        cancelOpen.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cancelOpen.setText("CANCEL");
        cancelOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelOpenActionPerformed(evt);
            }
        });

        openOK.setBackground(new java.awt.Color(127, 196, 223));
        openOK.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        openOK.setText("OK");
        openOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openOKActionPerformed(evt);
            }
        });

        openText.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        openText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        openText.setText("Opening Procedures at ");

        dateOpen.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("by");

        openBy.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout openingFrameLayout = new javax.swing.GroupLayout(openingFrame.getContentPane());
        openingFrame.getContentPane().setLayout(openingFrameLayout);
        openingFrameLayout.setHorizontalGroup(
            openingFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(openingFrameLayout.createSequentialGroup()
                .addGroup(openingFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(openingFrameLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(openOK, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 160, Short.MAX_VALUE))
                    .addGroup(openingFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(openText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateOpen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openBy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        openingFrameLayout.setVerticalGroup(
            openingFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, openingFrameLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(openingFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openBy, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(openText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(openingFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openOK, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        sudahBuka.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        sudahBuka.setMinimumSize(new java.awt.Dimension(600, 150));
        sudahBuka.setType(java.awt.Window.Type.UTILITY);

        sudahButton.setBackground(new java.awt.Color(127, 196, 223));
        sudahButton.setText("OK");
        sudahButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudahButtonActionPerformed(evt);
            }
        });

        sudahLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sudahLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        sudahLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        sudahLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout sudahBukaLayout = new javax.swing.GroupLayout(sudahBuka.getContentPane());
        sudahBuka.getContentPane().setLayout(sudahBukaLayout);
        sudahBukaLayout.setHorizontalGroup(
            sudahBukaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sudahBukaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sudahBukaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sudahBukaLayout.createSequentialGroup()
                        .addGap(0, 220, Short.MAX_VALUE)
                        .addComponent(sudahButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 220, Short.MAX_VALUE))
                    .addComponent(sudahLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sudahLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        sudahBukaLayout.setVerticalGroup(
            sudahBukaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sudahBukaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sudahLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(sudahLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sudahButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        EODFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        EODFrame.setLocation(new java.awt.Point(0, 0));
        EODFrame.setLocationByPlatform(true);
        EODFrame.setMinimumSize(new java.awt.Dimension(593, 186));
        EODFrame.setSize(new java.awt.Dimension(0, 0));
        EODFrame.setType(java.awt.Window.Type.UTILITY);

        cancelOpen1.setBackground(new java.awt.Color(255, 102, 102));
        cancelOpen1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cancelOpen1.setText("CANCEL");
        cancelOpen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelOpen1ActionPerformed(evt);
            }
        });

        openOK1.setBackground(new java.awt.Color(127, 196, 223));
        openOK1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        openOK1.setText("OK");
        openOK1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openOK1ActionPerformed(evt);
            }
        });

        openText1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        openText1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        openText1.setText("End Of Day Procedures at ");

        dateEOD.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout EODFrameLayout = new javax.swing.GroupLayout(EODFrame.getContentPane());
        EODFrame.getContentPane().setLayout(EODFrameLayout);
        EODFrameLayout.setHorizontalGroup(
            EODFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EODFrameLayout.createSequentialGroup()
                .addGroup(EODFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EODFrameLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(openOK1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelOpen1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EODFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(openText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateEOD)))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        EODFrameLayout.setVerticalGroup(
            EODFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EODFrameLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(EODFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openText1, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(dateEOD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(EODFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelOpen1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openOK1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        OpeningShiftFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        OpeningShiftFrame.setTitle("OPENING SHIFT");
        OpeningShiftFrame.setBackground(new java.awt.Color(128, 196, 223));
        OpeningShiftFrame.setFocusCycleRoot(false);
        OpeningShiftFrame.setLocation(new java.awt.Point(0, 0));
        OpeningShiftFrame.setMinimumSize(new java.awt.Dimension(480, 400));
        OpeningShiftFrame.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        OpeningShiftFrame.setType(java.awt.Window.Type.UTILITY);
        OpeningShiftFrame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OpeningShiftFrameKeyPressed(evt);
            }
        });

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(128, 196, 223), new java.awt.Color(153, 153, 153)));

        buttonopen1.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen1.setText("1");
        buttonopen1.setBorder(null);
        buttonopen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen1ActionPerformed(evt);
            }
        });
        buttonopen1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buttonopen1KeyPressed(evt);
            }
        });

        buttonopen3.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen3.setText("3");
        buttonopen3.setBorder(null);
        buttonopen3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen3ActionPerformed(evt);
            }
        });

        buttonopen2.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen2.setText("2");
        buttonopen2.setBorder(null);
        buttonopen2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen2ActionPerformed(evt);
            }
        });

        buttonopen4.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen4.setText("4");
        buttonopen4.setBorder(null);
        buttonopen4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen4ActionPerformed(evt);
            }
        });

        buttonopen5.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen5.setText("5");
        buttonopen5.setBorder(null);
        buttonopen5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen5ActionPerformed(evt);
            }
        });

        buttonopen6.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen6.setText("6");
        buttonopen6.setBorder(null);
        buttonopen6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen6ActionPerformed(evt);
            }
        });

        buttonopen7.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen7.setText("7");
        buttonopen7.setBorder(null);
        buttonopen7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen7ActionPerformed(evt);
            }
        });

        buttonopen8.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen8.setText("8");
        buttonopen8.setBorder(null);
        buttonopen8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen8ActionPerformed(evt);
            }
        });

        buttonopen9.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen9.setText("9");
        buttonopen9.setBorder(null);
        buttonopen9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen9ActionPerformed(evt);
            }
        });

        buttonopenclear.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopenclear.setText("CLEAR");
        buttonopenclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopenclearActionPerformed(evt);
            }
        });

        buttonopen0.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen0.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen0.setText("0");
        buttonopen0.setBorder(null);
        buttonopen0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen0ActionPerformed(evt);
            }
        });

        buttonopenBS.setBackground(new java.awt.Color(255, 204, 0));
        buttonopenBS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonopenBS.setText("BACKSPACE");
        buttonopenBS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopenBSActionPerformed(evt);
            }
        });

        openshifttext.setEditable(false);
        openshifttext.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        openshifttext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        openshifttext.setText("0");
        openshifttext.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        openshifttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openshifttextActionPerformed(evt);
            }
        });
        openshifttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                openshifttextKeyPressed(evt);
            }
        });

        buttonopenok.setBackground(new java.awt.Color(0, 204, 102));
        buttonopenok.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopenok.setText("OK");
        buttonopenok.setToolTipText("");
        buttonopenok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopenokActionPerformed(evt);
            }
        });

        buttonopencancel.setBackground(new java.awt.Color(255, 0, 0));
        buttonopencancel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        buttonopencancel.setText("CANCEL");
        buttonopencancel.setToolTipText("");
        buttonopencancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopencancelActionPerformed(evt);
            }
        });

        buttonopen000.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen000.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen000.setText("000");
        buttonopen000.setBorder(null);
        buttonopen000.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen000ActionPerformed(evt);
            }
        });

        buttonopen00.setBackground(new java.awt.Color(128, 196, 223));
        buttonopen00.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonopen00.setText("00");
        buttonopen00.setBorder(null);
        buttonopen00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonopen00ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonopen7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopen1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopen4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopen00, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonopen8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonopen0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonopen5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonopen2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonopen000, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(buttonopen6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopen3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopen9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonopenBS, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(buttonopenclear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopencancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonopenok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(openshifttext))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(openshifttext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonopen2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonopen3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonopenclear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonopen5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonopen4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonopen6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttonopenBS, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonopen8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonopen9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonopen7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttonopencancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonopen1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonopenok, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(buttonopen0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonopen000, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonopen00, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout OpeningShiftFrameLayout = new javax.swing.GroupLayout(OpeningShiftFrame.getContentPane());
        OpeningShiftFrame.getContentPane().setLayout(OpeningShiftFrameLayout);
        OpeningShiftFrameLayout.setHorizontalGroup(
            OpeningShiftFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OpeningShiftFrameLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        OpeningShiftFrameLayout.setVerticalGroup(
            OpeningShiftFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OpeningShiftFrameLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        errorprint.setLocationByPlatform(true);
        errorprint.setMinimumSize(new java.awt.Dimension(484, 300));

        errorprintOK.setText("OK");
        errorprintOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorprintOKActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel10.setText("Printer Tidak Di Temukan");

        javax.swing.GroupLayout errorprintLayout = new javax.swing.GroupLayout(errorprint.getContentPane());
        errorprint.getContentPane().setLayout(errorprintLayout);
        errorprintLayout.setHorizontalGroup(
            errorprintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(errorprintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(errorprintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(errorprintOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        errorprintLayout.setVerticalGroup(
            errorprintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, errorprintLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(errorprintOK, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );

        openshift.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        openshift.setLocationByPlatform(true);
        openshift.setMinimumSize(new java.awt.Dimension(400, 300));
        openshift.setType(java.awt.Window.Type.POPUP);

        opentextarea.setEditable(false);
        opentextarea.setColumns(20);
        opentextarea.setRows(5);
        jScrollPane2.setViewportView(opentextarea);

        closeopenshift.setBackground(new java.awt.Color(124, 194, 222));
        closeopenshift.setText("CLOSE");
        closeopenshift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeopenshiftActionPerformed(evt);
            }
        });

        openshiftprint.setBackground(new java.awt.Color(255, 101, 101));
        openshiftprint.setText("PRINT");
        openshiftprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openshiftprintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(closeopenshift, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(openshiftprint, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeopenshift, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(openshiftprint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout openshiftLayout = new javax.swing.GroupLayout(openshift.getContentPane());
        openshift.getContentPane().setLayout(openshiftLayout);
        openshiftLayout.setHorizontalGroup(
            openshiftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        openshiftLayout.setVerticalGroup(
            openshiftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        closingShiftFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        closingShiftFrame.setTitle("OPENING SHIFT");
        closingShiftFrame.setBackground(new java.awt.Color(128, 196, 223));
        closingShiftFrame.setFocusCycleRoot(false);
        closingShiftFrame.setLocation(new java.awt.Point(0, 0));
        closingShiftFrame.setMinimumSize(new java.awt.Dimension(480, 400));
        closingShiftFrame.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        closingShiftFrame.setType(java.awt.Window.Type.UTILITY);
        closingShiftFrame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closingShiftFrameKeyPressed(evt);
            }
        });

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(128, 196, 223), new java.awt.Color(153, 153, 153)));

        buttonclose1.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose1.setText("1");
        buttonclose1.setBorder(null);
        buttonclose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose1ActionPerformed(evt);
            }
        });
        buttonclose1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buttonclose1KeyPressed(evt);
            }
        });

        buttonclose3.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose3.setText("3");
        buttonclose3.setBorder(null);
        buttonclose3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose3ActionPerformed(evt);
            }
        });

        buttonclose2.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose2.setText("2");
        buttonclose2.setBorder(null);
        buttonclose2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose2ActionPerformed(evt);
            }
        });

        buttonclose4.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose4.setText("4");
        buttonclose4.setBorder(null);
        buttonclose4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose4ActionPerformed(evt);
            }
        });

        buttonclose5.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose5.setText("5");
        buttonclose5.setBorder(null);
        buttonclose5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose5ActionPerformed(evt);
            }
        });

        buttonclose6.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose6.setText("6");
        buttonclose6.setBorder(null);
        buttonclose6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose6ActionPerformed(evt);
            }
        });

        buttonclose7.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose7.setText("7");
        buttonclose7.setBorder(null);
        buttonclose7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose7ActionPerformed(evt);
            }
        });

        buttonclose8.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose8.setText("8");
        buttonclose8.setBorder(null);
        buttonclose8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose8ActionPerformed(evt);
            }
        });

        buttonclose9.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose9.setText("9");
        buttonclose9.setBorder(null);
        buttonclose9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose9ActionPerformed(evt);
            }
        });

        buttoncloseclear.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttoncloseclear.setText("CLEAR");
        buttoncloseclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttoncloseclearActionPerformed(evt);
            }
        });

        buttonclose0.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose0.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose0.setText("0");
        buttonclose0.setBorder(null);
        buttonclose0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose0ActionPerformed(evt);
            }
        });

        buttoncloseBS.setBackground(new java.awt.Color(255, 204, 0));
        buttoncloseBS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttoncloseBS.setText("BACKSPACE");
        buttoncloseBS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttoncloseBSActionPerformed(evt);
            }
        });

        closeshifttext.setEditable(false);
        closeshifttext.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        closeshifttext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        closeshifttext.setText("0");
        closeshifttext.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        closeshifttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeshifttextActionPerformed(evt);
            }
        });
        closeshifttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closeshifttextKeyPressed(evt);
            }
        });

        buttoncloseok.setBackground(new java.awt.Color(0, 204, 102));
        buttoncloseok.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttoncloseok.setText("OK");
        buttoncloseok.setToolTipText("");
        buttoncloseok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttoncloseokActionPerformed(evt);
            }
        });

        buttonclosecancel.setBackground(new java.awt.Color(255, 0, 0));
        buttonclosecancel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        buttonclosecancel.setText("CANCEL");
        buttonclosecancel.setToolTipText("");
        buttonclosecancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclosecancelActionPerformed(evt);
            }
        });

        buttonclose000.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose000.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose000.setText("000");
        buttonclose000.setBorder(null);
        buttonclose000.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose000ActionPerformed(evt);
            }
        });

        buttonclose00.setBackground(new java.awt.Color(128, 196, 223));
        buttonclose00.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        buttonclose00.setText("00");
        buttonclose00.setBorder(null);
        buttonclose00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonclose00ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonclose7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonclose1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonclose4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonclose00, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonclose8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonclose0, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonclose5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonclose2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonclose000, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(buttonclose6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonclose3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonclose9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttoncloseBS, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(buttoncloseclear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonclosecancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttoncloseok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(closeshifttext))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(closeshifttext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonclose2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonclose3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttoncloseclear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonclose5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonclose4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonclose6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttoncloseBS, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonclose8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonclose9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonclose7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttonclosecancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonclose1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttoncloseok, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(buttonclose0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonclose000, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonclose00, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout closingShiftFrameLayout = new javax.swing.GroupLayout(closingShiftFrame.getContentPane());
        closingShiftFrame.getContentPane().setLayout(closingShiftFrameLayout);
        closingShiftFrameLayout.setHorizontalGroup(
            closingShiftFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, closingShiftFrameLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        closingShiftFrameLayout.setVerticalGroup(
            closingShiftFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closingShiftFrameLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Application"); // NOI18N
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLayeredPane1.setBackground(new java.awt.Color(255, 153, 0));

        SalesPanel.setMinimumSize(new java.awt.Dimension(5, 5));

        noInvoice.setEditable(false);
        noInvoice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        noInvoice.setForeground(new java.awt.Color(153, 153, 153));
        noInvoice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        noInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noInvoiceActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "code", "Nama", "Harga", "Jumlah", "discount", "Total", "id_item", "barcode", "discPersenAmount", "unit_cost", "stocked"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setGridColor(new java.awt.Color(128, 196, 223));
        jTable1.setIntercellSpacing(new java.awt.Dimension(2, 2));
        jTable1.setRowHeight(30);
        jTable1.setSelectionBackground(new java.awt.Color(128, 196, 223));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.getTableHeader().setBackground(new java.awt.Color(128, 196, 223));
        jTable1.getTableHeader().setFont(new java.awt.Font("Tahoma", 0, 18));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(450);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setMinWidth(0);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(0);
            jTable1.getColumnModel().getColumn(6).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(7).setMinWidth(0);
            jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(8).setMinWidth(0);
            jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(9).setMinWidth(0);
            jTable1.getColumnModel().getColumn(9).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(10).setMinWidth(0);
            jTable1.getColumnModel().getColumn(10).setMaxWidth(0);
        }

        GrandTotal.setEditable(false);
        GrandTotal.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        GrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GrandTotal.setText("0");
        GrandTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GrandTotalActionPerformed(evt);
            }
        });

        barcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodeActionPerformed(evt);
            }
        });
        barcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barcodeKeyPressed(evt);
            }
        });

        discount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discount.setText("0");

        Pajak.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Pajak.setText("0");

        subTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        subTotal.setText("0");
        subTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subTotalActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Sub Total");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Diskon");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Pajak");

        sisatext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        sisatext.setText("0");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Sisa");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discount, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sisatext)
                            .addComponent(Pajak, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(subTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discount, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Pajak, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sisatext, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        no1.setBackground(new java.awt.Color(128, 196, 223));
        no1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no1.setText("1");
        no1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no1ActionPerformed(evt);
            }
        });

        no3.setBackground(new java.awt.Color(128, 196, 223));
        no3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no3.setText("3");
        no3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no3ActionPerformed(evt);
            }
        });

        no2.setBackground(new java.awt.Color(128, 196, 223));
        no2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no2.setText("2");
        no2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no2ActionPerformed(evt);
            }
        });

        no4.setBackground(new java.awt.Color(128, 196, 223));
        no4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no4.setText("4");
        no4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no4ActionPerformed(evt);
            }
        });

        no5.setBackground(new java.awt.Color(128, 196, 223));
        no5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no5.setText("5");
        no5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no5ActionPerformed(evt);
            }
        });

        no6.setBackground(new java.awt.Color(128, 196, 223));
        no6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no6.setText("6");
        no6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no6ActionPerformed(evt);
            }
        });

        no7.setBackground(new java.awt.Color(128, 196, 223));
        no7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no7.setText("7");
        no7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no7ActionPerformed(evt);
            }
        });

        no8.setBackground(new java.awt.Color(128, 196, 223));
        no8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no8.setText("8");
        no8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no8ActionPerformed(evt);
            }
        });

        no9.setBackground(new java.awt.Color(128, 196, 223));
        no9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        no9.setText("9");
        no9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no9ActionPerformed(evt);
            }
        });

        qty.setBackground(new java.awt.Color(128, 196, 223));
        qty.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        qty.setText("QTY");
        qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(no1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
            .addComponent(no2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(no9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(qty, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(no1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(no9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        paytable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipe Bayar", "Jumlah"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paytable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paytableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(paytable);
        if (paytable.getColumnModel().getColumnCount() > 0) {
            paytable.getColumnModel().getColumn(0).setResizable(false);
            paytable.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        hapus.setBackground(new java.awt.Color(224, 71, 71));
        hapus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        hapus.setText("<html><center>Hapus</center></html>");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        diskon.setBackground(new java.awt.Color(128, 196, 223));
        diskon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        diskon.setText("<html><center>Diskon</center></html>");
        diskon.setBorder(null);
        diskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diskonActionPerformed(evt);
            }
        });

        caribarang.setBackground(new java.awt.Color(128, 196, 223));
        caribarang.setFont(caribarang.getFont().deriveFont(caribarang.getFont().getSize()+7f));
        caribarang.setText("<html><center>Cari Barang</center></html>");
        caribarang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        caribarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caribarangActionPerformed(evt);
            }
        });

        bayar.setBackground(new java.awt.Color(42, 243, 79));
        bayar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        bayar.setText("<html><center>Bayar</center></html>");
        bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarActionPerformed(evt);
            }
        });

        pending.setBackground(new java.awt.Color(128, 196, 223));
        pending.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pending.setText("<html><center>Pending</center></html>");
        pending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(caribarang, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pending, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pending, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(caribarang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        customerName.setEditable(false);
        customerName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        customerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lookupCustomer.setBackground(new java.awt.Color(128, 196, 223));
        lookupCustomer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lookupCustomer.setText("<html><center>Cari Kostumer</center></hthml>");
        lookupCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lookupCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SalesPanelLayout = new javax.swing.GroupLayout(SalesPanel);
        SalesPanel.setLayout(SalesPanelLayout);
        SalesPanelLayout.setHorizontalGroup(
            SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SalesPanelLayout.createSequentialGroup()
                        .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lookupCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(GrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SalesPanelLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SalesPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );
        SalesPanelLayout.setVerticalGroup(
            SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SalesPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(GrandTotal)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SalesPanelLayout.createSequentialGroup()
                        .addComponent(barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(customerName)
                    .addComponent(lookupCustomer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SalesPanelLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MenuPanel.setMinimumSize(new java.awt.Dimension(5, 5));

        endofday.setBackground(new java.awt.Color(128, 196, 223));
        endofday.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        endofday.setText("<html><center>End Of Day Procedure</center></html?");
        endofday.setBorder(null);
        endofday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endofdayActionPerformed(evt);
            }
        });

        openingday.setBackground(new java.awt.Color(128, 196, 223));
        openingday.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        openingday.setText("<html><center>Opening Day Procedure</center></html>"); // NOI18N
        openingday.setBorder(null);
        openingday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openingdayActionPerformed(evt);
            }
        });

        OpenShift.setBackground(new java.awt.Color(128, 196, 223));
        OpenShift.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        OpenShift.setText("<html><center>Opening Shift</center></html>");
        OpenShift.setBorder(null);
        OpenShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenShiftActionPerformed(evt);
            }
        });

        closingshift.setBackground(new java.awt.Color(128, 196, 223));
        closingshift.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        closingshift.setText("<html><center>Closing Shift</center></html>");
        closingshift.setBorder(null);
        closingshift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closingshiftActionPerformed(evt);
            }
        });

        opendrawer.setBackground(new java.awt.Color(128, 196, 223));
        opendrawer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        opendrawer.setText("<html><center>Buka Laci Kasir</center></html>");
        opendrawer.setBorder(null);
        opendrawer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opendrawerActionPerformed(evt);
            }
        });

        PayIn.setBackground(new java.awt.Color(128, 196, 223));
        PayIn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        PayIn.setText("<html><center>Pay In</center></html>");
        PayIn.setBorder(null);
        PayIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayInActionPerformed(evt);
            }
        });

        PayOut.setBackground(new java.awt.Color(128, 196, 223));
        PayOut.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        PayOut.setText("<html><center>Pay Out</center></html>");
        PayOut.setBorder(null);
        PayOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayOutActionPerformed(evt);
            }
        });

        pendingsls.setBackground(new java.awt.Color(128, 196, 223));
        pendingsls.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pendingsls.setText("<html><center>Pending Sales</center></html>"); // NOI18N
        pendingsls.setBorder(null);
        pendingsls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingslsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pendingsls, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openingday, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(endofday, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(OpenShift, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(closingshift, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(opendrawer, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(PayIn, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(PayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(closingshift, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(OpenShift, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(opendrawer, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PayIn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PayOut, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(openingday, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(endofday, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(pendingsls, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(486, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(SalesPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(MenuPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SalesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(MenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SalesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(MenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel1.setBackground(new java.awt.Color(128, 196, 223));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dapos/ui/Dapos.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        salespanel.setBackground(new java.awt.Color(128, 196, 223));
        salespanel.setText("Penjualan");
        salespanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salespanelActionPerformed(evt);
            }
        });

        menubutton.setBackground(new java.awt.Color(128, 196, 223));
        menubutton.setText("Menu");
        menubutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menubuttonActionPerformed(evt);
            }
        });

        Logout.setBackground(new java.awt.Color(128, 196, 223));
        Logout.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Logout.setText("Log Out");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(salespanel, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menubutton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(menubutton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salespanel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(117, 117, 117))
        );

        jPanel12.setBackground(new java.awt.Color(128, 196, 223));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("User  :");

        NamaKasir.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        hostName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        hostName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        date.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        date.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel11.setText("Jabatan :");

        security.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(NamaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(security, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(hostName, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(NamaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hostName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(security, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(0, 759, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void noInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noInvoiceActionPerformed

    private void barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeActionPerformed
        
    }//GEN-LAST:event_barcodeActionPerformed

    private void barcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodeKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
        if(jTable1.getRowCount()<1){fillsales();}
        else {
            List brcd = new ArrayList();
        for (int i = 0; i<jTable1.getRowCount();i++){
            brcd.add(jTable1.getValueAt(i, 7));
        }
        if (brcd.contains(barcode.getText())){
        for(int i=0;i<jTable1.getRowCount();i++){
        String bar = jTable1.getValueAt(i, 7).toString();
        if(bar.equals(barcode.getText())){
        int qty = Integer.parseInt(jTable1.getValueAt(i, 3).toString()) +1 ;
        jTable1.setValueAt(String.valueOf(qty), i, 3);
        double discAmount = (jTable1.getValueAt(i, 8) == null)? 0 :Double.parseDouble(jTable1.getValueAt(i, 8).toString());
        int harga = Integer.parseInt(jTable1.getValueAt(i, 2).toString());
        int total = harga * qty;
        double totaldisc = (double)( (discAmount/100) * Double.parseDouble(String.valueOf(total)));
        int totalharga = (int) (total - totaldisc);
        jTable1.setValueAt(String.valueOf((int)totaldisc), i, 4);
        jTable1.setValueAt(String.valueOf(totalharga), i, 5);
        }
        subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
        Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
        discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
        GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
        sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
        barcode.requestFocus();
        }barcode.setText("");
        } else{fillsales();}
        }
        }
    }//GEN-LAST:event_barcodeKeyPressed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        DefaultTableModel tbl = (DefaultTableModel) this.jTable1.getModel();
       
        try{
        tbl.removeRow(jTable1.getSelectedRow());
        subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
        Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
        discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
        GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
        sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
        barcode.requestFocus();
        } catch(Exception e){
             DefaultTableModel tblpay = (DefaultTableModel) this.paytable.getModel();
        tblpay.removeRow(paytable.getSelectedRow());
        sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
        barcode.requestFocus();
        }
    }//GEN-LAST:event_hapusActionPerformed

    private void bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarActionPerformed
        payText.setText(sisatext.getText());
        PayFrame.setVisible(true);
        PayFrame.setLocationRelativeTo(null);
        payText.requestFocus();        
    }//GEN-LAST:event_bayarActionPerformed

    private void GrandTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GrandTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GrandTotalActionPerformed

    private void no1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no1ActionPerformed
         int row = jTable1.getSelectedRow();
         int qty = 1;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no1ActionPerformed

    private void no3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no3ActionPerformed
         int row = jTable1.getSelectedRow();
         int qty = 3;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal())); 
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no3ActionPerformed

    private void no2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no2ActionPerformed
        int row = jTable1.getSelectedRow();
         int qty = 2;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));         
         barcode.requestFocus();
    }//GEN-LAST:event_no2ActionPerformed

    private void no4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no4ActionPerformed
        int row = jTable1.getSelectedRow();
         int qty = 4;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no4ActionPerformed

    private void no5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no5ActionPerformed
        int row = jTable1.getSelectedRow();
         int qty = 5;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no5ActionPerformed

    private void no6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no6ActionPerformed
        int row = jTable1.getSelectedRow();
         int qty = 6;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no6ActionPerformed

    private void no7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no7ActionPerformed
        int row = jTable1.getSelectedRow();
        int qty = 7;
        int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no7ActionPerformed

    private void no8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no8ActionPerformed
       int row = jTable1.getSelectedRow();
         int qty = 8;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         barcode.requestFocus();
    }//GEN-LAST:event_no8ActionPerformed

    private void no9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no9ActionPerformed
        int row = jTable1.getSelectedRow();
         int qty = 9;
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }//GEN-LAST:event_no9ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            int row = jTable1.getSelectedRow();
            int col = jTable1.getSelectedColumn();
            int qty = (int) jTable1.getValueAt(row, col);
            int price =  Integer.parseInt((String) jTable1.getValueAt(row, 2));
            int disc = Integer.parseInt((String) jTable1.getValueAt(row, 4));
         int total = (qty * price) - disc;
            jTable1.setValueAt(String.valueOf(total), row, 5);
            subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
            Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
            discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
            GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
            sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
            barcode.requestFocus();
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        barcode.requestFocus();
    }//GEN-LAST:event_jTable1MouseEntered

    private void qtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyActionPerformed
        qtyFrame.setVisible(true);
        qtyFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_qtyActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
       bil += "1" ;
       qtyText.setText(bil);
       
    }//GEN-LAST:event_button1ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        bil += "2" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button2ActionPerformed

    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
        bil += "9" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button9ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        bil += "3" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button3ActionPerformed

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        bil += "4" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button4ActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        bil += "5" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button5ActionPerformed

    private void button6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button6ActionPerformed
        bil += "6" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button6ActionPerformed

    private void button7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button7ActionPerformed
        bil += "7" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button7ActionPerformed

    private void button8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button8ActionPerformed
        bil += "8" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button8ActionPerformed

    private void button0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button0ActionPerformed
        bil += "0" ;
       qtyText.setText(bil);
    }//GEN-LAST:event_button0ActionPerformed

    private void qtyTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyTextActionPerformed

    private void buttonBackspaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBackspaceActionPerformed
        String angka = qtyText.getText().substring(0, qtyText.getText().length() - 1);
        bil = angka;
        qtyText.setText(angka);
        
    }//GEN-LAST:event_buttonBackspaceActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        qtyText.setText("");
        bil="";
    }//GEN-LAST:event_buttonClearActionPerformed

    private void qtyTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyTextKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){ 
         int row = jTable1.getSelectedRow();
         int qty = Integer.parseInt((String) qtyText.getText());
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
         qtyFrame.dispose();
         qtyText.setText("");
         bil="";
        }
    }//GEN-LAST:event_qtyTextKeyPressed

    private void buttonPay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay1ActionPerformed
        pay += "1";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay1ActionPerformed

    private void buttonPay3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay3ActionPerformed
        pay += "3";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay3ActionPerformed

    private void buttonPay2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay2ActionPerformed
        pay += "2";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay2ActionPerformed

    private void buttonPay4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay4ActionPerformed
        pay += "4";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay4ActionPerformed

    private void buttonPay5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay5ActionPerformed
        pay += "5";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay5ActionPerformed

    private void buttonPay6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay6ActionPerformed
        pay += "6";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay6ActionPerformed

    private void buttonPay7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay7ActionPerformed
        pay += "7";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay7ActionPerformed

    private void buttonPay8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay8ActionPerformed
        pay += "8";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay8ActionPerformed

    private void buttonPay9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay9ActionPerformed
        pay += "9";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay9ActionPerformed

    private void buttonPayClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPayClearActionPerformed
        pay = "";
        payText.setText("0");
        payText.requestFocus();
    }//GEN-LAST:event_buttonPayClearActionPerformed

    private void buttonPay0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPay0ActionPerformed
        pay += "0";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
    }//GEN-LAST:event_buttonPay0ActionPerformed

    private void buttonPayBackspaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPayBackspaceActionPerformed
        String text = payText.getText().replace(",", "");
        int angka = Integer.parseInt(text.substring(0, text.length() - 1));
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        pay = String.valueOf(angka);
        payText.requestFocus();        
    }//GEN-LAST:event_buttonPayBackspaceActionPerformed

    private void payTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payTextActionPerformed

    private void payTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_payTextKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_0){
        pay += "0";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_1){
        pay += "1";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_2){
        pay += "2";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_3){
        pay += "3";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_4){
        pay += "4";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_5){
        pay += "5";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_6){
        pay += "6";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_7){
        pay += "7";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_8){
        pay += "8";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_9){
        pay += "9";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
        String text = payText.getText().replace(",", "");
        int angka = Integer.parseInt(text.substring(0, text.length() - 1));
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        pay = String.valueOf(angka);
        payText.requestFocus();
        }
    }//GEN-LAST:event_payTextKeyPressed

    private void buttonPay1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buttonPay1KeyPressed
        
    }//GEN-LAST:event_buttonPay1KeyPressed

    private void PayFrameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PayFrameKeyPressed
        payText.requestFocus();
    }//GEN-LAST:event_PayFrameKeyPressed

    private void PayMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayMethodActionPerformed
        payText.requestFocus();
    }//GEN-LAST:event_PayMethodActionPerformed

    private void ButtonPayCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPayCancelActionPerformed
        PayFrame.dispose();
        barcode.requestFocus();
    }//GEN-LAST:event_ButtonPayCancelActionPerformed

    private void ButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonOKActionPerformed
        int row = jTable1.getSelectedRow();
         int qty = Integer.parseInt((String) qtyText.getText());
         int price = Integer.parseInt((String) jTable1.getValueAt(row, 2));
         int subtotal = qty * price;
         int amount = Integer.parseInt((String) jTable1.getValueAt(row, 4));       
         double disc = (jTable1.getValueAt(row, 8) == null)? amount : ((Double.parseDouble(jTable1.getValueAt(row, 8).toString())/100) * subtotal); ;
         int total = (int) (subtotal - disc);
         jTable1.setValueAt(String.valueOf(qty), row, 3);
         jTable1.setValueAt(String.valueOf(total), row, 5);
         jTable1.setValueAt(String.valueOf((int)disc), row, 4);
         subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));                  
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
         qtyFrame.dispose();
         qtyText.setText("1");
         bil="";
    }//GEN-LAST:event_ButtonOKActionPerformed

    private void ButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCancelActionPerformed
        qtyFrame.dispose();
        barcode.requestFocus();
    }//GEN-LAST:event_ButtonCancelActionPerformed

    private void ButtonPay000ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPay000ActionPerformed
        pay += "000";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonPay000ActionPerformed

    private void ButtonPay00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPay00ActionPerformed
        // TODO add your handling code here:
        pay += "00";
        payText.setText(pay);
        int angka = Integer.parseInt((String) payText.getText());
        payText.setText(NumberFormat.getNumberInstance().format(angka));
        payText.requestFocus();

    }//GEN-LAST:event_ButtonPay00ActionPerformed

    private void subTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subTotalActionPerformed

    private void caribarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caribarangActionPerformed
        itemlist.setVisible(true);
        itemlist.setLocationRelativeTo(null);
        searchbox.requestFocus();
    }//GEN-LAST:event_caribarangActionPerformed

    private void buttonItemOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonItemOKActionPerformed
        String[] data = new String[11];
        int row = tableItem.getSelectedRow();
        data[0] = (String) tableItem.getValueAt(row, 0);
        data[1] = (String) tableItem.getValueAt(row, 1);
        int qty = 1;
        int harga = Integer.parseInt((String) tableItem.getValueAt(row, 2));
        data[2] = String.valueOf(harga) ;
        data[3] = String.valueOf(qty);
        data[4] = "0";
        data[5] = String.valueOf((Integer.parseInt(data[2]) * Integer.parseInt(data[3])) - Integer.parseInt(data[4]));
        data[6] = (String) tableItem.getValueAt(row, 3);
        data[7] = (String) tableItem.getValueAt(row, 4);
        data[8] = "0";
        data[9] = (String) tableItem.getValueAt(row, 5);
        data[10] = (String) tableItem.getValueAt(row, 6);
        DefaultTableModel tbl = (DefaultTableModel) this.jTable1.getModel();
        tbl.addRow(data);
        subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
        Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
        GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));
        sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
        itemlist.dispose();
        searchbox.setText("");
        barcode.requestFocus();
    }//GEN-LAST:event_buttonItemOKActionPerformed

    private void searchboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchboxKeyReleased
        DefaultTableModel table = (DefaultTableModel)tableItem.getModel();
        String cari = searchbox.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(table);
        tableItem.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + cari));
    }//GEN-LAST:event_searchboxKeyReleased

    private void buttonItemCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonItemCancelActionPerformed
        itemlist.dispose();
        barcode.requestFocus();
    }//GEN-LAST:event_buttonItemCancelActionPerformed

    private void diskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diskonActionPerformed
           discFrame.setVisible(true);
           discFrame.setLocationRelativeTo(null);
           discFrame.isMinimumSizeSet();
    }//GEN-LAST:event_diskonActionPerformed

    private void salespanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salespanelActionPerformed
     SalesPanel.setVisible(true);
     MenuPanel.setVisible(false);
    }//GEN-LAST:event_salespanelActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        UserSession.setU_id(0);
        UserSession.setU_name("");
        UserSession.setU_user("");
        this.dispose();
        new Pos().setVisible(true);
    }//GEN-LAST:event_LogoutActionPerformed

    private void ButtonPayOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPayOKActionPerformed
    String type = (String) PayMethod.getSelectedItem();
    int jumlah = Integer.parseInt(payText.getText().replace(",", ""));
    String[] data = new String[2];
    data[0] = type;
    data[1] = String.valueOf(jumlah);
    DefaultTableModel tbl = (DefaultTableModel) this.paytable.getModel();
    tbl.addRow(data);
    this.PayFrame.dispose();
    pay = "";
    payText.setText("0");
    sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
    int sisa = Integer.parseInt(sisatext.getText().replace(",", ""));
    if(sisa <= 0){
        
        try {
            pay();
            sales_invoice_item();
            sales_invoice_payment();
            stock_transaction();
            receipt();
        } catch (PrintException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_ButtonPayOKActionPerformed

    private void menubuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menubuttonActionPerformed
       SalesPanel.setVisible(false);
       MenuPanel.setVisible(true);
    }//GEN-LAST:event_menubuttonActionPerformed

    private void changeOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeOKActionPerformed
     changeFrame.dispose();
    }//GEN-LAST:event_changeOKActionPerformed

    private void discCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discCancelActionPerformed
      discFrame.dispose();
      barcode.requestFocus();
    }//GEN-LAST:event_discCancelActionPerformed

    private void openingdayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openingdayActionPerformed
       Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "SELECT * FROM `daily_procedures` WHERE id=(SELECT MAX(id) FROM `daily_procedures`)";
        rs=db.stm.executeQuery(sql);
        if(rs.next()){
        if( rs.getString("closing")==null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sudahLabel.setText("Sudah dilakukan Opening day pada "+f.format(f.parse(rs.getString("opening"))));
            sudahLabel2.setText("oleh "+rs.getString("openingbyname"));
            sudahBuka.setVisible(true);
            sudahBuka.setLocationRelativeTo(null);
        } else {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            dateOpen.setText(f.format(date));
            openBy.setText(UserSession.getU_name());
            openingFrame.setVisible(true);
            openingFrame.setLocationRelativeTo(null);
        }
        } else {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            dateOpen.setText(f.format(date));
            openBy.setText(UserSession.getU_name());
            openingFrame.setVisible(true);
            openingFrame.setLocationRelativeTo(null);
        
        }
        }catch(Exception e){ 
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            dateOpen.setText(f.format(date));
            openBy.setText(UserSession.getU_name());
            openingFrame.setVisible(true);
            openingFrame.setLocationRelativeTo(null);
        }
        
        
    }//GEN-LAST:event_openingdayActionPerformed

    private void openOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openOKActionPerformed
 openingday();
    }//GEN-LAST:event_openOKActionPerformed

    private void openingday(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        String tanggal = dateOpen.getText();
        String e_id = String.valueOf(UserSession.getU_id());
        String e_name = UserSession.getU_name();
        try{
            sql = "insert into daily_procedures (opening, opening_by, openingbyname)"+"values (?,?,?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, tanggal);
            pstm.setString(2, e_id);
            pstm.setString(3, e_name);
            pstm.executeUpdate();
            System.out.println("berhasil opening");
            openingFrame.dispose();
            }
        catch(Exception e){System.out.println("gagal Opening Day");}
    }
    
    public void endofday(){
    Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        String tanggal = dateEOD.getText();
        String e_id = String.valueOf(UserSession.getU_id());
        String e_name = UserSession.getU_name();
        try{
        sql = " UPDATE `daily_procedures` SET closing =? , closing_by=? , closingbyname=?"+"order by id desc limit 1";
        PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, tanggal);
            pstm.setString(2, e_id);
            pstm.setString(3, e_name);
            pstm.executeUpdate();
            System.out.println("berhasil End Of Day");
            EODFrame.dispose();
        }catch(Exception e){System.out.print("gagal EOD");}
    }
    
    private void cancelOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelOpenActionPerformed
        openingFrame.dispose();
    }//GEN-LAST:event_cancelOpenActionPerformed

    private void sudahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudahButtonActionPerformed
        sudahBuka.dispose();
    }//GEN-LAST:event_sudahButtonActionPerformed

    private void cancelOpen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelOpen1ActionPerformed
        EODFrame.dispose();
    }//GEN-LAST:event_cancelOpen1ActionPerformed

    private void openOK1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openOK1ActionPerformed
     endofday();
    }//GEN-LAST:event_openOK1ActionPerformed

    private void endofdayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endofdayActionPerformed
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateEOD.setText(f.format(date));
        EODFrame.setVisible(true);
        EODFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_endofdayActionPerformed

    private void opendrawerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opendrawerActionPerformed
    try {
        if(Integer.parseInt(opendrawer_security) == 1){cashdrawer();}
        else{
        JLabel jUserName = new JLabel("User Name");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField();
        Object[] ob = {jUserName, userName, jPassword, password};
        int result = JOptionPane.showConfirmDialog(null, ob, "Silahkan Input Username dan Password", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION){
            Connection con;
            Statement stat;
            ResultSet rs;
            String user;
            user = userName.getText();
            String pw;
            pw = password.getText();
            String sql;
            Koneksi db = new Koneksi();
            db.connect();
            con = (Connection) db.KoneksiDatabase;
            try{
                sql = "SELECT e.`name`, e.`username`, e.`password`, e.`security`,s.`open_drawer` FROM employee e LEFT JOIN SECURITY s ON e.`security`=s.`id` where e.username = '"+user+"' and e.password = '"+pw+"'" ;
                rs = db.stm.executeQuery(sql);
                if(rs.next()){
                if(Integer.parseInt(rs.getString("open_drawer")) == 1){
                cashdrawer();
                }
                }
            }
            catch(Exception e){}
        }
        }
        
    } catch (PrintException ex) {
        Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_opendrawerActionPerformed

    private void buttonopen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen1ActionPerformed
        shift += "1";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen1ActionPerformed

    private void buttonopen1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buttonopen1KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_1){
        shift += "1";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
    }//GEN-LAST:event_buttonopen1KeyPressed

    private void buttonopen3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen3ActionPerformed
        shift += "3";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen3ActionPerformed

    private void buttonopen2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen2ActionPerformed
        shift += "2";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen2ActionPerformed

    private void buttonopen4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen4ActionPerformed
        shift += "4";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen4ActionPerformed

    private void buttonopen5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen5ActionPerformed
        shift += "5";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen5ActionPerformed

    private void buttonopen6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen6ActionPerformed
        shift += "6";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen6ActionPerformed

    private void buttonopen7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen7ActionPerformed
        shift += "7";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen7ActionPerformed

    private void buttonopen8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen8ActionPerformed
        shift += "8";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen8ActionPerformed

    private void buttonopen9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen9ActionPerformed
        shift += "9";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen9ActionPerformed

    private void buttonopenclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopenclearActionPerformed
        shift = "";
        openshifttext.setText(shift);
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopenclearActionPerformed

    private void buttonopen0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen0ActionPerformed
        shift += "0";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen0ActionPerformed

    private void buttonopenBSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopenBSActionPerformed
        String text = openshifttext.getText().replace(",", "");
        int angka = Integer.parseInt(text.substring(0, text.length() - 1));
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        shift = String.valueOf(angka);
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopenBSActionPerformed

    private void openshifttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openshifttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_openshifttextActionPerformed

    private void openshifttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_openshifttextKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_0){
        shift += "0";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_1){
        shift += "1";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_2){
        shift += "2";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_3){
        shift += "3";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_4){
        shift += "4";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_5){
        shift += "5";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_6){
        shift += "6";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_7){
        shift += "7";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_8){
        shift += "8";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_9){
        shift += "9";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
        String text = openshifttext.getText().replace(",", "");
        int angka = Integer.parseInt(text.substring(0, text.length() - 1));
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        shift = String.valueOf(angka);
        openshifttext.requestFocus();
        }
        
    }//GEN-LAST:event_openshifttextKeyPressed

    private void buttonopenokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopenokActionPerformed
       openshift();
       openshifttext.setText("0");
        
    }//GEN-LAST:event_buttonopenokActionPerformed
    
    private void buttonopencancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopencancelActionPerformed
        shift = "";
        OpeningShiftFrame.dispose();
    }//GEN-LAST:event_buttonopencancelActionPerformed

    private void buttonopen000ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen000ActionPerformed
        shift += "000";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen000ActionPerformed

    private void buttonopen00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonopen00ActionPerformed
        shift += "00";
        openshifttext.setText(shift);
        int angka = Integer.parseInt((String) openshifttext.getText());
        openshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        openshifttext.requestFocus();
    }//GEN-LAST:event_buttonopen00ActionPerformed

    private void OpeningShiftFrameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OpeningShiftFrameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpeningShiftFrameKeyPressed

    private void OpenShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenShiftActionPerformed
        OpeningShiftFrame.setVisible(true);
        OpeningShiftFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_OpenShiftActionPerformed

    private void PayInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PayInActionPerformed

    private void PayOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PayOutActionPerformed

    private void paytableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paytableMouseClicked
       jTable1.getSelectionModel().clearSelection();
    }//GEN-LAST:event_paytableMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        paytable.getSelectionModel().clearSelection();
    }//GEN-LAST:event_jTable1MouseClicked

    private void pendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pendingActionPerformed

    private void lookupCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lookupCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lookupCustomerActionPerformed

    private void errorprintOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorprintOKActionPerformed
        errorprint.dispose();
    }//GEN-LAST:event_errorprintOKActionPerformed

    private void pendingslsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingslsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pendingslsActionPerformed

    private void openshiftprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openshiftprintActionPerformed
    try {
        shiftprint();
    } catch (PrintException ex) {
        Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_openshiftprintActionPerformed

    private void closeopenshiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeopenshiftActionPerformed
        openshift.dispose();
        ShiftPrint.reset();
    }//GEN-LAST:event_closeopenshiftActionPerformed

    private void closingshiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closingshiftActionPerformed
        closingShiftFrame.setVisible(true);
        closingShiftFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_closingshiftActionPerformed

    private void buttonclose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose1ActionPerformed
        shift += "1";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose1ActionPerformed

    private void buttonclose1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buttonclose1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonclose1KeyPressed

    private void buttonclose3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose3ActionPerformed
        shift += "3";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose3ActionPerformed

    private void buttonclose2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose2ActionPerformed
        shift += "2";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose2ActionPerformed

    private void buttonclose4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose4ActionPerformed
        shift += "4";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose4ActionPerformed

    private void buttonclose5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose5ActionPerformed
        shift += "5";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose5ActionPerformed

    private void buttonclose6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose6ActionPerformed
        shift += "6";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose6ActionPerformed

    private void buttonclose7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose7ActionPerformed
        shift += "7";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose7ActionPerformed

    private void buttonclose8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose8ActionPerformed
        shift += "8";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose8ActionPerformed

    private void buttonclose9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose9ActionPerformed
        // TODO add your handling code here:
        shift += "9";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose9ActionPerformed

    private void buttoncloseclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttoncloseclearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttoncloseclearActionPerformed

    private void buttonclose0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose0ActionPerformed
        // TODO add your handling code here:
        shift += "0";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose0ActionPerformed

    private void buttoncloseBSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttoncloseBSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttoncloseBSActionPerformed

    private void closeshifttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeshifttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_closeshifttextActionPerformed

    private void closeshifttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_closeshifttextKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_0){
        shift += "0";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_1){
        shift += "1";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_2){
        shift += "2";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_3){
        shift += "3";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_4){
        shift += "4";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_5){
        shift += "5";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_6){
        shift += "6";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_7){
        shift += "7";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_8){
        shift += "8";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_9){
        shift += "9";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
        String text = closeshifttext.getText().replace(",", "");
        int angka = Integer.parseInt(text.substring(0, text.length() - 1));
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        shift = String.valueOf(angka);
        closeshifttext.requestFocus();
        }
        
    }//GEN-LAST:event_closeshifttextKeyPressed

    private void buttoncloseokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttoncloseokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttoncloseokActionPerformed

    private void buttonclosecancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclosecancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonclosecancelActionPerformed

    private void buttonclose000ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose000ActionPerformed
        // TODO add your handling code here:
        shift += "000";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose000ActionPerformed

    private void buttonclose00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonclose00ActionPerformed
        // TODO add your handling code here:
        shift += "00";
        closeshifttext.setText(shift);
        int angka = Integer.parseInt((String) closeshifttext.getText());
        closeshifttext.setText(NumberFormat.getNumberInstance().format(angka));
        closeshifttext.requestFocus();
    }//GEN-LAST:event_buttonclose00ActionPerformed

    private void closingShiftFrameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_closingShiftFrameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_closingShiftFrameKeyPressed
    
    public void openshift(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        int openingbalance = Integer.parseInt(openshifttext.getText().replace(",", ""));
        int e_id=UserSession.getU_id();
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            sql="insert into cashier_shift(employeeid,openingbalance,openingtime, station)"+"values(?,?,?,?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, e_id);
            pstm.setInt(2, openingbalance);
            pstm.setString(3, f.format(date));
            pstm.setString(4, station);
            pstm.executeUpdate();
            System.out.println("berhasil opening shift");
            ShiftPrint = new LineFormatter(48);
            ShiftPrint.addLine(header1, LineFormatter.CENTER_ALIGN);
            ShiftPrint.addLine(header2, LineFormatter.CENTER_ALIGN);
            ShiftPrint.addLine(header3, LineFormatter.CENTER_ALIGN);
            ShiftPrint.addDivider("-");
            ShiftPrint.addLine(
            new LineColumn("Kasir", 24).addColumn("Kas Awal", 24, LineFormatter.RIGHT_ALIGN)
            );
            ShiftPrint.addDivider("-");
            ShiftPrint.addLine(
            new LineColumn(UserSession.getU_user(), 24).addColumn(Integer.toString(openingbalance), 24, LineFormatter.RIGHT_ALIGN)
            );
            ShiftPrint.addDivider("-");
            ShiftPrint.addLine("Terima Kasih", LineFormatter.CENTER_ALIGN);
            opentextarea.setText(ShiftPrint.render());
            System.out.println(ShiftPrint.render());
            OpeningShiftFrame.dispose();
            openshift.setVisible(true);
            openshift.setLocationRelativeTo(null);
        }catch(Exception e){
        System.out.println("gagal opening shift");
        }
    }
    
    public void shiftprint() throws PrintException{
    String printerMessage = ShiftPrint.render();
    String printerName = printerreceipt;
    boolean printerCheck = false;
    DocPrintJob job = null;
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    System.out.println("Number of print services: " + printServices.length);
    for (PrintService printer : printServices) {
        if (printer.getName().equals(printerName)) {
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            InputStream inputStream = new ByteArrayInputStream(printerMessage.getBytes());
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(inputStream, flavor, null);
            job = printer.createPrintJob();
            job.print(doc, null);
            printerCheck = true;
        }
    }
    finishdoc();
    cutter();
    cashdrawer();
    ShiftPrint.reset();
    }
    public void pay(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        int e_id = UserSession.getU_id();
        int subtotal = Integer.parseInt(subTotal.getText().replace(",", ""));
        int disc = Integer.parseInt(discount.getText().replace(",", ""));
        int pajak = Integer.parseInt(Pajak.getText().replace(",", ""));
        int grandtotal = Integer.parseInt(GrandTotal.getText().replace(",", ""));
        String CustName = customerName.getText();
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tgl = f.format(date);
        int id_inv = Integer.parseInt(noInvoice.getText());
        try{
        sql = "INSERT INTO `sales_invoice` (created,createdat,employeeid,subtotal,tax_amount,discount,grandtotal,customer_name)"+"values(?,?,?,?,?,?,?,?)";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, tgl);
        pstm.setString(2, station);
        pstm.setInt(3,e_id);
        pstm.setInt(4, subtotal);
        pstm.setInt(5, pajak);
        pstm.setInt(6, disc);
        pstm.setInt(7, grandtotal);
        pstm.setString(8, CustName);
        pstm.executeUpdate();
        System.out.println("Berhasil Simpan Sales_invoice");
        }catch(Exception e){}
    }
    
    public void sales_invoice_item(){
    Connection con;
    Statement stat;
    ResultSet rs;
    String sql ;
    Koneksi db = new Koneksi();
    db.connect();
    con = (Connection) db.KoneksiDatabase;
    Date date = new Date();
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String tgl = f.format(date);
    int id_inv = Integer.parseInt(noInvoice.getText());
    for (int i =0; i< jTable1.getRowCount(); i++){
        int id_item = Integer.parseInt((String) jTable1.getValueAt(i, 6));
        String item_name = (String) jTable1.getValueAt(i, 1);
        int qty =  Integer.parseInt((String) jTable1.getValueAt(i, 3));
        int price = Integer.parseInt((String) jTable1.getValueAt(i, 2));
        int unit_cost = Integer.parseInt((String) jTable1.getValueAt(i, 9));
        int disc_item = Integer.parseInt((String)jTable1.getValueAt(i, 4));
        int total = Integer.parseInt((String)jTable1.getValueAt(i, 5));
    try{
        sql = "INSERT INTO `sales_invoice_item` (date,item_id,item_name,qty,price,unit_cost,disc_item,total,sales_id)"+"values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstm1 = con.prepareStatement(sql);
        pstm1.setString(1, tgl);
        pstm1.setInt(2, id_item);
        pstm1.setString(3, item_name);
        pstm1.setInt(4, qty);
        pstm1.setInt(5, price);
        pstm1.setInt(6, unit_cost);
        pstm1.setInt(7, disc_item);
        pstm1.setInt(8, total);
        pstm1.setInt(9, id_inv);
        pstm1.executeUpdate();
        System.out.println("Berhasil Simpan Sales_invoice_item");
    }catch(Exception e){System.out.println("Gagal Simpan Sales_invoice_item"); }
    }
    }
    
    public void sales_invoice_payment(){
    Connection con;
    Statement stat;
    ResultSet rs;
    Koneksi db = new Koneksi();
    db.connect();
    con = (Connection) db.KoneksiDatabase;
    Date date = new Date();
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String tgl = f.format(date);
    int id_inv = Integer.parseInt(noInvoice.getText());
    
    for(int i=0; i<paytable.getRowCount();i++){
        String name= (String) paytable.getValueAt(i, 0);
        int jumlah = Integer.parseInt((String) paytable.getValueAt(i, 1));
        try{
        String sql3 = "INSERT INTO `sales_invoice_payment` (date, name, amount, sales_id)"+"values(?,?,?,?)";
        PreparedStatement pstm3 = con.prepareStatement(sql3);
        pstm3.setString(1, tgl);
        pstm3.setString(2, name);
        pstm3.setInt(3, jumlah);
        pstm3.setInt(4, id_inv);
        pstm3.executeUpdate();
        System.out.println("berhasil simpan sales_invoice_payment");
        }catch(Exception e){
    System.out.println("Gagal simpan sales_invoice_payment");
    }
    }
    }
    
    public void stock_transaction(){
    Connection con;
    Statement stat;
    ResultSet rs;
    Koneksi db = new Koneksi();
    db.connect();
    con = (Connection) db.KoneksiDatabase;
    Date date = new Date();
    SimpleDateFormat f = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    String tgl = f.format(date);
    int id_inv = Integer.parseInt(noInvoice.getText());

    for (int i=0; i<jTable1.getRowCount(); i++){
        int id_item = Integer.parseInt((String) jTable1.getValueAt(i, 6));
        int qty = Integer.parseInt((String) jTable1.getValueAt(i, 3));
        if(Integer.parseInt(jTable1.getValueAt(i, 10).toString())==1){
            try{
        String sql2= "INSERT INTO `stock_transactions` (date, item_id,qty,tag)"+"values(?,?,?,?)";
        PreparedStatement pstm2 = con.prepareStatement(sql2);
        pstm2.setString(1, tgl);
        pstm2.setInt(2, id_item);
        pstm2.setInt(3, -(qty));
        pstm2.setString(4, "Penjualan");
        pstm2.executeUpdate();
        System.out.println("Berhasil Simpan stock transactions");
        }catch(Exception e){
    System.out.println("Gagal Simpan stock transactions");
    }
    }
    }
    }
    
    public double subtotal(){
    subtotal = 0;
        for (int i=0;i<this.jTable1.getRowCount();i++){
        subtotal = subtotal + (Double.parseDouble(jTable1.getValueAt(i, 2).toString()) * Double.parseDouble(jTable1.getValueAt(i, 3).toString()) );
        }
        return subtotal;    
    }
    
    private double sisa(){
    int totalpay = 0;
    for (int i=0;i<this.paytable.getRowCount();i++){
    totalpay = (int) (totalpay + Double.parseDouble((String) paytable.getValueAt(i, 1)));
    }
    int sisa = (int) (grandtotal - totalpay);
    return sisa;   
    }
    
     private int totalpay(){
    int totalpay = 0;
    for (int i=0;i<this.paytable.getRowCount();i++){
    totalpay = (int) (totalpay + Double.parseDouble((String) paytable.getValueAt(i, 1)));
    }
    return totalpay;   
    }
    
    public double totalDisc(){
        totalDisc=0;
        for(int i=0; i<this.jTable1.getRowCount(); i++){
            totalDisc = totalDisc + Double.parseDouble(jTable1.getValueAt(i,4).toString());
            }
        return totalDisc;
    }
  
    public double totaltax(){
        Connection con;
        Statement stat;
        ResultSet rs;
        String sql ;
        Koneksi db = new Koneksi();
        db.connect();
        con = (Connection) db.KoneksiDatabase;
        try{
        sql = "select * from setting";
        rs = db.stm.executeQuery(sql);
        if(rs.next()){
            if(Integer.parseInt(rs.getString("tax"))==1){
            ppn = Integer.parseInt(rs.getString("tax_amount"));
            } else {ppn=0;}
        } 
        } catch (SQLException e){
        System.out.println("gagal load setting");
        }
            return ppn;
     }
    
    public double tax(){
    double nilai = ppn /100d;
    totalTax= nilai * (subtotal-totalDisc);
    return totalTax;
    }
    
    public double grandtotal(){
    grandtotal = subtotal + totalTax - totalDisc;
    return grandtotal;
    }
    
    public double subTotalDisc(double subtotalDisc){
    totalDisc = (subtotalDisc / 100 )* subtotal;
       return totalDisc;
    }
    
    public void saveInvoice(){
    Date tgl = new Date();
    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    System.out.println(f.format(tgl));
    }
    
    public void receipt() throws PrintException{
    noInv = noInvoice.getText() ;
    String paperSize = printersize;
    payAmountreceipt = String.valueOf(totalpay());
    payMethodereceipt = (String) PayMethod.getSelectedItem();
    subtotalreceipt = subTotal.getText();
    grandtotalreceipt = GrandTotal.getText();
    discreceipt = (!discount.getText().equals(""))? discount.getText().replace(",", "") :"0" ;
    taxreceipt = Pajak.getText();
    int kembalian = Integer.parseInt(payAmountreceipt.replace(".", "")) - Integer.parseInt(grandtotalreceipt.replace(",", ""));
    for(int i = 0; i<jTable1.getRowCount(); i++){
    itembon.add(jTable1.getValueAt(i, 1));
    qtybon.add(jTable1.getValueAt(i, 3));
    pricebon.add(jTable1.getValueAt(i, 2));
    discItembon.add(jTable1.getValueAt(i, 4));
    Totalbon.add(jTable1.getValueAt(i, 5));
    }
    if(paperSize.equals("80")){
        lf = new LineFormatter(48);
 
        lf.addLine(header1, LineFormatter.CENTER_ALIGN);
        lf.addLine(header2, LineFormatter.CENTER_ALIGN);
        lf.addLine(header3, LineFormatter.CENTER_ALIGN);
        lf.addDivider("=");
        lf.addLine(
        new LineColumn("Nota No. " + noInv, 24)
                        .addColumn(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 24, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Nama Kasir :" + UserSession.getU_name(), 24)
                .addColumn(station, 24, LineFormatter.RIGHT_ALIGN)
        );
        lf.addDivider("-");
        lf.addLine(
        new LineColumn("Nama Barang", 28, LineFormatter.LEFT_ALIGN)
                .addColumn("Diskon", 10, LineFormatter.RIGHT_ALIGN)
                .addColumn("Total", 10, LineFormatter.RIGHT_ALIGN)
        );
        lf.addDivider("-");
        for(int n=0; n<itembon.size();n++){
        String item = (String) itembon.get(n);
        String jumlah = (String) qtybon.get(n);
        int harga = Integer.parseInt((String) pricebon.get(n));
        int diskon = Integer.parseInt((String) discItembon.get(n));
        int total = Integer.parseInt((String) Totalbon.get(n));
        lf.addLine(
        new LineColumn(item, 30, LineFormatter.LEFT_ALIGN));
        lf.addLine(
        new LineColumn("  "+jumlah + " X "+NumberFormat.getInstance().format(harga),28, LineFormatter.LEFT_ALIGN)
                .addColumn(NumberFormat.getInstance().format(diskon), 10, LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getInstance().format(total), 10, LineFormatter.RIGHT_ALIGN)
        );
        }
        lf.addDivider("-");
        lf.addLine(
        new LineColumn("Subtotal :", 38, LineFormatter.RIGHT_ALIGN)
                .addColumn(subtotalreceipt, 10, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Diskon :",38, LineFormatter.RIGHT_ALIGN)
                .addColumn(discreceipt, 10, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Pajak :",38, LineFormatter.RIGHT_ALIGN)
                .addColumn(taxreceipt, 10, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Grandtotal :",38, LineFormatter.RIGHT_ALIGN)
                .addColumn(grandtotalreceipt, 10, LineFormatter.RIGHT_ALIGN)
        );
        for (int i=0; i<this.paytable.getRowCount(); i++){
            lf.addLine(
        new LineColumn(this.paytable.getValueAt(i, 0) + " :",38,LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getNumberInstance().format(Integer.parseInt((String) this.paytable.getValueAt(i, 1))), 10, LineFormatter.RIGHT_ALIGN)
        );
    }
        lf.addLine(
        new LineColumn("Kembalian :",38, LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getInstance().format(kembalian), 10, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(footer1, LineFormatter.CENTER_ALIGN);
        lf.addLine(footer2, LineFormatter.CENTER_ALIGN);
        lf.addLine(footer3, LineFormatter.CENTER_ALIGN);
    } else {
        lf = new LineFormatter(30);
 
        lf.addLine(header1, LineFormatter.CENTER_ALIGN);
        lf.addLine(header2, LineFormatter.CENTER_ALIGN);
        lf.addLine(header3, LineFormatter.CENTER_ALIGN);
        lf.addDivider("=");
        lf.addLine(
        new LineColumn("Nota No. " + noInv, 15)
                        .addColumn(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 15, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Nama Kasir :" + UserSession.getU_name(), 15)
                .addColumn(station, 15, LineFormatter.RIGHT_ALIGN)
        );
        lf.addDivider("-");
        lf.addLine(
        new LineColumn("Nama Barang", 10, LineFormatter.LEFT_ALIGN)
                .addColumn("Diskon", 10, LineFormatter.RIGHT_ALIGN)
                .addColumn("Total", 10, LineFormatter.RIGHT_ALIGN)
        );
        lf.addDivider("-");
        for(int n=0; n<itembon.size();n++){
        String item = (String) itembon.get(n);
        String jumlah = (String) qtybon.get(n);
        int harga = Integer.parseInt((String) pricebon.get(n));
        int diskon = Integer.parseInt((String) discItembon.get(n));
        int total = Integer.parseInt((String) Totalbon.get(n));
        lf.addLine(
        new LineColumn(item, 20, LineFormatter.LEFT_ALIGN));
        lf.addLine(
        new LineColumn(jumlah + " X "+NumberFormat.getInstance().format(harga),10, LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getInstance().format(diskon), 10, LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getInstance().format(total), 10, LineFormatter.RIGHT_ALIGN)
        );
        }
        lf.addDivider("-");
        lf.addLine(
        new LineColumn("Subtotal\t:", 15, LineFormatter.RIGHT_ALIGN)
                .addColumn(subtotalreceipt, 15, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Diskon\t:",15, LineFormatter.RIGHT_ALIGN)
                .addColumn(discreceipt, 15, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Pajak\t:",15, LineFormatter.RIGHT_ALIGN)
                .addColumn(taxreceipt, 15, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(
        new LineColumn("Grandtotal\t:",15, LineFormatter.RIGHT_ALIGN)
                .addColumn(grandtotalreceipt, 15, LineFormatter.RIGHT_ALIGN)
        );
        for (int i=0; i<this.paytable.getRowCount(); i++){
            lf.addLine(
        new LineColumn(this.paytable.getValueAt(i, 0) + " :",15,LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getNumberInstance().format(Integer.parseInt((String) this.paytable.getValueAt(i, 1))), 10, LineFormatter.RIGHT_ALIGN)
        );
    }
        lf.addLine(
        new LineColumn("Kembalian\t:",15, LineFormatter.RIGHT_ALIGN)
                .addColumn(NumberFormat.getInstance().format(kembalian), 15, LineFormatter.RIGHT_ALIGN)
        );
        lf.addLine(footer1, LineFormatter.CENTER_ALIGN);
        lf.addLine(footer2, LineFormatter.CENTER_ALIGN);
        lf.addLine(footer3, LineFormatter.CENTER_ALIGN);
    
    }
     System.out.println(lf.render());
     PrintReceipt();
     change.setText(NumberFormat.getInstance().format(kembalian));
     changeFrame.setVisible(true);
     changeFrame.setLocationRelativeTo(null);
    }
        
    public void PrintReceipt() throws PrintException{
    String printerMessage =lf.render();
    String printerName = printerreceipt;
    boolean printerCheck = false;
    DocPrintJob job = null;
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    System.out.println("Number of print services: " + printServices.length);
    for (PrintService printer : printServices) {
        if (printer.getName().equals(printerName)) {
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            InputStream inputStream = new ByteArrayInputStream(printerMessage.getBytes());
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(inputStream, flavor, null);
            job = printer.createPrintJob();
            job.print(doc, null);
            printerCheck = true;
        }
    }
    finishdoc();
    cutter();
    cashdrawer();
    clearTable();
    itembon.clear();
    qtybon.clear();
    pricebon.clear();
    discItembon.clear();
    Totalbon.clear();
    noInvoice();
    PayFrame.dispose();
    if (printerCheck == false) {
        errorprint.setLocationRelativeTo(null);
        errorprint.setVisible(true);
        System.out.println("The printer you were searching for could not be found.");
    }
    }
    
    public void finishdoc() throws PrintException{
    String printerName = printerreceipt;
    boolean printerCheck = false;
    DocPrintJob job = null;
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    for (PrintService printer : printServices) {
        if (printer.getName().equals(printerName)) {
            byte[] finish = {27,100,5};
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(finish, flavor, null);
            job = printer.createPrintJob();
            job.print(doc, null);
            printerCheck = true;
            }
    }
    if (printerCheck == false) {
        errorprint.setLocationRelativeTo(null);
        errorprint.setVisible(true);
        System.out.println("The printer you were searching for could not be found.");
    }
    }
    
    public void cutter() throws PrintException{
    String printerName = printerreceipt;
    boolean printerCheck = false;
    DocPrintJob job = null;
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    for (PrintService printer : printServices) {
        if (printer.getName().equals(printerName)) {
            byte[] cutter = {27,109};
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc cut = new SimpleDoc(cutter, flavor, null);
            job = printer.createPrintJob();
            job.print(cut, null);
            printerCheck = true;
        }
    }
    if (printerCheck == false) {
        errorprint.setLocationRelativeTo(null);
        errorprint.setVisible(true);
        System.out.println("The printer you were searching for could not be found.");
    }
    }
    
    public void cashdrawer() throws PrintException{
    String printerName = printerreceipt;
    boolean printerCheck = false;
    DocPrintJob job = null;
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    for (PrintService printer : printServices) {
        if (printer.getName().equals(printerName)) {
            byte[] cutter = {27,112,48,55,121};
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc cut = new SimpleDoc(cutter, flavor, null);
            job = printer.createPrintJob();
            job.print(cut, null);
            printerCheck = true;
        }
    }
    if (printerCheck == false) {
        errorprint.setLocationRelativeTo(null);
        errorprint.setVisible(true);
        System.out.println("The printer you were searching for could not be found.");
    }
    }
    
    
    public void clearTable(){
       DefaultTableModel tbl = (DefaultTableModel) this.jTable1.getModel();
        tbl.setRowCount(0);
        DefaultTableModel tbl2 = (DefaultTableModel) this.paytable.getModel();
        tbl2.setRowCount(0);
        ppn=0;
        subTotal.setText(NumberFormat.getNumberInstance().format(this.subtotal()));
         Pajak.setText(NumberFormat.getNumberInstance().format(this.tax()));
         discount.setText(NumberFormat.getNumberInstance().format(this.totalDisc()));
         GrandTotal.setText(NumberFormat.getNumberInstance().format(this.grandtotal()));         
         sisatext.setText(NumberFormat.getNumberInstance().format(this.sisa()));
         barcode.requestFocus();
    }
    public static void main(String args[]){

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Application().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCancel;
    private javax.swing.JButton ButtonOK;
    private javax.swing.JButton ButtonPay00;
    private javax.swing.JButton ButtonPay000;
    private javax.swing.JButton ButtonPayCancel;
    private javax.swing.JButton ButtonPayOK;
    private javax.swing.JFrame EODFrame;
    public static javax.swing.JTextField GrandTotal;
    private javax.swing.JButton Logout;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JLabel NamaKasir;
    private javax.swing.JButton OpenShift;
    private javax.swing.JFrame OpeningShiftFrame;
    private javax.swing.JTextField Pajak;
    private javax.swing.JFrame PayFrame;
    private javax.swing.JButton PayIn;
    private javax.swing.JComboBox<String> PayMethod;
    private javax.swing.JButton PayOut;
    private javax.swing.JPanel SalesPanel;
    private javax.swing.JTextField barcode;
    private javax.swing.JButton bayar;
    private javax.swing.JButton button0;
    private javax.swing.JButton button1;
    private javax.swing.JButton button2;
    private javax.swing.JButton button3;
    private javax.swing.JButton button4;
    private javax.swing.JButton button5;
    private javax.swing.JButton button6;
    private javax.swing.JButton button7;
    private javax.swing.JButton button8;
    private javax.swing.JButton button9;
    private javax.swing.JButton buttonBackspace;
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonItemCancel;
    private javax.swing.JButton buttonItemOK;
    private javax.swing.JButton buttonPay0;
    private javax.swing.JButton buttonPay1;
    private javax.swing.JButton buttonPay2;
    private javax.swing.JButton buttonPay3;
    private javax.swing.JButton buttonPay4;
    private javax.swing.JButton buttonPay5;
    private javax.swing.JButton buttonPay6;
    private javax.swing.JButton buttonPay7;
    private javax.swing.JButton buttonPay8;
    private javax.swing.JButton buttonPay9;
    private javax.swing.JButton buttonPayBackspace;
    private javax.swing.JButton buttonPayClear;
    private javax.swing.JButton buttonclose0;
    private javax.swing.JButton buttonclose00;
    private javax.swing.JButton buttonclose000;
    private javax.swing.JButton buttonclose1;
    private javax.swing.JButton buttonclose2;
    private javax.swing.JButton buttonclose3;
    private javax.swing.JButton buttonclose4;
    private javax.swing.JButton buttonclose5;
    private javax.swing.JButton buttonclose6;
    private javax.swing.JButton buttonclose7;
    private javax.swing.JButton buttonclose8;
    private javax.swing.JButton buttonclose9;
    private javax.swing.JButton buttoncloseBS;
    private javax.swing.JButton buttonclosecancel;
    private javax.swing.JButton buttoncloseclear;
    private javax.swing.JButton buttoncloseok;
    private javax.swing.JButton buttonopen0;
    private javax.swing.JButton buttonopen00;
    private javax.swing.JButton buttonopen000;
    private javax.swing.JButton buttonopen1;
    private javax.swing.JButton buttonopen2;
    private javax.swing.JButton buttonopen3;
    private javax.swing.JButton buttonopen4;
    private javax.swing.JButton buttonopen5;
    private javax.swing.JButton buttonopen6;
    private javax.swing.JButton buttonopen7;
    private javax.swing.JButton buttonopen8;
    private javax.swing.JButton buttonopen9;
    private javax.swing.JButton buttonopenBS;
    private javax.swing.JButton buttonopencancel;
    private javax.swing.JButton buttonopenclear;
    private javax.swing.JButton buttonopenok;
    private javax.swing.JButton cancelOpen;
    private javax.swing.JButton cancelOpen1;
    private javax.swing.JButton caribarang;
    private javax.swing.JLabel change;
    private javax.swing.JFrame changeFrame;
    private javax.swing.JButton changeOK;
    private javax.swing.JButton closeopenshift;
    private javax.swing.JFormattedTextField closeshifttext;
    private javax.swing.JFrame closingShiftFrame;
    private javax.swing.JButton closingshift;
    private javax.swing.JTextField customerName;
    private static javax.swing.JLabel date;
    private javax.swing.JLabel dateEOD;
    private javax.swing.JLabel dateOpen;
    private javax.swing.JButton discCancel;
    private javax.swing.JFrame discFrame;
    private javax.swing.JPanel discPanel;
    private javax.swing.JTextField discount;
    private javax.swing.JButton diskon;
    private javax.swing.JButton endofday;
    private javax.swing.JFrame errorprint;
    private javax.swing.JButton errorprintOK;
    private javax.swing.JButton hapus;
    private static javax.swing.JLabel hostName;
    private javax.swing.JFrame itemlist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton lookupCustomer;
    private javax.swing.JButton menubutton;
    private javax.swing.JButton no1;
    private javax.swing.JButton no2;
    private javax.swing.JButton no3;
    private javax.swing.JButton no4;
    private javax.swing.JButton no5;
    private javax.swing.JButton no6;
    private javax.swing.JButton no7;
    private javax.swing.JButton no8;
    private javax.swing.JButton no9;
    private javax.swing.JTextField noInvoice;
    private javax.swing.JLabel openBy;
    private javax.swing.JButton openOK;
    private javax.swing.JButton openOK1;
    private javax.swing.JLabel openText;
    private javax.swing.JLabel openText1;
    private javax.swing.JButton opendrawer;
    private javax.swing.JFrame openingFrame;
    private javax.swing.JButton openingday;
    private javax.swing.JFrame openshift;
    private javax.swing.JButton openshiftprint;
    private javax.swing.JFormattedTextField openshifttext;
    private javax.swing.JTextArea opentextarea;
    private javax.swing.JFormattedTextField payText;
    private javax.swing.JTable paytable;
    private javax.swing.JButton pending;
    private javax.swing.JButton pendingsls;
    private javax.swing.JButton qty;
    private javax.swing.JFrame qtyFrame;
    private javax.swing.JFormattedTextField qtyText;
    private javax.swing.JButton salespanel;
    private javax.swing.JTextField searchbox;
    private javax.swing.JLabel security;
    private javax.swing.JTextField sisatext;
    private javax.swing.JTextField subTotal;
    private javax.swing.JFrame sudahBuka;
    private javax.swing.JButton sudahButton;
    private javax.swing.JLabel sudahLabel;
    private javax.swing.JLabel sudahLabel2;
    private javax.swing.JTable tableItem;
    // End of variables declaration//GEN-END:variables

}

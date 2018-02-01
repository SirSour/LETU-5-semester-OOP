import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;
import javax.swing.plaf.metal.*;
import java.text.*;
import java.util.Date;
import java.util.Properties;

public class OrdersChoice extends JInternalFrame {

    private JLabel OrderNo, DateOfComplete, EmployeeSname,  EmployeeName;
    private JComboBox comboOrderNo, comboEmployeeSname,  comboEmployeeName;
    private JTextField txtDate;
    private JButton check,  order,  cancel,  print;
    private DateButton buttonDate;
  
    
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel panelTitles, panel, mainPanel, panelButtons;
    private static JTextArea txtInfo = new JTextArea(15, 40);
    private Connection connection;
    private static String info;

    public OrdersChoice() {
        super("Разбор заказов", false, true, false, true);
        OrderNo = new JLabel("ID заказа");
        EmployeeSname = new JLabel("Специалист");
        DateOfComplete = new JLabel("Дата выполнения заказа");
       
        comboOrderNo = new JComboBox();
        comboEmployeeSname = new JComboBox();
        comboEmployeeName = new JComboBox();
  
        check = new JButton("Разобранные заказы");
        order = new JButton("Добавить заказ");
        cancel = new JButton("Отмена");
        print = new JButton("Печать");
        buttonDate = new DateButton();
           
        OrderNo.setFont(new Font("sansserif", Font.ITALIC, 14));
        EmployeeSname.setFont(new Font("sansserif", Font.ITALIC, 14));
     //   EmployeeName.setFont(new Font("sansserif", Font.ITALIC, 14));
        DateOfComplete.setFont(new Font("sansserif", Font.ITALIC, 14));
       
        OrderNo.setForeground(new Color(85, 85, 119));
        EmployeeSname.setForeground(new Color(85, 85, 119));
     //   EmployeeName.setForeground(new Color(85, 85, 119));
        DateOfComplete.setForeground(new Color(85, 85, 119));
       

        panelTitles = new JPanel(new GridLayout(9, 2));
        panelTitles.add(OrderNo);
        panelTitles.add(comboOrderNo);
        panelTitles.add(EmployeeSname);
        panelTitles.add(comboEmployeeSname);
   //     panelTitles.add(EmployeeName);
   //     panelTitles.add(comboEmployeeName);
        panelTitles.add(DateOfComplete);
        panelTitles.add(buttonDate);
       

        comboOrderNo.addItem("Выбрать");
        comboEmployeeSname.addItem("Выбрать");
        
        
        panel = new JPanel(new FlowLayout());

        panel.add(panelTitles);

        panelButtons = new JPanel(new FlowLayout());

        panelButtons.add(check);
        panelButtons.add(order);
        panelButtons.add(cancel);
        panelButtons.add(print);
        setSize(550, 330);
        add(panel);
        setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
        setResizable(false);
        try {

            Statement s = odbcConnection.getDBConnection().createStatement();
        } catch (Exception excp) {
            excp.printStackTrace();
            info = info + excp.toString();
        }
        setIDOrder();
        setComboEmployee();
        

        order.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddOrder();
            }
        });
        
        check.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ShowChoice form = new ShowChoice();
                MainWindow.desktop.add(form);
                form.setVisible(true);
                try{
                    form.setSelected(true);
                } catch(Exception ex){ }
                
            new ShowChoice().setVisible(true);
            }
        });
        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
				ChoiceReport form = new ChoiceReport();
                MainWindow.desktop.add(form);
                form.setVisible(true);
                try{
                    form.setSelected(true);
                } catch(Exception ex){}                
            }
        });
        
        comboOrderNo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	comboOrderNo.setSelectedIndex(comboOrderNo.getSelectedIndex());
            }
        });
        
        comboEmployeeSname.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	comboEmployeeName.setSelectedIndex(comboEmployeeSname.getSelectedIndex());
            }
        });
        
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(panelTitles, BorderLayout.CENTER);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);
        add(mainPanel);

    }

    private void setIDOrder() {
        try {
        	Statement statement = odbcConnection.getDBConnection().createStatement();
        	String temp = ("SELECT OrderNo FROM Orders order by OrderNo");
       	 	ResultSet rst = statement.executeQuery(temp);
     
            while (rst.next()) {

                comboOrderNo.addItem(rst.getString("OrderNo"));
        
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
    }

    private void setComboEmployee() {

        try {
        
            ResultSet rst = odbcConnection.getDBConnection().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT Employee.empNo, Employee.Sname, Employee.Fname, Employee.Lname, Employee.Designation FROM Employee WHERE Employee.Designation = 'Покраска' "
                    		+ "OR Employee.Designation = 'Кузовные работы' "
                    		+ "OR Employee.Designation = 'Диагностирование электроники'"
                    		+ "OR Employee.Designation = 'Отделка салона'"
                    		+ "OR Employee.Designation = 'Шиномонтажные работы'"
                    		+ "OR Employee.Designation = 'Ремонт и обслуживание двигателей'");
          
            while (rst.next()) {
              
                comboEmployeeSname.addItem(rst.getString("Sname")); 
                comboEmployeeName.addItem(rst.getString("Fname")); 
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
    }

    public void AddOrder() {
    	
    	try {
    		if (comboOrderNo.getSelectedItem() == ("Выбрать")) {
               JOptionPane.showMessageDialog(null, "Выберите ID заказа", "Смотри, че делаешь-то",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
                    if (comboEmployeeSname.getSelectedItem() == ("Выбрать")) {
                        JOptionPane.showMessageDialog(null, "Выберите специалиста", "Смотри, че делаешь-то",
                                JOptionPane.DEFAULT_OPTION);
                        return;
                    }
           
    	try {
	      	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	      
	      	Properties connectionInfo = new Properties();
	      	connectionInfo.put("charSet", "Cp1251");
	      	try {
					Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
					} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	      		String db = "jdbc:ucanaccess://C://Program Files//eclipse//workspace//Сoursework//CW.mdb";
	      		Connection connection = DriverManager.getConnection(db, connectionInfo);
	          Statement statement = odbcConnection.getDBConnection().createStatement();
	     
	          String temp =  "INSERT INTO Choice (OrderNo, Sname, EmpName, DateComplete) "
	          		+ " VALUES (?, ?, ?, ?)";
	          PreparedStatement stmt = connection.prepareStatement(temp);
	          stmt.setString(1, comboOrderNo.getSelectedItem().toString());
	          stmt.setString(2, comboEmployeeSname.getSelectedItem().toString());
	          stmt.setString(3, comboEmployeeName.getSelectedItem().toString());
	    
	          Date date = dateFormat.parse(buttonDate.getText());
	          stmt.setDate(4, new java.sql.Date(date.getTime()));              
	     
	          stmt.executeUpdate();
	              JOptionPane.showMessageDialog(null, "Данные обновлены", "Обновление",
	                      JOptionPane.DEFAULT_OPTION);
	        
	          statement.close();
	      } catch (SQLException sqlex) {
	          sqlex.printStackTrace();
	         } 
			}catch (Exception ex) {
	            ex.printStackTrace();
	    }
    }
}


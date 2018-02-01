import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Payment extends JInternalFrame {

	private JLabel lblPayNo, lblClientName, lblClientNo, lblMode, lblDate, lblAmount, lblReceived;
	public JTextField txtPayNo, txtClientNo, txtClientName, txtAmount;
	public JComboBox comboClientNo, comboClientName, payMode, comboAmount, comboReceived;
	private JButton buttonPay, buttonPrint, buttonCancel, buttonSearch;
	private JPanel panel1, pane, panel3;
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	public DateButton buttonDate;
	String payNo, clientNo, clientName, mode, datePay, amount, received;

	public Payment() {
		super("Оплата", false, true, false, true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		lblPayNo = new JLabel("ID платежа");
		lblClientNo = new JLabel("ID клиента");
		lblClientName = new JLabel("Клиент");
		lblMode = new JLabel("Тип оплаты");
		lblDate = new JLabel("Дата оплаты");
		lblAmount = new JLabel("Сумма");
		lblReceived = new JLabel("Принял");

		txtPayNo = new JTextField(10);
		txtClientNo = new JTextField(10);
		txtClientName = new JTextField(10);
		txtAmount = new JTextField(10);

		buttonDate = new DateButton();
		buttonDate.setForeground(new Color(0, 0, 0));

		comboClientNo = new JComboBox();
		comboClientName = new JComboBox();
		comboAmount = new JComboBox();
		comboReceived = new JComboBox();
		payMode = new JComboBox();

		payMode.addItem("Наличные");
		payMode.addItem("Карта");

		buttonPay = new JButton("Оплата");
		buttonPrint = new JButton("Печать");
		buttonCancel = new JButton("Отмена");
		buttonSearch = new JButton("Поиск");

		panel1 = new JPanel(new GridLayout(7, 2));
		panel1.setPreferredSize(new Dimension(350, 250));
		panel1.add(lblPayNo);
		panel1.add(txtPayNo);
		// panel1.add(lblClientNo);
		// panel1.add(txtClientNo);
		// panel1.add(comboClientNo);
		panel1.add(lblClientName);
		// panel1.add(txtClientName);
		panel1.add(comboClientName);
		panel1.add(lblAmount);
		// panel1.add(txtAmount);
		panel1.add(comboAmount);
		panel1.add(lblMode);
		panel1.add(payMode);
		panel1.add(lblDate);
		panel1.add(buttonDate);
		panel1.add(lblReceived);
		panel1.add(comboReceived);

		pane = new JPanel();
		pane.add(buttonPay);

		// pane.add(buttonPrint);
		pane.add(buttonCancel);
		pane.add(buttonSearch);

		panel3 = new JPanel();
		panel3.add(panel1);
		panel3.add(pane);

		buttonPrint.setEnabled(false);
		add(panel3);

		setSize(500, 350);

		setIDName();
		GenerateID();
		setAmount();
		setComboReceived();

		setLocation((screen.width - 1100) / 2, ((screen.height - 700) / 2));
		setResizable(false);

		comboClientName.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comboClientName.setSelectedIndex(comboClientName.getSelectedIndex());
				comboClientNo.setSelectedIndex(comboClientName.getSelectedIndex());
				comboAmount.removeItem(comboAmount.getSelectedItem());

				setAmount();

			}
		});

		buttonCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(true);
				dispose();

			}
		});
		buttonPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Invoice form = new Invoice();
				MainWindow.desktop.add(form);
				form.setVisible(true);
				buttonPrint.setEnabled(false);
			}
		});

		buttonPay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (comboClientNo.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(null, "Все клиенты оплатили счета", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				if (comboClientName.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(null, "Все клиенты оплатили счета", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				GenerateID();

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

					String temp = "INSERT INTO Payment (PaymentNo, ClientNo, ClientName, PaymentMode, DateOfPay, Amount, ReceivedBy) "
							+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement stmt = connection.prepareStatement(temp);
					stmt.setString(1, txtPayNo.getText());

					stmt.setString(2, comboClientNo.getSelectedItem().toString());
					stmt.setString(3, comboClientName.getSelectedItem().toString());
					stmt.setString(4, (payMode.getSelectedItem()).toString());

					Date date = dateFormat.parse(buttonDate.getText());
					stmt.setDate(5, new java.sql.Date(date.getTime()));

					stmt.setString(6, comboAmount.getSelectedItem().toString());
					stmt.setString(7, (comboReceived.getSelectedItem()).toString());
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Данные обновлены", "Обновление", JOptionPane.DEFAULT_OPTION);
					// }
					statement.close();
				} catch (SQLException sqlex) {
					sqlex.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					Statement statement = odbcConnection.getDBConnection().createStatement();

					String temp = "UPDATE Clients SET PayStatus='Оплачено'" + "WHERE ClientNo LIKE  '"
							+ comboClientNo.getSelectedItem() + "'";
					int result = statement.executeUpdate(temp);

				} catch (SQLException sqlex) {
					sqlex.printStackTrace();
				}

				buttonPay.setEnabled(false);
				buttonPrint.setEnabled(true);
			}
		});

		buttonSearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					if (!txtPayNo.equals("")) {
						Statement statement = odbcConnection.getDBConnection().createStatement();
						String query = ("SELECT * FROM Payment WHERE PaymentNo ='" + txtPayNo.getText() + "'");

						ResultSet rs = statement.executeQuery(query);

						display(rs);

						statement.close();
					}
				} catch (SQLException sqlex) {
					sqlex.printStackTrace();
				}
				setVisible(true);

			}
		});

	}

	private void GenerateID() {

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT PaymentNo FROM Payment");
			ResultSet rst = statement.executeQuery(temp);

			txtPayNo.setText("1000");
			while (rst.next()) {
				String s;
				int number = rst.getInt(1);
				number = number + 1;
				s = "" + number;
				txtPayNo.setText(s);
			}
		} catch (Exception n) {
			JOptionPane.showMessageDialog(null, "Ошибка при добавлении ID");
		}
	}

	private void setIDName() {

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT * FROM Clients where PayStatus='Не оплачено'");
			ResultSet rst = statement.executeQuery(temp);

			while (rst.next()) {
				comboClientNo.addItem(rst.getString("ClientNo"));
				comboClientName.addItem(rst.getString("ClientName"));

			}
		} catch (Exception n) {
			n.printStackTrace();
		}

	}

	private void setAmount() {

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT Amount FROM Clients  where ClientNo='" + comboClientNo.getSelectedItem() + "'");
			ResultSet rst = statement.executeQuery(temp);

			while (rst.next()) {
				comboAmount.addItem(rst.getString("Amount"));

			}
		} catch (Exception n) {
			n.printStackTrace();
		}
	}

	private void setComboReceived() {

		try {

			ResultSet rst = odbcConnection.getDBConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(
							"SELECT Employee.empNo, Employee.Sname, Employee.Fname, Employee.Lname, Employee.Designation FROM Employee WHERE Employee.Designation = 'Покраска' "
									+ "OR Employee.Designation = 'Кузовные работы' "
									+ "OR Employee.Designation = 'Диагностирование электроники'"
									+ "OR Employee.Designation = 'Отделка салона'"
									+ "OR Employee.Designation = 'Шиномонтажные работы'"
									+ "OR Employee.Designation = 'Ремонт и обслуживание двигателей'");

			while (rst.next()) {
				comboReceived.addItem(rst.getString(2));
			}
		} catch (Exception n) {
			n.printStackTrace();
		}
	}

	public void display(ResultSet rs) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			boolean recordNumber = rs.next();
			if (recordNumber) {
				payNo = rs.getString("PaymentNo");
				clientNo = rs.getString("ClientNo");
				clientName = rs.getString("ClientName");
				mode = rs.getString("PaymentMode");
				datePay = rs.getString("DateOfPay");
				amount = rs.getString("Amount");
				received = rs.getString("ReceivedBy");

				txtPayNo.setText(payNo);
				comboClientNo.setSelectedItem(clientNo);
				comboClientName.setSelectedItem(clientName);
				payMode.setSelectedItem(mode);
				buttonDate.setText(datePay);
				comboAmount.setSelectedItem(amount);
				comboReceived.setSelectedItem(received);

			} else {
				JOptionPane.showMessageDialog(null, "Запись не найдена", "Ошибка", JOptionPane.DEFAULT_OPTION);
			}
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();

		}

	}
}

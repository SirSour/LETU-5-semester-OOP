import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

public class AddNewClient extends JInternalFrame {

	private JLabel clientNo, clientName, telephone, amount, dateOfOrder, payStatus;
	private JTextField txtClientNo, txtClientName, txtTelephone, txtAmount, txtDateOfOrder, txtPayStatus;
	private JButton add, cancel, clear, buttonDate;
	private JPanel panelTitles, panelButtons, mainPanel, panel2;
	private static JTextArea txtInfo = new JTextArea(15, 40);

	String strClinetNo, strClientName, strTelephone, strAmount, strDateOfOrder, strPayStatus;
	private Statement stmt;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public AddNewClient(JTable tableClientsList) {

		super("Добавление данных о новом клиенте", false, true, false, true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		clientNo = new JLabel("ID клиента");
		clientName = new JLabel("Имя клиента");
		telephone = new JLabel("Телефон");
		amount = new JLabel("Сумма");
		dateOfOrder = new JLabel("Дата заказа");
		payStatus = new JLabel("Статус оплаты");

		txtClientNo = new JTextField(10);
		txtClientName = new JTextField(10);
		txtTelephone = new JTextField(11);
		txtAmount = new JTextField(10);
		txtDateOfOrder = new JTextField(10);
		txtPayStatus = new JTextField(50);

		buttonDate = new DateButton();
		buttonDate.setForeground(new Color(0, 0, 0));

		txtClientNo.setText(strClinetNo);
		txtClientName.setText(strClientName);
		txtTelephone.setText(strTelephone);
		txtAmount.setText(strAmount);

		txtPayStatus.setText(strDateOfOrder);

		add = new JButton("Добавить");
		clear = new JButton("Почистить");
		cancel = new JButton("Отмена");

		panelTitles = new JPanel(new GridLayout(11, 2)); // прямоугольная компоновка
		panelTitles.setPreferredSize(new Dimension(350, 250));
		panelTitles.add(clientNo);
		panelTitles.add(txtClientNo);
		panelTitles.add(clientName);
		panelTitles.add(txtClientName);
		panelTitles.add(telephone);
		panelTitles.add(txtTelephone);
		panelTitles.add(amount);
		panelTitles.add(txtAmount);
		panelTitles.add(dateOfOrder);
		panelTitles.add(buttonDate);
		panelTitles.add(payStatus);
		panelTitles.add(txtPayStatus);

		panelButtons = new JPanel();
		panelButtons.add(add);
		panelButtons.add(clear);
		panelButtons.add(cancel);

		panel2 = new JPanel();
		panel2.add(panelTitles);
		panel2.add(panelButtons);

		add(panel2);
		setSize(400, 250);
		GenerateID();

		setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
		setResizable(false); // не менять размер

		try {
			Statement stmn = odbcConnection.getDBConnection().createStatement();
		} catch (Exception excp) {
			excp.printStackTrace();
		}

		add.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

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

					String temp = "INSERT INTO Clients (ClientNo, ClientName, Telephone, Amount, DateOfOrder, PayStatus) "
							+ " VALUES (?, ?, ?, ?, ?, ?)";
					PreparedStatement stmt = connection.prepareStatement(temp);
					stmt.setString(1, txtClientNo.getText());
					stmt.setString(2, txtClientName.getText());
					stmt.setString(3, txtTelephone.getText());
					stmt.setString(4, txtAmount.getText());
					stmt.setDate(5, Date.valueOf(buttonDate.getText()));
					stmt.setString(6, txtPayStatus.getText());

					stmt.executeUpdate();

					String ObjButtons[] = { "Да", "Нет" };
					int PromptResult = JOptionPane.showOptionDialog(null, "Данные добавлены. Хотите добавить ещё?",
							"Добавление информации", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
							ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {

						GenerateID();
						txtClientName.setText("");
						txtTelephone.setText("");
						txtAmount.setText("");
						txtPayStatus.setText("");

					} else {
						//Reloading(tableClientsList);
						setVisible(false);
					}

				} catch (SQLException sqlex) {
					sqlex.printStackTrace();

				}
			}
		});

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		clear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				txtClientName.setText("");
				txtTelephone.setText("");
				txtAmount.setText("");
				txtPayStatus.setText("");
			}
		});

		mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(panel2, BorderLayout.CENTER);
		mainPanel.add(panelButtons, BorderLayout.SOUTH);

		getContentPane().add(mainPanel);

		pack();
		setVisible(true);

	}

	class AbstractTable extends AbstractTableModel {

		private String[] titles = { "ID клиента", "Имя", "Телефон", "Сумма", "Дата заказа", "Статус оплаты" };

		private Object[][] data = new Object[50][6];

		public int getColumnCount() {
			return titles.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return titles[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	public void Reloading(JTable tableClientsList) {
		try {

			String sql = ("SELECT * FROM Clients ORDER BY ClientNo");
			int Numrow = 0;
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				tableClientsList.setValueAt(result.getString(1).trim(), Numrow, 0);
				tableClientsList.setValueAt(result.getString(2).trim(), Numrow, 1);
				tableClientsList.setValueAt(result.getString(3).trim(), Numrow, 2);
				tableClientsList.setValueAt(result.getString(4).trim(), Numrow, 3);
				tableClientsList.setValueAt(result.getDate(5), Numrow, 4);
				tableClientsList.setValueAt(result.getString(6).trim(), Numrow, 5);
				sdf.format(5);
				Numrow++;
			}
		} catch (SQLException sqlex) {
			JOptionPane.showMessageDialog(null, "Ошибка считывания данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void GenerateID() {

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT ClientNo FROM Clients");
			ResultSet rst = statement.executeQuery(temp);

			txtClientNo.setText("1000");
			while (rst.next()) {
				String s;
				int number = rst.getInt(1);
				number = number + 1;
				s = "" + number;
				txtClientNo.setText(s);
			}
		} catch (Exception n) {
			JOptionPane.showMessageDialog(null, "Ошибка при добавлении ID");
		}
	}

}

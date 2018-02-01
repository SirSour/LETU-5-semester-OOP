import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

public class AddNewOrder extends JInternalFrame {

	private JLabel OrderNo, RegNo, Model, DateOfOrder, Comment;
	private JTextField txtOrderNo, txtRegNo, txtModel, txtDateOfOrder, txtComment;
	private JButton add, cancel, clear, buttonDate;
	private JPanel panelTitles, panelButtons, mainPanel, panel2;
	private static JTextArea txtInfo = new JTextArea(15, 40);

	String strOrderNo, strRegNo, strModel, strDateOfOrder, strComment;
	private Statement stmt;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public AddNewOrder(JTable tableOrdersList) {

		super("Добавление данных о новом заказе", false, true, false, true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		OrderNo = new JLabel("ID заказа");
		RegNo = new JLabel("Регистрационный номер");
		Model = new JLabel("Модель");
		DateOfOrder = new JLabel("Дата заказа");
		Comment = new JLabel("Комментарии");

		txtOrderNo = new JTextField(10);
		txtRegNo = new JTextField(10);
		txtModel = new JTextField(10);
		txtComment = new JTextField(50);

		buttonDate = new DateButton();
		buttonDate.setForeground(new Color(0, 0, 0));

		txtOrderNo.setText(strOrderNo);
		txtRegNo.setText(strRegNo);
		txtModel.setText(strModel);
		txtComment.setText(strComment);

		add = new JButton("Добавить");
		clear = new JButton("Почистить");
		cancel = new JButton("Отмена");

		panelTitles = new JPanel(new GridLayout(10, 2)); // прямоугольная компоновка
		panelTitles.setPreferredSize(new Dimension(400, 250));
		panelTitles.add(OrderNo);
		panelTitles.add(txtOrderNo);
		panelTitles.add(RegNo);
		panelTitles.add(txtRegNo);
		panelTitles.add(Model);
		panelTitles.add(txtModel);
		panelTitles.add(DateOfOrder);
		panelTitles.add(buttonDate);
		panelTitles.add(Comment);
		panelTitles.add(txtComment);

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

					String temp = "INSERT INTO Orders (OrderNo, RegNo, Model, DateOfOrder, Comment) "
							+ " VALUES (?, ?, ?, ?, ?)";
					PreparedStatement stmt = connection.prepareStatement(temp);
					stmt.setString(1, txtOrderNo.getText());
					stmt.setString(2, txtRegNo.getText());
					stmt.setString(3, txtModel.getText());
					stmt.setDate(4, Date.valueOf(buttonDate.getText()));
					stmt.setString(5, txtComment.getText());

					stmt.executeUpdate();

					String ObjButtons[] = { "Да", "Нет" };
					int PromptResult = JOptionPane.showOptionDialog(null, "Данные добавлены. Хотите добавить ещё?",
							"Добавление информации", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
							ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {

						GenerateID();
						txtRegNo.setText("");
						txtModel.setText("");
						txtComment.setText("");

					} else {
						Reloading(tableOrdersList);
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

				txtOrderNo.setText("");
				txtRegNo.setText("");
				txtModel.setText("");
				txtComment.setText("");
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

		private String[] titles = { "ID заказа", "Регистрационный номер", "Модель авто", "Дата заказа", "Комментарии" };

		private Object[][] data = new Object[50][5];

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

	public void Reloading(JTable tableOrdersList) {
		try {
			AbstractTable model = new AbstractTable();
			tableOrdersList.setModel(model);
			model.fireTableStructureChanged();
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String sql = ("SELECT * FROM Orders ORDER BY OrderNo");
			int Numrow = 0;
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				tableOrdersList.setValueAt(result.getString(1).trim(), Numrow, 0);
				tableOrdersList.setValueAt(result.getString(2).trim(), Numrow, 1);
				tableOrdersList.setValueAt(result.getString(3).trim(), Numrow, 2);
				tableOrdersList.setValueAt(result.getDate(4), Numrow, 3);
				tableOrdersList.setValueAt(result.getString(5).trim(), Numrow, 4);
				sdf.format(4);
				Numrow++;
			}
		} catch (SQLException sqlex) {
			JOptionPane.showMessageDialog(null, "Ошибка считывания данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void GenerateID() {

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT OrderNo FROM Orders");
			ResultSet rst = statement.executeQuery(temp);

			txtOrderNo.setText("1000");
			while (rst.next()) {
				String s;
				int number = rst.getInt(1);
				number = number + 1;
				s = "" + number;
				txtOrderNo.setText(s);
			}
		} catch (Exception n) {
			JOptionPane.showMessageDialog(null, "Ошибка при добавлении ID");
		}
	}

}

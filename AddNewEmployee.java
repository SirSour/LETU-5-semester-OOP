import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class AddNewEmployee extends JInternalFrame {

	private JLabel empNo, Sname, Fname, Lname, Designation, CountWorks, lblPhoto;
	private JTextField txtempNo, txtSname, txtFname, txtLname, txtDesignation, txtCountWorks;
	private JButton add, cancel, clear;

	private JPanel panelTitles, panelButtons, mainPanel, panel2;

	private static JTextArea txtInfo = new JTextArea(15, 40);
	private Connection connection;
	String strempNo, strSname, strFname, strLname, strDesignation, strCountWorks, getPicture;
	String getPhoto;

	final JFileChooser fileChoose = new JFileChooser();
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public AddNewEmployee(JTable tableEmployee) {

		super("Добавление данных о новом специалисте.", false, true, false, true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		empNo = new JLabel("ID работника");
		Sname = new JLabel("Фамилия");
		Fname = new JLabel("Имя");
		Lname = new JLabel("Отчество");
		Designation = new JLabel("Специальность");
		CountWorks = new JLabel("Кол-во заказов");

		txtempNo = new JTextField(10);
		txtSname = new JTextField(10);
		txtFname = new JTextField(10);
		txtLname = new JTextField(10);
		txtDesignation = new JTextField(20);
		txtCountWorks = new JTextField(5);

		txtempNo.setText(strempNo);
		txtSname.setText(strSname);
		txtFname.setText(strFname);
		txtLname.setText(strLname);
		txtDesignation.setText(strDesignation);
		txtCountWorks.setText(strCountWorks);

		add = new JButton("Добавить");
		clear = new JButton("Почистить");
		cancel = new JButton("Отмена");

		panelTitles = new JPanel(new GridLayout(10, 2)); // прямоугольная компоновка
		panelTitles.setPreferredSize(new Dimension(400, 250));
		panelTitles.add(empNo);
		panelTitles.add(txtempNo);
		panelTitles.add(Sname);
		panelTitles.add(txtSname);
		panelTitles.add(Fname);
		panelTitles.add(txtFname);
		panelTitles.add(Lname);
		panelTitles.add(txtLname);
		panelTitles.add(Designation);
		panelTitles.add(txtDesignation);
		panelTitles.add(CountWorks);
		panelTitles.add(txtCountWorks);

		panelButtons = new JPanel();
		panelButtons.add(add);
		panelButtons.add(clear);
		panelButtons.add(cancel);

		panel2 = new JPanel();
		panel2.add(panelTitles);
		panel2.add(panelButtons);

		add(panel2);
		setSize(400, 250);

		setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
		setResizable(false); // не менять размер
		GenerateID();
		try {
			Statement stmn = odbcConnection.getDBConnection().createStatement();
		} catch (Exception excp) {
			excp.printStackTrace();
		}

		txtSname.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_SPACE)
						|| (c == KeyEvent.VK_DELETE))) {
					txtSname.requestFocus();
					JOptionPane.showMessageDialog(null, "Только текст.", "Ошибка", JOptionPane.DEFAULT_OPTION);
					e.consume();
				}
			}
		});

		txtFname.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_SPACE)
						|| (c == KeyEvent.VK_DELETE))) {
					txtFname.requestFocus();
					JOptionPane.showMessageDialog(null, "Только текст.", "Ошибка", JOptionPane.DEFAULT_OPTION);
					e.consume();
				}
			}
		});
		txtLname.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_SPACE)
						|| (c == KeyEvent.VK_DELETE))) {
					txtLname.requestFocus();
					JOptionPane.showMessageDialog(null, "Только текст.", "Ошибка", JOptionPane.DEFAULT_OPTION);
					e.consume();
				}
			}
		});
		txtDesignation.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_SPACE)
						|| (c == KeyEvent.VK_DELETE))) {
					txtDesignation.requestFocus();
					JOptionPane.showMessageDialog(null, "Только текст.", "Ошибка", JOptionPane.DEFAULT_OPTION);
					e.consume();
				}
			}
		});
		txtCountWorks.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String content = textField.getText();
				if (content.length() != 0) {
					try {
						Integer.parseInt(content);
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Неверный формат данных", "Ошибка",
								JOptionPane.DEFAULT_OPTION);
						textField.requestFocus();
						txtCountWorks.setText("");
					}
				}
			}
		});

		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (txtSname.getText() == null || txtSname.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите фамилию работника", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtSname.requestFocus();
					return;
				}
				if (txtFname.getText() == null || txtFname.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите имя работника", "Ошибка", JOptionPane.DEFAULT_OPTION);
					txtFname.requestFocus();
					return;
				}
				if (txtLname.getText() == null || txtLname.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите отчество работника", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtLname.requestFocus();
					return;
				}
				if (txtDesignation.getText() == null || txtDesignation.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите специальность работника", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					Designation.requestFocus();
					return;
				}
				if (txtCountWorks.getText() == null || txtCountWorks.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите кол-во выполненных заказов", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtCountWorks.requestFocus();
					return;
				}

				try {
					Properties connectionInfo = new Properties();
					connectionInfo.put("charSet", "Cp1251");
					try {
						Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String db = "jdbc:ucanaccess://C://Program Files//eclipse//workspace//Сoursework//CW.mdb";
					connection = DriverManager.getConnection(db, connectionInfo);

					String temp = "INSERT INTO Employee (empNo, Sname, Fname, Lname, Designation, CountWorks) "
							+ " VALUES (?, ?, ?, ?, ?, ?)";
					PreparedStatement stmt = connection.prepareStatement(temp);
					stmt.setString(1, txtempNo.getText());
					stmt.setString(2, txtSname.getText());
					stmt.setString(3, txtFname.getText());
					stmt.setString(4, txtLname.getText());
					stmt.setString(5, txtDesignation.getText());
					stmt.setString(6, txtCountWorks.getText());
					stmt.executeUpdate();

					try {
						lblPhoto.setIcon(
								new ImageIcon("C:\\Program Files\\eclipse\\workspace\\Сoursework\\images\\car.jpeg"));
					} catch (Exception p) {
					}

					String ObjButtons[] = { "Да", "Нет" };
					int PromptResult = JOptionPane.showOptionDialog(null, "Данные добавлены. Хотите добавить ещё?",
							"Добавление информации", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
							ObjButtons, ObjButtons[1]);
					if (PromptResult == 0) {

						GenerateID();
						txtSname.setText("");
						txtFname.setText("");
						txtLname.setText("");
						txtDesignation.setText("");
						txtCountWorks.setText("");

					} else {
						LoadTableEmployee(tableEmployee);
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

				txtempNo.setText("");
				txtSname.setText("");
				txtFname.setText("");
				txtLname.setText("");
				txtDesignation.setText("");
				txtCountWorks.setText("");
			}
		});

		mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(panel2, BorderLayout.CENTER);
		mainPanel.add(panelButtons, BorderLayout.SOUTH);

		getContentPane().add(mainPanel);

		pack();
		setVisible(true);

	}

	private void GenerateID() {

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT empNo FROM Employee");
			ResultSet rst = statement.executeQuery(temp);

			txtempNo.setText("1000");
			while (rst.next()) {
				String s;
				int number = rst.getInt(1);
				number = number + 1;
				s = "" + number;
				txtempNo.setText(s);
			}
		} catch (Exception n) {
			JOptionPane.showMessageDialog(null, "Ошибка при добавлении ID");
		}
	}

	class AbstractTable extends AbstractTableModel {

		private String[] titles = { "ID", "Фамилия", "Имя", "Отчество", "Специальность", "Заказы" };
		public Object[][] data = new Object[50][6];

		public void removeRow() {
			fireTableDataChanged();
		}

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
			fireTableDataChanged();
			fireTableStructureChanged();
		}
	}

	public void LoadTableEmployee(JTable tableEmployee) {
		try {
			AbstractTable model = new AbstractTable();
			tableEmployee.setModel(model);
			model.fireTableStructureChanged();
			// используя временные таблицы
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT * FROM Employee ORDER BY empNo");
			int numberRow = 0;
			ResultSet result = statement.executeQuery(temp);
			while (result.next()) {
				tableEmployee.setValueAt(result.getString("empNo"), numberRow, 0);
				tableEmployee.setValueAt(result.getString("Sname"), numberRow, 1);
				tableEmployee.setValueAt(result.getString("Fname"), numberRow, 2);
				tableEmployee.setValueAt(result.getString("Lname"), numberRow, 3);
				tableEmployee.setValueAt(result.getString("Designation"), numberRow, 4);
				tableEmployee.setValueAt(result.getString("CountWorks"), numberRow, 5);
				numberRow++;
			}
			revalidate();
			repaint();
		} catch (SQLException sqlex) {
			txtInfo.append(sqlex.toString());
		}
	}
}

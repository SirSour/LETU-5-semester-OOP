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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

public class NewEntry extends JInternalFrame {

	private JLabel empNo, Sname, Fname, Lname, Designation, CountWorks;
	private JTextField txtempNo, txtSname, txtFname, txtLname, txtDesignation, txtCountWorks;
	private JButton update, clear, cancel;

	private JPanel panelTitles, panelButtons, mainPanel, panel2;

	private static JTextArea txtInfo = new JTextArea(15, 40);

	String strempNo, strSname, strFname, strLname, strDesignation, strCountWorks;
	int number;

	final JFileChooser fileChoose = new JFileChooser();
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public NewEntry(JTable tableEmployee, String strempNo, String strSname, String strFname, String strLname,
			String strDesignation, String strCountWorks) {

		super("Обновление данных о работнике.", false, true, false, true);
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

		update = new JButton("Обновить");
		clear = new JButton("Почистить");
		cancel = new JButton("Отмена");

		panelTitles = new JPanel(new GridLayout(11, 2)); // прямоугольная компоновка
		panelTitles.setPreferredSize(new Dimension(350, 250));
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
		panelButtons.add(update);
		panelButtons.add(clear);
		panelButtons.add(cancel);

		panel2 = new JPanel();
		panel2.add(panelTitles);
		panel2.add(panelButtons);

		add(panel2);
		setSize(400, 250);
		setLocation((screen.width - 1000) / 2, ((screen.height - 700) / 2));
		setResizable(false); // не менять размер

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

		try {
			Statement stmn = odbcConnection.getDBConnection().createStatement();
		} catch (Exception excp) {
			excp.printStackTrace();
		}

		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtempNo.getText() == null || txtempNo.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите ID работника", "Ошибка", JOptionPane.DEFAULT_OPTION);
					txtempNo.requestFocus();
					return;
				}
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
					Statement statement = odbcConnection.getDBConnection().createStatement();

					String temp = "UPDATE Employee SET " + "Sname      ='" + txtSname.getText() + "',Fname      ='"
							+ txtFname.getText() + "',Lname      ='" + txtLname.getText() + "',Designation='"
							+ txtDesignation.getText() + "',CountWorks  ='" + txtCountWorks.getText()
							+ "' WHERE empNo LIKE'" + txtempNo.getText() + "'";
					int result = statement.executeUpdate(temp);

					LoadTableEmployee(tableEmployee);
					setVisible(false);
					dispose();

				} catch (SQLException sqlex) {
					JOptionPane.showMessageDialog(null, "Ошибка при обновлении базы данных", "Ошибка",
							JOptionPane.ERROR_MESSAGE);
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

	public void LoadTableEmployee(JTable tableEmployee) {
		try {

			// используя временные таблицы
			Statement statement = odbcConnection.getDBConnection().createStatement();
			String temp = ("SELECT * FROM Employee ORDER BY empNo ASC");
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

	public void Display(ResultSet rs) {
		try {

			boolean recordNumber = rs.next();
			if (recordNumber) {
				number = rs.getInt("empNo");
				strSname = rs.getString("Sname");
				strFname = rs.getString("Fname");
				strLname = rs.getString("Lname");
				strDesignation = rs.getString("Designation");
				strCountWorks = rs.getString("CountWorks");

				txtSname.setText(strSname);
				txtFname.setText(strFname);
				txtLname.setText(strLname);
				txtDesignation.setText(strDesignation);
				txtCountWorks.setText(strCountWorks);
			} else {
				JOptionPane.showMessageDialog(null, "Запись не найдена", "Ошибка", JOptionPane.DEFAULT_OPTION);
			}
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	public void delete() {

		int DResult = JOptionPane.showConfirmDialog(null, "Уверены, что хотите удалить данную информацию?");
		if (DResult == JOptionPane.NO_OPTION) {
			JOptionPane.showMessageDialog(null, "Отмена удаления", "Удаление", JOptionPane.DEFAULT_OPTION);
		}

		if (DResult == JOptionPane.YES_OPTION) {
			try {
				Statement statement = odbcConnection.getDBConnection().createStatement();
				if (!txtempNo.equals("")) {

					String query = ("DELETE  FROM Employee where empNo='" + txtempNo.getText() + "'");
					int result = statement.executeUpdate(query);
					if (result != 0) {
						JOptionPane.showMessageDialog(null, "Информация удалена", "Удаление",
								JOptionPane.DEFAULT_OPTION);

					} else {
						txtempNo.setText("");
						txtSname.setText("");
						txtFname.setText("");
						txtLname.setText("");
						txtDesignation.setText("");
						txtCountWorks.setText("");
					}
					result = statement.executeUpdate(query);
					statement.close();
				}
				setVisible(false);
				dispose();
			} catch (SQLException sqlex) {
				sqlex.printStackTrace();
			}
		}
	}
}

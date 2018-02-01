import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

public class NewEntryClient extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel clientNo, clientName, telephone, amount, dateOfOrder, payStatus;
	private JTextField txtClientNo, txtClientName, txtTelephone, txtAmount, txtDateOfOrder, txtPayStatus;
	private JButton buttonUpdate, buttonSearch, buttonClear, buttonDelete, buttonCancel;
	String strClinetNo, strClientName, strTelephone, strAmount, strDateOfOrder, strPayStatus;
	private JPanel panelTitles;
	private JPanel panelButtons;
	private DateButton date_Order;
	private Statement stmt;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private static JTextArea txtInfo = new JTextArea(15, 40);

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public NewEntryClient(JTable tableClientsList, String strClinetNo, String strClientName, String strTelephone,
			String strAmount, String strDateOfOrder, String strPayStatus) {
		super("Обновление данных о клиенте", false, true, false, true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setSize(400, 250);
		setLocation((screen.width - 1000) / 2, ((screen.height - 700) / 2));

		clientNo = new JLabel("ID клиента");
		clientName = new JLabel("Имя клиента");
		telephone = new JLabel("Телефон");
		amount = new JLabel("Сумма");
		dateOfOrder = new JLabel("Дата заказа");
		payStatus = new JLabel("Статус оплаты");

		txtClientNo = new JTextField(10);
		txtClientName = new JTextField(20);
		txtTelephone = new JTextField(20);
		txtAmount = new JTextField(10);
		txtDateOfOrder = new JTextField(10);
		txtPayStatus = new JTextField(20);

		date_Order = new DateButton();
		date_Order.setForeground(new Color(0, 0, 0));

		txtClientNo.setText(strClinetNo);
		txtClientName.setText(strClientName);
		txtTelephone.setText(strTelephone);
		txtAmount.setText(strAmount);
		txtDateOfOrder.setText(strDateOfOrder);
		txtPayStatus.setText(strPayStatus);

		buttonUpdate = new JButton("Обновить");
		buttonSearch = new JButton("Поиск");
		buttonClear = new JButton("Почистить");
		buttonDelete = new JButton("Удалить");
		buttonCancel = new JButton("Отмена");

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
		panelTitles.add(date_Order);
		panelTitles.add(payStatus);
		panelTitles.add(txtPayStatus);

		panelButtons = new JPanel();
		panelButtons.add(buttonUpdate);
		panelButtons.add(buttonClear);
		panelButtons.add(buttonCancel);

		getContentPane().add(panelTitles, BorderLayout.CENTER);
		getContentPane().add(panelButtons, BorderLayout.PAGE_END);
		pack();

		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtClientNo.getText() == null || txtClientNo.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите ID заказа", "Ошибка", JOptionPane.DEFAULT_OPTION);
					txtClientNo.requestFocus();
					return;
				}
				if (txtClientName.getText() == null || txtClientName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите имя клиента", "Ошибка", JOptionPane.DEFAULT_OPTION);
					txtClientName.requestFocus();
					return;
				}
				if (txtTelephone.getText() == null || txtTelephone.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите телефон клиента", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtTelephone.requestFocus();
					return;
				}
				if (txtAmount.getText() == null || txtAmount.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите конечную стоимость услуг", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtAmount.requestFocus();
					return;
				}

				try {
					Statement statement = odbcConnection.getDBConnection().createStatement();

					String temp = "UPDATE Clients SET ClientName = '" + txtClientName.getText() + "', Telephone ='"
							+ txtTelephone.getText() + "', Amount ='" + txtAmount.getText() + "', PayStatus ='"
							+ txtPayStatus.getText() + "'  WHERE ClientNo = '" + txtClientNo.getText() + "'";
					int result = statement.executeUpdate(temp);

					Reloading(tableClientsList);
					setVisible(false);
					dispose();

				} catch (SQLException sqlex) {
					JOptionPane.showMessageDialog(null, "Ошибка при обновлении базы данных", "Ошибка",
							JOptionPane.ERROR_MESSAGE);
					sqlex.printStackTrace();
				}
			}

		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		buttonClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				txtClientName.setText("");
				txtTelephone.setText("");
				txtAmount.setText("");
				txtPayStatus.setText("");
			}
		});
	}

	public void delete() {
		String buttons[] = { "Да", "Нет" };
		int result1 = JOptionPane.showOptionDialog(null, "Удалить?", "Удаление информации", JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE, null, buttons, buttons[1]);

		if (result1 != 0) {
			JOptionPane.showMessageDialog(null, "Ладно, не удаляем.", "Удаление", JOptionPane.DEFAULT_OPTION);
		}

		else {
			try {
				Statement statement = odbcConnection.getDBConnection().createStatement();
				if (!txtClientNo.equals("")) {

					String query = ("DELETE  FROM Clients where ClientNo='" + txtClientNo.getText() + "'");
					int result = statement.executeUpdate(query);
					if (result != 0) {
						JOptionPane.showMessageDialog(null, "Информация удалена", "Удаление",
								JOptionPane.DEFAULT_OPTION);

					} else {
						txtClientNo.setText("");
						txtClientName.setText("");
						txtTelephone.setText("");
						txtAmount.setText("");
						txtDateOfOrder.setText("");

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

			Statement stmt = odbcConnection.getDBConnection().createStatement();
			String sql = ("SELECT * FROM Clients ORDER BY ClientNo");
			int Numrow = 0;
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				tableClientsList.setValueAt(result.getString("ClientNo").trim(), Numrow, 0);
				tableClientsList.setValueAt(result.getString("ClientName").trim(), Numrow, 1);
				tableClientsList.setValueAt(result.getString("Telephone").trim(), Numrow, 2);
				tableClientsList.setValueAt(result.getString("Amount").trim(), Numrow, 3);
				tableClientsList.setValueAt(result.getDate("DateOfOrder"), Numrow, 4);
				tableClientsList.setValueAt(result.getString("PayStatus").trim(), Numrow, 5);
				sdf.format(5);
				Numrow++;
			}
		} catch (SQLException sqlex) {
			JOptionPane.showMessageDialog(null, "Ошибка считывания данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}
}

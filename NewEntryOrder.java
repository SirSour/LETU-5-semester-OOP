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

public class NewEntryOrder extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel orderNo, regNo, model, dateOfOrder, comment;
	private JTextField txtOrderNo, txtRegNo, txtModel, txtDateOfOrder, txtComment;
	private JButton buttonUpdate, buttonClear, buttonCancel;
	String strOrderNo, strRegNo, strModel, strDateOfOrder, strComment;
	private JPanel panelTitles;
	private JPanel panelButtons;
	private DateButton date_Order;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private static JTextArea txtInfo = new JTextArea(15, 40);

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public NewEntryOrder(JTable tableOrdersList, String strOrderNo, String strRegNo, String strModel,
			String strDateOfOrder, String strComment) {
		super("Обновление данных о заказе", false, true, false, true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setSize(400, 250);
		setLocation((screen.width - 1000) / 2, ((screen.height - 700) / 2));

		orderNo = new JLabel("ID заказа");
		regNo = new JLabel("Регистрационный номер");
		model = new JLabel("Модель");
		dateOfOrder = new JLabel("Дата заказа");
		comment = new JLabel("Комментарий");

		txtOrderNo = new JTextField(10);
		txtRegNo = new JTextField(10);
		txtModel = new JTextField(10);
		txtDateOfOrder = new JTextField(10);
		txtComment = new JTextField(50);

		date_Order = new DateButton();
		date_Order.setForeground(new Color(0, 0, 0));

		txtOrderNo.setText(strOrderNo);
		txtRegNo.setText(strRegNo);
		txtModel.setText(strModel);
		txtDateOfOrder.setText(strDateOfOrder);
		txtComment.setText(strComment);

		buttonUpdate = new JButton("Обновить");

		buttonClear = new JButton("Почистить");

		buttonCancel = new JButton("Отмена");

		panelTitles = new JPanel(new GridLayout(11, 2)); // прямоугольная компоновка
		panelTitles.setPreferredSize(new Dimension(350, 250));
		panelTitles.add(orderNo);
		panelTitles.add(txtOrderNo);
		panelTitles.add(regNo);
		panelTitles.add(txtRegNo);
		panelTitles.add(model);
		panelTitles.add(txtModel);
		panelTitles.add(dateOfOrder);
		panelTitles.add(date_Order);
		panelTitles.add(comment);
		panelTitles.add(txtComment);

		panelButtons = new JPanel();
		panelButtons.add(buttonUpdate);
		panelButtons.add(buttonClear);
		panelButtons.add(buttonCancel);

		getContentPane().add(panelTitles, BorderLayout.CENTER);
		getContentPane().add(panelButtons, BorderLayout.PAGE_END);
		pack();

		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtOrderNo.getText() == null || txtOrderNo.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите ID заказа", "Ошибка", JOptionPane.DEFAULT_OPTION);
					txtOrderNo.requestFocus();
					return;
				}
				if (txtRegNo.getText() == null || txtRegNo.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите регистрационный номер", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtRegNo.requestFocus();
					return;
				}
				if (txtModel.getText() == null || txtModel.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите модель автомобиля", "Ошибка",
							JOptionPane.DEFAULT_OPTION);
					txtModel.requestFocus();
					return;
				}
				if (txtComment.getText() == null || txtComment.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Опишите проблему", "Ошибка", JOptionPane.DEFAULT_OPTION);
					txtComment.requestFocus();
					return;
				}

				try {
					Statement statement = odbcConnection.getDBConnection().createStatement();

					String temp = "UPDATE Orders SET RegNo = '" + txtRegNo.getText() + "', Model ='"
							+ txtModel.getText() + "', Comment ='" + txtComment.getText() + "'  WHERE OrderNo = '"
							+ txtOrderNo.getText() + "'";
					int result = statement.executeUpdate(temp);

					Reloading(tableOrdersList);
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

				txtRegNo.setText("");
				txtModel.setText("");
				txtComment.setText("");
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
				if (!txtOrderNo.equals("")) {

					String query = ("DELETE  FROM Orders where OrderNo='" + txtOrderNo.getText() + "'");
					int result = statement.executeUpdate(query);
					if (result != 0) {
						JOptionPane.showMessageDialog(null, "Информация удалена", "Удаление",
								JOptionPane.DEFAULT_OPTION);

					} else {
						txtOrderNo.setText("");
						txtRegNo.setText("");
						txtModel.setText("");
						txtComment.setText("");
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

			Statement stmt = odbcConnection.getDBConnection().createStatement();
			String sql = ("SELECT * FROM Orders ORDER BY OrderNo");
			int Numrow = 0;
			ResultSet result = stmt.executeQuery(sql);
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
}

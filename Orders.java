import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class Orders extends JPanel {

	private static JTable tableOrdersList;
	private JScrollPane jsp;
	private JButton buttonAddNew, buttonRefresh, buttonClose, buttonDelete, buttonPrint;
	private JPanel tablePanel;
	private JPanel buttonPanel;
	private Statement stmt;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private static int selectedRow;
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public Orders() {

		setSize(1245, 710);
		setLayout(new BorderLayout());

		tableOrdersList = new JTable(new AbstractTable());

		jsp = new JScrollPane(tableOrdersList);
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(jsp, BorderLayout.CENTER);

		buttonAddNew = new JButton("Добавить");
		buttonDelete = new JButton("Удалить");
		buttonRefresh = new JButton("Обновить");
		buttonClose = new JButton("Закрыть");
		buttonPrint = new JButton("Печать");

		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(buttonAddNew);
		buttonPanel.add(buttonDelete);
		buttonPanel.add(buttonRefresh);
		buttonPanel.add(buttonPrint);
		buttonPanel.add(buttonClose);

		tablePanel.add(buttonPanel, BorderLayout.PAGE_END);
		tablePanel.setPreferredSize(new Dimension(1245, 640));
		tablePanel.setBackground(new Color(0, 0, 0));
		add(tablePanel, BorderLayout.NORTH);
		try {
			stmt = odbcConnection.getDBConnection().createStatement();
		} catch (Exception excp) {
			JOptionPane.showMessageDialog(null, "Ошибка подключения к базе", "Ошибка подключения",
					JOptionPane.ERROR_MESSAGE);
		}

		Reloading(tableOrdersList);

		tableOrdersList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					NewEntryOrder form = new NewEntryOrder(tableOrdersList,
							tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 0).toString(),
							tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 1).toString(),
							tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 2).toString(),
							tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 3).toString(),
							tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 4).toString());
					MainWindow.desktop.add(form);
					form.setVisible(true);
					try {
						form.setSelected(true);
					} catch (Exception ex) {
					}
				}
			}
		});

		buttonClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		buttonDelete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent r) {

				new NewEntryOrder(tableOrdersList,
						tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 0).toString(),
						tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 1).toString(),
						tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 2).toString(),
						tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 3).toString(),
						tableOrdersList.getValueAt(tableOrdersList.getSelectedRow(), 4).toString()).delete();

				Reloading(tableOrdersList);
			}
		});
		buttonPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				OrderReport form = new OrderReport();
				MainWindow.desktop.add(form);
				form.setVisible(true);
				try {
					form.setSelected(true);
				} catch (Exception ex) {
				}
			}
		});
		buttonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reloading(tableOrdersList);
			}
		});

		buttonAddNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AddNewOrder form = new AddNewOrder(tableOrdersList);
				MainWindow.desktop.add(form);
				form.setVisible(true);
				try {
					form.setSelected(true);
				} catch (Exception ex) {
				}
			}
		});

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

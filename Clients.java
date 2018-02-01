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

public class Clients extends JPanel {

	private static JTable tableClientsList;
	private JScrollPane jsp;
	private JButton buttonAddNew, buttonRefresh, buttonClose, buttonDelete, buttonPrint;
	private JPanel tablePanel;
	private JPanel buttonPanel;
	private Statement stmt;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public Clients() {

		setSize(1245, 710);
		setLayout(new BorderLayout());

		tableClientsList = new JTable(new AbstractTable());

		jsp = new JScrollPane(tableClientsList);
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

		Reloading(tableClientsList);

		tableClientsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					NewEntryClient form = new NewEntryClient(tableClientsList,
							tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 0).toString(),
							tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 1).toString(),
							tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 2).toString(),
							tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 3).toString(),
							tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 4).toString(),
							tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 5).toString());
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

				new NewEntryClient(tableClientsList,
						tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 0).toString(),
						tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 1).toString(),
						tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 2).toString(),
						tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 3).toString(),
						tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 4).toString(),
						tableClientsList.getValueAt(tableClientsList.getSelectedRow(), 5).toString()).delete();

				Reloading(tableClientsList);
			}
		});
		buttonPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ClientReport form = new ClientReport();
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
				Reloading(tableClientsList);
			}
		});

		buttonAddNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AddNewClient form = new AddNewClient(tableClientsList);
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
			AbstractTable model = new AbstractTable();
			tableClientsList.setModel(model);
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

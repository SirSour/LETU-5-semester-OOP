import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Employee extends JPanel {

	private JScrollPane jScrollP;
	static JTable tableEmployee;
	private JButton buttonAdd, buttonRefreshData, buttonDelete, buttonClose, buttonPrint;
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel tablePanel;
	private JPanel buttonPanel;

	private static JTextArea txtInfo = new JTextArea(15, 40);

	public Employee() {

		setSize(1245, 710);

		setLayout(new BorderLayout());
		tableEmployee = new JTable(new AbstractTable());

		jScrollP = new JScrollPane(tableEmployee);
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(jScrollP, BorderLayout.CENTER);

		buttonDelete = new JButton("Удалить");
		buttonAdd = new JButton("Добавление");
		buttonRefreshData = new JButton("Обновление");
		buttonPrint = new JButton("Печать");
		buttonClose = new JButton("Закрыть");

		buttonPanel = new JPanel(new FlowLayout()); // компоновка слева направо
		buttonPanel.add(buttonAdd);
		buttonPanel.add(buttonDelete);
		buttonPanel.add(buttonRefreshData);
		buttonPanel.add(buttonPrint);
		buttonPanel.add(buttonClose);

		try {
			Statement stmt = odbcConnection.getDBConnection().createStatement();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ошибка при подключении к базе", "Ошибка подключения",
					JOptionPane.ERROR_MESSAGE);
		}

		removeAll();
		LoadTableEmployee(tableEmployee);
		revalidate();
		repaint();

		tableEmployee.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					NewEntry form = new NewEntry(tableEmployee,
							tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 0).toString(),
							tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 1).toString(),
							tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 2).toString(),
							tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 3).toString(),
							tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 4).toString(),
							tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 5).toString());
					MainWindow.desktop.add(form);

					form.setVisible(true);
					try {
						form.setSelected(true);
					} catch (Exception ex) {
					}
				}
			}
		});

		buttonAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AddNewEmployee form = new AddNewEmployee(tableEmployee);
				MainWindow.desktop.add(form);
				form.setVisible(true);
				try {
					form.setSelected(true);
				} catch (Exception ex) {
				}

			}

		});

		buttonDelete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent r) {

				new NewEntry(tableEmployee, tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 0).toString(),
						tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 1).toString(),
						tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 2).toString(),
						tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 3).toString(),
						tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 4).toString(),
						tableEmployee.getValueAt(tableEmployee.getSelectedRow(), 5).toString()).delete();

				LoadTableEmployee(tableEmployee);
			}
		});
		buttonRefreshData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoadTableEmployee(tableEmployee);
			}
		});

		buttonPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeReport form = new EmployeeReport();
				MainWindow.desktop.add(form);
				form.setVisible(true);
				try {
					form.setSelected(true);
				} catch (Exception ex) {
				}
			}
		});
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		tablePanel.add(buttonPanel, BorderLayout.SOUTH);
		tablePanel.setPreferredSize(new Dimension(1245, 640));

		add(tablePanel, BorderLayout.NORTH);
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
}

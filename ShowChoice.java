
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.*;
import java.text.SimpleDateFormat;

public class ShowChoice extends JInternalFrame {

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	private static JTable tableChoice;
	private JScrollPane jScrollPane;
	private JPanel panel, panelButtons;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private JButton close;

	private static JTextArea txtInfo = new JTextArea(15, 40);
	private static String info;

	public ShowChoice() {
		super("Заказы по датам");
		tableChoice = new JTable(new AbstractTable());

		jScrollPane = new JScrollPane(tableChoice);

		panel = new JPanel(new BorderLayout());
		panel.add(jScrollPane, BorderLayout.CENTER);

		close = new JButton("Закрыть");

		panelButtons = new JPanel(new FlowLayout());

		panelButtons.add(close);

		setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
		try {

			Statement s = odbcConnection.getDBConnection().createStatement();
		} catch (Exception excp) {
			excp.printStackTrace();
			info = info + excp.toString();
		}

		try {
			Statement statement = odbcConnection.getDBConnection().createStatement();
			{
				String temp = ("SELECT * FROM Choice Order by DateComplete");
				int Numrow = 0;
				ResultSet result = statement.executeQuery(temp);
				while (result.next()) {
					tableChoice.setValueAt(result.getString("OrderNo"), Numrow, 0);
					tableChoice.setValueAt(result.getString("Sname"), Numrow, 1);
					tableChoice.setValueAt(result.getString("EmpName"), Numrow, 2);
					tableChoice.setValueAt(result.getDate("DateComplete"), Numrow, 3);
					sdf.format(4);
					Numrow++;

				}

			}

		} catch (SQLException sqlex) {
			txtInfo.append(sqlex.toString());
		}
		close.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(false);

			}
		});

		panel.add(panelButtons, BorderLayout.SOUTH);
		panel.setPreferredSize(new Dimension(750, 300));
		panel.setBackground(new Color(200, 200, 200));
		panel.setBounds(2, 200, 770, 2);
		setSize(750, 400);
		add(panel);
	}

	class AbstractTable extends javax.swing.table.AbstractTableModel {

		private String[] columnNames = { "OrderNo", "Sname", "EmpName", "DateComplete" };
		private Object[][] data = new Object[50][4];

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

}

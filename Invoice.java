import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

class Invoice extends JInternalFrame {

	public Container content;
	public JPanel reportingPanel;
	public JTabbedPane listsTabs;
	public JPanel chartPanel;
	public JButton hide;
	public JTextArea listPane;
	public JPanel reportPanel;
	public JButton drawGraphButton;
	public int ID;

	private static Connection connection = null;
	private JButton buttonPrint, buttonCancel;
	private JPanel panel;
	Statement stmt = null;
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public Invoice() {

		super("Квитанция", true, true, true, true);

		content = getContentPane();
		content.setBackground(new Color(85, 102, 68));

		buttonPrint = new JButton("Печать");
		buttonCancel = new JButton("Отмена");

		panel = new JPanel();
		panel.add(buttonPrint);
		panel.add(buttonCancel);

		reportingPanel = new JPanel();
		reportingPanel.setLayout(new BorderLayout());
		reportingPanel.setBorder(BorderFactory.createEtchedBorder());
		reportingPanel.add(new JLabel("Квитанция для оплаты"), BorderLayout.NORTH);
		reportingPanel.add(panel, BorderLayout.SOUTH);

		reportPanel = new JPanel();
		reportPanel.setLayout(new GridLayout(1, 1));
		reportPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(51, 85, 68)));
		reportPanel.setBackground(new Color(153, 170, 187));

		reportingPanel.add(new JScrollPane(reportPanel), BorderLayout.CENTER);

		Certificate();

		listPane.setEditable(false);
		listPane.setFont(new Font("Serif", Font.PLAIN, 12));
		listPane.setForeground(Color.black);

		listPane.setLineWrap(true);
		listPane.setWrapStyleWord(true);
		reportPanel.add(listPane);
		setLocation((screen.width - 1270) / 2, ((screen.height - 740) / 2));
		setResizable(false);

		try {
			Statement s = odbcConnection.getDBConnection().createStatement();
		} catch (Exception excp) {
			excp.printStackTrace();
		}

		JPanel dpanel = new JPanel();
		dpanel.setBorder(BorderFactory.createLoweredBevelBorder());
		dpanel.setLayout(new GridLayout(1, 1));
		DateFormat defaultDate = DateFormat.getDateInstance(DateFormat.FULL);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		content.add(reportingPanel, BorderLayout.CENTER);

		setLocation(5, 0);
		setSize(500, 400);

	}

	public void Certificate() {
		listPane = new JTextArea() {

			public void paint(Graphics g) {
				g.setColor(Color.black);
				g.drawString("Квитанция", 200, 50);
				g.drawString("ID      " + new Payment().txtPayNo.getText(), 80, 100);
				g.drawString("Клиент   " + new Payment().txtClientNo.getText(), 80, 130);
				g.drawString("Сумма   " + new Payment().txtAmount.getText(), 80, 160);
				// g.drawString("Оплачено " + new Payment().buttonDate.getText(), 80, 190);

				g.drawString("Принял         " + new Payment().comboReceived.getSelectedItem(), 80, 220);
				g.setColor(Color.red);
				g.drawString("СТО", 200, 260);
				super.paint(g);
			}
		};
	}
}

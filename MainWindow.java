import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MainWindow extends JFrame implements WindowListener {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private JMenu menuOperations, menuFiles, menuReports;
	private JMenuItem menuNewuser, menuExit;
	private JMenuItem menuOrders, menuPayment, menuEmployee, menuOrdersChoice, menuClients;
	private JMenuItem menuEmployeeReport, menuOrdersReport, menuClientsReport, menuChoiceReport;

	private JLabel welcome;
	private BufferedImage image;
	public static JDesktopPane desktop; // ���������� ��� ������� ����������� Jframe
	Dimension window = Toolkit.getDefaultToolkit().getScreenSize(); // ������

	public MainWindow() {
		super("��� \"������ � �������\"");
		this.setJMenuBar(CreateJMenuBar());
	
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit()
				.getImage("C:\\Program Files\\eclipse\\workspace\\�oursework\\images\\css3.png"));
		this.setLocation((window.width - 1250) / 2, ((window.height - 750) / 2));
		this.setSize(1245, 710);
		
		this.addWindowListener(this);
		this.setResizable(false);
	

		welcome = new JLabel("������! �� ��������� ����������: " + new Date() + " ", JLabel.CENTER);
		welcome.setFont(new Font("monospaced", Font.PLAIN, 12));
		welcome.setForeground(Color.black);

		try {
			image = ImageIO.read(new File("C:\\Program Files\\eclipse\\workspace\\�oursework\\images\\1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		desktop = new JDesktopPane() {
			@Override
			protected void paintComponent(Graphics grphcs) {
				super.paintComponent(grphcs);
				grphcs.drawImage(image, 0, 0, null);
			}
		};
		desktop.setBorder(BorderFactory.createEmptyBorder());
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	
		getContentPane().add(welcome, BorderLayout.PAGE_END, JLabel.CENTER);
		getContentPane().add(desktop, BorderLayout.CENTER);

		setVisible(true);
	}

	
	protected JMenuBar CreateJMenuBar() {

		JMenuBar menubar = new JMenuBar();

		/* �������� */

		menuOperations = new JMenu("��������");
		menuOperations.setForeground((Color.black));
		menuOperations.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuOperations.setMnemonic('�');
		menuOperations.setEnabled(false);

		menuNewuser = new JMenuItem("����� ����������");
		menuNewuser.setForeground(Color.black);
		menuNewuser.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuNewuser.setMnemonic('�');
		menuNewuser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuNewuser.setActionCommand("�������� ������ ����������� � �������");
		menuNewuser.addActionListener(menuListener);

		menuExit = new JMenuItem("�����");
		menuExit.setForeground(Color.black);
		menuExit.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuExit.setMnemonic('�');
		menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menuExit.setActionCommand("�����");
		menuExit.addActionListener(menuListener);

		menuOperations.add(menuNewuser);
		menuOperations.addSeparator();
		menuOperations.add(menuExit);

		menubar.add(menuOperations);

		/* ����� */

		menuFiles = new JMenu("�����");
		menuFiles.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuFiles.setForeground((Color.black));
		menuFiles.setMnemonic('�');
		menuFiles.setEnabled(false);

		menuEmployee = new JMenuItem("�����������");
		menuEmployee.setForeground(Color.black);
		menuEmployee.setEnabled(true);
		menuEmployee.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuEmployee.setMnemonic('�');
		menuEmployee.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		menuEmployee.setActionCommand("�����������");
		menuEmployee.addActionListener(menuListener);

		menuOrders = new JMenuItem("������");
		menuOrders.setForeground(Color.black);
		menuOrders.setEnabled(true);
		menuOrders.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuOrders.setMnemonic('�');
		menuOrders.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		menuOrders.setActionCommand("������");
		menuOrders.addActionListener(menuListener);

		menuOrdersChoice = new JMenuItem("������ �������");
		menuOrdersChoice.setForeground(Color.black);
		menuOrdersChoice.setEnabled(true);
		menuOrdersChoice.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuOrdersChoice.setMnemonic('�');
		menuOrdersChoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
		menuOrdersChoice.setActionCommand("������ �������");
		menuOrdersChoice.addActionListener(menuListener);

		menuClients = new JMenuItem("�������");
		menuClients.setForeground(Color.black);
		menuClients.setEnabled(true);
		menuClients.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuClients.setMnemonic('�');
		menuClients.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK));
		menuClients.setActionCommand("�������");
		menuClients.addActionListener(menuListener);

		menuPayment = new JMenuItem("������");
		menuPayment.setForeground(Color.black);
		menuPayment.setEnabled(false);
		menuPayment.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuPayment.setMnemonic('�');
		menuPayment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.CTRL_MASK));
		menuPayment.setActionCommand("������");
		menuPayment.addActionListener(menuListener);

		menuFiles.add(menuEmployee);
		menuFiles.addSeparator();
		menuFiles.add(menuOrders);
		menuFiles.addSeparator();
		menuFiles.add(menuOrdersChoice);
		menuFiles.addSeparator();
		menuFiles.add(menuClients);
		menuFiles.addSeparator();
		menuFiles.add(menuPayment);

		menubar.add(menuFiles);

		/* ���������� */

		menuReports = new JMenu("����������");
		menuReports.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuReports.setForeground(Color.black);
		menuReports.setMnemonic('�');

		menuEmployeeReport = new JMenuItem("�����������");
		menuEmployeeReport.setForeground(Color.black);
		menuEmployeeReport.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuEmployeeReport.setMnemonic('�');
		menuEmployeeReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuEmployeeReport.setActionCommand("���������� �� ����������");
		menuEmployeeReport.addActionListener(menuListener);

		menuOrdersReport = new JMenuItem("������");
		menuOrdersReport.setForeground(Color.black);
		menuOrdersReport.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuOrdersReport.setMnemonic('�');
		menuOrdersReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuOrdersReport.setActionCommand("���������� �� �������");
		menuOrdersReport.addActionListener(menuListener);

		menuChoiceReport = new JMenuItem("������ �������");
		menuChoiceReport.setForeground(Color.black);
		menuChoiceReport.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuChoiceReport.setMnemonic('�');
		menuChoiceReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		menuChoiceReport.setActionCommand("���������� �� �������");
		menuChoiceReport.addActionListener(menuListener);

		menuClientsReport = new JMenuItem("�������");
		menuClientsReport.setForeground(Color.black);
		menuClientsReport.setFont(new Font("monospaced", Font.PLAIN, 12));
		menuClientsReport.setMnemonic('�');
		menuClientsReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuClientsReport.setActionCommand("���������� �� ��������");
		menuClientsReport.addActionListener(menuListener);

		menuReports.add(menuEmployeeReport);
		menuReports.addSeparator();
		menuReports.add(menuOrdersReport);
		menuReports.addSeparator();
		menuReports.add(menuChoiceReport);
		menuReports.addSeparator();
		menuReports.add(menuClientsReport);

		menubar.add(menuReports);

		return menubar;
	}

	public void LoginAdmin() {
		menuOperations.setEnabled(true);
		menuFiles.setEnabled(true);
		menuPayment.setEnabled(true);
		menuReports.setEnabled(true);
	}

	public void LoginGuest() {
		menuOperations.setEnabled(false);
		menuFiles.setEnabled(false);
		menuReports.setEnabled(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		ClosingWindow();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	private void ClosingWindow() {

		String buttons[] = { "��", "���" };
		int result = JOptionPane.showOptionDialog(null, "������ �����?", "�����", JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE, null, buttons, buttons[1]);
		if (result == 0)
			System.exit(0);
	}

	ActionListener menuListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			String actionCom = e.getActionCommand();
			if (actionCom.equalsIgnoreCase("�������� ������ ����������� � �������")) {
				NewUser form = new NewUser();
				desktop.add(form);
				form.setVisible(true);

			} else if (actionCom.equalsIgnoreCase("�����")) {
				ClosingWindow();
			} else if (actionCom.equalsIgnoreCase("�����������")) {
				Employee form = new Employee();
				desktop.add(form);
				form.setVisible(true);

			} else if (actionCom.equalsIgnoreCase("������")) {
				Orders form = new Orders();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("������ �������")) {
				OrdersChoice form = new OrdersChoice();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("�������")) {
				Clients form = new Clients();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("������")) {
				Payment form = new Payment();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("���������� �� ����������")) {
				EmployeeReport form = new EmployeeReport();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("���������� �� �������")) {
				OrderReport form = new OrderReport();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("���������� �� ��������")) {
				ClientReport form = new ClientReport();
				desktop.add(form);
				form.setVisible(true);
			} else if (actionCom.equalsIgnoreCase("���������� �� �������")) {
				ChoiceReport form = new ChoiceReport();
				desktop.add(form);
				form.setVisible(true);
			}
		}
	};
}

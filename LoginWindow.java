import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginWindow extends JFrame {

	private JLabel lblUser, lblPassword, lblCategory;
	public JTextField txtUser;
	private JPasswordField txtPassword;
	private JComboBox comboCategory;
	private JButton buttonLogin;
	private Connection connection;

	Dimension window = Toolkit.getDefaultToolkit().getScreenSize(); // ������

	public LoginWindow() {

		super("���� � �������");
		// �������� ������ ��������� ����������
		this.setIconImage(Toolkit.getDefaultToolkit()
				.getImage("C:\\Program Files\\eclipse\\workspace\\�oursework\\images\\css3.png"));
		this.getContentPane().setLayout(null);
		this.setSize(370, 220);
		this.setResizable(false); // ������ ������ �������
		this.setLocation((window.width - 500) / 2, ((window.height - 350) / 2));
		this.setBackground(Color.LIGHT_GRAY);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		lblUser = new JLabel("�����");
		lblPassword = new JLabel("������");
		txtUser = new JTextField();
		txtPassword = new JPasswordField();
		lblCategory = new JLabel("������");
		comboCategory = new JComboBox();
		comboCategory.addItem("�������������");
		comboCategory.addItem("�����");

		buttonLogin = new JButton(
				new ImageIcon("C:\\Program Files\\eclipse\\workspace\\�oursework\\images\\login.png"));
		buttonLogin.setBorderPainted(false);
		buttonLogin.setFocusPainted(false);
		buttonLogin.setContentAreaFilled(false);

		lblCategory.setBounds(40, 100, 100, 25);
		lblUser.setBounds(40, 30, 100, 25);
		lblPassword.setBounds(40, 65, 100, 25);
		txtUser.setBounds(150, 30, 160, 25);
		txtPassword.setBounds(150, 65, 160, 25);
		comboCategory.setBounds(150, 100, 160, 25);
		buttonLogin.setBounds(133, 130, 100, 50);

		lblCategory.setFont(new Font("monospaced", Font.PLAIN, 16));
		lblCategory.setForeground(Color.BLACK);
		comboCategory.setFont(new Font("monospaced", Font.PLAIN, 16));
		lblUser.setFont(new Font("monospaced", Font.PLAIN, 16));
		lblUser.setForeground(Color.BLACK);
		lblPassword.setFont(new Font("monospaced", Font.PLAIN, 16));
		lblPassword.setForeground(Color.BLACK);
		txtUser.setFont(new Font("monospaced", Font.CENTER_BASELINE, 16));
		txtPassword.setFont(new Font("monospaced", Font.CENTER_BASELINE, 16));

		this.add(lblCategory);
		this.add(comboCategory);
		this.add(lblUser);
		this.add(txtUser);
		this.add(lblPassword);
		this.add(txtPassword);
		this.add(buttonLogin);
		getRootPane().setDefaultButton(buttonLogin);
		// pack();
		ButtonListener listener = new ButtonListener();
		buttonLogin.addActionListener(listener);

		connection = odbcConnection.getDBConnection();
		if (connection == null) {
			JOptionPane.showMessageDialog(null, "������ ��� ����������� ���� ������", "������",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}

	private class ButtonListener implements ActionListener {

		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == buttonLogin) {
				if (txtUser.getText() == null || txtUser.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "������� �����", "������ ����", JOptionPane.DEFAULT_OPTION);
					txtUser.requestFocus();
					return;
				}
				if (txtPassword.getText() == null || txtPassword.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "������� ������", "������ ����", JOptionPane.DEFAULT_OPTION);
					txtPassword.requestFocus();
					return;
				}
				Login();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (txtUser.getText() == null || txtUser.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "������� �����", "������ ����", JOptionPane.DEFAULT_OPTION);
					txtUser.requestFocus();
					return;
				}
				if (txtPassword.getText() == null || txtPassword.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "������� ������", "������ ����", JOptionPane.DEFAULT_OPTION);
					txtPassword.requestFocus();
					return;
				}
				Login();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void Login() {

		String SQL;
		String category = comboCategory.getSelectedItem().toString();
		String username = txtUser.getText();
		String password = txtPassword.getText();

		SQL = "SELECT * FROM Users WHERE Username='" + username + "'  AND Password='" + password + "'AND Category='"
				+ category + "'";
		try {
			Statement statement = connection.createStatement();
			statement.execute(SQL);
			ResultSet resultSet = statement.getResultSet();
			boolean recordFound = resultSet.next();
			if (recordFound) {
				LoadMainWindow();
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "������������ ����� ��� ������.", "������ �����",
						JOptionPane.INFORMATION_MESSAGE);
				txtUser.setText("");
				txtPassword.setText("");
				txtUser.requestFocus(); // ����� �� ���� � �������
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "���������� ����� � �������.\n" + "��������� ������.", "������ �����.",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void LoadMainWindow() {
		if (comboCategory.getSelectedItem().equals("�������������")) {
			new MainWindow().LoginAdmin();
		} else
			new MainWindow().LoginGuest();
	}
}

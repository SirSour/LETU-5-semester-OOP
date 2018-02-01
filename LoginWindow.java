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

	Dimension window = Toolkit.getDefaultToolkit().getScreenSize(); // размер

	public LoginWindow() {

		super("Вход в систему");
		// Поменяли иконку основного приложения
		this.setIconImage(Toolkit.getDefaultToolkit()
				.getImage("C:\\Program Files\\eclipse\\workspace\\Сoursework\\images\\css3.png"));
		this.getContentPane().setLayout(null);
		this.setSize(370, 220);
		this.setResizable(false); // нельзя менять размеры
		this.setLocation((window.width - 500) / 2, ((window.height - 350) / 2));
		this.setBackground(Color.LIGHT_GRAY);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		lblUser = new JLabel("Логин");
		lblPassword = new JLabel("Пароль");
		txtUser = new JTextField();
		txtPassword = new JPasswordField();
		lblCategory = new JLabel("Доступ");
		comboCategory = new JComboBox();
		comboCategory.addItem("Администратор");
		comboCategory.addItem("Гость");

		buttonLogin = new JButton(
				new ImageIcon("C:\\Program Files\\eclipse\\workspace\\Сoursework\\images\\login.png"));
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
			JOptionPane.showMessageDialog(null, "Ошибка при подключении базы данных", "Ошибка",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}

	private class ButtonListener implements ActionListener {

		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == buttonLogin) {
				if (txtUser.getText() == null || txtUser.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "Введите логин", "Пустое поле", JOptionPane.DEFAULT_OPTION);
					txtUser.requestFocus();
					return;
				}
				if (txtPassword.getText() == null || txtPassword.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "Введите пароль", "Пустое поле", JOptionPane.DEFAULT_OPTION);
					txtPassword.requestFocus();
					return;
				}
				Login();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (txtUser.getText() == null || txtUser.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "Введите логин", "Пустое поле", JOptionPane.DEFAULT_OPTION);
					txtUser.requestFocus();
					return;
				}
				if (txtPassword.getText() == null || txtPassword.getText().equals(" ")) {
					JOptionPane.showMessageDialog(null, "Введите пароль", "Пустое поле", JOptionPane.DEFAULT_OPTION);
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
				JOptionPane.showMessageDialog(null, "Неправильный логин или пароль.", "Ошибка входа",
						JOptionPane.INFORMATION_MESSAGE);
				txtUser.setText("");
				txtPassword.setText("");
				txtUser.requestFocus(); // фокус на поле с логином
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Невозможно войти в систему.\n" + "Проверьте данные.", "Ошибка входа.",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void LoadMainWindow() {
		if (comboCategory.getSelectedItem().equals("Администратор")) {
			new MainWindow().LoginAdmin();
		} else
			new MainWindow().LoginGuest();
	}
}

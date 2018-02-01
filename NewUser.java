import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class NewUser extends JInternalFrame {

	private JLabel lblUser, lblPassword, lblCategory, lblName;
	private JPasswordField txtPassword;
	public JTextField txtUser, txtName;
	private JComboBox comboCategory;
	private JButton buttonSave, buttonCancel;

	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	public NewUser() {
		super("Добавление нового пользователя", false, true, false, true);
		this.setSize(350, 270);
		// this. setLocation((screen.width - 1000) / 2, ((screen.height - 700) / 2));
		this.setLocation((screen.width - 500) / 2, ((screen.height - 350) / 2));
		this.setLayout(null);

		lblName = new JLabel("Имя");
		lblCategory = new JLabel("Категория");
		lblUser = new JLabel("Логин");
		lblPassword = new JLabel("Пароль");

		txtName = new JTextField();
		comboCategory = new JComboBox();
		comboCategory.addItem("Администратор");
		comboCategory.addItem("Гость");
		txtUser = new JTextField();
		txtPassword = new JPasswordField();

		buttonSave = new JButton("Сохранить");
		buttonCancel = new JButton("Отмена");

		lblName.setBounds(30, 20, 150, 25);
		this.add(lblName);
		txtName.setBounds(150, 20, 150, 25);
		this.add(txtName);
		lblCategory.setBounds(30, 50, 100, 25);
		this.add(lblCategory);
		comboCategory.setBounds(150, 50, 150, 25);
		this.add(comboCategory);
		lblUser.setBounds(30, 80, 100, 25);
		this.add(lblUser);
		txtUser.setBounds(150, 80, 150, 25);
		this.add(txtUser);
		lblPassword.setBounds(30, 110, 100, 25);
		this.add(lblPassword);
		txtPassword.setBounds(150, 110, 150, 25);
		this.add(txtPassword);
		buttonSave.setBounds(60, 180, 100, 25);
		this.add(buttonSave);
		buttonCancel.setBounds(180, 180, 100, 25);
		this.add(buttonCancel);

		txtName.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_SPACE)
						|| (c == KeyEvent.VK_DELETE))) {
					JOptionPane.showMessageDialog(null, "Недопустимый формат", "Ошибка", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});

		txtUser.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetter(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_SPACE)
						|| (c == KeyEvent.VK_DELETE))) {
					JOptionPane.showMessageDialog(null, "Недопустимый формат", "Ошибка", JOptionPane.ERROR_MESSAGE);
					e.consume(); // не отправлять в другие источники событий
				}
			}
		});

		buttonCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (txtName.getText() == null || txtName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите имя", "Пустое поле", JOptionPane.DEFAULT_OPTION);
					txtName.requestFocus();
					return;
				}
				if (txtUser.getText() == null || txtUser.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите логин", "Пустое поле", JOptionPane.DEFAULT_OPTION);
					txtUser.requestFocus();
					return;
				}
				if (txtPassword.getText() == null || txtPassword.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Введите пароль", "Пустое поле", JOptionPane.DEFAULT_OPTION);
					txtPassword.requestFocus();
					return;
				}
				try {
					Statement stmt = odbcConnection.getDBConnection().createStatement();
					String sql = "INSERT INTO Users (Name, Category, Username, Password) VALUES ('" + txtName.getText()
							+ "', '" + comboCategory.getSelectedItem() + "', '" + txtUser.getText() + "', '"
							+ txtPassword.getText() + "')";
					int result = stmt.executeUpdate(sql); // возвращает кол-во обновленных строк
					if (result > 0) {
						JOptionPane.showMessageDialog(null, "Новый пользователь добавлен", "Успех",
								JOptionPane.DEFAULT_OPTION);
						dispose();
					}
				} catch (Exception except) {
					JOptionPane.showMessageDialog(null, "Ошибка при обновлении базы", "Ошибка",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}

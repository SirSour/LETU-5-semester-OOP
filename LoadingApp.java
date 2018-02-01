import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class LoadingApp {

	private JLabel splashImage;
	private JLabel splashText;
	private JWindow window;
	private JPanel panel;

	public LoadingApp(int duration) {
		window = new JWindow();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		window.setBounds((screen.width - 490) / 2, (screen.height - 300) / 2, 400, 300);

		panel = (JPanel) window.getContentPane();
		splashImage = new JLabel(
				new ImageIcon("C:\\Program Files\\eclipse\\workspace\\Сoursework\\images\\garage.png"));
		splashText = new JLabel("СТО \"Винтик и Шпунтик\"", SwingConstants.CENTER);
		panel.add(splashImage, BorderLayout.CENTER);
		panel.add(splashText, BorderLayout.SOUTH);

		window.setVisible(true);
		try {
			Thread.sleep(duration);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		window.setVisible(false);
		window.dispose();
	}
}


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

public class Main implements Runnable {

	private final JFrame frame;

	public Main(JFrame form) {
		this.frame = form;
	}

	public void run() {
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public static void main(String args[]) {
		new LoadingApp(3000);
		EventQueue.invokeLater(new Main(new LoginWindow())); // ���������, ����� �� ���� ������� � ��������

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("OptionPane.yesButtonText", "��");
					UIManager.put("OptionPane.noButtonText", "���");
					UIManager.put("OptionPane.cancelButtonText", "O�������");
					break;
				}
			}
		} catch (Exception e) {

		}
	}
}

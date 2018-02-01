import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ClientReport extends JInternalFrame {

	public ClientReport() {
		super("Отчётность", true, true, true, true);

		printList();

	}

	private void printList() {
		try {
			Document docum = new Document(PageSize.A4, 50, 0, 10, 10);
			PdfPTable table = new PdfPTable(6);
			try {
				PdfWriter writer = PdfWriter.getInstance(docum, new FileOutputStream("ClientReport.pdf"));

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}

			try {
				docum.open();

				BaseFont bf = BaseFont.createFont("c:\\Windows\\Fonts\\tahoma.ttf", BaseFont.IDENTITY_H,
						BaseFont.EMBEDDED);
				com.itextpdf.text.Font _font = new com.itextpdf.text.Font(bf, com.itextpdf.text.Font.DEFAULTSIZE,
						com.itextpdf.text.Font.NORMAL);
				Paragraph title = new Paragraph("Отчётность по клиентам", _font);
				title.setSpacingAfter(50);
				title.setSpacingBefore(50);

				com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf);
				docum.add(title);

				table.addCell(new PdfPCell(new Phrase("ID клиента", font)));
				table.addCell(new PdfPCell(new Phrase("Клиент", font)));
				table.addCell(new PdfPCell(new Phrase("Телефон", font)));
				table.addCell(new PdfPCell(new Phrase("Сумма заказа", font)));
				table.addCell(new PdfPCell(new Phrase("Дата заказа", font)));
				table.addCell(new PdfPCell(new Phrase("Статус оплаты", font)));

				Statement statement = odbcConnection.getDBConnection().createStatement();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				String temp = ("select ClientNo, ClientName, Telephone, Amount, DateOfOrder, PayStatus from Clients");
				ResultSet rst = statement.executeQuery(temp);

				while (rst.next()) {
					table.addCell(new Phrase(rst.getString("ClientNo").trim()));
					table.addCell(new Phrase(rst.getString("ClientName").trim(), font));
					table.addCell(new Phrase(rst.getString("Telephone").trim()));
					table.addCell(new Phrase(rst.getString("Amount").trim()));
					table.addCell(new Phrase(dateFormat.format(rst.getDate("DateOfOrder"))));
					table.addCell(new Phrase(rst.getString("PayStatus").trim(), font));
				}
				if (rst != null) {
					rst.close();
				}
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				docum.add(table);
			} catch (DocumentException ex) {
				ex.printStackTrace();
			}

			docum.close();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "Записи не найдены" + sqle.getMessage());
			return;
		}
		if (Desktop.isDesktopSupported()) {
			try {
				File myFile = new File("C:\\Program Files\\eclipse\\workspace\\Сoursework\\ClientReport.pdf");
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
			}
		}
	}
}

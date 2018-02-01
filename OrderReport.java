import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class OrderReport extends JInternalFrame {

	public OrderReport() {
		super("Отчётность", true, true, true, true);

		printList();

	}

	private void printList() {
		try {
			Document docum = new Document(PageSize.A4, 50, 0, 10, 10);
			PdfPTable table = new PdfPTable(5);
			try {
				PdfWriter writer = PdfWriter.getInstance(docum, new FileOutputStream("OrderReport.pdf"));

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
				Paragraph title = new Paragraph("Отчётность по заказам", _font);
				title.setSpacingAfter(50);
				title.setSpacingBefore(50);

				com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf);
				docum.add(title);

				table.addCell(new PdfPCell(new Phrase("ID заказа", font)));
				table.addCell(new PdfPCell(new Phrase("Регистрационный номер", font)));
				table.addCell(new PdfPCell(new Phrase("Модель", font)));
				table.addCell(new PdfPCell(new Phrase("Дата заказа", font)));
				table.addCell(new PdfPCell(new Phrase("Описание проблемы", font)));

				Statement statement = odbcConnection.getDBConnection().createStatement();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				String temp = ("select orderNo, RegNo, Model, DateOfOrder, Comment from Orders");
				ResultSet rst = statement.executeQuery(temp);

				while (rst.next()) {
					table.addCell(new Phrase(rst.getString("orderNo").trim()));
					table.addCell(new Phrase(rst.getString("RegNo").trim(), font));
					table.addCell(new Phrase(rst.getString("Model").trim(), font));
					table.addCell(new Phrase(dateFormat.format(rst.getDate("DateOfOrder"))));
					table.addCell(new Phrase(rst.getString("Comment").trim(), font));
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
				File myFile = new File("C:\\Program Files\\eclipse\\workspace\\Сoursework\\OrderReport.pdf");
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
			}
		}
	}
}

package fr.pantheonsorbonne.miage.diploma;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class DateSnippet implements DiplomaSnippet {

	private Date date;

	public DateSnippet() {
		this(new Date());
	}

	public DateSnippet(Date date) {
		this.date = date;
	}

	@Override
	public void write(PdfWriter writer) throws DocumentException {
		ColumnText ct = new ColumnText(writer.getDirectContent());
		ct.setSimpleColumn(200, 140, 350, 0);
		Font font = FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
		String timeStamp = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE).format(this.date);
		Chunk chunk = new Chunk(timeStamp, font);
		ct.addElement(chunk);
		ct.go();
	}

}

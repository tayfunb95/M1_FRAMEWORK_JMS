package fr.pantheonsorbonne.miage.diploma;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class NameSnippet implements DiplomaSnippet {

	private String name;

	public NameSnippet(String name) {
		this.name = name;
	}

	@Override
	public void write(PdfWriter writer) throws DocumentException {
		ColumnText ct = new ColumnText(writer.getDirectContent());
		ct.setSimpleColumn(200, 50, 550, 350);
		Font font = FontFactory.getFont(FontFactory.COURIER, 30, BaseColor.BLACK);
		Chunk chunk = new Chunk(name, font);
		ct.addElement(chunk);
		ct.go();
	}

}

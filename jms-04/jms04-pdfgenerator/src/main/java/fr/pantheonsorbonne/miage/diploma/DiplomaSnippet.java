package fr.pantheonsorbonne.miage.diploma;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

public interface DiplomaSnippet {

	void write(PdfWriter writer) throws DocumentException;
}

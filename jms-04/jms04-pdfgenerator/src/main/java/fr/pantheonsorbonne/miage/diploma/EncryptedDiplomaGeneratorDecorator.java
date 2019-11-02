package fr.pantheonsorbonne.miage.diploma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class EncryptedDiplomaGeneratorDecorator extends DiplomaGeneratorDecorator {

	private String password;

	public EncryptedDiplomaGeneratorDecorator(DiplomaGenerator other, String password) {
		super(other);
		this.password = password;
	}

	@Override
	public InputStream getContent() {

		try (InputStream is = other.getContent()) {
			try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				PdfReader pdfReader = new PdfReader(is);
				PdfStamper pdfStamper = new PdfStamper(pdfReader, os);

				pdfStamper.setEncryption(null, this.password.getBytes(), 0,
						PdfWriter.ENCRYPTION_AES_256 );
				pdfStamper.close();
				return new ByteArrayInputStream(os.toByteArray());
			}

		} catch (IOException | DocumentException e) {

			e.printStackTrace();
			throw new RuntimeException("failed to generate Encrypted File");
		}

	}

	@Override
	protected Collection<DiplomaSnippet> getDiplomaSnippets() {
		return Collections.emptyList();
	}

}

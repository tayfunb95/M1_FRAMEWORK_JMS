package fr.pantheonsorbonne.miage.diploma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;

import com.google.common.io.ByteStreams;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class AbstractDiplomaGenerator implements DiplomaGenerator {

	private Collection<DiplomaSnippet> snippets = new HashSet<>();

	public AbstractDiplomaGenerator() {
		super();
		

	}

	/**
	 * provides all the snippets used for the concrete diploma implementation
	 * 
	 * @return
	 */
	abstract protected Collection<DiplomaSnippet> getDiplomaSnippets();

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.pantheonsorbonne.miage.DiplomaGenerator#getContent()
	 */
	@Override
	public InputStream getContent() {

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {

			this.writeToStream(bos);
			Thread.sleep(10000);

			return new ByteArrayInputStream(bos.toByteArray());

		} catch (IOException | InterruptedException e) {

			throw new RuntimeException("failed to generate the file to stream to", e);
		}

	}

	protected void writeToStream(OutputStream os) {
		Document document = new Document();
	
		try {

			Path image = new File("src/main/resources/diploma.png").toPath();
			Rectangle rect = new Rectangle(800f, 600f);
			document.setPageSize(rect);

			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();

			for (DiplomaSnippet snippet : this.getDiplomaSnippets()) {
				snippet.write(writer);
			}

			document.add(Image.getInstance(image.toAbsolutePath().toString()));

		} catch (DocumentException | IOException e) {
			throw new RuntimeException("failed to generate Document", e);
		} finally {
			document.close();
		}
	}

}
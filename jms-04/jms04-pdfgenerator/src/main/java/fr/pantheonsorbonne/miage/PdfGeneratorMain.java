package fr.pantheonsorbonne.miage;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

import fr.pantheonsorbonne.miage.jms.PdfGeneratorMessageHandler;
import fr.pantheonsorbonne.ufr27.miage.DiplomaInfo;

public class PdfGeneratorMain {
	public static void main(String[] args) throws JMSException, NamingException, InterruptedException, IOException {

		// initialize CDI 2.0 SE container
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.disableDiscovery().addPackages(true, PdfGeneratorMain.class)
				.initialize()) {

			// create a message produce and consumer
			final PdfGeneratorMessageHandler handler = container.select(PdfGeneratorMessageHandler.class).get();

			try {
				while (true) {
					handler.consume();
				}

			}

			finally {
				handler.close();
			}

		}

	}

}

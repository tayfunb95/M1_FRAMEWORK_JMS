package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

public class AppPublisher {
	public static void main(String[] args) throws JMSException, NamingException, InterruptedException, IOException {

		// initialize CDI 2.0 SE container
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.disableDiscovery().addPackages(AppPublisher.class).initialize()) {

			// create a message produce and consumer
			final OrderPublisher orderPublisher = container.select(OrderPublisher.class).get();

			Scanner scan = new Scanner(System.in);
			String line = "";
			while (!(line = scan.nextLine()).equals("EXIT"))
				orderPublisher.publish(line);

			orderPublisher.close();

		}

	}

}

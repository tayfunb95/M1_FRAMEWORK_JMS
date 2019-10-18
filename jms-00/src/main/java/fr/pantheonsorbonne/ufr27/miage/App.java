package fr.pantheonsorbonne.ufr27.miage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

public class App {
	public static void main(String[] args) throws JMSException, NamingException, InterruptedException, IOException {

		// launch ActiveMQ artemis
		startBroker();

		// initialize CDI 2.0 SE container
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		try (SeContainer container = initializer.disableDiscovery().addPackages(App.class).initialize()) {

			// create a message produce and consumer
			final Producer producer = container.select(Producer.class).get();
			final Consumer consumer = container.select(Consumer.class).get();
			final AtomicInteger counter = new AtomicInteger(0);
			// the consumer reads the message in its own thread
			Thread consumerThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (!Thread.currentThread().isInterrupted()) {
						counter.addAndGet(1);
						System.out.println("received: " + consumer.consume());
					}

				}
			});
			consumerThread.start();

			// the producer produces
			for (int i = 0; i < 1000; i++)
				System.out.println("send: " + producer.produce());

			while (counter.get() < 1000) {
				Thread.sleep(100);
			}
			consumerThread.interrupt();
			Thread.sleep(1000);			

			producer.close();
			consumer.close();

		}

		stopBroker();
		System.exit(0);

	}

	private static EmbeddedActiveMQ embedded;
	private static void startBroker() {
		try {
			// creates the broker
			embedded = new EmbeddedActiveMQ();

			// make sure every user can connect
			embedded.setSecurityManager(new DummyActiveMQSecurityManager());
			embedded = embedded.start();
		} catch (Exception e) {
			System.out.println("failed to start embedded ActiveMQ Broker");
		}
	}
	private static void stopBroker() {
		try {
			embedded.stop();
		} catch (Exception e) {
			System.out.println("failed to stop embedded ActiveMQ Broker");
		}
	}
}

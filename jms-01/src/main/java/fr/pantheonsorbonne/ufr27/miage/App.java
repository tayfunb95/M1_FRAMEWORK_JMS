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
			final OrderProducer orderProducer = container.select(OrderProducer.class).get();
			final OrderConsumer orderConsumer = container.select(OrderConsumer.class).get();

			final DeliveryProducer deliveryProducer = container.select(DeliveryProducer.class).get();
			final DeliveryConsumer deliveryConsumer = container.select(DeliveryConsumer.class).get();

			deliveryProducer.produce();
			orderProducer.produce();

			System.out.println("Message read from orderConsumer: " + orderConsumer.consume());
			System.out.println("Message read from deliveryConsumer: " + deliveryConsumer.consume());

			orderProducer.close();
			orderConsumer.close();
			deliveryConsumer.close();
			deliveryProducer.close();

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

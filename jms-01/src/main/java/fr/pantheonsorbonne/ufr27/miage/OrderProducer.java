package fr.pantheonsorbonne.ufr27.miage;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

public class OrderProducer implements Closeable {

	@Inject
	@Named("order")
	Queue queue;

	@Inject
	ConnectionFactory connectionFactory;

	Connection connection;
	Session session;
	MessageProducer messageProducer;

	int index = 0;

	@PostConstruct
	private void init() {
		try {
			this.connection = connectionFactory.createConnection();
			connection.start();
			this.session = connection.createSession();
			this.messageProducer = session.createProducer(queue);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

	public String produce() {
		try {
			String sentText = "Order number: " + (index++);
			this.messageProducer.send(this.session.createTextMessage(sentText));
			return sentText;
		} catch (JMSException e) {
			System.out.println("Failed to send message to queue");
			return "Nothing sent";
		}
	}

	@Override
	public void close() throws IOException {
		try {
			messageProducer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			System.out.println("failed to close JMS resources");
		}

	}

}

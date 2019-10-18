package fr.pantheonsorbonne.ufr27.miage;

import java.util.Hashtable;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSProducer {

	private static final Context JNDI_CONTEXT;

	static {
		Hashtable<String, String> jndiBindings = new Hashtable<>();
		jndiBindings.put("java.naming.factory.initial",
				"org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory");
		jndiBindings.put("connectionFactory.ConnectionFactory", "tcp://localhost:61616");
		jndiBindings.put("queue.queues/OrderQueue", "OrderQueue");
		Context c = null;
		try {
			c = new InitialContext(jndiBindings);
		} catch (NamingException e) {
			e.printStackTrace();
			c = null;
			System.exit(-1);

		} finally {
			JNDI_CONTEXT = c;
		}
	}

	@Produces
	public Queue getJMSQueue() throws NamingException {
		return (Queue) JNDI_CONTEXT.lookup("queues/OrderQueue");
	}

	@Produces
	public ConnectionFactory getJMSConnectionFactory() throws NamingException {
		return (ConnectionFactory) JNDI_CONTEXT.lookup("ConnectionFactory");
	}

	
}

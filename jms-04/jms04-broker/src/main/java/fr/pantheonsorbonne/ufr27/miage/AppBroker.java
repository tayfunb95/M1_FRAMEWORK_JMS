package fr.pantheonsorbonne.ufr27.miage;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;



public class AppBroker {
	public static void main(String[] args) throws Exception {

		// launch ActiveMQ artemis
		startBroker();
		
		
		
		Thread.sleep(10000000);

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

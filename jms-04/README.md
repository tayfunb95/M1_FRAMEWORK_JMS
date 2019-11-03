# Scope #

Demonstrate a complex scenario where a webapp calls a remote service through JMS to generate binary files

# projects #

* jms04-broker: runs the JMS broker (Apache ActiveMQ Artemis)
* jms04-models: provides the Java Objects Models for other projets
* jms04-pdfgenerator: consume JMS message as JAXBElement and produce binary diploma files 
* jms04-webapp: provide a REST api to create/download diplomas, stores binary diploma files in memory and produces Diploma Requests as JMS Messages and Consumes binary diploma files

# step-by-step #

```bash
mvn clean package install
```
## start the broker ##
```bash
cd jms04-broker
mvn exec:java
```
## start REST Api ##
```bash
cd jms04-webapp
mvn exec:java
```
## start PDF Generator Service ##
```bash
cd jms04-pdfgenerator
mvn exec:java
```
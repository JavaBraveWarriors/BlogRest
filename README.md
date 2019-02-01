# BlogRest
This application is used to create and manage a blog.
The technical task is described in [Technical Task][1]. You can see the specification [here][8]

## Necessary tools
* [Maven 3][2]
* [Java 8 or latest version][4]

## Getting started
Clone this repo to your local machine using:
```
git clone https://github.com/JavaBraveWarriors/BlogRest.git
```
>This project uses two profiles. Profiles determine what the connection to the database will be.
If you don't specify a profile, the dev profile will be activated by default.
 * dev is used for development
 * prod is used for production server
 Example:
 ```
 mvn -Pdev <comands>
 mvn -Pprod <comands>
 ```
 
To run the application on the embedded Jetty server, go to the project root and execute the following commands:
```
mvn -Pdev clean install 
cd rest/
mvn -Pdev jetty:tun
```
## Production deployment
* In the *settings.xml* file ($M2_HOME/conf/settings.xml) write the settings for access to the server. Example:
```
 <server>
    <id>apache-tomcat</id>
    <username>tomcat-manager</username>
    <password>password</password>
  </server>
```
> *Node*: server identifier in the settings.xml file must match the identifier in pom.xml in the root of the plugin project "tomcat7-maven-plugin".
* When the settings are completed, run the following commands:
```
mvn -Pprod clean install
mvn -Pprod tomcat7:deploy
```
* If you want to redeploy the project:
```
mvn -Pprod clean install
mvn -Pprod tomcat7:redeploy
```

## Technology stack
* [Spring Framework 5.1.3.RELEASE][5]
* [Maven 3][6]
* [Docker 17.05.0-ce][7]
* [Java 8][4]

### Links
* How setup deploy a real environment [Docker Containers Setup][3]

[1]: docs/TechnicalTask.md
[2]: https://maven.apache.org/install.html
[3]: docs/DockerContainersSetup.md
[4]: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[5]: https://docs.spring.io/spring-framework/docs/5.1.3.RELEASE/spring-framework-reference/
[6]: https://maven.apache.org/guides/
[7]: https://docs.docker.com/
[8]: docs/Specification.md
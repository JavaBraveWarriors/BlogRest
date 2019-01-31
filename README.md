# BlogRest
This application is used to create and manage a blog.
The technical task is described in [Technical Task][1]

##Necessary tools
* [Maven][2]

##Getting started
Clone this repo to your local machine using:
```
git clone https://github.com/JavaBraveWarriors/BlogRest.git
```
To run the application on the embedded Jetty server, go to the project root and execute the following commands:
```
mvn -Pdev clean install 
cd rest/
mvn -Pdev jetty:tun
```
##Deploy to server
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

##Technology stack
* Spring Framework
* Maven
* Docker

###Links
* How setup deploy a real environment [Docker Containers Setup][3]

[1]: docs/TechnicalTask.md
[2]: https://maven.apache.org/install.html
[3]: docs/DockerContainersSetup.md
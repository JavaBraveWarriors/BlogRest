# How setup docker-compose: Tomcat + Mysql

If you want to quickly deploy a real environment, you can use the files in the [dockerFiles](../dockerFiles) .
## Necessary tools:
* [Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/). 
* [Docker-compose](https://docs.docker.com/compose/install/).
## Custom settings: 
All configuration files are located in the corresponding [DataBaseSetup](../dockerFiles/dbSetup), [ActiveMQSetup](../dockerFiles/activeMQSetup) and [tomcatSetup](../dockerFiles/tomcatSetup).

You can configure tomcat-users. To do this, open the [tomcat-users.xml](../dockerFiles/tomcatSetup/tomcat-users.xml) file and create a user for yourself.

In the [context.xml](../dockerFiles/tomcatSetup/context.xml) file, you can configure access to tomcat for different ip addresses.

The script for initializing the database is located in the [DataBaseSetup](../dockerFiles/dbSetup).
## Install:
When everything is ready, go to the [dockerFiles](../dockerFiles), open the terminal and execute the following command:
>**Note**: Make sure that you have free ports: 
* 8888 - you will have access to tomcat through it.
* 61613 - you will have access to activeMq via this port.
* 8161 - you will have access to web-interface activeMQ to manage queues, consumers and producers. 
```
docker-compose build
docker-compose run -p 8888:8080 -p 61613:61613 app
```
## Break containers:
If you want to break the environment, run the following commands:
>**Note**: when you close the environment, all data will be deleted!
```
 docker-compose rm -vf
```
Using the command you can kill the containers:
```
docker kill yam_tomcat
docker kill yam_activeMQ
docker kill yam_mysql
```
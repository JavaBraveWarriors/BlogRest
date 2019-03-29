# How setup docker-compose:

If you want to quickly deploy a real environment, you can use the files in the [dockerFiles](../dockerFiles) .
### Necessary tools:
* [Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/). 
* [Docker-compose](https://docs.docker.com/compose/install/).

## Version 1 (Tomcat + MySQL + ActiveMQ):
### Custom settings: 
All configuration files are located in the corresponding [DataBaseSetup](../dockerFiles/version1/dbSetup), [ActiveMQSetup](../dockerFiles/version1/activeMQSetup) and [tomcatSetup](../dockerFiles/version1/tomcatSetup).

You can configure tomcat-users. To do this, open the [tomcat-users.xml](../dockerFiles/version1/tomcatSetup/tomcat-users.xml) file and create a user for yourself.

In the [context.xml](../dockerFiles/version1/tomcatSetup/context.xml) file, you can configure access to tomcat for different ip addresses.

The script for initializing the database is located in the [DataBaseSetup](../dockerFiles/version1/dbSetup).

### Install:
When everything is ready, go to the [dockerFiles](../dockerFiles/version1), open the terminal and execute the following command:
>**Note**: Make sure that you have free ports: 
* 8888 - you will have access to tomcat through it.
* 61613 - you will have access to activeMq via this port.
* 8161 - you will have access to web-interface activeMQ to manage queues, consumers and producers. 
```
docker-compose build
docker-compose run -p 8888:8080 -p 61613:61613 app
```
### Break containers:
If you want to break the environment, run the following commands:
>**Note**: when you close the environment, all data will be deleted!
```
 docker-compose rm -vf
```
Using the command you can kill the containers:
```
docker rm -f yam_tomcat
docker rm -f yam_activeMQ
docker rm -f yam_mysql
```

## Version 2 (Apache load balancer + MySQL + ActiveMQ + 2 instances of Tomcat):
### Custom settings: 
All configuration files are located in the corresponding 
[DataBaseSetup](../dockerFiles/version2/dbSetup), 
[ActiveMQSetup](../dockerFiles/version2/activeMQSetup), 
[tomcatSetup1](../dockerFiles/version2/tomcatSetupInstance1) 
and [tomcatSetup2](../dockerFiles/version2/tomcatSetupInstance2).

You can configure tomcat-users.
To do this, open the [tomcat-users.xml](../dockerFiles/version2/tomcatData/tomcat-users.xml)
file and create a user for yourself.

In the [context1-th.xml](../dockerFiles/version2/tomcatData/context.xml) file, you can configure access to tomcat for different ip addresses.

The script for initializing the database is located in the [DataBaseSetup](../dockerFiles/version2/dbSetup).

### Install:
When everything is ready, go to the [dockerFiles](../dockerFiles/version2), open the terminal and execute the following command:
>**Note**: Make sure that you have free ports: 
* 8888 - you will have access to first tomcat instance through it.
* 8881 - yot will have access to second tomcat instance through it.
* 61613 - you will have access to activeMq via this port.
* 8161 - you will have access to web-interface activeMQ to manage queues, consumers and producers. 
* 5555 - apache load balancer.
```
docker-compose build
docker-compose run -dT --name apache --service-ports apache
```
### Break containers:
If you want to break the environment, run the following commands:
>**Note**: when you close the environment, all data will be deleted!
```
 docker-compose rm -vf
```
Using the commands you can kill the containers:
```
docker rm -f yam_tomcat_1-th_instance
docker rm -f yam_tomcat_2-th_instance
docker rm -f yam_mysql
docker rm -f yam_activeMQ
docker rm -f apache
```
Using the commands you cat restart environment:
```
docker-compose rm -vf
docker rm -f yam_tomcat_1-th_instance
docker rm -f yam_tomcat_2-th_instance
docker rm -f yam_mysql
docker rm -f yam_activeMQ
docker rm -f apache
docker-compose build
docker-compose run -dT --name apache --service-ports apache
docker logs apache
```
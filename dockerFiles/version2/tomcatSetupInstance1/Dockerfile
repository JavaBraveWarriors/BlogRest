FROM tomcat:latest

# Update Apt and then install Nano editor (RUN can be removed)
RUN apt-get update && apt-get install -y \
    nano \
&& mkdir -p /usr/local/tomcat/conf 


# Copy configurations (Tomcat users, Manager app)
COPY ../tomcatData/tomcat-users.xml /usr/local/tomcat/conf/
COPY ../tomcatData/context.xml /usr/local/tomcat/webapps/manager/META-INF/
COPY ../tomcatData/context.xml /usr/local/tomcat/webapps/host-manager/META-INF/
COPY ../tomcatData/context.xml /usr/local/tomcat/conf/
COPY server.xml /usr/local/comcat/conf/

COPY ../tomcatData/start-app.sh /usr/start-app.sh
USER root
RUN ["chmod", "+x", "/usr/start-app.sh"]
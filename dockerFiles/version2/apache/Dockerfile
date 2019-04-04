FROM httpd:2.4.27

COPY httpd.conf /usr/local/apache2/conf/httpd.conf
COPY httpd-vhosts.conf /usr/local/apache2/conf/extra/
COPY proxy-html.conf /usr/local/apache2/conf/extra/
COPY rest-api.conf /usr/local/apache2/conf/extra/
COPY front-end.conf /usr/local/apache2/conf/extra/
COPY static /usr/local/apache2/htdocs/static
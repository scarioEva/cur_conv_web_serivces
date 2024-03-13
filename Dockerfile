FROM tomcat:8.0-alpine

LABEL maintainer="taha-22"

ARG JMETER_VERSION="5.1.1"
ENV JMETER_HOME /opt/apache-jmeter-${JMETER_VERSION}
ENV JMETER_BIN	${JMETER_HOME}/bin
ENV JMETER_DOWNLOAD_URL  https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JMETER_VERSION}.tgz
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk/jre


# Install JDK8 &JMETER
RUN apk update \
	&& apk upgrade \
	&& apk add ca-certificates \
	&& update-ca-certificates \
	&& apk add --update openjdk8-jre tzdata curl unzip bash \
	&& apk add --no-cache nss \
	&& rm -rf /var/cache/apk/* \

ENV PATH $PATH:$JMETER_BIN

# Configure tomcat and REST web service
ADD ./tomcat-users.xml /usr/local/tomcat/conf/
ADD ./CurConvRS/dist/CurConvRS.war /usr/local/tomcat/webapps/
ADD ./rates.json /usr/local

# Configure jmeter testing
RUN mkdir -p /usr/local/jmetertests
ADD ./jmeterCurConvRS.jmx /usr/local/jmetertests/jmeterCurConvRS.jmx



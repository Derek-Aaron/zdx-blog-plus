FROM  openjdk:17
COPY *.jar /zdx.jar
ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8888

ENTRYPOINT ["java","-Xmx512m","-Xms512m","-jar", "/zdx.jar", "--spring.profiles.active=pro","--server.port=8888", "--jasypt.encryptor.password=xxxx","--logging.file.name=/home/zdx/logs/zdx.log","--logging.level.com.zdx=info"]
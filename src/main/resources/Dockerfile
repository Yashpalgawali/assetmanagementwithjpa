FROM openjdk:8
ARG JAR_FILE=target/*.war
COPY ./target/newassetmanagement.war assetmanage.war
ENTRYPOINT [ "java" , "-war" , "*/assetmanage.war" ]



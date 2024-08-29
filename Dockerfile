FROM openjdk:17               
ADD doctor-service-0.0.1-SNAPSHOT.jar doctor-service-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","doctor-service-0.0.1-SNAPSHOT.jar"]   
EXPOSE 9090

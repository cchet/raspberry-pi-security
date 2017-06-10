SET AUTH_SERVER_LOCATION="http://192.168.1.103:8081/rpisec/v2/api-docs?group="
SET APP_SERVER_LOCATION="http://192.168.1.103:8080/rpisec/v2/api-docs?group="
REM This files generates the swagger clients
REM Ensure that the apps are running under the defined location before invoking this script
REM Auth server client api generation
java -jar swagger-codegen-cli-2.2.2.jar generate -i %AUTH_SERVER_LOCATION%AuthOAuth2Authentication -l java -o client/auth -c auth-config.json
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %AUTH_SERVER_LOCATION%AuthClientRestController -l java -o client/auth -c auth-config.json
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %AUTH_SERVER_LOCATION%AuthUserRestController -l java -o client/auth -c auth-config.json
REM Comment App server client api generation
java -jar swagger-codegen-cli-2.2.2.jar generate -i %APP_SERVER_LOCATION%AppClientRestController -l java -o client/app -c app-config.json
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %APP_SERVER_LOCATION%AppInternalRestController -l java -o client/app -c app-config.json
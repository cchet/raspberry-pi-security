SET AUTH_SERVER_LOCATION="http://localhost:8081/rpisec/v2/api-docs?group="
SET APP_SERVER_LOCATION="http://localhost:8081/rpisec/v2/api-docs?group="
REM This files generates the swagger clients
REM Ensure that the apps are running under the defined location before invoking this script
REM Auth server client api generation
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %AUTH_SERVER_LOCATION%OAuth2Authentication -l java -o client/auth -c auth-config.json
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %AUTH_SERVER_LOCATION%ClientRestController -l java -o client/auth -c auth-config.json
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %AUTH_SERVER_LOCATION%UserRestController -l java -o client/auth -c auth-config.json
REM Comment App server client api generation
java "-Dmodels" "-Dapis" "-DmodelTests=false" "-DapiTests=false" "-DmodelDocs=false" "-DapiDocs=false" -jar swagger-codegen-cli-2.2.2.jar generate -i %APP_SERVER_LOCATION%ClientRestController -l java -o client/app -c app-config.json
@echo off
echo begin...
set BACKUP_PATH=.\backup
set DEPLOY_PATH=.\deploy
set I2F_VERSION=1.0

set BACKUP_PATH=%BACKUP_PATH%-%I2F_VERSION%
set DEPLOY_PATH=%DEPLOY_PATH%-%I2F_VERSION%

echo clean backup ...
rd /q /s %BACKUP_PATH%
del /q /s %BACKUP_PATH%

echo backup...
move /Y %DEPLOY_PATH% %BACKUP_PATH%

echo copy jars...


md %DEPLOY_PATH%

copy ..\i2f-agent\target\i2f-agent-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-core\target\i2f-core-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-core-j2ee\target\i2f-core-j2ee-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-extension\target\i2f-extension-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-spring\target\i2f-spring-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-springboot\target\i2f-springboot-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-springcloud\target\i2f-springcloud-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\i2f-generator\target\i2f-generator-%I2F_VERSION%.jar %DEPLOY_PATH%
copy ..\dir-diff\target\dir-diff.jar %DEPLOY_PATH%


echo copy poms...

set MAVEN_POM_PATH=%DEPLOY_PATH%\pom
md %MAVEN_POM_PATH%

copy ..\pom.xml %MAVEN_POM_PATH%\i2f-boost-pom.xml
copy ..\i2f-agent\pom.xml %MAVEN_POM_PATH%\i2f-agent-pom.xml
copy ..\i2f-core\pom.xml %MAVEN_POM_PATH%\i2f-core-pom.xml
copy ..\i2f-core-j2ee\pom.xml %MAVEN_POM_PATH%\i2f-core-j2ee-pom.xml
copy ..\i2f-extension\pom.xml %MAVEN_POM_PATH%\i2f-extension-pom.xml
copy ..\i2f-spring\pom.xml %MAVEN_POM_PATH%\i2f-spring-pom.xml
copy ..\i2f-springboot\pom.xml %MAVEN_POM_PATH%\i2f-springboot-pom.xml
copy ..\i2f-springcloud\pom.xml %MAVEN_POM_PATH%\i2f-springcloud-pom.xml
copy ..\i2f-generator\pom.xml %MAVEN_POM_PATH%\i2f-generator-pom.xml
copy ..\dir-diff\pom.xml %MAVEN_POM_PATH%\dir-diff-pom.xml


echo done.

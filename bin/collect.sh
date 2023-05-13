echo begin...
BACKUP_PATH=./backup
DEPLOY_PATH=./deploy
I2F_VERSION=1.0

BACKUP_PATH=$BACKUP_PATH-$I2F_VERSION
DEPLOY_PATH=$DEPLOY_PATH-$I2F_VERSION

echo clean backup ...
rm -rf $BACKUP_PATH

echo backup...
mv $DEPLOY_PATH $BACKUP_PATH

echo copy jars...

mkdir $DEPLOY_PATH
cp ../i2f-agent/target/i2f-agent-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-core/target/i2f-core-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-core-j2ee/target/i2f-core-j2ee-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-extension/target/i2f-extension-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-spring/target/i2f-spring-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-springboot/target/i2f-springboot-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-springcloud/target/i2f-springcloud-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-generator/target/i2f-generator-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-streaming/target/i2f-streaming-$I2F_VERSION.jar $DEPLOY_PATH
cp ../i2f-translate/target/i2f-translate-$I2F_VERSION.jar $DEPLOY_PATH
cp ../itl-ddiff/target/itl-ddiff.jar $DEPLOY_PATH
cp ../itl-crypt/target/itl-crypt.jar $DEPLOY_PATH

echo copy poms...

MAVEN_POM_PATH=$DEPLOY_PATH/pom
mkdir $MAVEN_POM_PATH

cp ../pom.xml $MAVEN_POM_PATH/i2f-boost-pom.xml
cp ../i2f-agent/pom.xml $MAVEN_POM_PATH/i2f-agent-pom.xml
cp ../i2f-core/pom.xml $MAVEN_POM_PATH/i2f-core-pom.xml
cp ../i2f-core-j2ee/pom.xml $MAVEN_POM_PATH/i2f-core-j2ee-pom.xml
cp ../i2f-extension/pom.xml $MAVEN_POM_PATH/i2f-extension-pom.xml
cp ../i2f-spring/pom.xml $MAVEN_POM_PATH/i2f-spring-pom.xml
cp ../i2f-springboot/pom.xml $MAVEN_POM_PATH/i2f-springboot-pom.xml
cp ../i2f-springcloud/pom.xml $MAVEN_POM_PATH/i2f-springcloud-pom.xml
cp ../i2f-generator/pom.xml $MAVEN_POM_PATH/i2f-generator-pom.xml
cp ../i2f-streaming/pom.xml $MAVEN_POM_PATH/i2f-streaming-pom.xml
cp ../i2f-translate/pom.xml $MAVEN_POM_PATH/i2f-translate-pom.xml
cp ../itl-ddiff/pom.xml $MAVEN_POM_PATH/itl-ddiff-pom.xml
cp ../itl-crypt/pom.xml $MAVEN_POM_PATH/itl-crypt-pom.xml

echo done.

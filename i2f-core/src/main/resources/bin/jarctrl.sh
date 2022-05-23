#!/bin/bash

# jar name
AppName=$1
# control option
ctrlOption=$2

# AppName jar relative this bash's location
# when jar in parent dir
# it should be ..
jarRelativePath=

# max wait kill force timeout
MAX_WAIT=30
# enable shortcut shell
ENUM_SHORTCUT_ENABLE=1
ENUM_SHORTCUT_DISABLE=0

ENABLE_SHORTCUT=$ENUM_SHORTCUT_DISABLE

# 个性化启动参数
# springboot 环境变量配置
# app.name
APP_NAME=
# app.env
APP_ENV=
# max.size
LOG_MAX_SIZE=20MB
# logging.config
LOG_CONFIG_FILE=logback-spring.xml

# jvm 配置
# java home,use system default when empty
JAVA_HOME=
# not as opts when empty
USER_TIME_ZONE=Asia/Shanghai
# not as opts when empty
XMS_SIZE=512M
# not as opts when empty
XMX_SIZE=512M
# not as opts when empty
PERM_SIZE=256M
# not as opts when empty
MAX_PERM_SIZE=512M
# not as opts when empty
DUMP_OOM=1
# not as opts when empty
PRINT_GC=1
# not as opts when empty
PARALLEL_GC=1
# not as opts when empty
NEW_RATIO=1
# not as opts when empty
SURVIVOR_RATIO=30

# java executable path
JAVA_PATH=java
# use point java when java home not empty
if [[ "$JAVA_HOME" -ne "" ]]; then
  JAVA_PATH=${JAVA_HOME}/bin/java
fi

APP_HOME=`pwd`
if [[ "$jarRelativePath" -ne "" ]]; then
  APP_HOME="$(cd `dirname $0`/$jarRelativePath; pwd)"
  cd ${APP_HOME}
fi

# jvm opts
JVM_OPTS="-Dname=$AppName"
LOG_DIR=${APP_HOME}/logs
LOG_PATH=${LOG_DIR}/${AppName}.log
PID_PATH=${APP_HOME}/pid.$AppName.txt

function help()
{
    echo -e "\033[0;31m please input 2nd arg:option \033[0m  \033[0;34m {start|stop|restart|shutdown|reboot|status|log|snapshot|backup|recovery|clean|pack|unpack|pidstop|pidstart|pidreboot} \033[0m"
    echo -e "\033[0;34m start \033[0m : to run a jar which called AppName"
    echo -e "\033[0;34m stop \033[0m : to stop a jar which called AppName"
    echo -e "\033[0;34m restart \033[0m : to stop and run a jar which called AppName"
    echo -e "\033[0;34m shutdown \033[0m : to shutdown(force kill) a jar which called AppName"
    echo -e "\033[0;34m reboot \033[0m : to shutdown and run a jar which called AppName"
    echo -e "\033[0;34m status \033[0m : to check run status for a jar which called AppName"
    echo -e "\033[0;34m log \033[0m : to lookup the log for a jar which called AppName"
    echo -e "\033[0;34m snapshot \033[0m : to make a snapshot to ./snapshot for a jar which called AppName"
    echo -e "\033[0;34m backup \033[0m : to backup to ./backup a jar which called AppName"
    echo -e "\033[0;34m recovery \033[0m : to recovery from ./backup and save current to ./newest for a jar which called AppName"
    echo -e "\033[0;34m clean \033[0m : to clean dirs ./backup ./snapshot ./newest ./logs for a jar which called AppName"
    echo -e "\033[0;34m pack \033[0m : zip upk_AppName to AppName and backup source AppName to src_AppName"
    echo -e "\033[0;34m unpack \033[0m : unzip AppName to upk_AppName"
    echo -e "\033[0;34m pidstop \033[0m : stop AppName by pid file called pid.AppName"
    echo -e "\033[0;34m pidstart \033[0m : start AppName and save pid to file called pid.AppName"
    echo -e "\033[0;34m pidreboot \033[0m : reboot AppName rely pidstop and then pidstart"
    exit 1
}

function prepareBootJvmOpts()
{
  if [[ "$USER_TIME_ZONE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Duser.timezone=$USER_TIME_ZONE"
  fi
  if [[ "$XMS_SIZE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Xms$XMS_SIZE"
  fi
  if [[ "$XMX_SIZE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Xmx$XMX_SIZE"
  fi
  if [[ "$PERM_SIZE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:PermSize=$PERM_SIZE"
  fi
  if [[ "$MAX_PERM_SIZE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:MaxPermSize=$MAX_PERM_SIZE"
  fi
  if [[ "$DUMP_OOM" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError"
  fi
  if [[ "$PRINT_GC" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+PrintGCDateStamps  -XX:+PrintGCDetails"
  fi
  if [[ "$PARALLEL_GC" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+UseParallelGC -XX:+UseParallelOldGC"
  fi
  if [[ "$PARALLEL_GC" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+UseParallelGC -XX:+UseParallelOldGC"
  fi
  if [[ "$NEW_RATIO" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:NewRatio=$NEW_RATIO"
  fi
  if [[ "$SURVIVOR_RATIO" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:SurvivorRatio=$SURVIVOR_RATIO"
  fi
  if [[ "$APP_NAME" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Dapp.name=$APP_NAME"
  fi
  if [[ "$APP_ENV" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Dapp.env=$APP_ENV"
  fi
  if [[ "$LOG_MAX_SIZE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Dmax.size=$LOG_MAX_SIZE"
  fi
  if [[ "$LOG_CONFIG_FILE" -ne "" ]]; then
    JVM_OPTS="$JVM_OPTS -Dlogging.config=$LOG_CONFIG_FILE"
  fi
}

prepareBootJvmOpts

echo "-----------------------"
echo "jarctrl.sh running..."
echo "AppName: $AppName"
echo "ctrlOption: $ctrlOption"
echo "maxWait: $MAX_WAIT"
echo "enableShortcut: $ENABLE_SHORTCUT"
echo "jvmOpts: $JVM_OPTS"
echo "appHome: $APP_HOME"
echo "logDir: $LOG_DIR"
echo "logPath: $LOG_PATH"
echo "pidPath: $PID_PATH"
echo "-----------------------"

if [ "$ctrlOption" = "" ];
then
    help
fi

if [ "$AppName" = "" ];
then
    echo -e "\033[0;31m please input 1st arg:appName \033[0m"
    exit 1
fi


PID=""
function query()
{
  PID=""
  PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
}

CMD=""
function mkcmd()
{
  if [ x"$CMD" != x"" ]; then
    if [ $ENABLE_SHORTCUT == $ENUM_SHORTCUT_ENABLE ];then
      echo "./jarctrl.sh $CMD" > ./$CMD.sh
      chmod a+x ./$CMD.sh
    fi
  fi
}

function start()
{
    query

    if [ x"$PID" != x"" ]; then
        echo "$AppName is running..."
    else
        chmod a+x $AppName
        mkdir ${LOG_DIR}
        nohup $JAVA_PATH -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 &
        chmod a+r $LOG_DIR/*.log
        echo "Start $AppName success..."
        sleep 3
        query
        if [ x"$PID" != x"" ]; then
          echo "$AppName is running ok."
        else
          echo "$AppName maybe Interrupted!"
        fi
    fi

    CMD="start"
    mkcmd
}

function stop()
{
    echo "Stop $AppName"

    PID=""
    query

    if [ x"$PID" != x"" ]; then
        kill -TERM $PID
        echo "$AppName (pid:$PID) exiting..."
        CUR_WAIT=0
        while [ x"$PID" != x"" ]
        do
            sleep 1
            query
            CUR_WAIT=`expr ${CUR_WAIT} + 1`
            echo "wait $AppName stop timeout $CUR_WAIT..."
            if [ $CUR_WAIT == $MAX_WAIT ];then
              echo "$AppName has timeout of max wait stop,force kill run..."
              kill -9 $PID
            fi
        done
        echo "$AppName exited."
    else
        echo "$AppName already stopped."
    fi

    CMD="stop"
    mkcmd
}


function shutdown()
{
    echo "Shutdown $AppName"

    PID=""
    query(){
      PID=""
        PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
    }

    query
    if [ x"$PID" != x"" ]; then
        kill -9 $PID
        echo "$AppName (pid:$PID) shutdown..."
        while [ x"$PID" != x"" ]
        do
            sleep 1
            query
            kill -9 $PID
        done
        echo "$AppName shutdown."
    else
        echo "$AppName already shutdown."
    fi

    CMD="shutdown"
    mkcmd
}

function reboot()
{
    shutdown
    sleep 2
    start

    CMD="reboot"
    mkcmd
}

function restart()
{
    stop
    sleep 2
    start

    CMD="restart"
    mkcmd
}

function status()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|wc -l`
    if [ $PID != 0 ];then
        echo "$AppName is running..."
    else
        echo "$AppName is not running..."
    fi

    CMD="status"
    mkcmd
}

function log()
{
    tail -f -n 500 $LOG_PATH
}

function snapshot()
{
  mkdir ./snapshot
  TIME_NOW=$(date "+%Y%m%d%H%M%S")
  cp $AppName ./snapshot/$AppName.$TIME_NOW
  echo "$AppName has snapshot as $AppName.$TIME_NOW."
}

function backup()
{
  mkdir ./backup
  cp $AppName ./backup
  TIME_NOW=$(date "+%Y%m%d%H%M%S")
  echo $TIME_NOW > ./backup/_time
  echo "$AppName has backup."
}

function recovery()
{
  mkdir ./newest
  mv $AppName ./newest
  TIME_NOW=$(date "+%Y%m%d%H%M%S")
  echo $TIME_NOW > ./newest/_time
  cp ./backup/$AppName ./
  echo "$AppName has recovery."
}

function clean()
{
  rm -rf ./backup ./newest ./snapshot ./logs ./start.sh ./stop.sh ./reboot.sh ./shutdown.sh ./restart.sh ./status.sh
  echo "clean done."
}

function unpack()
{
  rm -rf ./upk_$AppName
  unzip $AppName -d ./upk_$AppName
  echo "$AppName has unpack."
}

function pack()
{
  mv $AppName ./src_$AppName
  zip -q -r $AppName ./upk_$AppName
  rm -rf ./upk_$AppName
  echo "$AppName has pack."
}


function pidstop()
{
  if [ -d ${PID_PATH} ]; then
    echo "not pid file found:$PID_PATH"
    return
  fi

  FPID=$(cat ${PID_PATH})
  if [[ "$FPID" -ne "" ]]; then
      echo "kill pid is:$FPID"
      kill -9 $FPID
      echo "" > $PID_PATH
  else
    echo "not pid found."
  fi

  CMD="pidstop"
  mkcmd
}

function pidstart()
{
  if [ ! -d ${PID_PATH} ]; then
    echo "not pid file,create..."
    touch ${PID_PATH}
  fi

  FPID=$(cat ${PID_PATH})
  if [[ "$FPID" -ne "" ]]; then
      echo "process has running ..."
      return
  fi

  echo "" > $PID_PATH
  chmod a+x $AppName
  mkdir ${LOG_DIR}
  nohup $JAVA_PATH -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 & echo $! > $PID_PATH
  chmod a+r $LOG_DIR/*.log
  echo "Start $AppName success..."

  CMD="pidstart"
  mkcmd
}

function pidreboot()
{
    pidstop
    sleep 2
    pidstart

  CMD="pidreboot"
  mkcmd
}

case $ctrlOption in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    shutdown)
    shutdown;;
    reboot)
    reboot;;
    status)
    status;;
    log)
    log;;
    snapshot)
    snapshot;;
    backup)
    backup;;
    recovery)
    recovery;;
    clean)
    clean;;
    unpack)
    unpack;;
    pack)
    pack;;
    pidstop)
    pidstop;;
    pidstart)
    pidstart;;
    pidreboot)
    pidreboot;;
    *)
    help;;
esac

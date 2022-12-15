#!/bin/bash

# jar name
AppName=
# control option
ctrlOption=$1
AppEnv=test

# try find jar file in current
if [ "$AppName" == "" ];
then
  _p_suffix=.jar
  _p_path=

  for _p_file in $(ls -a $_p_path | grep -v grep | grep $_p_suffix)
  do
      _p_fix=${_p_file##*.}
      if [ x"$_p_suffix" == x".$_p_fix" ]; then
         AppName=$_p_file
         break
      fi
  done
fi


echo "-----------------------"
echo "jarctrl.sh running..."
echo "AppName: $AppName"
echo "ctrlOption: $ctrlOption"
echo "-----------------------"

# AppName jar relative this bash's location
# when jar in parent dir
# it should be ..
jarRelativePath=

# max wait kill force timeout
MAX_WAIT=30
# const variable define
BOOL_TRUE=1
BOOL_FALSE=0

RMI_ENABLE=$BOOL_TRUE
RMI_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9400 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.net.preferIPv4Stack=true"

ENABLE_SHORTCUT=$BOOL_FALSE

APP_HOME=`pwd`
if [[ "$jarRelativePath" -ne "" ]]; then
  APP_HOME="$(cd `dirname $0`/$jarRelativePath; pwd)"
  cd ${APP_HOME}
fi


# jvm opts
JVM_OPTS="-Djar.name=$AppName"
LOG_DIR_NAME=logs
LOG_DIR=${APP_HOME}/${LOG_DIR_NAME}
LOG_PATH=${LOG_DIR}/${AppName}.log
PID_PATH=${APP_HOME}/pid.$AppName.txt

# logback
ENABLE_LOGBACK=$BOOL_TRUE
# logging.config
LOGBACK_CONFIG_FILE=classpath:logback-spring.xml
LOGBACK_APP_NAME=$AppName
LOGBACK_APP_ENV=
LOGBACK_APP_LOG_MAX_SIZE=200MB

if [ $ENABLE_LOGBACK == $BOOL_TRUE ];then
  if [ ! -f "$LOG_PATH" ];then
    LOG_PATH=${LOG_DIR}/`ls -t ${LOG_DIR} | grep .log | grep ${AppName}.all. | head -n 1`
  fi
  if [ ! -f "$LOG_PATH" ];then
    LOG_PATH=${LOG_DIR}/`ls -t ${LOG_DIR} | grep .log | grep ${AppName}.info. | head -n 1`
  fi
  if [ ! -f "$LOG_PATH" ];then
    LOG_PATH=${LOG_DIR}/`ls -t ${LOG_DIR} | grep .log | grep ${AppName}.warn. | head -n 1`
  fi
  if [ ! -f "$LOG_PATH" ];then
    LOG_PATH=${LOG_DIR}/`ls -t ${LOG_DIR} | grep .log | grep ${AppName}.error. | head -n 1`
  fi
fi

echo $LOG_PATH

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

if [[ $RMI_ENABLE == $BOOL_TRUE ]]; then
  JVM_OPTS="${JVM_OPTS} ${RMI_OPTS}"
fi

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
    echo -e "\033[0;34m except \033[0m : to lookup the exception log for a jar which called AppName"
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
  echo "----jvm opts begin----"
  if [[ -n "$AppEnv" ]]; then
    JVM_OPTS="$JVM_OPTS -Dspring.profiles.active=$AppEnv"
    echo "-Dspring.profiles.active=$AppEnv"
  fi
  if [[ -n "$USER_TIME_ZONE" ]]; then
    JVM_OPTS="$JVM_OPTS -Duser.timezone=$USER_TIME_ZONE"
    echo "-Duser.timezone=$USER_TIME_ZONE"
  fi
  if [[ -n "$XMS_SIZE" ]]; then
    JVM_OPTS="$JVM_OPTS -Xms$XMS_SIZE"
    echo "-Xms$XMS_SIZE"
  fi
  if [[ -n "$XMX_SIZE" ]]; then
    JVM_OPTS="$JVM_OPTS -Xmx$XMX_SIZE"
    echo "-Xmx$XMX_SIZE"
  fi
  if [[ -n "$PERM_SIZE" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:PermSize=$PERM_SIZE"
    echo "-XX:PermSize=$PERM_SIZE"
  fi
  if [[ -n "$MAX_PERM_SIZE" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:MaxPermSize=$MAX_PERM_SIZE"
    echo "-XX:MaxPermSize=$MAX_PERM_SIZE"
  fi
  if [[ -n "$DUMP_OOM" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError"
    echo "-XX:+HeapDumpOnOutOfMemoryError"
  fi
  if [[ -n "$PRINT_GC" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+PrintGCDateStamps  -XX:+PrintGCDetails"
    echo "-XX:+PrintGCDateStamps  -XX:+PrintGCDetails"
  fi
  if [[ -n "$PARALLEL_GC" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+UseParallelGC -XX:+UseParallelOldGC"
    echo "-XX:+UseParallelGC -XX:+UseParallelOldGC"
  fi
  if [[ -n "$PARALLEL_GC" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:+UseParallelGC -XX:+UseParallelOldGC"
    echo "-XX:+UseParallelGC -XX:+UseParallelOldGC"
  fi
  if [[ -n "$NEW_RATIO" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:NewRatio=$NEW_RATIO"
    echo "-XX:NewRatio=$NEW_RATIO"
  fi
  if [[ -n "$SURVIVOR_RATIO" ]]; then
    JVM_OPTS="$JVM_OPTS -XX:SurvivorRatio=$SURVIVOR_RATIO"
    echo "-XX:SurvivorRatio=$SURVIVOR_RATIO"
  fi

  if [ $ENABLE_LOGBACK == $BOOL_TRUE ];then
    echo "----logback begin----"
    if [[ -n "$LOGBACK_CONFIG_FILE" ]]; then
      JVM_OPTS="$JVM_OPTS -Dlogging.config=$LOGBACK_CONFIG_FILE"
      echo "-Dlogging.config=$LOGBACK_CONFIG_FILE"
    fi

    if [[ -n "$LOGBACK_APP_NAME" ]]; then
      JVM_OPTS="$JVM_OPTS -Dlogback.app.name=$LOGBACK_APP_NAME"
      echo "-Dlogback.app.name=$LOGBACK_APP_NAME"
    fi
    if [[ -n "$LOGBACK_APP_ENV" ]]; then
      JVM_OPTS="$JVM_OPTS -Dlogback.app.env=$LOGBACK_APP_ENV"
      echo "-Dlogback.app.env=$LOGBACK_APP_ENV"
    fi
    if [[ -n "$LOGBACK_APP_LOG_MAX_SIZE" ]]; then
      JVM_OPTS="$JVM_OPTS -Dlogback.app.log.max.size=$LOGBACK_APP_LOG_MAX_SIZE"
      echo "-Dlogback.app.log.max.size=$LOGBACK_APP_LOG_MAX_SIZE"
    fi
    if [[ -n "$LOG_DIR_NAME" ]]; then
      JVM_OPTS="$JVM_OPTS -Dlogback.app.log.dir.name=$LOG_DIR_NAME"
      echo "-Dlogback.app.log.dir.name=$LOG_DIR_NAME"
    fi
    echo "----logback end----"
  fi

  echo "----jvm opts end----"
}


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
    if [[ $ENABLE_SHORTCUT == $BOOL_TRUE ]];then
      echo "./jarctrl.sh $CMD" > ./$CMD.sh
      chmod a+x ./$CMD.sh
    fi
  fi
}

function start()
{
    prepareBootJvmOpts

    query

    if [ x"$PID" != x"" ]; then
        echo "$AppName is running..."
    else
        chmod a+x $AppName
        mkdir ${LOG_DIR}
        if [[ $ENABLE_LOGBACK == $BOOL_TRUE ]]; then
          nohup $JAVA_PATH -jar  $JVM_OPTS $AppName >/dev/null 2>&1 &
        else
          nohup $JAVA_PATH -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 &
        fi
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

function except()
{
    tail -f -n 5000 $LOG_PATH | grep -inA50 exception
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
  prepareBootJvmOpts

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
  if [[ $ENABLE_LOGBACK == $BOOL_TRUE ]]; then
    nohup $JAVA_PATH -jar  $JVM_OPTS $AppName >/dev/null 2>&1 & echo $! > $PID_PATH
  else
    nohup $JAVA_PATH -jar  $JVM_OPTS $AppName > $LOG_PATH 2>&1 & echo $! > $PID_PATH
  fi

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
    except)
    except;;
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

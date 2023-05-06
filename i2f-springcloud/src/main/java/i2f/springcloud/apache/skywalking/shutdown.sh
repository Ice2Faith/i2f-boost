echo try kill webapp...
PID=`ps -ef | grep -v grep | grep java | grep skywalking-webapp.jar | awk '{print $2}'`
if [[ -n "${PID}" ]]; then
  echo webapp pid is $PID
  kill -9 $PID
  echo webapp has killed.
fi

echo try kill oap service...
PID=`ps -ef | grep -v grep | grep java | grep org.apache.skywalking.oap.server.starter.OAPServerStartUp | awk '{print $2}'`
if [[ -n "${PID}" ]]; then
  echo oap service pid is $PID
  kill -9 $PID
  echo oap service has killed.
fi
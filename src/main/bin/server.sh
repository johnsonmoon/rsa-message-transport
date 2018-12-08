#!/bin/bash

source /etc/profile

WORK_HOME=$(cd "$(dirname "$0")"; pwd)/..
echo ${WORK_HOME}
cd ${WORK_HOME}

JAVA_CMD="java"
CLASS_PATH="./lib/*:${CLASSPATH}"
MAIN_CLASS="com.github.johnsonmoon.rsaencryptionanddecryption.Main"
JAVA_OPTS="-Dwork.home=${WORK_HOME} -Dspring.config.location=file:${WORK_HOME}/conf/ \
           -Xmx512m -Xms256m -Xss256K -XX:MaxMetaspaceSize=256m \
           -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:+ExplicitGCInvokesConcurrent"

function status(){
    num=`ps -ef | grep java | grep rsaencryptionanddecryption | wc -l`
    if [ ${num} -eq 0 ] ; then
        echo "stopped"
        exit 0
    else
        echo "started"
        exit 0  
    fi
}

function start(){
    echo "Starting server..."
    num=`ps -ef | grep java | grep rsaencryptionanddecryption | wc -l`
    if [ ${num} -eq 0 ] ; then
    	nohup ${JAVA_CMD} ${JAVA_OPTS} -classpath ${CLASS_PATH} ${MAIN_CLASS} >> /dev/null 2>&1 &
    	sleep 3
    	echo "Server started."
    	exit 0
    else
    	echo "Server has already started."
    	exit 0	
    fi
}

function restart(){
    echo "Restarting..."
    num=`ps -ef | grep java | grep rsaencryptionanddecryption | wc -l`
    if [ ${num} -ne 0 ] ; then
        pid=`ps -ef | grep java | grep rsaencryptionanddecryption | awk '{print $2}'`
        kill -9 ${pid}
        sleep 3
    fi
    nohup ${JAVA_CMD} ${JAVA_OPTS} -classpath ${CLASS_PATH} ${MAIN_CLASS} >> /dev/null 2>&1 &
    sleep 3
    echo "Server started."
}

function stop(){
    echo "Stopping server..."
    num=`ps -ef | grep java | grep rsaencryptionanddecryption | wc -l`
    if [ ${num} -ne 0 ] ; then
    	pid=`ps -ef | grep java | grep rsaencryptionanddecryption | awk '{print $2}'`
    	kill -9 ${pid}
    	sleep 3
    	echo "Server stopped."
    	exit 0
    else
    	echo "Server has not started."
    	exit 0
    fi
}

function help(){
    echo "-----------------------"
    echo "Usage: ./server.sh [command]"
    echo ""
    echo "Commands:"
    echo "    status       Check status of server."
    echo "    start        Start server."
    echo "    stop         Stop  server."
    echo "    help         Print help options."
    echo "-----------------------"
}

if [ ! $1 ] ; then 
    help
elif [ $1 = "help" ] ; then
    help
elif [ $1 = "status" ] ; then
    status
elif [ $1 = "start" ] ; then 
    start
elif [ $1 = "stop" ] ; then
    stop
elif [ $1 = "debug" ] ; then
    debug
elif [ $1 = "restart" ] ; then
    restart
else
    help
fi

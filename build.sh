#!/bin/sh

#--------------------------------------------
# No need to edit anything past here
#--------------------------------------------
if test -z "${JAVA_HOME}" ; then
    echo "ERROR: JAVA_HOME not found in your environment."
    echo "Please, set the JAVA_HOME variable in your environment to match the"
    echo "location of the Java Virtual Machine you want to use."
    exit
fi

if test -f ${JAVA_HOME}/lib/tools.jar ; then
    CLASSPATH=${CLASSPATH}:${JAVA_HOME}/lib/tools.jar
fi

# convert the existing path to unix
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

ANT_JAR='build/lib/ant.jar'
OPTIONAL_JAR='build/lib/optional.jar'

CLASSPATH=${CLASSPATH}:${ANT_JAR}
CLASSPATH=${CLASSPATH}:${OPTIONAL_JAR}
CLASSPATH=${CLASSPATH}:build/lib/junit.jar
CLASSPATH=${CLASSPATH}:lib/xml-apis.jar
CLASSPATH=${CLASSPATH}:lib/xercesImpl.jar
CLASSPATH=${CLASSPATH}:lib/log4j.jar

# convert the unix path to windows
if [ "$OSTYPE" = "cygwin32" ] || [ "$OSTYPE" = "cygwin" ] ; then
   CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

BUILDFILE=build/build.xml

${JAVA_HOME}/bin/java -classpath ${CLASSPATH} -Dfile.encoding=UTF-8 org.apache.tools.ant.Main \
                      -buildfile ${BUILDFILE} "$@"

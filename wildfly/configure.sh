#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
ECHO "$DIR"
WILDFLY_HOME=C:/Users/Mateusz/Desktop/7Semestr/JEE/lab/wildfly/wildfly-18.0.0.Final
ECHO "$WILDFLY_HOME"
${WILDFLY_HOME}/bin/jboss-cli.bat --connect --file=${DIR}/database_connection.cli
${WILDFLY_HOME}/bin/jboss-cli.bat --connect --file=${DIR}/security_domain.cli
${WILDFLY_HOME}/bin/jboss-cli.bat --connect --file=${DIR}/auth_method.cli

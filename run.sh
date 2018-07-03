#!/bin/bash

export DB_SCHEMA=kitchen_talk
export DB_USER=postgres
export DB_PASSWORD=test1234
export DB_DATABASE=postgres
export DB_POOL_SIZE=10
export DB_QUERY_TIMEOUT=20000
export DB_HOST=localhost
export APP_PORT=9013

mvn clean package

java -Xms256m -Xmx512m -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Dlogback.configurationFile=src/main/resources/logback.xml -jar target/kt-legal-fat.jar

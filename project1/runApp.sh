cd ./bin
java -classpath . -Djava.rmi.server.codebase=file:./ client/TestApp peer1 BACKUP ../filesToTest/test.txt 1

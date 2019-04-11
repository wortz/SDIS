cd ./bin
java -classpath . -Djava.net.preferIPv4Stack=true -Djava.rmi.server.codebase=file:./ peer/Peer 1.0 1 peer2 232.1.1.0 4465 232.1.1.1 4466 232.1.1.1 4467 &
rm -rf bin
rm -f *.jar
mkdir -p bin
javac -d bin -sourcepath src src/peer/*.java src/protocols/*.java src/rmi/*.java src/client/*.java src/channels/*.java src/utility/*.java src/file/*
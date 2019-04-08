rm -rf bin
rm -f *.jar
mkdir -p bin
javac -d bin -sourcepath src src/client/*.java src/*.java src/rmi/*.java
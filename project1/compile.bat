del "bin\*.class" "bin\client\*.class" "bin\channels\*.class" "bin\rmi\*.class" "bin\utility\*.class"
rmdir bin\client bin\rmi bin\channels bin\utility bin
mkdir bin
javac -d bin -sourcepath src src/peer/*.java src/rmi/*.java src/client/*.java src/channels/*.java src/utility/*.java

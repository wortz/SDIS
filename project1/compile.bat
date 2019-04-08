del "bin\*.class" "bin\client\*.class" "bin\channels\*.class" "bin\rmi\*.class"
rmdir bin\client bin\rmi bin\channels
javac -d bin -sourcepath src src/*.java src/rmi/*.java src/client/*.java src/channels/*.java
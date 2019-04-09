del "bin\*.class" "bin\client\*.class" "bin\channels\*.class" "bin\rmi\*.class" "bin\utility\*.class"
rmdir bin\client bin\rmi bin\channels bin\utility
javac -d bin -sourcepath src src/*.java src/rmi/*.java src/client/*.java src/channels/*.java src/utility/*.java
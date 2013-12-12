rm -rvf bin/*
javac $(find src -name "*.java") -sourcepath src -classpath libs/commons-io-2.4.jar:libs/commons-io-2.4.jar -d bin
jar cvf bin/initandroidproject.jar -C bin .
java -classpath bin/initandroidproject.jar:libs/commons-io-2.4.jar:libs/commons-io-2.4.jar mn.uwvm.initandroidproject.Main

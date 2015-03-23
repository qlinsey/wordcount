
rm -rf ./classes/
rm -rf ./wc_output/*

mkdir classes
cd ./src/main/java/question

$JAVA_HOME/bin/javac *.java -d "../../../../classes"

cd ../../../..

$JAVA_HOME/bin/java -cp classes question.StartCount

rm TRMIPolite/TinyTM/*.class
rm TRMIPolite/TinyTM/contention/*.class
rm TRMIPolite/TinyTM/DBank/*.class
rm TRMIPolite/TinyTM/exceptions/*.class
rm TRMIPolite/TinyTM/ofree/*.class
javac -classpath .:./TRMIPolite TRMIPolite/TinyTM/*.java
javac -classpath .:./TRMIPolite TRMIPolite/TinyTM/contention/*.java
javac -classpath .:./TRMIPolite TRMIPolite/TinyTM/DBank/*.java
javac -classpath .:./TRMIPolite TRMIPolite/TinyTM/exceptions/*.java
javac -classpath .:./TRMIPolite TRMIPolite/TinyTM/ofree/*.java
rm DSTMBenchmark/*.class
rm DSTMBenchmark/GenericDSTM/*.class
javac DSTMBenchmark/*.java
javac -classpath .:./TRMIPolite DSTMBenchmark/GenericDSTM/*.java


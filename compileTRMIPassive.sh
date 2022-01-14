rm TRMIPassive/TinyTM/*.class
rm TRMIPassive/TinyTM/contention/*.class
rm TRMIPassive/TinyTM/DBank/*.class
rm TRMIPassive/TinyTM/exceptions/*.class
rm TRMIPassive/TinyTM/ofree/*.class
javac -classpath .:./TRMIPassive TRMIPassive/TinyTM/*.java
javac -classpath .:./TRMIPassive TRMIPassive/TinyTM/contention/*.java
javac -classpath .:./TRMIPassive TRMIPassive/TinyTM/DBank/*.java
javac -classpath .:./TRMIPassive TRMIPassive/TinyTM/exceptions/*.java
javac -classpath .:./TRMIPassive TRMIPassive/TinyTM/ofree/*.java
rm DSTMBenchmark/*.class
rm DSTMBenchmark/GenericDSTM/*.class
javac DSTMBenchmark/*.java
javac -classpath .:./TRMIPassive DSTMBenchmark/GenericDSTM/*.java


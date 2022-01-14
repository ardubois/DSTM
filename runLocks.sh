#!/bin/bash

if [ -z $1 ]
then
	NSERVER=3
else
	NSERVER=$1
fi

if [ -z $2 ]
then
	NOBJSERVER=10
else NOBJSERVER=$2
fi

if [ -z $3 ]
then
	NCLIENT=3
else
	NCLIENT=$3
fi

if [ -z $4 ]
then
	WRITES=20
else
	WRITES=$4
fi

if [ -z $5 ]
then
	NTRANS=100
else
	NTRANS=$5
fi


if [ -z $6 ]
then
	NOBJTRANS=500
else
	NOBJTRANS=$6
fi
#=$(expr $5 - 1)
#fi

for i in $(seq 0 0);
do
  java DSTMBenchmark.GenericLocks.GenericCoordinator $NSERVER $NCLIENT $NOBJSERVER&

  pid=$!
  printf "Locks\t$NCLIENT\t"
  for i in $(seq 0 $(($NSERVER - 1)));
  do
	taskset -c $(($i+$NCLIENT)) java DSTMBenchmark.GenericLocks.GenericServer $i $NOBJSERVER &
  done

  for i in $(seq 0 $(($NCLIENT-1)));
  do
	taskset -c $i java DSTMBenchmark.GenericLocks.GenericClient $i $NSERVER $NOBJSERVER $WRITES $NTRANS $NOBJTRANS &
 done
 wait $pid
done




#!/bin/ksh

rm -f *.class

if ! jc MainBadSig.java
then
	exit 1
fi

if ! jc MainWorkOwnersFile.java
then
	exit 1
fi

while :
do
	if ! jr MainBadSig ../assert1
	then
		exit 1
	fi

	if ! jr MainWorkOwnersFile ../assert1
	then
		exit 1
	fi

	sleep 2
done

exit 0

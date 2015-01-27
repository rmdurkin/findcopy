#!/bin/ksh

rm -f *.class

if ! jc MainWorkOwnersDbms.java
then
	exit 1
fi

if ! jc MainBadSigDbms.java
then
	exit 1
fi

while :
do
	rm -f badsig1.txt badsig2.txt

	# set up a random seed to force the Dbms layer to
	# do the same thing for each invocation for this script

	export RANDSEED=$RANDOM
	echo $RANDSEED

	# create the bad signature database

	if ! jr MainBadSigDbms
	then
		exit 1
	fi
	wc -l < badsig2.txt

	# run the list of work owners

	if ! jr MainWorkOwnersDbms
	then
		exit 1
	fi

	date
	echo
done

exit 0

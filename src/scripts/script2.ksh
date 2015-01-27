#!/bin/ksh

rm -f *.class

if ! jc MainSingleOwnerDbms.java
then
	exit 1
fi

if ! jc MainBadSigDbms.java
then
	exit 1
fi

while :
do
	rm -f badsig1.txt badsig2.txt out1 out2

	# set up a random seed to force the Dbms layer to
	# do the same thing for each invocation for this script

	export RANDSEED=$RANDOM

	# process the documents for a single owner,
	# without the bad signature database

	if ! jr MainSingleOwnerDbms "0" >out1
	then
		exit 1
	fi

	# create a list of bad signatures for all documents

	if ! jr MainBadSigDbms >/dev/null
	then
		exit 1
	fi

	# run again for a single owner, relative to these bad signatures

	mv badsig2.txt badsig1.txt

	if ! jr MainSingleOwnerDbms "0" >out2
	then
		exit 1
	fi

	# display number of hits for both approaches

	echo

	ns=`wc -l < badsig1.txt`
	echo "number of bad signatures = $ns"

	ow=`wc -l < out1`
	nw=`wc -l < out2`
	echo "old way number of hits = $ow"
	echo "new way number of hits = $nw"

	p1=`script2awk out1`
	p2=`script2awk out2`
	echo "old percentage of cp = $p1"
	echo "new percentage of cp = $p2"

	date
done

exit 0

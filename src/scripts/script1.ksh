#!/bin/ksh

rm -f *.class badsig1.txt badsig2.txt out1 out2

# create random documents

docsdir="docs"

rm -rf $docsdir
mkdir $docsdir

if ! javac MainRandDocs.java
then
	exit 1
fi

java MainRandDocs $docsdir 250

# process the documents for a single owner,
# without the bad signature database

if ! javac MainSingleOwner.java
then
	exit 1
fi

java MainSingleOwner $docsdir "owner1" >out1

# create a list of bad signatures for the documents

if ! javac MainBadSig.java
then
	exit 1
fi

java MainBadSig $docsdir >/dev/null

# run again for a single owner, relative to these bad signatures

mv badsig2.txt badsig1.txt

java MainSingleOwner $docsdir "owner1" >out2

# display number of hits for both approaches

wc -l out1
wc -l out2

exit 0

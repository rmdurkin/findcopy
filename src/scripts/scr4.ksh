#!/bin/ksh

if ! jc MainTemplates.java
then
	exit 1
fi

if ! jc MainLineWord.java
then
	exit 1
fi

if ! jc MainOwnerCopies.java
then
	exit 1
fi

while :
do
	jr MainTemplates 0.01
	jr MainLineWord
	jr MainOwnerCopies

	date

	break
done

exit 0

#!/bin/ksh

rm -f *.class ../fclib/*.class

cp=".;..;"

for i in *.java
do
	b=${i%.java}

	if ! javac -classpath "$cp" $i
	then
		print "$i   compile"
		continue
	fi

	if ! java -classpath "$cp" -enableassertions -Xmx300000k $b
	then
		print "$i   runtime"
		continue
	fi

	print "$i   pass"
done

rm -f *.class ../fclib/*.class

exit 0

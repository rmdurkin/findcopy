#!/bin/ksh

awk '
	$0 ~ /^gt:/ {
		total++;
	}
	$0 ~ /^cp:/ {
		cp++;
		total++;
	}
	$0 ~ /^rnd:/ {
		total++;
	}
	END {
		if (total == 0)
			total = 1;
		perc = int(cp * 100.0 / total + 0.5);
		printf("%d\n", perc);
	}
' $1

exit 0

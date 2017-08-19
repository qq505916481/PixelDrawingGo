#!/bin/bash

DO_LOOP="yes"

while getops "l" OPTION 2> /dev/null; do
	case ${OPTION} in
		l)
			DO_LOOP="yes"
			;;
		\?)
			break
			;;
	esac
done

LOOPS=0
# don’t bail out of bash script if an error appear
set +e

echo $DO_LOOP

while [ "$LOOPS" -eq 0 ] || [ "$DO_LOOP" == "yes" ]; do
	if [ "$DO_LOOP" == "yes" ]; then
		java -jar test.jar 568 531
	else
		exec java -jar test.jar 568 531
	fi
	
	if [ "$DO_LOOP" == "yes" ]; then
		if [ ${LOOPS} -gt 0 ]; then
			echo "Restarted $LOOPS times"
		fi
		echo "To escape the loop, press CTRL+C now. Otherwise, wait 5 seconds for the programme to restart."
		echo ""
		sleep 5
		((LOOPS++))
	fi
done

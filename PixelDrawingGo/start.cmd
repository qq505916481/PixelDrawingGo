@echo off
set /p isLoop="Run with loop? <y/n>"

if /i "%isLoop%" == "n" goto :run
if /i "%isLoop%" == "y" goto :Loop

set count = 0

:Loop
	java -jar test.jar 568 531
	
	ping 127.0.0.1 -n 5 > nul
	
	set /a count = count+1
	echo =========================
	echo Restarted %count% times
	goto :end
:run
	java -jar test.jar 568 531
	goto :end
:end

if /i "%isLoop%" == "y" goto :Loop
pause
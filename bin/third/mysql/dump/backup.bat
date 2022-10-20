@echo off
set bak_host=%1
set bak_user=%2
set bak_password=%3

set bak_db=%4
set bak_table=%5
set bak_dir=%6

set _ret=
set _ret_goto=

set _ret_goto=_next1
goto formatNowDate
:_next1
set bak_time=%_ret%

set bak_path=%bak_dir%\%bak_db%\%bak_time:~0,8%
mkdir %bak_path%
set bak_file=%bak_path%\%bak_table%-%bak_time%.sql

set _ret_goto=_next2
goto formatNowDate
:_next2
echo ------------ begin %_ret% ------------
echo backup table %bak_db%.%bak_table% to %bak_file% ...

.\mysql8\mysqldump.exe -h %bak_host% -u%bak_user% -p%bak_password% --no-tablespaces --skip-extended-insert --single-transaction --quick %bak_db% %bak_table%  >  %bak_file%

echo backup done for backup table %bak_db%.%bak_table% to %bak_file%
set _ret_goto=_next3
goto formatNowDate
:_next3
echo ------------ end %_ret% ------------

goto _exit

:formatNowDate
set _ret=

set yyyy=%date:~0,4%
set MM=%date:~5,2%
set dd=%date:~8,2%
set HH=%time:~0,2%
set mi=%time:~3,2%
set ss=%time:~6,2%
if "%MM:~0,1%" == " " (set MM=0%MM:~1,1%)
if "%dd:~0,1%" == " " (set dd=0%dd:~1,1%)
if "%HH:~0,1%" == " " (set HH=0%HH:~1,1%)
if "%mi:~0,1%" == " " (set mi=0%mi:~1,1%)
if "%ss:~0,1%" == " " (set ss=0%ss:~1,1%)

set _ret=%yyyy%%MM%%dd%%HH%%mi%%ss%
goto %_ret_goto%


:_exit

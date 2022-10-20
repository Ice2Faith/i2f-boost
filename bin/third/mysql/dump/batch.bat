@echo off
set bak_host=localhost
set bak_user=root
set bak_password=123456

set bak_db=common_db
set bak_dir=D:\bak

:: add your table blow here like this

call backup.bat %bak_host% %bak_user% %bak_password% %bak_db% sys_user "%bak_dir%"
call backup.bat %bak_host% %bak_user% %bak_password% %bak_db% sys_role "%bak_dir%"
call backup.bat %bak_host% %bak_user% %bak_password% %bak_db% sys_dict "%bak_dir%"


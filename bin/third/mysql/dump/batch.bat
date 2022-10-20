@echo off
:: %需要转义
set esc_per=%%

set bak_host=localhost
set bak_user=root
set bak_password=admin#2022

set bak_db=test_db
set bak_dir=G:\db_backup


:: add your table blow here like this

call backup.bat %bak_host% %bak_user% %bak_password% %bak_db% sys_user %bak_dir%
call backup.bat %bak_host% %bak_user% %bak_password% %bak_db% sys_role %bak_dir%
call backup.bat %bak_host% %bak_user% %bak_password% %bak_db% sys_dict %bak_dir%


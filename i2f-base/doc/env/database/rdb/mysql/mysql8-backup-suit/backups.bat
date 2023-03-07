@echo off
:: %需要转义
set esc_per=%%

set bak_dir=.\db_backup

:: 获取完整路径
for %%a in (%bak_dir%) do set bak_dir=%%~fa


set _backups_ret_path=%cd%
cd .\mysql8\bin

:: add your table blow here like this
call bak.bat biz_car_info %bak_dir%



cd %_backups_ret_path%

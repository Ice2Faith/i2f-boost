# %需要转义
esc_per=\%
esc_srp=\#

bak_host=localhost
bak_user=root
bak_password=admin\%123

bak_db=test_db
bak_dir=/home/db_backup

chmod a+x ./backup.sh

# add your table blow here like this

./backup.sh ${bak_host} ${bak_user} ${bak_password} ${bak_db} sys_user ${bak_dir}
./backup.sh ${bak_host} ${bak_user} ${bak_password} ${bak_db} sys_role ${bak_dir}
./backup.sh ${bak_host} ${bak_user} ${bak_password} ${bak_db} sys_dict ${bak_dir}


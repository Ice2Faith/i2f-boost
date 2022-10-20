bak_host=localhost
bak_user=root
bak_password=123456

bak_db=test_db
bak_dir=/home/bak

chmod a+x ./backup.sh

# add your table blow here like this

./backup.sh ${bak_host} ${bak_user} ${bak_password} ${bak_db} sys_user "${bak_dir}"
./backup.sh ${bak_host} ${bak_user} ${bak_password} ${bak_db} sys_role "${bak_dir}"
./backup.sh ${bak_host} ${bak_user} ${bak_password} ${bak_db} sys_dict "${bak_dir}"

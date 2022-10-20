bak_host=$1
bak_user=$2
bak_password=$3

bak_db=$4
bak_table=$5
bak_dir=$6

bak_time=$(date "+%Y%m%d%H%M%S")

bak_path=${bak_dir}/${bak_db}/${bak_time:0:8}
mkdir -p ${bak_path}
bak_file=${bak_path}/${bak_table}-${bak_time}.sql

echo ------------ begin $(date "+%Y-%m-%d %H:%M:%S") ------------
echo backup table ${bak_db}.${bak_table} to ${bak_file}...

chmod a+x ./mysql8/mysqldump
./mysql8/mysqldump -h ${bak_host} -u${bak_user} -p${bak_password}  --single-transaction --quick ${bak_db} ${bak_table} > ${bak_file}


echo backup done for backup table ${bak_db}.${bak_table} to ${bak_file} .
echo ------------ end $(date "+%Y-%m-%d %H:%M:%S") ------------

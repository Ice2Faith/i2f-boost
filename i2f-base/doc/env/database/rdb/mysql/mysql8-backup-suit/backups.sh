# %需要转义
esc_per=\%
esc_srp=\#

bak_dir=./backup_db

# 获取完整路径
bak_dir=`realpath $bak_dir`

_backups_ret_path=`pwd`
cd ./mysql8/bin
chmod a+x ./bak.sh

# add your table blow here like this
./bak.sh biz_car_info "${bak_dir}"







cd $_backups_ret_path
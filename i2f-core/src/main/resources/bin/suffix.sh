#!/bin/bash
_p_suffix=$1
_p_path=$2

for _p_file in $(ls -a $_p_path | grep -v grep | grep $_p_suffix)
do
    _p_fix=${_p_file##*.}
    if [ x"$_p_suffix" == x".$_p_fix" ]; then
       echo $_p_file
    fi
done

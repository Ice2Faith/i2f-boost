#!/bin/bash

KEEP_COUNT=10

_p_name=
_p_root_path=..

echo clean begin ...

if [ "$_p_name" == "" ];
then
    _p_name=$(basename $(pwd))
fi

echo clean name : $_p_name

_p_path=${_p_root_path}/backup.${_p_name}
echo clean path : $_p_path

_p_count=$(ls -t ${_p_path} | wc -l)
echo all count : $_p_count
echo keep count : $KEEP_COUNT

_p_del_count=$(($_p_count - $KEEP_COUNT))
echo clean count : $_p_del_count

if [ $_p_del_count -gt 0 ] 
then
    for item in $(ls -t ${_p_path} | tail -n ${_p_del_count}); do
        echo del: ${_p_path}/${item}
        rm -r ${_p_path}/${item}
    done
else
  echo not need clean item.
fi

echo clean done.


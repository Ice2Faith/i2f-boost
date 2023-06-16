AppName=dist
echo begin deploy ${AppName} ...

_p_now=$(date "+%Y%m%d%H%M%S")
_p_bak=${AppName}.${_p_now}

echo backup ...
mv ${AppName} ${_p_bak}

echo unzip ...
unzip ${AppName}.zip -d dist > /dev/null

echo verify directory...
if [ -d "${AppName}/${AppName}" ];then
    rm -rf tmp.${AppName}
    mv ${AppName} tmp.${AppName}
    mv tmp.${AppName}/${AppName} .
    rm -rf tmp.${AppName}
fi

echo done.

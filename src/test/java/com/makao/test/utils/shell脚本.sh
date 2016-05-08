#!/bin/sh 

#执行的命令
CMD=$1
#结果保存文件  
RESULT_FILE=$2 

echo `${CMD}` >> ${RESULT_FILE}
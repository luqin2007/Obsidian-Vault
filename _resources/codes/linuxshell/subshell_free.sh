#!/bin/bash

sum=0
df | grep "^/" > tmp
while read name total used free other
do
    echo "free=$free"
    let sum+=free
    echo "sum=$sum"
done < tmp

echo "sum=$sum"
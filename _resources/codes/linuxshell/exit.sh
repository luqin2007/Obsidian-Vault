#!/bin/bash

for i in {1..5}
do
    [ $i -eq 3 ] && exit
    echo $i
done
echo "Game over!"
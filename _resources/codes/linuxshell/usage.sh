#!/bin/bash

function print_usage() {
    cat << EOF
Usage: --help | -h
  Print help information for script
Usage: --memory | -m
  Monitor memory information
Usage: --network | -n
  Monitor network interface information
EOF
}

case $1 in
--memory|-m)
	free;;
--network|-n)
    ip -s link;;
--help|-h)
	print_usage;;
*)
	print_usage;;
esac
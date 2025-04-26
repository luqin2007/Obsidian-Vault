#!/bin/bash

function check_services() {
    for i in "$@"
    do
        if systemctl --quiet is-active ${i}.service; then
            echo -e "${date_time}; \e[92mservice $i is active\e[0m"
        else
            echo -e "${date_time}; \e[92mservice $i is not active\e[0m"
        fi
    done
}

check_services httpd sshd vaftpd
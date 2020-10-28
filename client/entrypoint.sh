#!/bin/sh


socat tcp-listen:23456,fork,reuseaddr unix-connect:/docker.sock &
java -Djava.security.egd=file:/dev/./urandom -jar /client.jar --spring.profiles.active=prod
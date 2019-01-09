#!/bin/bash

docker rm -f master-nginx

docker run -d --name master-nginx -p 8080:80 -v $(pwd)/default.conf:/etc/nginx/conf.d/default.conf nginx:alpine


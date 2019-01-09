#!/bin/bash

docker rm -f cross-nginx

docker run -d --name cross-nginx -p 8080:80 -v $(pwd)/default.conf:/etc/nginx/conf.d/default.conf nginx


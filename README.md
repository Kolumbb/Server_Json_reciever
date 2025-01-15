# NestApi#Running container
  docker run -d \
  -p 8443:8443 \
  -v <user_defined_catalog_to_map>:/app/received_json \
  --name nest-api \
  nest-api

  #Curl example 
  curl -i -X POST http://localhost:8443/api -d '{"hello":"jello"}' -H "Content-Type: application/json"

  #Running interactive command line session inside container
  docker exec -it <container_name_or_id> /bin/bash

  #Forcing to stop container with running application
  docker kill <container_name_or_id>

  #Clearing process history
  docker rm <container_name_or_id>

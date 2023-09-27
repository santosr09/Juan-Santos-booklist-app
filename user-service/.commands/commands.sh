#=================================================
ELK

#ZIPKIN
java -jar zipkin-server-2.23.16-exec.jar

Dashboard:
http://localhost:9411

#ELASTICSEARCH
bin\elasticsearch.bat

Dashboard:
http://localhost:9200

#KIBANA
bin\kibana.bat

Dashboard:
http://localhost:5601

#LOGSTASH
bin\logstash -f simple-config.config

#=================================================

#copy a file from local to EC2 instance
scp -i ./uno-ceo.pem ./docker-compose.yaml ec2-user@ec2-44-195-232-229.compute-1.amazonaws.com:/home/ec2-user/


ssh -i "uno-ceo.pem" ec2-user@ec2-18-212-182-240.compute-1.amazonaws.com

scp -i ./uno-ceo.pem ./docker-compose.yaml ec2-user@ec2-18-212-182-240.compute-1.amazonaws.com:/home/ec2-user/

#SOLUTION TO ERROR:  Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock:
sudo chmod 666 /var/run/docker.sock

#=================================================
# DOCKER ON EC2
sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user

#ADD DOCKER USER TO USER GROUP TO AVOID USE SUDO
sudo usermod -a -G docker ec2-user

#START DOCKER SERVICE
sudo service docker start

#=================================================

#INSTALL DOCKER COMPOSE
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

#=================================================

#BUILD/CREATE A DOCKER IMAGE:
docker build --tag=user-service --force-rm=true .

#BEFORE PUSH AN IMAGE TO A REPOSITORY, SHOULD BE TAGGED
docker tag <IMAGE-ID> santosr09/user-service
#docker tag 248748ae188a santosr09/books-api-gateway

#PUSH AN IMAGE TO THE REPOSITORY:
docker push santosr09/user-service

#RUN A CONTAINER
docker run -d --network host -v /home/ec2-user/api-logs:/. santosr09/user-service

docker run -d --network host santosr09/user-service

#CONNECT TO EC2 INSTANCE
ssh -i uno-ceo.pem ec2-user@ec2-54-144-224-76.compute-1.amazonaws.com

#=========================================

#ERROR: Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/info": dial unix /var/run/docker.sock: connect: permission denied
#errors pretty printing info

#SOLUTION TO ERROR:  Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock:
sudo chmod 666 /var/run/docker.sock

#ERROR: Failed to start docker.service: The name org.freedesktop.PolicyKit1 was not provided by any .service files
#See system logs and 'systemctl status docker.service' for details.

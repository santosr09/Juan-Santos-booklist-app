#=================================================

sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user

#ADD DOCKER USER TO USER GROUP TO AVOID USE SUDO
sudo usermod -a -G docker ec2-user

#START DOCKER SERVICE
service docker start

#=================================================

#BUILD/CREATE A DOCKER IMAGE:
docker build --tag=book-discovery-service --force-rm=true .

#BEFORE PUSH AN IMAGE TO A REPOSITORY, SHOULD BE TAGGED
docker tag <IMAGE-ID> santosr09/book-discovery-service
#docker tag 8a6a99619b02 santosr09/books_discovery-service

#PUSH AN IMAGE TO THE REPOSITORY:
docker push santosr09/book-discovery-service

#RUN A CONTAINER
docker run -dp 8010:8010 santosr09/book-discovery-service

#CONNECT TO EC2 INSTANCE
ssh -i uno-ceo.pem ec2-user@ec2-54-144-224-76.compute-1.amazonaws.com

#=========================================

#ERROR: Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/info": dial unix /var/run/docker.sock: connect: permission denied
#errors pretty printing info

#SOLUTION TO ERROR:
sudo chmod 666 /var/run/docker.sock

#ERROR: Failed to start docker.service: The name org.freedesktop.PolicyKit1 was not provided by any .service files
#See system logs and 'systemctl status docker.service' for details.

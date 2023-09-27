1 CONNECT TO EC2 INSTANCE
chmod 400 book-app-keyPair.pem

=============================================
In PowerShell execute these commands:

$path = ".\book-app-keyPair.pem"
# Reset to remove explicit permissions
icacls.exe $path /reset
# Give current user explicit read-permission
icacls.exe $path /GRANT:R "$($env:USERNAME):(R)"
# Disable inheritance and remove inherited permissions
icacls.exe $path /inheritance:r

=============================================
#INSIDE THE EC2 INSTANCE EXECUTE:

#Install and start Docker on AWS EC2
sudo yum update -y
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user

# Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/containers/json": dial unix /var/run/docker.sock: connect: permission denied
sudo chmod 666 /var/run/docker.sock


#INSTALL DOCKER COMPOSE:
# https://docs.docker.com/compose/install/
# 1 Run this command to download the current stable release of Docker Compose:
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 2 Apply executable permissions to the binary:
sudo chmod +x /usr/local/bin/docker-compose

#COPY THE DOCKER COMPOSE FILE FROM HOST TO ec2-user folder
scp -i "C:\dev\test\keypairs\book-app-keyPair.pem" "C:\dev\github\Juan-Santos-booklist-app\user-service\docker-compose.yaml" ec2-user@ec2-3-144-239-98.us-east-2.compute.amazonaws.com:/home/ec2-user/

scp -i "C:\dev\test\keypairs\book-app-keyPair.pem" "C:\dev\github\Juan-Santos-booklist-app\user-service\install-docker.sh" ec2-user@ec2-3-144-239-98.us-east-2.compute.amazonaws.com:/home/ec2-user/

#*********  CONNECT TO EC2 INSTANCE:  *************
#**************************************************

ssh -i "C:\dev\test\keypairs\book-app-keyPair.pem" ec2-user@ec2-3-144-239-98.us-east-2.compute.amazonaws.com

#**************************************************

#LOGIN IN DOCKER-HUB (use Access token)
docker login -u santosr09

c3953a57-f2e6-4016-b8ed-579a8f146cda

#RUN DOCKER COMPOSE
docker-compose -f docker-compose.yaml up


# game-server

build
mvn install
sudo docker build -t game-server .
sudo docker run -itd -p 8080:8080  game-server
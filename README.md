### dependencies
Download and untar JDK 15 (https://jdk.java.net/15/) 

### environment
```
export JAVA_HOME=~/Downloads/jdk-15.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
```
#### build
```
mvn clean install
```
### start redis
```
docker-compose up -d
```
### start server
```
./run.sh
```
NOTE: check your ulimits and set them to high values before running client or `ab`
### start client
```
./run-client.sh
```
### simple test 
```
ab -n 10000 -c 5 http://localhost:9090/
```
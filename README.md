#Put a container under some pressure
This is a simple app to put disk load on something (CloudFoundry in my case).

```
export WRITERS=50
java -jar target/diskwriter-0.0.1-SNAPSHOT.jar
````

`WRITERS` causes the app to spawn a that many threads. Each thread writes a 1MB file over and over again. 


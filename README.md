#### Requirements
* Java 21
* Maven


## Running the application locally

#### Step 1: 
Clone the repository
#### Step 2: 
By terminal, access the application root folder
#### Step 3:
Create a new file called .env and add this content:

```properties
SERVER_PORT=8080
POSTGRES_DB=1_global_devices_api_db
POSTGRES_USER=root
POSTGRES_PASSWORD=root
POSTGRES_PORT=5432
FLYWAY_SCHEMAS=public
```

#### Step 4: 
Make sure that the TCP port 8080 isn't in use and run the command below:
```
docker compose up --build -d
```

#### Step 5: 
Check the application running and the application documentation on http://localhost:8080/swagger-ui/index.html

## Running the application tests
By terminal, access the application root folder and run the command:

```
mvn clean package
```




## Curls:

#### Create a new Device

Possible values to state field:


| State     | Description |
|-----------|:-----------:|
| AVAILABLE |  The device is available for use.  |
| IN_USE    |  The device is currently in use.  |
| INACTIVE  | The device is inactive or out of operation. |

```
curl --location 'http://localhost:8080/devices' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Smart Sensor 021",
    "brand": "Kualit",
    "state": "INACTIVE"
}'
```

#### Find Device by ID

```
curl --location 'http://localhost:8080/devices/1'
```

#### Filter devices by brand

```
curl --location 'http://localhost:8080/devices?brand=Kualit'
```

#### Filter devices by state
```
curl --location 'http://localhost:8080/devices?state=IN_USE'
```

#### Update Device partially
```
curl --location --request PATCH 'http://localhost:8080/devices/3' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Smart Sensor 022"
}'
```

#### Update device totally
```
curl --location --request PUT 'http://localhost:8080/devices/3' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Smart Sensor X20",
    "brand": "KualitTec",
    "state": "AVAILABLE"
}'
```

#### Delete device
```
curl --location --request DELETE 'http://localhost:8080/devices/3'
```

#### Important:

1. All the curl commands above are using the default port 8080.
If you want to use another port, you need to change the .env file. <br>
2. The postman collection is available at the  folder ./postman
3. 
### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.7/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.


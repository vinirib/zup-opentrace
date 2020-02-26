# Zup OpenTrace

An spring boot application with Jaegger OpenTracing implementation. This example is an practice of tutorial of zuppers: 
https://www.zup.com.br/blog/microsservico-com-opentrace-em-kotlin
This example will send an Http Request to http://viacep.com.br to find an brazillian zipcode address and send all trace to Jaegger.

## Getting Started

Clone this project, you can open on your favorite IDE or run with Docker Compose, the files is on the root directory.

### Prerequisites

#### To run in container
 - Docker
 - Docker Compose
 
#### To run in develop
  - Openjdk11
  - maven

### Installing

#### Running on docker container

Open terminal on root directory, and send command:

```
docker-compose --build
```

At the first time will be take a while.

After that, send command:

```
docker-compose up
```


## Testing Endpoint

Send a Http Post request to the address:

Example:
http://localhost/address/38400386/json

The endpoint is composed by
```
http://localhost/address/{brazillian-zip-code}/{format}
```
### Seeing the tracing

Access the address

http://localhost:16686/


### Implementation

To add Jaegger to an spring boot application is easly, add this dependencies on the project:

```
    <dependency>
        <groupId>io.opentracing.contrib</groupId>
        <artifactId>opentracing-spring-jaeger-web-starter</artifactId>
        <version>2.0.3</version>
    </dependency>
    <dependency>
        <groupId>io.github.openfeign.opentracing</groupId>
        <artifactId>feign-opentracing</artifactId>
        <version>0.3.0</version>
    </dependency>
```

After this add this properties:

``` 
opentracing.jaeger.service-name=zipcode-finder
opentracing.jaeger.enabled=TRUE
opentracing.jaeger.udp-sender.host=localhost
```
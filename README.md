# Reactive Programming

### What is Reactive programming ?
In plain terms reactive programming is about non-blocking applications that are asynchronous and event-driven and require a small number of threads to scale vertically (i.e. within the JVM) rather than horizontally (i.e. through clustering).

Dont worry if you didn't get  it. Will describe it with examples. keep reading....

### Why we need Reactive programming?

Lets first discuss how website loading works. When ever we hit some URL say facebook. It brings the basic page and slowely start adding extra information like images,videos, notifications etc. So what exactly happens.

When we hit the website in the background it hits several asyncronous services or API to get the indivisual information. Based on the response of the API the data loads on screen. Thats why we can see the text first and the images and videos after that on website.

Why are we using asyncronous service here ? 
Synchronous means that you call a web service (or function) and wait until it returns - all other code execution and user interaction is stopped until the call returns .This increases the loading time of the website and for some time website looks in hung state. So we use asyncronous services. Asynchronous means that you do not halt all other operations while waiting for the web service call to return.It increases the efficiency but still there is scope of improvement.

We implemented Asyncronous service in the UI but the response of the service is still syncronous means all the result comes in 1 response like if we call a getUser service.It will return all the users in 1 response.

Example if we want to show user list on the screen. If the users are less in number then its ok, but if users are in thousands then it decreases the efficiency.

Thats where ***Java reactive programming (RxJava)*** comes into picture. It will return the response in chunks. It will drastrically increases the speed of a service.

### Spring webflux module

Spring Framework 5 includes a new spring-webflux module. We will acheive the asyncronous services using spring webflux. It exposes 2 major API types ***Flux and Mono***. These API works on the concept of subscriber and publisher. Flux and Mono are publishers.


### Difference between Flux and Mono
Mono can publish almost 1 result i.e either 0 or 1 where as a flux can publish 0 to infinite results.

lets discuss them 1 by 1

### Dependencies

```java
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>
		<dependency>
			<groupId>io.reactivex.rxjava2</groupId>
			<artifactId>rxjava</artifactId>
			<version>2.2.15</version>
		</dependency>
```

### Creating Flux

#### 1. Flux.just()
Use this method when we have to pass comma seperated values.

```java
Flux<Integer> primaryFlux = Flux.just(1, 2, 3, 4);
```

#### 2. Flux.fromIterable()
This method is used when we have instance of iterable like list, set.

```java
 List<String> list = Arrays.asList("foo", "bar", "foobar");
 Flux<String> stringFlux = Flux.fromIterable(list);
```

#### 3. Flux.range()
This method is used to create a publisher of numbers.

```java
 Flux.range(4,4);      // it will give 4,5,6,7 as output
```

#### 4. Flux.empty()
This method is used to create a empty flux
```java
 Flux<Integer> empty = Flux.empty();
```

#### 5. Flux.error()
This method is used to publish some error response.
```java
 Flux.error(new Exception("Something went wrong"));
```

#### 6. Flux.duration()
This method emita long values staring from 0 in given duration
```java
 Flux.interval(Duration.ofMillis(1000));
```


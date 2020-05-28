# Reactive Programming

### What is Reactive programming ?
In plain terms reactive programming is about non-blocking applications that are asynchronous and event-driven and require a small number of threads to scale vertically (i.e. within the JVM) rather than horizontally (i.e. through clustering).

Dont worry if you didn't get  it. keep reading....

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

## Flux
Flux can publish 0 or more values. It can of any type.

### Flux creation methods

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
These are the most common used flux creation methods. There are few more like fromArray, fromStream, create etc

### Mono creation methods

#### Mono.just()
Just like we have this method in Flux we have just() method in *Mono*. The only difference is we can pass only 1 value to it
```java
Mono<Integer> mono = Mono.just(1);
```

#### Mono.empty()
Same as Flux this method is used to create empty Mono. Usually comes when we dont want to return null, we can return empty Mono.
```java
Mono<Object> empty = Mono.empty();
```
#### Mono.error()
It will publish exception.
```java
Mono<Object> exception = Mono.error(new Exception("Something went wrong"));
```

#### Mono.from()
This is used to create a new publisher from existing one
```java
 Flux<Integer> primaryFlux = Flux.just(1, 2, 3, 4);
 Mono<Integer> mono = Mono.from(primaryFlux);
```

#### Mono.justOrEmpty()
This method can take Optional value or direct value. 
In case of optional value it will return value if Optional is not null else only emit onComplete
In case of direct value it will return the value or emit onComplete if null is passed
```java
 Mono<Object> justOrEmptyMono1 = Mono.justOrEmpty(Optional.ofNullable(null));
 Mono<Integer> justOrEmptyMono2 = Mono.justOrEmpty(Optional.ofNullable(1));
 Mono<Integer> justOrEmptyMono3 = Mono.justOrEmpty(1);
 Mono<Object> justOrEmptyMono4 = Mono.justOrEmpty(null);
```
There are many more methods to create Mono like *Mono.fromCallable(), Mono.fromRunnable(), Mono.fromFuture() etc.

### Subsribing publishers

A pulisher can only publish value when some one subscribe to it.When we subscribe to any publishe, we can get 1 of three results 
1 response
2 error
3 complete indication

And a subscriber has 3 methods to handle these results

 - onNext()
 - onError()
 - onConplete()

InNext() method is called when any success response emits from the publisher
OnError() method is called when any error response emits from the publisher
onComplete() method get called in the end when there is no more values left to emit. Its a kind of indicator to tell subscriber that subscription is completed.

```java
flux.subscribe(
onNext(),
onError(),
onComplete()
);
```

 ***OnError() and onComplete() are terminator methods means once they get called, the subscription ends.***
 
```java
        Flux<Integer> integerFlux = Flux.just(1,2,3,4);
        integerFlux.subscribe(
                (x) -> System.out.println(x),
                error -> System.out.println("Error : " + error),
                () -> System.out.println("Subscription completed")
        );
```
In the above example onNext() method will be called 4 times and onComplete() once. There is no error thats why onError will not get called.

```java
Flux<Object> primaryFlux = Flux.just(1, 2, 3, 4, new Exception("Something went wrong"));
primaryFlux.subscribe(
 		(x) -> System.out.println(x),
                error -> System.out.println("Error : " + error),
                () -> System.out.println("Subscription completed")
);
```
In this example onNext() will get called 4 times and onError() will get called once. Subscription got cancelled after onError() method so onComplete() will not get called.

So from the above example we can say that only one of onError() and onComplete() can get called in any subscription.

### Intermittent functions 
These are the functions which takes the values from the publisher and perform some functions on it before passing it to subscriber.

Lets discuss few of them.

#### 1. map
This method is used to transform the values from publisher (Flux or mono) before passing it to subscriber

```java
Flux<Integer> fluxWithError = Flux.range(11, 4)
	.map(x -> {
	    if (x < 40) {
		return x * x;
	    } else {
		throw new RuntimeException("Value too large" + x);
	    }
	});
fluxWithError.subscribe(
	System.out::println,
	error -> System.out.println("Error : " + error),
	() -> System.out.println("Subscription completed")
);

// Output :
//        121
//        144
//        169
//        196
//        Subscription completed
```

#### 1. buffer
We can use it in 2 ways : 1st with buffer size and 2nd with time duration

In case of buffer size we can set the exact values which we want to emit at 1 time.
```java
Flux<Integer> integerFlux = Flux.just(1,2,3,4);
integerFlux
	.buffer(2)
	.subscribe(x-> System.out.println(x));   // [[1, 2], [3, 4]]
```

Here we defined buffer size as 2. So it keeps adding values in buffer and only emit when it reached the buffer size.

In case of buffer duration, publisher publishes values in every specified time duration
```java
Flux<Integer> integerFlux = Flux.just(1,2,3,4);
integerFlux
	.buffer(Duration.ofMillis(5000))
	.subscribe(x-> System.out.println(x));  // [1,2,3,4]
```
It will emit all the values in 1 go because before 5000 ms it added all the values in buffer. If suppose we are getting these values from some slow database and able to get only 2 values in 5000 ms then it will also publish values like
```java
// [[1, 2], [3, 4]]
```

#### 2. doOnEach()
This method is used to check all the values of publisher before passing it to the subscriber

```java
Flux.just(1,2,3,4)
	.doOnEach(x -> System.out.println(x.get()))
	.subscribe(x -> System.out.println("subscriber value" + x));
```


Output :
```java
1
subscriber value1
2
subscriber value2
3
subscriber value3
4
subscriber value4
null              // In case when OnComplete runs the value of x is 0 is printed when we did x.get()
```

with this method we can check which all method will get called and how many times 

```java
Flux.just(1,2,3,4)
	.doOnEach(x -> System.out.println(x.getType()))
	.subscribe(x -> System.out.println("subscriber value" + x));

```

output  :
```java
onNext
subscriber value1
onNext
subscriber value2
onNext
subscriber value3
onNext
subscriber value4
onComplete
```

#### 3. log()
This method is used for the logging purpose

```java
Flux.just(1,2,3,4)
	.log()
	.subscribe(x -> System.out.println("subscriber value" + x));
```
output on console :
```java
2020-05-28 20:56:43.767  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
2020-05-28 20:56:43.768  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | request(unbounded)
2020-05-28 20:56:43.768  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | onNext(1)
subscriber value1
2020-05-28 20:56:43.768  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | onNext(2)
subscriber value2
2020-05-28 20:56:43.768  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | onNext(3)
subscriber value3
2020-05-28 20:56:43.768  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | onNext(4)
subscriber value4
2020-05-28 20:56:43.768  INFO 10658 --- [or-http-epoll-2] reactor.Flux.Array.2                     : | onComplete()
```
It will log all the methods called in subscriber with the order in which they get called.

#### 4. delayElement()
This method is used to delay the emition of value from the publisher for the given time
```java
Flux.just(1,2,3,4)
	.delayElements(Duration.ofMillis(5000))
	.subscribe(x -> System.out.println("subscriber value" + x));
```
It will print 1 value on console 4 times in every 5 sec 

#### 5. subscribeOn()
By default the whole subscription process works in the main thread which subscribes to the publisher. We can change that using subscribeOn() method
```java
Flux.just(1,2,3,4)
	.subscribeOn(Schedulers.parallel())
	.subscribe(x -> System.out.println("subscriber value" + x));
```

#### 6. blockFirst()
This method subscribes to the flux and returns the first element or null if no value is present. This is usually used for testing
```java
AtomicLong cancelCount = new AtomicLong();
  Flux.range(1, 10)
    .doOnCancel(cancelCount::incrementAndGet)
    .blockFirst();
  assertThat(cancelCount.get()).isEqualTo(1);
  
```


#### 7. blockLast()
This method subscribes to the flux and returns the last element or null if no value is present. This is also usually used for testing

```java
AtomicLong cancelCount = new AtomicLong();
  Flux.range(1, 10)
    .doOnCancel(cancelCount::incrementAndGet)
    .blockFirst();
  assertThat(cancelCount.get()).isEqualTo(10);
```

There are many more methods present in Flux and Mono. For whole list you can refer to 

https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html

https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html

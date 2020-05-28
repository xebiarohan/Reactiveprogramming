package com.reactiveprogramming.reactiveprogramming.observsables;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/observables")
public class FluxExamples {

    @GetMapping("/flux")
    public ResponseEntity<Void> fluxExample() {
        List<String> list = Arrays.asList("foo", "bar", "foobar");

        // Flux.just
        Flux<Object> primaryFlux = Flux.just(1, 2, 3, 4, new Exception("Something went wrong"));
        primaryFlux.subscribe(val -> System.out.println(val));

        Mono<Integer> just = Mono.just(1);

        Mono<Object> empty = Mono.empty();

        Mono<Object> exception = Mono.error(new Exception("Something went wrong"));

        Mono<Object> from = Mono.from(primaryFlux);

        Mono<Object> justOrEmptyMono1 = Mono.justOrEmpty(Optional.ofNullable(null));
        Mono<Integer> justOrEmptyMono2 = Mono.justOrEmpty(Optional.ofNullable(1));
        Mono<Integer> justOrEmptyMono3 = Mono.justOrEmpty(1);
        Mono<Object> justOrEmptyMono4 = Mono.justOrEmpty(null);


        // Flux error
        Flux<Object> errorFLux = Flux.error(new Exception("Something went wrong"));

        // Flux duration
        Flux.interval(Duration.ofMillis(1000));

        // Flux.fromIterable example
        Flux<String> stringFlux = Flux.fromIterable(list);
        List<String> strings = new ArrayList<>();
        stringFlux.subscribe(x -> strings.add(x));

        // Flux.range
        Flux.range(4,4);      // it will give 4,5,6,7 as output

        // Subscription Example  onNext value only
        Flux<Integer> range = Flux.range(1, 4);
        range.subscribe(System.out::println);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/flux/buffer")
    public ResponseEntity<Flux> bufferFlux() {
        // Empty flux
        Flux<Integer> integerFlux = Flux.range(1,10);


        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(integerFlux.buffer(2).buffer(Duration.ofMillis(5000)));
    }

    @GetMapping("/flux/withError")
    public ResponseEntity<Void> nextWithError() {
        // onNext and onError condition
        Flux<Integer> fluxWithError = Flux.range(11, 15)
                .map(x -> {
                    if (x < 13) {
                        return x * x;
                    } else {
                        throw new RuntimeException("Value too large " + x);
                    }
                });
        fluxWithError.subscribe(
                System.out::println,
                error -> System.out.println("Error : " + error)
        );

        //Output
        // 121
        // 144
        // Error : java.lang.RuntimeException: Value too small 11
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/flux/onComplete")
    public ResponseEntity<Void> nextWithErrorAndComplete() {
        // onNext and onError condition
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

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/connectableFlux")
    public ResponseEntity<Void> connectableFlux() {

        ConnectableFlux<Object> publish = Flux.create(flux -> {
            while (true) {
                flux.next(System.currentTimeMillis());
            }
        }).publish();
        publish.subscribe(x -> System.out.println("Before connecting " + x));
        publish.connect();
        publish.subscribe(x -> System.out.println("After connecting " + x));  //this will never get executed
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/fluxResponse")
    public ResponseEntity getFluxResponse() {
//        Flux<String> alpha = Flux.just("alpha")
//                .map(val -> val);
//
//        return alpha;

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just("test"));
    }


    @GetMapping(value = "/fluxBuffer",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<String>> getFluxWithBuffer() {
        List<String> list = Arrays.asList("foo", "bar", "foobar");
        return Flux.fromIterable(list)
                .map(String::toUpperCase)
                .buffer(2);
    }

    @GetMapping("/doOnNextExample")
    public ResponseEntity<String> doOnNext() {
        Flux.just(1,2,3,4)
                .doOnEach(x -> System.out.println(x.getType()))
                .subscribe(x -> System.out.println("subscriber value" + x));

        return ResponseEntity.ok("test");
    }

    @GetMapping("/logExample")
    public ResponseEntity<String> text() {
        Flux.just(1,2,3,4)
                .log()
                .subscribe(x -> System.out.println("subscriber value" + x));

        return ResponseEntity.ok("test");
    }

    @GetMapping("/delayExample")
    public ResponseEntity<String> delayElement() {
        Flux.just(1,2,3,4)
                .delayElements(Duration.ofMillis(5000))
                .subscribe(x -> System.out.println("subscriber value" + x));

        return ResponseEntity.ok("test");
    }


    @GetMapping("/subscribeOnExample")
    public ResponseEntity<String> subscribeOn() {
        Flux.just(1,2,3,4)
                .subscribeOn(Schedulers.parallel())
                .subscribe(x -> System.out.println("subscriber value" + x));

        return ResponseEntity.ok("test");
    }

    @GetMapping("/timeout")
    public ResponseEntity<String> timeout() {
        Flux.just(1,2,3,4)
                .timeout(Duration.ofMillis(1000))
                .subscribe(x -> System.out.println("subscriber value" + x));

        return ResponseEntity.ok("test");
    }

    @GetMapping("/bufferUntilChanged")
    public ResponseEntity<String> bufferUntilChanged() {
        Flux.just(1,1,2,2,3,4)
                .bufferUntilChanged()
                .subscribe(x -> System.out.println("subscribed value" + x));

        return ResponseEntity.ok("test");
    }

    @GetMapping("/cache")
    public ResponseEntity<String> cache() {
        Flux<Integer> flux = Flux.just(1, 1, 2, 2, 3, 4)
                .cache(2);

        flux.subscribe(System.out::println);

        flux.subscribe(System.out::println);
        return ResponseEntity.ok("test");
    }


    // Cancelling a subscribe() with Its Disposable
}

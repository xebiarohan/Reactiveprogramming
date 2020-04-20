package com.reactiveprogramming.reactiveprogramming.observsables;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/observables")
public class FluxExamples {

    @GetMapping("/flux")
    public ResponseEntity<Void> fluxExample() {
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");

        // Flux.just
        Flux<Integer> primaryFlux = Flux.just(1, 2, 3, 4);
        primaryFlux.subscribe(val -> System.out.println(val));


        // Flux.fromIterable example
        Flux<String> stringFlux = Flux.fromIterable(iterable);
        List<String> strings = new ArrayList<>();
        stringFlux.subscribe(x -> strings.add(x));

        // Flux.range
        Flux.range(4,4);      // it will give 4,5,6,7 as output

        // Subscription Example  onNext value only
        Flux<Integer> range = Flux.range(1, 4);
        range.subscribe(System.out::println);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
    // Cancelling a subscribe() with Its Disposable
}

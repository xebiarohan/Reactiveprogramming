package com.reactiveprogramming.reactiveprogramming.observsables;

import io.reactivex.Observable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/observables")
public class ObservablesExamples {

    @GetMapping("/examples")
    public ResponseEntity<Void> getFirstObservable() {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 45);

        // Using just
        Observable<String> observable = Observable.just("Hello");
        observable.subscribe(value -> System.out.println(value));


        // Using fromArray
        Observable<String> stringObservable = Observable.fromArray(letters);
        stringObservable
                .map(String::toUpperCase)
                .subscribe(s -> System.out.println(s));


        // Using Collection
        Observable<Integer> integerObservable = Observable.fromIterable(list);
        integerObservable
                .filter(x -> x > 2)
                .subscribe(x -> System.out.println(x));


        // scan operator  similar to reduce method
        Observable.fromIterable(list)
                .scan(0, (x, y) -> x + y)
                .subscribe(value -> System.out.println(value));


        // GroupBy
        List<Integer> evenNumbers = new ArrayList<>();
        List<Integer> oddNumbers = new ArrayList<>();
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .groupBy(i -> 0 == (i % 2) ? "EVEN" : "ODD")
                .subscribe(group -> {
                    group.subscribe(number -> {
                        if (group.getKey().equals("EVEN")) {
                            System.out.println("EVEN number" + number);
                            evenNumbers.add(number);
                        } else {
                            oddNumbers.add(number);
                        }
                    });
                });
        evenNumbers.stream().forEach(x -> System.out.println("EVEN NUMBER " + x));
        oddNumbers.stream().forEach(x -> System.out.println("ODD NUMBER "+ x));


        //  Empty Observable
                Observable.empty();

        //  ConditionalOperator
        Observable.empty()
                .defaultIfEmpty(2)
                .subscribe(x -> System.out.println(x));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

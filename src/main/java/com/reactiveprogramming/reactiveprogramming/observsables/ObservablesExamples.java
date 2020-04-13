package com.reactiveprogramming.reactiveprogramming.observsables;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/observables")
public class ObservablesExamples {

    @GetMapping("/examples")
    public ResponseEntity<Void> getFirstObservable() throws InterruptedException {
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

        //  ConditionalOperator   defaultIfEmpty
        Observable.empty()
                .defaultIfEmpty(2)
                .subscribe(x -> System.out.println(x));


        // getting first Value conditionally from observable
        Observable.fromArray(letters)
                .first("default value")
                .subscribe(x -> System.out.println(x));


        //  If value comes then print otherwise nothing will print
        Observable.empty()
                .firstElement()
                .subscribe(x -> System.out.println(x));

        // takeWhile, Contain, SkipWhile, SkipUntil, TakeUntil

        Observable.fromIterable(list)
                .takeWhile(i -> i> 2)
                .subscribe(x -> System.out.println(x));



        // Connectable Operator
        //A ConnectableObservable resembles an ordinary Observable, except that it doesn't begin
        // emitting items when it is subscribed to, but only when the connect operator is applied to it

        ConnectableObservable<Long> connectable
                = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(5)
                .publish();
        connectable.subscribe(i -> System.out.println(i));

        connectable.connect();
        Thread.sleep(500);

        // Subject  acts as a Observable and subscriber
        PublishSubject<Integer> subject = PublishSubject.create();
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(x -> System.out.println("value of first Observable " + x));
        subject.onNext(10);
        subject.subscribe(y -> System.out.println("value of second Observable " + y));


        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

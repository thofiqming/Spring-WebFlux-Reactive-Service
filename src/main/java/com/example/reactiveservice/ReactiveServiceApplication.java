package com.example.reactiveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Optional;

@SpringBootApplication
public class ReactiveServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveServiceApplication.class, args);
    }

}

@RestController
@RequestMapping("/test/v1")
@CrossOrigin
class Controller {

    @GetMapping("/values")
    public Flux<Integer> values() {
        return Flux.generate(
                () -> Tuples.of(0, 1),
                (state, sink) -> {
                    System.out.println(Thread.currentThread().getName());
                    sink.next(state.getT1());
                    return Tuples.of(state.getT2(), state.getT1() + state.getT2());
                }
        );
    }
}

@RestController
@RequestMapping("/shipment/v1")
@CrossOrigin
class ShipmentController {

    @RequestMapping(value="/values", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> values() {
        System.out.println("request received from front end ");
        return Flux
                .interval(Duration.ofSeconds(1))
                .take(20)
                .map(aLong -> String.format("event%d", aLong));
    }

    @GetMapping("/test")
    public ResponseEntity<String> values1() {
        return ResponseEntity.of(Optional.of("Test Result"));
    }
}

package training;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GatherersDemo {

    static void main() {
        var windows = IntStream.range(0, 10)
                .boxed()
                .gather(Gatherers.windowFixed(3))
                .toList();
        System.out.println(windows);

        Gatherer<Object, Employee.EmployeeBuilder, Employee> gatherer = Gatherer.ofSequential(
                Employee.EmployeeBuilder::new,
                Gatherer.Integrator.of(
                        (builder, element, downstream) -> {
                            if (element instanceof Long id) {
                                builder.id(id);
                            } else if (element instanceof String name) {
                                builder.name(name);
                                downstream.push(builder.build());
                            }
                            return true;
                        })
                );



        List<Employee> employees = Stream.of(1L, "John Doe", 2L, "Jane Doe")
                .gather(gatherer).toList();
        System.out.println(employees);

    }
}

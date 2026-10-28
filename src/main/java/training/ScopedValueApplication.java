package training;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class ScopedValueApplication {

    private final Random random = new Random();
    private final ScopedValue<String> requestId = ScopedValue.newInstance();


    static void main() {
        new ScopedValueApplication().run();
    }
    @SneakyThrows
    private void run() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.invokeAll(IntStream.range(0, 3)
                    .mapToObj(i -> Executors.callable(this::processOrder))
                    .toList()
            );
        }
    }

    @SneakyThrows
    private void processOrder() {
        String id = UUID.randomUUID().toString();
        System.out.println("process: %s %s".formatted(Thread.currentThread().getName(), id));
        Thread.sleep(random.nextInt(1000));
        ScopedValue.where(requestId, id).run(this::saveOrder);
    }
    private void saveOrder() {
        String id = requestId.get();
        System.out.println("save: %s %s".formatted(Thread.currentThread().getName(), id));
    }
}

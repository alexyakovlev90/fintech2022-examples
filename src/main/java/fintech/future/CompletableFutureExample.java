package fintech.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    private final Source source;
    private final Destination dest;

    public CompletableFutureExample(Source source, Destination dest) {
        this.source = source;
        this.dest = dest;
    }

    public static void main(String[] args) {
        Source source = new Source();
        Destination dest = new Destination();

        CompletableFutureExample example = new CompletableFutureExample(source, dest);

        CompletableFuture<Data> future = example.readData();
        CompletableFuture<Data> processed1 = future.thenApplyAsync(example::processData1);
        CompletableFuture<Data> processed2 = future.thenApplyAsync(example::processData2);

        var result = processed1.thenCombine(processed2, example::mergeData)
                .thenAccept(example::writeData);

        result.join();
    }

    public CompletableFuture<Data> readData() {
        return CompletableFuture.supplyAsync(source::readFromSource);
    }

    public Data processData1(Data data) {
        System.out.println("1 – Start processing data");
        sleep(2000);
        System.out.println("1 – End processing data");
        return new Data("process1");
    }

    public Data processData2(Data seed) {
        System.out.println("2 – Start processing data");
        sleep(1000);
        System.out.println("2 – End processing data");
        return new Data("process2");
    }

    public Data mergeData(Data data1, Data data2) {
        // do some merge logic
        System.out.println("Merging data");
        return new Data("Merge");
    }

    public void writeData(Data data) {
        dest.publishResult(data);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("Never happens");
        }
    }

    private static class Source {
        Data readFromSource() {
            return new Data("Source");
        }
    }

    private record Data(String processorName) {
    }

    private static class Destination {
        void publishResult(Data data) {
            System.out.println("Publish data in queue topic: " + data.toString());
        }
    }
}

package fintech.forkjoin;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForkJoinExample {

    public static void main(String[] args) {

        Node root = new NodeImpl(22);

        long start = System.currentTimeMillis();
        Long result = new ForkJoinPool()
                .invoke(new ValueSumCounter(root));
        long stop = System.currentTimeMillis();

        System.out.println("Result: " + result);
        System.out.println("Time count: " + (stop - start) + "ms");
    }

    public static class ValueSumCounter extends RecursiveTask<Long> {
        private final Node node;

        public ValueSumCounter(Node node) {
            this.node = node;
        }

        @Override
        protected Long compute() {
            long sum = node.getValue();
            List<ValueSumCounter> subTasks = new LinkedList<>();

            for (Node child : node.getChildren()) {
                ValueSumCounter task = new ValueSumCounter(child);
                task.fork(); // запустим асинхронно
                subTasks.add(task);
            }

            for (ValueSumCounter task : subTasks) {
                sum += task.join(); // дождёмся выполнения задачи и прибавим результат
            }

            return sum;
        }
    }

    public interface Node {
        Collection<Node> getChildren();

        long getValue();
    }

    public static class NodeImpl implements Node {

        List<Node> nodes;
        long value;

        public NodeImpl(int depth) {
            nodes = IntStream.rangeClosed(1, depth - 1)
                    .boxed()
                    .map(NodeImpl::new)
                    .collect(Collectors.toList());
            value = depth;
        }

        @Override
        public Collection<Node> getChildren() {
            return nodes;
        }

        @Override
        public long getValue() {
            return value;
        }
    }
}

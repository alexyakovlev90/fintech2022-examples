package fintech.forkjoin;

import fintech.forkjoin.Cache.ActiveOffersCache;
import fintech.forkjoin.Cache.MerchantCache;
import fintech.forkjoin.Cache.OfferCache;
import fintech.forkjoin.MerchantRepository.Merchant;
import fintech.forkjoin.OfferRepository.Offer;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CacheWarmUpService {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new ActiveOffersWarmUpAction());
        forkJoinPool.shutdown();
    }

    static class ActiveOffersWarmUpAction extends RecursiveAction {

        ActiveOffersCache activeOffersCache = new ActiveOffersCache();
        OfferCache offerCache = new OfferCache();
        MerchantCache merchantCache = new MerchantCache();

        @Override
        protected void compute() {
            List<ForkJoinTask<Offer>> offerTasks = activeOffersCache.getAll().stream()
                    .map(Offer::getId)
                    .map(offerId ->
                            new CacheWarmUpTask<>("OfferCache", () -> this.offerCache.get(offerId))
                    )
                    .map(RecursiveTask::fork)
                    .collect(Collectors.toList());

            List<ForkJoinTask<Merchant>> merchantTasks = offerTasks.stream()
                    .map(ForkJoinTask::join)
                    .map(offer ->
                            new CacheWarmUpTask<>("MerchantCache", () -> this.merchantCache.get(offer.getMerchantId()))
                    )
                    .map(RecursiveTask::fork)
                    .collect(Collectors.toList());

            merchantTasks.forEach(ForkJoinTask::join);
        }
    }

    static class CacheWarmUpTask<V> extends RecursiveTask<V> {

        private final String cacheName;
        private final Supplier<V> supplier;

        public CacheWarmUpTask(String cacheName, Supplier<V> supplier) {
            this.cacheName = cacheName;
            this.supplier = supplier;
        }

        @Override
        protected V compute() {
            System.out.println("Start cache warm up: " + cacheName);
            return supplier.get();
        }
    }
}

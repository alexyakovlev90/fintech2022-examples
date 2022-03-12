package fintech.forkjoin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class OfferRepository {

    public Offer getOffer(Long id) {
        return new Offer(id);
    }

    public List<Offer> getActiveOffers() {
        return LongStream.range(1, 1000)
                .boxed()
                .map(Offer::new)
                .collect(Collectors.toList());
    }


    public static class Offer {
        private final Long id;
        private final Long merchantId;

        public Offer(Long id) {
            this.id = id;
            merchantId = id;
        }

        public Long getId() {
            return id;
        }

        public Long getMerchantId() {
            return merchantId;
        }

        @Override
        public String toString() {
            return "Offer{" +
                    "id=" + id +
                    '}';
        }
    }
}

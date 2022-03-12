package fintech.forkjoin;

import fintech.forkjoin.MerchantRepository.Merchant;
import fintech.forkjoin.OfferRepository.Offer;

import java.util.List;

public interface Cache {

    class OfferCache implements Cache {

        private final OfferRepository offerRepository = new OfferRepository();

        public Offer get(Long id) {
            Offer offer = offerRepository.getOffer(id);
            System.out.println("Warm up Offer: " + offer);
            return offer;
        }
    }

    class ActiveOffersCache implements Cache {

        private final OfferRepository offerRepository = new OfferRepository();

        public List<Offer> getAll() {
            List<Offer> activeOffers = offerRepository.getActiveOffers();
            System.out.println("Warm up Active offers: " + activeOffers);
            return activeOffers;
        }
    }

    class MerchantCache implements Cache {

        private final MerchantRepository merchantRepository = new MerchantRepository();

        public Merchant get(Long id) {
            Merchant merchant = merchantRepository.getMerchant(id);
            System.out.println("Warm up Merchant:" + merchant);
            return merchant;
        }
    }
}

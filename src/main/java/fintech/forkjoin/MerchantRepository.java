package fintech.forkjoin;

public class MerchantRepository {

    public Merchant getMerchant(Long id) {
        return new Merchant(id);
    }

    public static class Merchant {
        private final Long id;

        public Merchant(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Merchant{" +
                    "id=" + id +
                    '}';
        }
    }
}

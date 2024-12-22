package D22;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class D22 {

    public static final int MODULO = 16777216;
    public static final int DEPTH = 2000;

    public void main() {
        List<Long> secrets = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("src/D22/input.txt"))) {
            while (sc.hasNextLong()) {
                secrets.add(sc.nextLong());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long sum = secrets.parallelStream()
                .map(secret -> generateSecretData(secret))
                .map(data -> data.secretHistory)
                .mapToLong(secretHistory -> secretHistory[DEPTH - 1])
                .sum();

        System.out.println(sum);

        long best = secrets.parallelStream()
                .map(secret -> generateSecretData(secret))
                .flatMap(data -> data.deltasToPrice.entrySet().stream())
                .collect(
                        Collectors.toConcurrentMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                Long::sum
                        )
                )
                .entrySet()
                .stream()
                //.peek(System.out::println)
                // get values
                .map(Map.Entry::getValue)
                .max(Long::compare)
                .orElse(0L);

        System.out.println(best);
    }

    private static SecretData generateSecretData(long secret) {
        Map<Sequence, Long> deltasToPrice = new HashMap<>();
        long[] deltas = new long[2001];
        long[] secretHistory = new long[2001];

        secretHistory[0] = secret;
        for (int i = 0; i < DEPTH + 1; i++) {
            long oldSecret = secret;

            secret = prune(mix(secret, secret * 64));
            secret = prune(mix(secret, secret / 32));
            secret = prune(mix(secret, secret * 2048));

            deltas[i] = secret % 10 - oldSecret % 10;
            secretHistory[i] = secret;

            if (i >= 4) {
                long[] sequenceDeltas = new long[4];
                System.arraycopy(deltas, i - 4, sequenceDeltas, 0, 4);
                Sequence sequence = new Sequence(sequenceDeltas);
                deltasToPrice.putIfAbsent(sequence, oldSecret % 10);
            }
        }

        return new SecretData(deltasToPrice, secretHistory);
    }

    private static long mix(long secret, long value) {
        return value ^ secret;
    }

    private static long prune(long secret) {
        return secret % MODULO;
    }

    private static class SecretData {
        Map<Sequence, Long> deltasToPrice;

        long[] secretHistory;

        SecretData(Map<Sequence, Long> deltasToPrice, long[] secretHistory) {
            this.deltasToPrice = deltasToPrice;
            this.secretHistory = secretHistory;
        }
    }

    private static class Sequence {
        @Override
        public String toString() {
            return Arrays.toString(deltas);
        }

        long[] deltas;

        Sequence(long[] deltas) {
            this.deltas = deltas;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(deltas);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Sequence sequence = (Sequence) obj;
            return Arrays.equals(deltas, sequence.deltas);
        }
    }
}

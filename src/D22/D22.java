package D22;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class D22 {

    public static final int MODULO = 16777216;

    public void main() {

        ArrayList<Long> secrets = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File("src/D22/input.txt"));
            while (sc.hasNextLong()) {
                secrets.add(sc.nextLong());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long sum = 0;
        for (var secret : secrets) {
            int depth = 2000;
            long generated = generate(secret, depth);
            System.out.println(generated);
            sum += generated;
        }
        System.out.println(sum);
    }

    long generate(long secret, int depth) {
        for (int i = 0; i < depth; i++) {
            long secretMultiplied = secret * 64;
            secret = mix(secret, secretMultiplied);
            secret = prune(secret);

            var secretDivided = secret / 32;
            secret = mix(secret, secretDivided);
            secret = prune(secret);

            secretMultiplied = secret * 2048;
            secret = mix(secret, secretMultiplied);
            secret = prune(secret);
        }

        return secret;
    }

    long mix(long secret, long value) {
        return value ^ secret;
    }

    long prune(long secret) {
        return secret % MODULO;
    }
}

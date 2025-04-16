import java.util.*;

public class RobotDeliver {
    public static void main(String[] args) {

        Thread threadLeader = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }

                    System.out.println("Лидер сейчас: " +
                            sizeToFreq.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getKey() + " (встретилось " +
                            sizeToFreq.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getValue() + " раз)");
                }
            }
        });
        threadLeader.start();

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String gr = generateRoute("RLRFR", 100);

                int repeatedCountChars = 0;
                for (int j = 0; j < gr.length(); j++) {
                    if (gr.charAt(j) == 'R') {
                        repeatedCountChars++;
                    }
                }

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(repeatedCountChars)) {
                        sizeToFreq.put(repeatedCountChars, sizeToFreq.get(repeatedCountChars) + 1);
                    } else {
                        sizeToFreq.put(repeatedCountChars, 1);
                    }
                    sizeToFreq.notify();
                }
            });
            thread.start();
        }

        threadLeader.interrupt();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
}

import java.util.*;
import java.util.stream.Collectors;

public class RobotDeliver {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
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
                }
            });
            threads.add(thread);
            thread.start();

        }

        for (Thread thread : threads) {
            thread.join();
        }

        Map<Integer, Integer> sorted = sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        int i = 0;
        for (Map.Entry<Integer, Integer> entry : sorted.entrySet()) {
            if (i == 0) {
                System.out.println("Самое частое количество повторений " + entry.getKey() + " (встретилось " + entry.getValue() + " раз)");
                System.out.println("Другие размеры:");
                i++;
            } else {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
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
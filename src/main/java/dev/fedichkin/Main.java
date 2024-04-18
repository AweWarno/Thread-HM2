package dev.fedichkin;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        final int count = 100;
        String[] texts = new String[100];
        for (int i = 0; i < count; i++) {
            texts[i] = generateRoute("RLRFR", count);
        }


        List<Thread> threads = new ArrayList<>();
        for (String text : texts) {
            Runnable logic = () -> {
                int maxCount = 0;
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == 'R') {
                        maxCount++;
                    }
                }

                synchronized (sizeToFreq) {
                    sizeToFreq.put(maxCount,
                            sizeToFreq.getOrDefault(maxCount, 0) + 1);
                }
            };

            Thread thread = new Thread(logic);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }

        List<Map.Entry<Integer, Integer>> list = sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue())
                .toList()
                .reversed();

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                System.out.printf("Самое частое количество повторений %d (встретилось %d раз) \n",
                        list.get(i).getKey(),
                        list.get(i).getValue());

                System.out.println("Другие размеры:");
            } else {
                System.out.printf("- %d (%d раз) \n",
                        list.get(i).getKey(),
                        list.get(i).getValue());
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
}
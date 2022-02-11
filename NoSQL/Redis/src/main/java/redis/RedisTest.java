package redis;


import java.util.*;

import static java.lang.System.out;

public class RedisTest {

    // Задержка
    private static final int SLEEP = 1000; // 1 секунда


    private static void log(int userId) {
        String log = String.format("На главной странице показываем пользователя %d", userId );
        out.println(log);
    }

    private static int changeOrdinary()
    {
        double rand = Math.random();
        if(rand < 0.1){
            return (new Random().nextInt(NUM_USERS - 1)) + 1;
        }
        return 0;
    }

    public static final int NUM_USERS = 20;

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();

            //Создание очереди в Redis
            for (int userId = 1; userId <= NUM_USERS; userId++) {
                redis.logPageVisit(userId);
                Thread.sleep(10);
            }

            //Бесконечный цикл отображения пользователей
            while (true) {
                int change = changeOrdinary();
                if(change != 0) {
                    out.println(">Пользователь " + change + " оплатил платную услугу");
                    log(change);
                    Thread.sleep(SLEEP);
                    redis.logPageVisit(change);
                } else {
                    int userId =  redis.calculateUserIdByRange(0);
                    log(userId);
                    Thread.sleep(SLEEP);
                    redis.logPageVisit(userId);
                }

            }
    }
}

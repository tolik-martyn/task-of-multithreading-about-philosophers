import java.util.concurrent.CountDownLatch;

import static java.lang.StringTemplate.STR;

/**
 * Пять безмолвных философов сидят вокруг круглого стола, перед каждым философом стоит тарелка спагетти.
 * Вилки лежат на столе между каждой парой ближайших философов.
 * Каждый философ может либо есть, либо размышлять.
 * Философ может есть только тогда, когда держит две вилки — взятую справа и слева.
 * Философ не может есть два раза подряд, не прервавшись на размышления (можно не учитывать)
 * Философ может взять только две вилки сразу, то есть обе вилки должны быть свободны
 * Каждый философ должен поесть три раза
 */
public class RoundTable extends Thread {

    private final int countOfPhilosophers;
    private final int countOfMeals;

    private final Fork[] forks;
    private final Philosopher[] philosophers;
    private final CountDownLatch cdl;

    public RoundTable(int countOfPhilosophers, int countOfMeals) {
        this.countOfPhilosophers = countOfPhilosophers;
        this.countOfMeals = countOfMeals;
        this.forks = new Fork[countOfPhilosophers];
        this.philosophers = new Philosopher[countOfPhilosophers];
        this.cdl = new CountDownLatch(countOfPhilosophers);
        initialize();
    }


    @Override
    public void run() {
        System.out.println("Философы готовы начать прием пищи!");
        try {
            for (Philosopher philosopher : philosophers) {
                philosopher.start();
            }
            cdl.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Философы закончили прием пищи!");

    }

    public synchronized boolean takeForks(Fork leftFork, Fork rightFork) {
        if (!leftFork.isUsing() && !rightFork.isUsing()) {
            leftFork.setUsing(true);
            rightFork.setUsing(true);
            return true;
        }
        return false;
    }

    public void putForks(Fork leftFork, Fork rightFork) {
        leftFork.setUsing(false);
        rightFork.setUsing(false);
    }

    private void initialize() {
        for (int i = 0; i < countOfPhilosophers; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < countOfPhilosophers; i++) {
            philosophers[i] = new Philosopher(STR."Philosopher-\{i + 1}", forks[i], forks[(i + 1) % countOfPhilosophers],
                    this, cdl, 2);
        }
    }

    public int getCountOfMeals() {
        return countOfMeals;
    }
}

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosopher extends Thread {

    private final String name;

    private final Fork leftFork;
    private final Fork rightFork;

    private final int maxCountOfMealsOfPhil;
    private int curCountOfMealsOfPhil;
    private int countOfMealsOfTable;


    private final Random random = new Random();

    private final RoundTable table;
    private final CountDownLatch cdl;

    public Philosopher(String name, Fork rightFork, Fork leftFork,
                       RoundTable table, CountDownLatch cdl, int maxCountOfMeals) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.table = table;
        this.maxCountOfMealsOfPhil = maxCountOfMeals;
        this.countOfMealsOfTable = table.getCountOfMeals();
        this.curCountOfMealsOfPhil = 0;
        this.cdl = cdl;
    }

    private void think() {
        System.out.println(STR."\{name} начал размышлять.");
        toWait(1, 4);
        System.out.println(STR."\{name} закончил размышлять.");
    }

    private void eat() {
        System.out.println(STR."\{name} проверяет, свободны или заняты "
                + STR."вилки \{leftFork.getId()} и \{rightFork.getId()}.");
        if (table.takeForks(leftFork, rightFork)) {
            System.out.println(STR."\{name} начинает есть и использует "
                    + STR."вилки \{leftFork.getId()} и \{rightFork.getId()}.");
            toWait(2, 3);
            System.out.println(STR."\{name} поел и кладет на стол "
                    + STR."вилки \{leftFork.getId()} и \{rightFork.getId()}.");
            curCountOfMealsOfPhil++;
            countOfMealsOfTable--;
            table.putForks(leftFork, rightFork);
        } else {
            System.out.println(STR."Вилки \{leftFork.getId()} и(или) \{rightFork.getId()} заняты.");
            think();
            eat();
        }
    }

    private void toWait(int from, int to) {
        try {
            Thread.sleep(random.nextInt(from, to) * 1000L);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (countOfMealsOfTable != 0) {
            if (curCountOfMealsOfPhil == maxCountOfMealsOfPhil) {
                System.out.println(STR."\{name} не может есть более \{maxCountOfMealsOfPhil} раз подряд.");
                curCountOfMealsOfPhil = 0;
                think();
            } else {
                eat();
            }
        }
        System.out.println(STR."\{name} наелся и больше не хочет.");
        cdl.countDown();
    }
}

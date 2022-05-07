package ch7.calculator;

public class RecCalculator implements Calculator {

    @Override
    public long factorial(long num) {
        if (num == 0)
            return 0;
        else
            return num * factorial(num - 1);
    }
}

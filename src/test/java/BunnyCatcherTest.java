import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.System;

import frc.team5115.Classes.Software.BunnyCatcher;

public class BunnyCatcherTest {
    final MockBunnyCatcher mock = new MockBunnyCatcher();
    final BunnyCatcher bunnyCatcher = new BunnyCatcher(mock);

    @Test
    void outputForAllDeltas() {
        for(int i = 0; i < 361; i++) {
            double speed = testTurnTowardsAngle(100, 100+(double)i);
            System.out.println("Delta: " + i + " | Speed: " + speed);
        }
    }

    @Test
    void testTurnTowardsAngle_BigPositive() {
        final double expected = +30;
        final double actual = testTurnTowardsAngle(0, 100);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTurnTowardsAngle_BigNegative() {
        final double expected = -30;
        final double actual = testTurnTowardsAngle(0, -100);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTurnTowardsAngle_SmallPositive() {
        final double expected = +10;
        final double actual = testTurnTowardsAngle(5, 15);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTurnTowardsAngle_SmallNegative() {
        final double expected = -10;
        final double actual = testTurnTowardsAngle(0, -10);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTurnTowardsAngle_SmallNegativeWrap() {
        final double expected = -13;
        final double actual = testTurnTowardsAngle(10, -3);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTurnTowardsAngle_SmallPositiveWrap() {
        final double expected = 16;
        final double actual = testTurnTowardsAngle(-5, 11);
        Assertions.assertEquals(expected, actual);
    }

    double testTurnTowardsAngle(double current, double setpoint) {
        mock.setPosition(current);
        bunnyCatcher.updateAngle();
        bunnyCatcher.turnTowardsAngle(setpoint, 0);
        return mock.getSpeed();
    }
}

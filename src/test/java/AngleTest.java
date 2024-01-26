import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import frc.team5115.Classes.Accessory.Angle;

public class AngleTest {
    @Test
    void testRollover_easy_10() {
        double actual = Angle.rollover(10, 0);
        double expected = 10;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testRollover_medium_370() {
        double actual = Angle.rollover(370, 0);
        double expected = 10;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testRollover_medium_3251() {
        double actual = Angle.rollover(3251, 0);
        double expected = 11;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testRollover_medium_neg_11() {
        double actual = Angle.rollover(-11, 0);
        double expected = 349;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testRollover_hard_neg_3601() {
        double actual = Angle.rollover(-3601, 0);
        double expected = 359;
        Assertions.assertEquals(expected, actual);
    }
}

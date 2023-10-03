import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.System;

import frc.team5115.Classes.Software.BunnyCatcher;

public class BunnyCatcherTest {
    final MockBunnyCatcher mock = new MockBunnyCatcher();
    final BunnyCatcher bunnyCatcher = new BunnyCatcher(mock);

    @Test
    void outputForAllDeltas() {
        //Prints out the speed for all possible deltas of the BunnyCatcher
        double expected = 0;
        // for(double i = 0; i <= 360; i+=1) {
        //     for(double j = 0; j <= 360; j+=1){
        //         double speed = testTurnTowardsAngle(i, j);
        //         System.out.println("current: " + i + " | setpoint: " + j + " | Delta: " + (j - i) + " | Speed: " + speed);
        //         if( (j-i) >= 30 && (j-i) < 180) {
        //             expected = 30;
        //         }
        //         else if( (j-i) <= -30 && (j-i) >= -180) {
        //             expected = -30;
        //         }
        //         else if(-30 <= (j-i) && (j-i) <= 30){
        //             expected = j-i;
        //         }
        //         else if((j-i) >= 180 && (j-i) <= 330){
        //             expected = -30;
        //         }
        //         else if((j-i) >= 330){
        //             expected = (j-i) - 360;
        //         }
        //         else if((j-i) < -330 && (j-i) >= -360){
        //             expected = 360 + (j-i);
        //         }
        //         else if((j-i) < -180){
        //             expected = 30;
        //         }


        //         if (speed == -0) {
        //             speed = 0;
        //         }


        //         Assertions.assertEquals(expected, speed);


        //     }
        // }
    }

    // @Test
    // void testTurnTowardsAngle_SmallNegativeWrap() {
    //     final double expected = -13;
    //     final double actual = testTurnTowardsAngle(10, -3);
    //     Assertions.assertEquals(expected, actual);
    // }

    // @Test
    // void testTurnTowardsAngle_SmallPositiveWrap() {
    //     final double expected = 16;
    //     // final double actual = testTurnTowardsAngle(-5, 11);
    //     final double actual = 16;
    //     Assertions.assertEquals(expected, actual);
    // }

    // ^^ HardwareBunnyCatcher instantiates a SparkMax with ID 100 and our mock catcher instantiates a SparkMax with ID 100, so it errors because it's mad that we have two spark maxes with the same ID

    double testTurnTowardsAngle(double current, double setpoint) {
        //This gives the position of
        mock.setPosition(current);
        bunnyCatcher.updateAngle();
        bunnyCatcher.turnTowardsAngle(setpoint, 0);
        return mock.getSpeed();
    }
}

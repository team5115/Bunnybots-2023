import frc.team5115.Classes.Hardware.HardwareBunnyCatcher;

//Mock Bunny Catcher Class for testing the HardwareBunnyCatcher Class
public class MockBunnyCatcher extends HardwareBunnyCatcher {
    //position in degrees of the BunnyCatcher
    double position;
    //Speed the BunnyCatcher Requires to get to that position
    double speed;

    //Basic Constructor that Begins the call to the HardwareBunnyCatcher Class
    public MockBunnyCatcher() {
        System.out.println("Bunny Catcher has been Created");

    }

    @Override
    public double getPosition() {
        //gets the position in degrees of the BunnyCatcher
        return position;
    }

    public double getSpeed() {
        //gets the speed of the BunnyCatcher
        return speed;
    }

    @Override
    public void setSpeed(double speed) {
        //sets the speed of the BunnyCatcher
        this.speed = speed;
    }

    public void setPosition(double position) {
        //sets the position in degrees of the BunnyCatcher
        this.position = position;
    }
}
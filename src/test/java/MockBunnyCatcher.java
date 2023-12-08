import frc.team5115.Classes.Hardware.HardwareBunnyCatcher;

//Mock Bunny Catcher Class for testing the HardwareBunnyCatcher Class
public class MockBunnyCatcher extends HardwareBunnyCatcher {
    //position in degrees of the BunnyCatcher
    double position;
    //Speed the BunnyCatcher Requires to get to that position
    double speed;

    //Basic Constructor that Begins the call to the HardwareBunnyCatcher Class
    public MockBunnyCatcher() {
        System.out.println("Mock Bunny Catcher has been created");
    }

    @Override
    public double getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Set the mock position in degrees 
     * @param position of the angle in degrees
     */
    public void setPosition(double position) {
        this.position = position;
    }
}
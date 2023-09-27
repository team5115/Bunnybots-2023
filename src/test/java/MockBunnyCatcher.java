import frc.team5115.Classes.Hardware.HardwareBunnyCatcher;

public class MockBunnyCatcher extends HardwareBunnyCatcher {
    double position;
    double speed;

    public MockBunnyCatcher() {

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

    public void setPosition(double position) {
        this.position = position;
    }
}
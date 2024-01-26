package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.BunnyCatcher;

public class SpinBunnyCatcher extends Command{
    static final double stabilizingAngleTolerance = 10;
    private BunnyCatcher bunnyCatcher;
    private double absoluteSetpoint;
    private int direction;
    private boolean stabilizing;
    private boolean atSetpoint;

    public SpinBunnyCatcher(BunnyCatcher bunnyCatcher, double deltaAngle, int direction) {
        this.bunnyCatcher = bunnyCatcher;
        this.direction = direction;
        absoluteSetpoint = bunnyCatcher.getAngle().getDegrees(0) + deltaAngle;
    }

    @Override
    public void initialize() {
        System.out.println("CatchBunny command INITIALIZED");
    }

    @Override
    public void execute() {
        final double distance = Math.abs(new Angle(absoluteSetpoint).getDelta(bunnyCatcher.getAngle()));

        if (distance < stabilizingAngleTolerance) {
            stabilizing = true;
        }

        atSetpoint = bunnyCatcher.turnTowardsAngle(absoluteSetpoint, stabilizing ? 0 : direction);
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("CatchBunny command ENDS");
        bunnyCatcher.stop();
    }

    @Override
    public boolean isFinished() {
        return atSetpoint;
    }
}

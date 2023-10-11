package frc.team5115.Commands.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.BunnyCatcher;

public class CatchBunny extends CommandBase{
    static final double stoppingAngleTolerance = 50;
    private BunnyCatcher bunnyCatcher;
    private double absoluteSetpoint;
    
    public CatchBunny(BunnyCatcher bunnyCatcher, double absoluteSetpoint) {
        this.bunnyCatcher = bunnyCatcher;
        this.absoluteSetpoint = absoluteSetpoint;
    }

    @Override
    public void initialize() {
        System.out.println("COMMAND INITIALIZED");
    }

    @Override
    public void execute() {
        System.out.println("COMMAND EXECUTED");
        bunnyCatcher.turnTowardsAngle(absoluteSetpoint, 1);
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("COMMAND END");
        bunnyCatcher.stop();
    }

    @Override
    public boolean isFinished() {
        System.out.println("Setpoint: " + absoluteSetpoint);
        System.out.println("BC: " + bunnyCatcher.getAngle().getDegrees(0));
        System.out.println("Dist: " + Math.abs(new Angle(absoluteSetpoint).getDelta(bunnyCatcher.getAngle())));
        return Math.abs(new Angle(absoluteSetpoint).getDelta(bunnyCatcher.getAngle())) < stoppingAngleTolerance;
    }
}

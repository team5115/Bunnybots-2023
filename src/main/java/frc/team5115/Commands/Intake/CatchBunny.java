package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.BunnyCatcher;

public class CatchBunny extends CommandBase{
    static final double stoppingAngleTolerance = 5;
    private BunnyCatcher bunnyCatcher;
    private double absoluteSetpoint;
    private Timer doneTimer;
    
    public CatchBunny(BunnyCatcher bunnyCatcher, double absoluteSetpoint) {
        this.bunnyCatcher = bunnyCatcher;
        this.absoluteSetpoint = absoluteSetpoint;
        doneTimer = new Timer();
    }

    @Override
    public void initialize() {
        System.out.println("COMMAND INITIALIZED");
        doneTimer.reset();
        doneTimer.stop();
    }

    @Override
    public void execute() {
        double speed = bunnyCatcher.turnTowardsAngle(absoluteSetpoint, 0);
        System.out.println("Setpoint: " + absoluteSetpoint);
        System.out.println("Current Angle: " + bunnyCatcher.getAngle().getDegrees(0));
        System.out.println("Speed: " + speed);

        boolean atAngle = Math.abs(new Angle(absoluteSetpoint).getDelta(bunnyCatcher.getAngle())) < stoppingAngleTolerance;
        if (atAngle) {
            doneTimer.start();
        } else {
            doneTimer.reset();
        }
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("COMMAND END");
        bunnyCatcher.stop();
    }

    @Override
    public boolean isFinished() {
        return doneTimer.get() > 0.1;
    }
}

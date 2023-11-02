package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.BunnyCatcher;

public class CatchBunny extends CommandBase{

    // static final double stoppingAngleTolerance = 5;
    static final double stoppingSpeedTolerance = 3;
    static final double stabilizingAngleTolerance = 10;
    private BunnyCatcher bunnyCatcher;
    private double absoluteSetpoint;
    private Timer doneTimer;
    private int direction;
    private boolean stabilizing;
    private double speed;

    public CatchBunny(BunnyCatcher bunnyCatcher, double absoluteSetpoint, int direction) {
        this.bunnyCatcher = bunnyCatcher;
        this.absoluteSetpoint = absoluteSetpoint;
        this.direction = direction;
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
        final double distance = Math.abs(new Angle(absoluteSetpoint).getDelta(bunnyCatcher.getAngle()));

        if (distance < stabilizingAngleTolerance) {
            stabilizing = true;
        }

        speed = bunnyCatcher.turnTowardsAngle(absoluteSetpoint, stabilizing ? 0 : direction);

        /*
        boolean atAngle = distance < stoppingAngleTolerance;
        if (atAngle) {
            doneTimer.start();
        } else {
            doneTimer.reset();
        }
        */
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("COMMAND END");
        bunnyCatcher.stop();
    }

    @Override
    public boolean isFinished() {
        // return doneTimer.get() > 0.1;
        return Math.abs(speed) < stoppingSpeedTolerance;
    }
}

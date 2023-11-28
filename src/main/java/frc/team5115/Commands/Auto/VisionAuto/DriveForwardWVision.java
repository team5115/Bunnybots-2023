package frc.team5115.Commands.Auto.VisionAuto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Software.Drivetrain;

public class DriveForwardWVision extends CommandBase{
    private Drivetrain drivetrain;
    private double dist;
    private Pose2d start;
    private double speed;
    private boolean doneMoving;
    private Timer timer;
    private double timeout;
    private double photonPoseEstimator;

    public DriveForwardWVision(Drivetrain drivetrain2, double d, double e) {
        
    }

    public DriveForwardWVision(Drivetrain drivetrain, double dist, double speed, double photonPoseEstimator) {
        this.dist = dist;
        this.drivetrain = drivetrain;
        this.speed = speed;
        this.photonPoseEstimator = photonPoseEstimator;
        timeout = Math.abs(dist / speed) + 1; // meters divided by m/s gives seconds! plus extra time
        timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.start();
        timer.reset();
        start = drivetrain.photonPoseEstimator();
    }

    @Override
    public void execute() {
        doneMoving = drivetrain.UpdateMovingWithVision(dist, start, speed);
           System.out.println("Right Distance: " + drivetrain.getRightDistance() + "Left Distance: " + drivetrain.getRightDistance());
    }

    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        if(timer.get() > timeout) return true;
        return doneMoving;
    }
}
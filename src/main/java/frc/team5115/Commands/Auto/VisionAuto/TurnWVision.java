  package frc.team5115.Commands.Auto.VisionAuto;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Classes.Software.PhotonVision;
   
public class TurnWVision extends CommandBase{
    private final Timer grandTimer;
    private Pose2d start;
    private boolean turned = false;
    private PhotonVision photonVision;
    private final double angle;
    private Drivetrain drivetrain;
    private double photonPoseEstimator;
    private boolean doneMoving;
    private double dist;
    private double speed;

    public TurnWVision(PhotonVision photonVision, double absoluteAngle, double photonPoseEstimator) {
        angle = Angle.rollover(absoluteAngle, -180);
        grandTimer = new Timer();
        photonVision = new PhotonVision();
        this.photonPoseEstimator = photonPoseEstimator;
        
    }

    @Override
    public void initialize() {
        grandTimer.reset();
        grandTimer.start();
        start = drivetrain.photonPoseEstimator();

    }

    @Override
    public void execute() {
      doneMoving = drivetrain.TurnWithVision(dist, start, speed);        
    }

    @Override
    public void end(boolean interrupted){
        System.out.println("finished turning");
        drivetrain.stop();
    }

   private void stop() {
    }

{
    }

    @Override
    public boolean isFinished() {
        // timeout if the command has been running for too long
        if (grandTimer.get() > 4) {
            System.out.println("Turning attempt timed out after 4 seconds");
        }
        return true;

        
        // finish if it didn't move this timestep
     /*   if (// didn't move) {
            System.out.println("It didn't move.");
            return false;
        }
        return turned; */
    }
}
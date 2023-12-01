package frc.team5115.Classes.Software;

import java.util.Optional;
import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Accessory.ThrottleControl;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Hardware.NAVx;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * The drivetrain subsystem. Provides a number of high-complexity utility functions for interacting with the drivetrain.
 */
public class Drivetrain extends SubsystemBase{
    private final ThrottleControl throttle;
    private final RamseteController ramseteController;
    private final SwerveDriveKinematics kinematics;
    private final HardwareDrivetrain hardwareDrivetrain;
    private final NAVx navx;
    private final PhotonVision photonVision;
    private SwerveDrivePoseEstimator poseEstimator;
    SwerveDriveKinematics swervekinematics = new SwerveDriveKinematics(new Translation2d(), new Translation2d(), new Translation2d(), new Translation2d());

    public Drivetrain(PhotonVision photonVision, NAVx nav) {
        this.photonVision = photonVision;
        throttle = new ThrottleControl(3, -3, 0.2);
        hardwareDrivetrain = new HardwareDrivetrain();
        ramseteController = new RamseteController();
        kinematics = new SwerveDriveKinematics(); //TODO fill this out
        navx = nav;
    }

    public void init() {
        poseEstimator = new SwerveDrivePoseEstimator(kinematics, navx.getYawRotation2D(), hardwareDrivetrain.getModulePositions(), getEstimatedPose());
        System.out.println("Angle from navx" + navx.getYawDeg()
        );
    }

	public double getRightDistance() {
        return 0; //TODO
    }

    public double getLeftDistance() {
        return 0; //TODO
    }

    /**
	 * Sets the encoder values to 0.
	 */
    public void resetEncoders() {
        hardwareDrivetrain.resetEncoders();
    }

    public void toggleThrottle(){
        throttle.toggleThrottle();
    }

    public void toggleSlowMode() {
        throttle.toggleSlowMode();
    }

    /**
     * Enable or disable throttle. set to false to make throttle.getThrottle() return 0, true for normal function
     * @param enable true to allow stuff using throttle to move, false will just make getThrottle return 0
     */    
    public void setThrottleEnabled(boolean enable) {
        throttle.setThrottleEnabled(enable);
    }
    
    public void SwerveDrive(double forward, double turn, double right, boolean rookieMode){
        if(rookieMode){
            right *= 0.1;
            turn *= 0.1;
            forward *= 0.1;
        }else{
            right *= 0.2;
            turn *= 0.2;
            forward *= 0.2;
        }
        hardwareDrivetrain.drive(right, forward, turn, false, false);

        // Front left module state
       // drivetrain.plugAndChugDrive(moduleStates);
    }

	/**
	 * Updates the odometry of the robot.
     * should run every robot tick
	 */
    public void UpdateOdometry() {
        poseEstimator.update(navx.getYawRotation2D(), hardwareDrivetrain.getModulePositions());

        Optional<EstimatedRobotPose> result = photonVision.getEstimatedGlobalPose(poseEstimator.getEstimatedPosition());
        if (result.isPresent()) {
            EstimatedRobotPose camPose = result.get();
            poseEstimator.addVisionMeasurement(camPose.estimatedPose.toPose2d(), camPose.timestampSeconds);
            System.out.println("vision is really working");
        }
    }

	/**
	 * @return The estimated pose of the robot
	 */
    public Pose2d getEstimatedPose() {
        return poseEstimator.getEstimatedPosition();
    }

	/**
	 * Generate a command that will make the robot follow a given trajectory.
	 * @param trajectory The trajectory to follow
	 */
    public Command getRamseteCommand(Trajectory trajectory) {
        poseEstimator.getEstimatedPosition();
        return null; // TODO fill this out with actual code to generate a ramsete command using the getEstimatedPose() method
    }

    public void stop() {
        hardwareDrivetrain.drive(0, 0, 0, false, false);
    }

	/**
	 * Resets the NAVx.
	 */
    public void resetNAVx(){
        navx.resetNAVx();
    }

	/**
	 * @return The current pitch in degrees, given by the NAVx
	 */
    public double getPitchDeg() {
        return navx.getPitchDeg();
    }

	/**
	 * @return The current yaw in degrees, given by the NAVx
	 */
    public double getYawDeg() {
        return navx.getPitchDeg();
    }
}

package frc.team5115.Classes.Software;

import frc.team5115.Constants;

import java.util.Optional;
import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Accessory.ThrottleControl;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Hardware.NAVx;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * The drivetrain subsystem. Provides a number of high-complexity utility functions for interacting with the drivetrain.
 */
public class Drivetrain extends SubsystemBase{
    public NetworkTable ShooterCam;
    public NetworkTableEntry ty;
    public NetworkTableEntry tx;
    public NetworkTableEntry tv;
    private final ThrottleControl throttle;
    private final PIDController anglePID;
    private final RamseteController ramseteController;
    private final DifferentialDriveKinematics kinematics;
    private final HardwareDrivetrain drivetrain;
    private final NAVx navx;
    private final PhotonVision photonVision;
    private DifferentialDrivePoseEstimator poseEstimator;
    private double leftSpeed;
    SwerveModuleState[] moduleStates;
    SwerveDriveKinematics swervekinematics = new SwerveDriveKinematics(new Translation2d(), new Translation2d(), new Translation2d(), new Translation2d());
    private double rightSpeed;

    public static final double kD = 0.25;
    public static final double hD = 0.044;
    public static final double bA = 10;
    public static final double MaxArea = 0.1;

    public Drivetrain(PhotonVision photonVision, NAVx nav) {
        this.photonVision = photonVision;
        throttle = new ThrottleControl(3, -3, 0.2);
        anglePID = new PIDController(0.019, 0.0001, 0.0012);
        drivetrain = new HardwareDrivetrain();
        ramseteController = new RamseteController();
        kinematics = new DifferentialDriveKinematics(Constants.TRACKING_WIDTH_METERS);
        navx = nav;
    }

    public void init() {
        // poseEstimator = new DifferentialDrivePoseEstimator(
        //     kinematics, navx.getYawRotation2D(), 0.0, 0.0,
        //     new Pose2d(FieldConstants.startX, FieldConstants.startY, FieldConstants.startAngle), 
        //     VecBuilder.fill(1, 1, 1),
        //     VecBuilder.fill(0, 0, 0)
        // );

        poseEstimator = new DifferentialDrivePoseEstimator(
            kinematics, navx.getYawRotation2D(), getLeftDistance(), getRightDistance(), new Pose2d(), VecBuilder.fill(1, 1, 1), VecBuilder.fill(0, 0, 0)
        );
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
        drivetrain.resetEncoders();
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

    private double[] normalizeVector(double x, double y) {
        if(Math.abs(x) > 1){
            x = x/Math.abs(x);
            y = y/Math.abs(x);
        }
        else if (Math.abs(y) > 1){
            y = y/Math.abs(y);
            x = x/Math.abs(y);
        }
        return new double[] {x, y};
    }
    
    public void SwerveDrive(double forward, double turn, double right, boolean x){
        if(x){
        right *= 0.1;
        turn *= 0.1;
        forward *= 0.1;
        }else{
        right *= 0.2;
        turn *= 0.2;
        forward *= 0.2;
        }
        drivetrain.drive(right, forward, turn, false, false);

        // Front left module state
       // drivetrain.plugAndChugDrive(moduleStates);
    }

	/**
	 * Updates the odometry of the robot.
	 */
    public void UpdateOdometry() {
        poseEstimator.update(navx.getYawRotation2D(), getLeftDistance(), getRightDistance());

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
        UpdateOdometry();
        return poseEstimator.getEstimatedPosition();
    }

	/**
	 * Generate a command that will make the robot follow a given trajectory.
	 * @param trajectory The trajectory to follow
	 */
    public Command getRamseteCommand(Trajectory trajectory) {
        return null;
    }

    public void stop() {
        drivetrain.drive(0, 0, 0, false, false);
    }


    public boolean UpdateMovingWithVision(double dist, Pose2d pose, double speedMagnitude) {
        double realdist = pose.getTranslation().getDistance(getEstimatedPose().getTranslation());
        final double speed = speedMagnitude * Math.signum(dist);
        SwerveDrive(speed, 0, 0, false);
        final double tolerance = 0.1;
        return Math.abs(realdist-dist) < tolerance;
    }

    public boolean TurnWithVision(double dist, Pose2d start, double speed) {
        return false;
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

    public Pose2d photonPoseEstimator() {
        return null;
    }
}

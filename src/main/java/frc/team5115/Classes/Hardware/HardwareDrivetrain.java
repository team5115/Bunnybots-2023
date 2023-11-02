package frc.team5115.Classes.Hardware;

import static frc.team5115.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import edu.wpi.first.math.controller.*;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import frc.team5115.Classes.Hardware.SwerveDriveModule;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.team5115.Classes.Accessory.SwerveUtils;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
/**
 * The drivetrain hardware subsystem. Provides methods to interact with the actual hardware of the drivetrain.
 */
public class HardwareDrivetrain{
    // Competition feedforward values - 6 inch diameter on KITT comp robot with arm and ballasts
    public final double leftKs = 0.0378;
    public final double leftKv = 2.7479;
    public final double leftKa = 0.32825;
    
    private final double rightKs = 0.0528;
    private final double rightKv = 2.8399;
    private final double rightKa = 0.26071;
    
    private NAVx gyro = new NAVx();

    // private final double combinedkP = 3.4349;
    // END of comp robot values

    // Testbed feedforward values - 6 inch diameter on KATT testbed
    // private final double leftKs = 0.090949;
    // private final double leftKv = 2.783;
    // private final double leftKa = 0.16477;
    
    // private final double rightKs = 0.099706;
    // private final double rightKv = 2.8314;
    // private final double rightKa = 0.14565;
    // END of testbed values
    

    private final double leftKp = 0.2;
    private final double rightKp = 0.2;
    private final double Ki = 0.1;
    private final double Kd = 0.1;
    // END of testbed values

    SwerveDriveModule frontLeft = new SwerveDriveModule(FRONT_LEFT_MOTOR_ID, FRONT_LEFT_TURN_MOTOR_ID, 0);
    SwerveDriveModule frontRight = new SwerveDriveModule(FRONT_RIGHT_MOTOR_ID, FRONT_RIGHT_TURN_MOTOR_ID, 0);
    SwerveDriveModule backLeft = new SwerveDriveModule(BACK_LEFT_MOTOR_ID,BACK_LEFT_TURN_MOTOR_ID, 0);
    SwerveDriveModule backRight = new SwerveDriveModule(BACK_RIGHT_MOTOR_ID, BACK_RIGHT_TURN_MOTOR_ID, 0);

    private final SimpleMotorFeedforward leftFeedForward = new SimpleMotorFeedforward(leftKs, leftKv, leftKa);
    private final SimpleMotorFeedforward rightFeedForward = new SimpleMotorFeedforward(rightKs, rightKv, rightKa);
    private final PIDController leftPID = new PIDController(leftKp, Ki, Kd);
    private final PIDController rightPID = new PIDController(rightKp, Ki, Kd);

    private final CANSparkMax frontLeftOld = new CANSparkMax(FRONT_LEFT_MOTOR_ID+100, MotorType.kBrushless);
    private final CANSparkMax frontRightOld = new CANSparkMax(FRONT_RIGHT_MOTOR_ID+100, MotorType.kBrushless);
    private final CANSparkMax backLeftOld = new CANSparkMax(BACK_LEFT_MOTOR_ID+100, MotorType.kBrushless);
    private final CANSparkMax backRightOld = new CANSparkMax(BACK_RIGHT_MOTOR_ID+100, MotorType.kBrushless);

    private final RelativeEncoder leftEncoder = frontLeftOld.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    private final RelativeEncoder rightEncoder = frontRightOld.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

      // Slew rate filter variables for controlling lateral acceleration
    private double m_currentRotation = 0.0;
    private double m_currentTranslationDir = 0.0;
    private double m_currentTranslationMag = 0.0;

    private SlewRateLimiter m_magLimiter = new SlewRateLimiter(1);
    private SlewRateLimiter m_rotLimiter = new SlewRateLimiter(1);
    private double m_prevTime = WPIUtilJNI.now() * 1e-6;

  // Odometry class for tracking robot pose
  SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(
      DriveConstants.kDriveKinematics,
      Rotation2d.fromDegrees(gyro.getYawDeg()),
      new SwerveModulePosition[] {
          frontLeft.getPosition(),
          frontRight.getPosition(),
          backLeft.getPosition(),
          backRight.getPosition()
      });

	/**
	 * `HardwareDrivetrain` constructor.
	 * @param arm - The arm subsystem to use
	 */
    public HardwareDrivetrain(){
        resetEncoders();
        frontRightOld.setInverted(true);
    }

  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(
        Rotation2d.fromDegrees(gyro.getYawDeg()),
        new SwerveModulePosition[] {
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        });
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        Rotation2d.fromDegrees(gyro.getYawDeg()),
        new SwerveModulePosition[] {
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        },
        pose);
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed        Speed of the robot in the x direction (forward).
   * @param ySpeed        Speed of the robot in the y direction (sideways).
   * @param rot           Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the
   *                      field.
   * @param rateLimit     Whether to enable rate limiting for smoother control.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, boolean rateLimit) {
    
    double xSpeedCommanded;
    double ySpeedCommanded;

    if (rateLimit) {
      // Convert XY to polar for rate limiting
      double inputTranslationDir = Math.atan2(ySpeed, xSpeed);
      double inputTranslationMag = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));

      // Calculate the direction slew rate based on an estimate of the lateral acceleration
      double directionSlewRate;
      if (m_currentTranslationMag != 0.0) {
        directionSlewRate = Math.abs(DriveConstants.kDirectionSlewRate / m_currentTranslationMag);
      } else {
        directionSlewRate = 500.0; //some high number that means the slew rate is effectively instantaneous
      }
      

      double currentTime = WPIUtilJNI.now() * 1e-6;
      double elapsedTime = currentTime - m_prevTime;
      double angleDif = SwerveUtils.AngleDifference(inputTranslationDir, m_currentTranslationDir);
      if (angleDif < 0.45*Math.PI) {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
      }
      else if (angleDif > 0.85*Math.PI) {
        if (m_currentTranslationMag > 1e-4) { //some small number to avoid floating-point errors with equality checking
          // keep currentTranslationDir unchanged
          m_currentTranslationMag = m_magLimiter.calculate(0.0);
        }
        else {
          m_currentTranslationDir = SwerveUtils.WrapAngle(m_currentTranslationDir + Math.PI);
          m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
        }
      }
      else {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(0.0);
      }
      m_prevTime = currentTime;
      
      xSpeedCommanded = m_currentTranslationMag * Math.cos(m_currentTranslationDir);
      ySpeedCommanded = m_currentTranslationMag * Math.sin(m_currentTranslationDir);
      m_currentRotation = m_rotLimiter.calculate(rot);


    } else {
      xSpeedCommanded = xSpeed;
      ySpeedCommanded = ySpeed;
      m_currentRotation = rot;
    }

    // Convert the commanded speeds into the correct units for the drivetrain
    double xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed;

    SwerveModuleState[] swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, Rotation2d.fromDegrees(gyro.getYawDeg()))
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));
    SwerveDriveKinematics.desaturateWheelSpeeds(
        swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
    frontLeft.setDesiredState(swerveModuleStates[0]);
    frontRight.setDesiredState(swerveModuleStates[1]);
    backLeft.setDesiredState(swerveModuleStates[2]);
    backRight.setDesiredState(swerveModuleStates[3]);
  }

  /**
   * Sets the wheels into an X formation to prevent movement.
   */
  public void setX() {
    frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    backLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    backRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DriveConstants.kMaxSpeedMetersPerSecond);
    frontLeft.setDesiredState(desiredStates[0]);
    frontRight.setDesiredState(desiredStates[1]);
    backLeft.setDesiredState(desiredStates[2]);
    backRight.setDesiredState(desiredStates[3]);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    frontLeft.resetEncoders();
    backLeft.resetEncoders();
    frontRight.resetEncoders();
    backRight.resetEncoders();
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    gyro.resetNAVx();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Rotation2d.fromDegrees(gyro.getYawDeg()).getDegrees();
  }

    /**
     * @param motorID - The ID of the motor to get an encoder of.
     * @return A reference to an encoder matching the id
     */
    public RelativeEncoder getEncoder(int motorID){
        switch (motorID) {
            case BACK_LEFT_MOTOR_ID:
            case FRONT_LEFT_MOTOR_ID:
                return leftEncoder;
            
            case BACK_RIGHT_MOTOR_ID:
            case FRONT_RIGHT_MOTOR_ID:
                return rightEncoder;
                
            default:
                throw new Error("Encoder ID " + motorID + " is invalid!");
        }
    }

	/**
	 * @param motorID - The ID of the motor to get the encoder distance of
	 * @return The distance traveled by the motor of the given ID
	 */
    public double getEncoderDistance(int motorID){
        switch (motorID) {
            case BACK_LEFT_MOTOR_ID:
            case FRONT_LEFT_MOTOR_ID:
                return leftEncoder.getPosition()*NEO_DISTANCE_CALIBRATION;
            
            case BACK_RIGHT_MOTOR_ID:
            case FRONT_RIGHT_MOTOR_ID:
                return rightEncoder.getPosition()*NEO_DISTANCE_CALIBRATION;
                
            default:
                throw new Error("Encoder ID " + motorID + " is invalid!");
        }
    }

	/**
	 * @param motorID - The ID of the motor to get the encoder velocity of
	 * @return The velocity of the motor of the given ID
	 */
    public Double getEncoderVelocity(int motorID){
        switch (motorID) {
            case BACK_LEFT_MOTOR_ID:
            case FRONT_LEFT_MOTOR_ID:
                return leftEncoder.getVelocity()*NEO_VELOCITY_CALIBRATION;
            
            case BACK_RIGHT_MOTOR_ID:
            case FRONT_RIGHT_MOTOR_ID:
                return rightEncoder.getVelocity()*NEO_VELOCITY_CALIBRATION;
                
            default:
                throw new Error("Encoder ID " + motorID + " is invalid!");
        }
    }

    /**
     * Sets the voltages of the individual motors, without PID for mecanum compatibility
     * @param frontLeftOldSpeed - The speed of the front left motor     
     * @param frontRightOldSpeed - The speed of the front right motor
     * @param backLeftOldSpeed - The speed of the back left motor     
     * @param backRightOldSpeed - The speed of the back right motor
     */
    @Deprecated
    public void plugAndChugDrive(double frontLeftOldSpeed, double frontRightOldSpeed, double backLeftOldSpeed, double backRightOldSpeed){
        frontLeftOld.set(frontLeftOldSpeed);
        frontRightOld.set(frontRightOldSpeed);
        backLeftOld.set(backLeftOldSpeed);
        backRightOld.set(backRightOldSpeed);
    }

    public void PlugAndVoltDrive(double frontLeftOldVoltage, double frontRightOldVoltage, double backLeftOldVoltage, double backRightOldVoltage){
        frontLeftOld.setVoltage(frontLeftOldVoltage);
        frontRightOld.setVoltage(frontRightOldVoltage);
        backLeftOld.setVoltage(backLeftOldVoltage);
        backRightOld.setVoltage(backRightOldVoltage);
    }

    public void PlugAndVoltDrive(double leftVoltage, double rightVoltage) {
        PlugAndVoltDrive(leftVoltage, rightVoltage, leftVoltage, rightVoltage);
    }

    /**
     * Sets the speeds of the motors. Uses feedforward but not PID. (right now PID is broken)
     * 
     * @param leftSpeed the speed for the left motors in meters per second
     * @param rightSpeed the speed for the right motors in meters per second
     */
    public void plugAndFFDrive(double leftSpeed, double rightSpeed) {
        final double accelerationLimit = getAccelerationLimit(); // can't bother figuring the units, but it's not m/s^2
        final double currentLeftVelocity = getLeftVelocity();
        final double currentRightVelocity = getRightVelocity();

        // limit left acceleration
        if(Math.abs(leftSpeed - currentLeftVelocity) > accelerationLimit) {
            leftSpeed = currentLeftVelocity + accelerationLimit * Math.signum(leftSpeed - currentLeftVelocity);
        }
        // limit right acceleration
        if(Math.abs(rightSpeed - currentRightVelocity) > accelerationLimit) {
            rightSpeed = currentRightVelocity + accelerationLimit * Math.signum(rightSpeed - currentRightVelocity);
        }

        double leftVoltage = leftFeedForward.calculate(leftSpeed);
        double rightVoltage = rightFeedForward.calculate(rightSpeed);
        leftVoltage += leftPID.calculate(currentLeftVelocity, leftSpeed);
        rightVoltage += rightPID.calculate(currentRightVelocity, rightSpeed);
        // Work on better PID Analyzer

        leftVoltage = MathUtil.clamp(leftVoltage, -DRIVE_MOTOR_MAX_VOLTAGE, DRIVE_MOTOR_MAX_VOLTAGE);
        rightVoltage = MathUtil.clamp(rightVoltage, -DRIVE_MOTOR_MAX_VOLTAGE, DRIVE_MOTOR_MAX_VOLTAGE);

        if(Math.abs(leftSpeed) < 0.05) leftVoltage = 0;
        if(Math.abs(rightSpeed) < 0.05) rightVoltage = 0;

        backLeftOld.follow(frontLeftOld);
        backRightOld.follow(frontRightOld);
        frontLeftOld.setVoltage(leftVoltage);
        frontRightOld.setVoltage(rightVoltage);
    }

    private double getAccelerationLimit() {
        return 1.5;
    }

    public double getLeftVelocity() {
        return leftEncoder.getVelocity() * NEO_VELOCITY_CALIBRATION;
    }

    public double getRightVelocity() {
        return rightEncoder.getVelocity() * NEO_VELOCITY_CALIBRATION;
    }
}

package frc.team5115;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import com.revrobotics.CANSparkMax.IdleMode;

public class Constants{

    public static final boolean MECANUM = false; 
  
    //motor ids
    public static final byte FRONT_LEFT_MOTOR_ID = 1;
    public static final byte FRONT_RIGHT_MOTOR_ID = 2;
    public static final byte BACK_LEFT_MOTOR_ID = 3;
    public static final byte BACK_RIGHT_MOTOR_ID = 4;

    public static final double TALON_ENCODER_CALIBRATION = (63.837/4104.5);
    public static final double NEO_VELOCITY_CALIBRATION = (0.47877871986/(60*10.71));
    public static final double NEO_DISTANCE_CALIBRATION = (0.47877871986/(10.71));
    public static final double MAX_VOLTAGE = 12;

    //X-Box
    public static final byte JOY_X_AXIS_ID = 0;
    public static final byte JOY_Y_AXIS_ID = 1;
    public static final byte JOY_Z_AXIS_ID = 4; 

    // NEW Feedforward
    public static final double kS = 0.18296; 
    public static final double kV = 4.2023;
    public static final double kA = 0.28613;

    public static final double DRIVE_MOTOR_MAX_VOLTAGE = 12;
    // distance between the left wheels and the right wheels in meters
    // 0.70 for wide, 0.57 for long
    public static final double TRACKING_WIDTH_METERS = 0.70;
    // distance between the front and back axles in meters
    // UNKOWN!!!!! somebody needs to measure this. 0.58 is ARBITRARY
    public static final double TRACKING_LENGTH_METERS = 0.58;

    public static final double TARGET_ANGLE = 1;

    public static class SwerveConstants{
        public static final int DrivingMotorPinionTeeth = 14;

        // Invert the turning encoder, since the output shaft rotates in the opposite direction of
        // the steering motor in the MAXSwerve Module.
        public static final boolean TurningEncoderInverted = true;
    
        // Calculations required for driving motor conversion factors and feed forward
        public static final double WheelDiameterMeters = 0.0762;
        public static final double WheelCircumferenceMeters = WheelDiameterMeters * Math.PI;
        // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
        public static final double DrivingMotorReduction = (45.0 * 22) / (DrivingMotorPinionTeeth * 15);
    
        public static final double DrivingEncoderPositionFactor = (WheelDiameterMeters * Math.PI)
            / DrivingMotorReduction; // meters
        public static final double DrivingEncoderVelocityFactor = ((WheelDiameterMeters * Math.PI)
            / DrivingMotorReduction) / 60.0; // meters per second
    
        public static final double TurningEncoderPositionFactor = (2 * Math.PI); // radians
        public static final double TurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second
    
        public static final double TurningEncoderPositionPIDMinInput = 0; // radians
        public static final double TurningEncoderPositionPIDMaxInput = TurningEncoderPositionFactor; // radians
    
        public static final double DrivingP = 0.04;
        public static final double DrivingI = 0;
        public static final double DrivingD = 0;
        public static final double DrivingFF = 1;
        public static final double DrivingMinOutput = -1;
        public static final double DrivingMaxOutput = 1;
    
        public static final double TurningP = 1;
        public static final double TurningI = 0;
        public static final double TurningD = 0;
        public static final double TurningFF = 0;
        public static final double TurningMinOutput = -1;
        public static final double TurningMaxOutput = 1;
    
        public static final IdleMode DrivingMotorIdleMode = IdleMode.kBrake;
        public static final IdleMode TurningMotorIdleMode = IdleMode.kBrake;
    
        public static final int DrivingMotorCurrentLimit = 50; // amps
        public static final int TurningMotorCurrentLimit = 20; // amps 
    }

    // Photon vision constants    
    public static class FieldConstants {
        public static final double length = Units.feetToMeters(54);
        public static final double width = Units.feetToMeters(27);
        public static final double startX = 7;
        public static final double startY = -0; 
        public static final double startAngleDeg = 0;
        public static final Rotation2d startAngle = Rotation2d.fromDegrees(startAngleDeg);
    }


    public static class VisionConstants {
        public static final String leftCameraName = "HD_USB_Camera";
        public static final String rightCameraName = null;

        private static final double cameraPosX = 0.277813055626;
        private static final double cameraPosY = 0.346869443739;
        private static final double cameraPosZ = 0.0889001778004;
        private static final double cameraRoll = 0.0;
        private static final double cameraPitch = 158.0;
        private static final double cameraYaw = 22.5;

        public static final Transform3d robotToCamL = new Transform3d( new Translation3d(-cameraPosX, -cameraPosY, cameraPosZ), new Rotation3d(cameraRoll, cameraPitch, +cameraYaw)); 
        public static final Transform3d robotToCamR = new Transform3d( new Translation3d(+cameraPosX, -cameraPosY, cameraPosZ), new Rotation3d(cameraRoll, cameraPitch, -cameraYaw)); 
        
    }

}
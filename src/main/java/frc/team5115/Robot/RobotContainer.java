package frc.team5115.Robot;

import static frc.team5115.Constants.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Classes.Accessory.I2CHandler;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Software.*;
import frc.team5115.Commands.Auto.AutoCommandGroup;
import frc.team5115.Commands.Intake.CatchBunny;
import frc.team5115.Commands.Intake.IntakeBerry;
import frc.team5115.Commands.Intake.MoveArm;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.networktables.GenericEntry;

public class RobotContainer {
    private final Joystick joy;
    private final PhotonVision photonVision;
    private final Drivetrain drivetrain;
    private final BunnyCatcher bunnyCatcher;
    private final ShuffleboardTab shuffleboardTab;
    private final GenericEntry pathChooser;
    private final I2CHandler i2cHandler;
    private final NAVx navx;
    private final Arm arm;
    private AutoCommandGroup autoCommandGroup;

    public RobotContainer() {
        joy = new Joystick(0);
        navx = new NAVx();
        i2cHandler = new I2CHandler();
        photonVision = new PhotonVision();
        drivetrain = new Drivetrain(photonVision, navx);

        HardwareBunnyCatcher hardwareBunnyCatcher = new HardwareBunnyCatcher();
        HardwareArm hardwareArm = new HardwareArm(navx, i2cHandler);
        
        arm = new Arm(hardwareArm);
        bunnyCatcher = new BunnyCatcher(hardwareBunnyCatcher);
        
        shuffleboardTab = Shuffleboard.getTab("SmartDashboard");
        pathChooser = shuffleboardTab.add("Are we doing the outside auto?", false).getEntry();

        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton(joy, XboxController.Button.kA.value).onTrue(new InstantCommand(drivetrain :: toggleSlowMode));
        new JoystickButton(joy, XboxController.Button.kA.value).onTrue(new CatchBunny(bunnyCatcher, 50));
    }

    public void startTeleop(){
        drivetrain.init();
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        
        System.out.println("Starting teleop");
        drivetrain.resetEncoders();
    }

    public void disabledInit(){
        drivetrain.stop();
    }

    public void stopEverything(){
        drivetrain.stop();
        arm.stop();
    }

    public void startAuto(){
        if(autoCommandGroup != null) {
            autoCommandGroup.cancel();
        }
        drivetrain.resetEncoders();
        drivetrain.resetNAVx();
        drivetrain.stop();

        boolean doOutsidePath = pathChooser.getBoolean(false);
        System.out.println("Do outside path? " + doOutsidePath + "@@@@@@@@@@@");

        autoCommandGroup = new AutoCommandGroup(drivetrain, doOutsidePath);
        autoCommandGroup.schedule();
    }

    public void autoPeriod() {
        // i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        //drivetrain.UpdateOdometry();
    }

    public void teleopPeriodic() {
        // i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        // drivetrain.UpdateOdometry();
        drivetrain.SwerveDrive(-joy.getRawAxis(JOY_Y_AXIS_ID), joy.getRawAxis(JOY_X_AXIS_ID), joy.getRawAxis(JOY_Z_AXIS_ID));

        // double forward = -joy.getRawAxis(JOY_Y_AXIS_ID); // negated because Y axis on controller is negated
        // double turn = ;
        // drivetrain.TankDrive(forward, turn);
        
        // System.out.println(drivetrain.getEstimatedPose());
    }
}

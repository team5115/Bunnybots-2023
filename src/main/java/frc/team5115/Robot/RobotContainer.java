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
    private final Timer timer;
    private final Joystick joy;
    private final PhotonVision photonVision;
    private final Drivetrain drivetrain;
    private final BunnyCatcher bunnyCatcher;
    private final ShuffleboardTab tab;
    private final GenericEntry center;
    private AutoCommandGroup autoCommandGroup;
    private boolean centerAuto = false;
    private final I2CHandler i2cHandler;
    private final NAVx navx;
    // private Arm arm;

    public RobotContainer() {
        joy = new Joystick(0);
        navx = new NAVx();
        i2cHandler = new I2CHandler();
        photonVision = new PhotonVision();
        drivetrain = new Drivetrain(photonVision, navx);

        HardwareBunnyCatcher hardwareBunnyCatcher = new HardwareBunnyCatcher();
        // HardwareArm hardwareArm = new HardwareArm(navx, i2cHandler);
        // arm = new Arm(hardwareArm);
        bunnyCatcher = new BunnyCatcher(hardwareBunnyCatcher);
        
        tab = Shuffleboard.getTab("SmartDashboard");
        center = tab.add("Are we doing center balacing auto?", false).getEntry();

        timer = new Timer();
        timer.reset();
        configureButtonBindings();
    }

    public void configureButtonBindings() {
        // new JoystickButton(joy, XboxController.Button.kA.value).onTrue(new InstantCommand(drivetrain :: toggleSlowMode));
        new JoystickButton(joy, XboxController.Button.kA.value).onTrue(new CatchBunny(bunnyCatcher, 50));
    }

    public void startTeleop(){
        drivetrain.init();
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        // arm.zeroArm();
        
        System.out.println("Starting teleop");
        drivetrain.resetEncoders();
    }

    public void disabledInit(){
        drivetrain.stop();
        // i2cHandler.Disable();
    }

    public void stopEverything(){
        drivetrain.stop();
        // arm.stop();
    }

    public void startAuto(){
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        drivetrain.resetEncoders();
        drivetrain.resetNAVx();
        drivetrain.stop();
        centerAuto = center.getBoolean(false);
        System.out.println("Good auto? " + centerAuto + "!!!!!!!");
        autoCommandGroup.schedule();
    }

    public void autoPeriod() {
       //drivetrain.UpdateOdometry();
        i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
    }

    public void teleopPeriodic() {
        // i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        // drivetrain.UpdateOdometry();
        // double forward = -joy.getRawAxis(JOY_Y_AXIS_ID); // negated because Y axis on controller is negated
        // double turn = joy.getRawAxis(JOY_Z_AXIS_ID);
        // drivetrain.TankDrive(forward, turn);
        
        // System.out.println(drivetrain.getEstimatedPose());
    }
}

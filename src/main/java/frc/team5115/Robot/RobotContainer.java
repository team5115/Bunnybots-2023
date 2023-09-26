package frc.team5115.Robot;

import static frc.team5115.Constants.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Classes.Acessory.I2CHandler;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Software.*;
import frc.team5115.Commands.Auto.AutoCommandGroup;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.networktables.GenericEntry;

public class RobotContainer {
    private final Timer timer;
    private final Joystick joy1;
    private final Joystick joy2;
    private final PhotonVision photonVision;
    private final Drivetrain drivetrain;
    private final ShuffleboardTab tab;
    private final GenericEntry center;
    private AutoCommandGroup autoCommandGroup;
    private boolean centerAuto = false;
    private final I2CHandler i2cHandler;
    private final NAVx navx;

    public RobotContainer() {
        joy1 = new Joystick(0);
        joy2 = new Joystick(1);

        navx = new NAVx();
        i2cHandler = new I2CHandler();

        photonVision = new PhotonVision();

        HardwareDrivetrain hardwareDrivetrain = new HardwareDrivetrain();
        drivetrain = new Drivetrain(photonVision, hardwareDrivetrain, navx);
        
        tab = Shuffleboard.getTab("SmartDashboard");
        center = tab.add("Are we doing center balacing auto?", false).getEntry();

        timer = new Timer();
        timer.reset();
        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton(joy2, XboxController.Button.kA.value).onTrue(new InstantCommand(drivetrain :: toggleSlowMode));
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
    }

    public void teleopPeriodic() {
        // i2cHandler.updatePitch();
        
        // drivetrain.UpdateOdometry();
        double forward = -joy2.getRawAxis(JOY_Y_AXIS_ID); // negated because Y axis on controller is negated
        double turn = joy2.getRawAxis(JOY_Z_AXIS_ID);
        drivetrain.TankDrive(forward, turn);
        
        // System.out.println(drivetrain.getEstimatedPose());
    }
}

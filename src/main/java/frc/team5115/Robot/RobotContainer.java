package frc.team5115.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Classes.Accessory.I2CHandler;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Software.*;
import frc.team5115.Commands.Auto.AutoCommandGroup;
import frc.team5115.Commands.Intake.CatchBunny;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.networktables.GenericEntry;

public class RobotContainer {
    private final Joystick joy;
    private final PhotonVision photonVision;
    private final Drivetrain drivetrain;
    private final BunnyCatcher bunnyCatcher;
    private final GenericEntry Rookie;
    private final GenericEntry OutsidePath;
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
        
        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("SmartDashboard");
        Rookie = shuffleboardTab.add("Rookie?", false).getEntry();
        OutsidePath = shuffleboardTab.add("Do Outside Path?", false).getEntry();

        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton(joy, XboxController.Button.kA.value).onTrue(new InstantCommand(drivetrain :: toggleSlowMode));
        new JoystickButton(joy, XboxController.Button.kA.value).onTrue(new CatchBunny(bunnyCatcher, 90, 1));
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
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        drivetrain.resetEncoders();
        drivetrain.resetNAVx();
        drivetrain.stop();

        boolean doOutsidePath = OutsidePath.getBoolean(false);

        autoCommandGroup = new AutoCommandGroup(drivetrain, doOutsidePath);
        autoCommandGroup.schedule();
    }

    public void autoPeriod() {
        // i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        drivetrain.UpdateOdometry();
    }

    public void teleopPeriodic() {
        // i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        drivetrain.UpdateOdometry();
        drivetrain.SwerveDrive(-joy.getRawAxis(1), joy.getRawAxis(4), joy.getRawAxis(0), Rookie.getBoolean(false));
    }
}

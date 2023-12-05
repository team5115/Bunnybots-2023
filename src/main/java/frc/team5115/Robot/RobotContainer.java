package frc.team5115.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
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
    private final Drivetrain drivetrain;
    private final BunnyCatcher bunnyCatcher;
    private final GenericEntry rookie;
    private final GenericEntry outsidePath;
    private final I2CHandler i2cHandler;
    private final NAVx navx;
    private final Arm arm;
    private AutoCommandGroup autoCommandGroup;

    public RobotContainer() {        
        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("SmartDashboard");
        rookie = shuffleboardTab.add("Rookie?", false).getEntry();
        outsidePath = shuffleboardTab.add("Do Outside Path?", false).getEntry();

        joy = new Joystick(0);
        navx = new NAVx();
        i2cHandler = new I2CHandler();

        HardwareDrivetrain hardwareDrivetrain = new HardwareDrivetrain();
        PhotonVision photonVision = new PhotonVision();
        drivetrain = new Drivetrain(hardwareDrivetrain, photonVision, navx, outsidePath);
        
        HardwareBunnyCatcher hardwareBunnyCatcher = new HardwareBunnyCatcher();
        bunnyCatcher = new BunnyCatcher(hardwareBunnyCatcher);
        
        HardwareArm hardwareArm = new HardwareArm(navx, i2cHandler);
        arm = new Arm(hardwareArm);

        configureButtonBindings();
    }

    public void configureButtonBindings() {
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

        boolean doOutsidePath = outsidePath.getBoolean(false);
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
        drivetrain.SwerveDrive(-joy.getRawAxis(1), joy.getRawAxis(4), joy.getRawAxis(0), rookie.getBoolean(false));
    }
}

package frc.team5115.Robot;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Classes.Accessory.I2CHandler;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Software.*;
import frc.team5115.Commands.ArmStateControl.*;
import frc.team5115.Commands.Auto.AutoCommandGroup;

public class RobotContainer {
    private final Joystick joy1;
    private final Joystick joy2;
    private final Drivetrain drivetrain;
    private final BunnyCatcher bunnyCatcher;
    private final GenericEntry rookie;
    private final GenericEntry outsidePath;
    private final I2CHandler i2cHandler;
    private final NAVx navx;
    private final Arm arm;
    private final MechanismCoordination coordination;
    private AutoCommandGroup autoCommandGroup;

    public RobotContainer() {
        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("SmartDashboard");
        rookie = shuffleboardTab.add("Rookie?", false).getEntry();
        outsidePath = shuffleboardTab.add("Do Outside Path?", false).getEntry();

        joy1 = new Joystick(0);
        joy2 = new Joystick(1);
        navx = new NAVx();
        i2cHandler = new I2CHandler();

        HardwareDrivetrain hardwareDrivetrain = new HardwareDrivetrain();
        PhotonVision photonVision = new PhotonVision();
        drivetrain = new Drivetrain(hardwareDrivetrain, photonVision, navx, outsidePath);
        
        HardwareBunnyCatcher hardwareBunnyCatcher = new HardwareBunnyCatcher();
        bunnyCatcher = new BunnyCatcher(hardwareBunnyCatcher);
        
        HardwareArm hardwareArm = new HardwareArm(navx, i2cHandler);
        arm = new Arm(hardwareArm);

        coordination = new MechanismCoordination(MechanismCoordination.State.FullyStowed);

        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton(joy2, XboxController.Button.kA.value).onTrue(new DeployCatcher(bunnyCatcher, coordination));
        new JoystickButton(joy2, XboxController.Button.kB.value).onTrue(new StowCatcher(bunnyCatcher, coordination));
        new JoystickButton(joy2, XboxController.Button.kX.value).onTrue(new DeployArm(arm, coordination));
        new JoystickButton(joy2, XboxController.Button.kY.value).onTrue(new StowArm(arm, coordination));
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

    public void startTeleop(){
        drivetrain.init();
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        
        System.out.println("Starting teleop");
        drivetrain.resetEncoders();
    }

    public void autoPeriod() {
        i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        drivetrain.updateOdometry();
    }

    public void teleopPeriodic() {
        // spin the catcher based on value from right joystick Y axis
        bunnyCatcher.spin(joy2.getRawAxis(5));

        i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        drivetrain.updateOdometry();
        drivetrain.SwerveDrive(-joy1.getRawAxis(1), joy1.getRawAxis(4), joy1.getRawAxis(0), rookie.getBoolean(false));
    }
}

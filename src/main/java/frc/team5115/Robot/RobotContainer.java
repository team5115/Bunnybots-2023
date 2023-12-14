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
    private final Joystick joyDrive;
    private final Joystick joyManips;
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

        joyDrive = new Joystick(0);
        joyManips = new Joystick(1);
        navx = new NAVx();
        i2cHandler = new I2CHandler();

        HardwareDrivetrain hardwareDrivetrain = new HardwareDrivetrain(navx);
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
        new JoystickButton(joyManips, XboxController.Button.kA.value).onTrue(new DeployCatcher(bunnyCatcher, coordination));
        new JoystickButton(joyManips, XboxController.Button.kB.value).onTrue(new StowCatcher(bunnyCatcher, coordination));
        new JoystickButton(joyManips, XboxController.Button.kX.value).onTrue(new DeployArm(arm, coordination));
        new JoystickButton(joyManips, XboxController.Button.kY.value).onTrue(new StowArm(arm, coordination));
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
        drivetrain.init();

        boolean doOutsidePath = outsidePath.getBoolean(false);
        autoCommandGroup = new AutoCommandGroup(drivetrain, doOutsidePath, arm, bunnyCatcher, coordination);
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
        arm.updateController();
    }

    public void teleopPeriodic() {
        i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        drivetrain.updateOdometry();
        arm.updateController();

        // spin the catcher based on value from right joystick Y axis
        bunnyCatcher.spin(joyManips.getRawAxis(5));

        drivetrain.SwerveDrive(-joyDrive.getRawAxis(1), joyDrive.getRawAxis(4), joyDrive.getRawAxis(0), rookie.getBoolean(false));
    }

    public void startTest() {
        drivetrain.init();
    }

    public void testPeriodic() {
        i2cHandler.updatePitch();
        System.out.println("i2c.getPitch() " + i2cHandler.getPitch() + "| navx.getPitch() " + navx.getPitchDeg() + " | arm.getAngle() " + arm.getAngle().toString());
        arm.spinIn();
    }
}

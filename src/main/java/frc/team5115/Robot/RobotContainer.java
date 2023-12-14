package frc.team5115.Robot;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Classes.Accessory.I2CHandler;
import frc.team5115.Classes.Hardware.HardwareArm;
import frc.team5115.Classes.Hardware.HardwareBunnyCatcher;
import frc.team5115.Classes.Hardware.HardwareDrivetrain;
import frc.team5115.Classes.Hardware.NAVx;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Classes.Software.PhotonVision;
import frc.team5115.Commands.Arms.DeployArm;
import frc.team5115.Commands.Arms.DeployCatcher;
import frc.team5115.Commands.Arms.StowArm;
import frc.team5115.Commands.Arms.StowCatcher;
import frc.team5115.Commands.Auto.AutoCommandGroup;

public class RobotContainer {
    private final Joystick joyDrive;
    private final Joystick joyManips;
    private final Drivetrain drivetrain;
    private final BunnyCatcher bunnyCatcher;
    private final GenericEntry rookie;
    private final GenericEntry outsidePath;
    private final GenericEntry testingValue1;
    private final GenericEntry testingValue2;
    private final GenericEntry testingValue3;
    private final I2CHandler i2cHandler;
    private final NAVx navx;
    private final Arm arm;
    private AutoCommandGroup autoCommandGroup;

    public RobotContainer() {
        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("SmartDashboard");
        rookie = shuffleboardTab.add("Rookie?", false).getEntry();
        outsidePath = shuffleboardTab.add("Do Outside Path?", false).getEntry();
        
        testingValue1 = shuffleboardTab.add("test #1 oooh", 0).getEntry();
        testingValue2 = shuffleboardTab.add("test #2 oooh", 0).getEntry();
        testingValue3 = shuffleboardTab.add("test #3 oooh", 0).getEntry();

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

        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton(joyManips, XboxController.Button.kA.value).onTrue(new DeployCatcher(bunnyCatcher, arm));
        new JoystickButton(joyManips, XboxController.Button.kB.value).onTrue(new StowCatcher(bunnyCatcher));
        new JoystickButton(joyManips, XboxController.Button.kX.value).onTrue(new DeployArm(arm, bunnyCatcher));
        new JoystickButton(joyManips, XboxController.Button.kY.value).onTrue(new StowArm(arm));
    }

    public void disabledInit(){
        drivetrain.stop();
    }

    public void stopEverything(){
        drivetrain.stop();
        arm.stop();
    }

    public void startTest() {
    }

    public void testPeriodic() {
    }

    public void startAuto(){
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        drivetrain.resetEncoders();
        drivetrain.resetNAVx();
        drivetrain.stop();
        drivetrain.init();

        boolean doOutsidePath = outsidePath.getBoolean(false);
        autoCommandGroup = new AutoCommandGroup(drivetrain, doOutsidePath, arm, bunnyCatcher);
        autoCommandGroup.schedule();
    }

    public void autoPeriod() {
        i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        drivetrain.updateOdometry();
        arm.updateController();
    }

    public void startTeleop(){
        // drivetrain.init();
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        
        System.out.println("Starting teleop");
        drivetrain.resetEncoders();
    }

    public void teleopPeriodic() {
        i2cHandler.updatePitch();
        bunnyCatcher.updateAngle();
        // drivetrain.updateOdometry();
        arm.updateController();

        // spin the catcher based on value from right joystick Y axis
        // bunnyCatcher.spin(joyManips.getRawAxis(5) / 4.0);

        // drivetrain.SwerveDrive(-joyDrive.getRawAxis(1), joyDrive.getRawAxis(4), joyDrive.getRawAxis(0), rookie.getBoolean(false));
    }
}

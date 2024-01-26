package frc.team5115.Robot;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team5115.Classes.Accessory.I2CHandler;
import frc.team5115.Classes.Hardware.HardwareArm;
import frc.team5115.Classes.Hardware.HardwareDrivetrain;
import frc.team5115.Classes.Hardware.NAVx;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Classes.Software.PhotonVision;
import frc.team5115.Commands.Arms.DeployArm;
import frc.team5115.Commands.Arms.StowArm;
import frc.team5115.Commands.Auto.AutoCommandGroup;

public class RobotContainer {
    private final Joystick joyDrive;
    private final Joystick joyManips;
    private final Drivetrain drivetrain;
    // private final BunnyCatcher bunnyCatcher;
    private final GenericEntry rookie;
    private final GenericEntry outsidePath;
    private final GenericEntry doAuto;
    // private final GenericEntry testingValue1;
    // private final GenericEntry testingValue2;
    // private final GenericEntry testingValue3;
    private final I2CHandler i2cHandler;
    private final NAVx navx;
    private final Arm arm;
    private AutoCommandGroup autoCommandGroup;

    public RobotContainer() {
        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("SmartDashboard");
        rookie = shuffleboardTab.add("Rookie?", false).getEntry();
        outsidePath = shuffleboardTab.add("Do Outside Path?", false).getEntry();
        doAuto = shuffleboardTab.add("Do auto at all?", false).getEntry();
        // testingValue1 = shuffleboardTab.add("test #1", 0).getEntry();
        // testingValue2 = shuffleboardTab.add("test #2", 0).getEntry();
        // testingValue3 = shuffleboardTab.add("test #3", 0).getEntry();

        joyDrive = new Joystick(0);
        joyManips = new Joystick(1);
        navx = new NAVx();
        i2cHandler = new I2CHandler();

        HardwareDrivetrain hardwareDrivetrain = new HardwareDrivetrain(navx);
        PhotonVision photonVision = new PhotonVision();
        drivetrain = new Drivetrain(hardwareDrivetrain, photonVision, navx, outsidePath);
        
        // HardwareBunnyCatcher hardwareBunnyCatcher = new HardwareBunnyCatcher();
        // bunnyCatcher = new BunnyCatcher(hardwareBunnyCatcher);
        
        HardwareArm hardwareArm = new HardwareArm(navx, i2cHandler);
        arm = new Arm(hardwareArm);

        configureButtonBindings();
    }

    public void configureButtonBindings() {
        new JoystickButton(joyManips, XboxController.Button.kB.value).onTrue(new StowArm(arm));
        new JoystickButton(joyManips, XboxController.Button.kA.value).onTrue(new DeployArm(arm));
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

        autoCommandGroup = new AutoCommandGroup(drivetrain,
            outsidePath.getBoolean(false),
            doAuto.getBoolean(true));
        autoCommandGroup.schedule();
    }

    public void autoPeriod() {
        // i2cHandler.updatePitch();
        // bunnyCatcher.updateAngle();
        drivetrain.updateOdometry();
        // arm.updateController();
    }

    public void startTeleop(){
        if(autoCommandGroup != null) autoCommandGroup.cancel();
        
        System.out.println("Starting teleop");
        drivetrain.resetEncoders();
    }

    public void teleopPeriodic() {

        // drivetrain.updateOdometry();
        // i2cHandler.updatePitch();
        // bunnyCatcher.updateAngle();
        // arm.updateController();

        // // spin bunny catcher in or out
        // if (joyManips.getRawButton(XboxController.Button.kLeftBumper.value)) {
        //     bunnyCatcher.spin(-0.2);
        // } else if (joyManips.getRawAxis(XboxController.Axis.kLeftTrigger.value) > 0.3) {
        //     bunnyCatcher.spin(+0.2);
        // } else {
        //     bunnyCatcher.spin(+0);
        // }

        // spin berry catcher in or out
        if (joyManips.getRawButton(XboxController.Button.kRightBumper.value)) {
            arm.spin(+0.65);
        } else if (joyManips.getRawAxis(XboxController.Axis.kRightTrigger.value) > 0.3) {
            arm.spin(-0.65);
        } else {
            arm.spin(+0);
        }

        arm.turnRaw(joyManips.getRawAxis(XboxController.Axis.kLeftY.value) * -0.3);

        drivetrain.SwerveDrive(-joyDrive.getRawAxis(1), joyDrive.getRawAxis(4), joyDrive.getRawAxis(0), rookie.getBoolean(false));
    }
}

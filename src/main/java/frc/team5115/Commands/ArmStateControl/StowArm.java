package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.Action;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Commands.Intake.MoveArm;

public class StowArm extends SequentialCommandGroup {
    final MechanismCoordination coordination;
    Command callback;

    public StowArm(Arm arm, MechanismCoordination coordination) {
        this.coordination = coordination;

        addCommands(
            new InstantCommand(this::checkForAllowability),
            new MoveArm(new Angle(0), arm).withTimeout(10), // TODO determine what angle is stowed
            new WaitCommand(0.1),
            new InstantCommand(this::runCallback)
        );
    }

    private void checkForAllowability() {
        callback = coordination.tryPerformAction(Action.StowArm);
        if (callback == null) end(true);
    }
    
    private void runCallback() {
        callback.schedule();
    }
}

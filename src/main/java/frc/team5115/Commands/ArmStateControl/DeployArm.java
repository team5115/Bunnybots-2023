package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.Action;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Commands.Intake.MoveArm;

public class DeployArm extends SequentialCommandGroup {
    public DeployArm(Arm arm, MechanismCoordination coordination) {
        // check for the action, then the callback will run at the end of _this_ command group and will set the state to the new state
        Command callback = coordination.tryPerformAction(Action.DeployArm);
        if (callback == null) return;

        addCommands(
            // TODO determine what angle is deployed
            new MoveArm(new Angle(90), arm),
            new WaitCommand(0.1),
            callback
        );
    }
}

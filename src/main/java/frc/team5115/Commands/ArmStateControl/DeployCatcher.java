package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.Action;
import frc.team5115.Classes.Software.BunnyCatcher;

public class DeployCatcher extends SequentialCommandGroup {
    public DeployCatcher(BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        // check for the action, then the callback will run at the end of _this_ command group and will set the state to the new state
        Command callback = coordination.tryPerformAction(Action.DeployCatcher);
        if (callback == null) return;

        addCommands(
            new InstantCommand(bunnyCatcher::deployCatcher),
            new WaitCommand(0.5),
            callback
        );
    }
}
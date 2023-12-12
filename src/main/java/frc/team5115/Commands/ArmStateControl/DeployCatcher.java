package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.Action;
import frc.team5115.Classes.Software.BunnyCatcher;

public class DeployCatcher extends SequentialCommandGroup {
    final MechanismCoordination coordination;
    Command callback;

    public DeployCatcher(BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        this.coordination = coordination;

        addCommands(
            new InstantCommand(this::checkForAllowability),
            new InstantCommand(bunnyCatcher::deployCatcher),
            new WaitCommand(0.5),
            callback
        );
    }
    
    private void checkForAllowability() {
        Command callback = coordination.tryPerformAction(Action.DeployCatcher);
        if (callback == null) end(true);
    }
}
package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.Action;
import frc.team5115.Classes.Software.BunnyCatcher;

public class StowCatcher extends SequentialCommandGroup {
    final MechanismCoordination coordination;
    Command callback;

    public StowCatcher(BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        this.coordination = coordination;

        addCommands(
            new InstantCommand(this::checkForAllowability),
            new InstantCommand(bunnyCatcher::stowCatcher),
            new WaitCommand(0.5),
            new InstantCommand(this::runCallback)
        );
    }
    
    private void checkForAllowability() {
        callback = coordination.tryPerformAction(Action.StowCatcher);
        if (callback == null) end(true);
    }
    
    private void runCallback() {
        callback.schedule();
    }
}
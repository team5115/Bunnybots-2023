package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Software.BunnyCatcher;
import frc.team5115.Commands.Intake.SpinBunnyCatcher;

public class CatchBunny extends SequentialCommandGroup {
    public CatchBunny(BunnyCatcher bunnyCatcher) {
        addCommands(
            new InstantCommand(bunnyCatcher::deployCatcher),
            new WaitCommand(0.5),
            new SpinBunnyCatcher(bunnyCatcher, 180, 1),
            new WaitCommand(0.5),
            new InstantCommand(bunnyCatcher::stowCatcher)
        );
    }   
}

package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Classes.Software.Paths;
import frc.team5115.Commands.Auto.VisionAuto.DriveForwardWVision;

public class AutoCommandGroup extends SequentialCommandGroup {
    final Drivetrain drivetrain;
	final Paths paths;

    public AutoCommandGroup(Drivetrain drivetrain, boolean inIdealPosition){
        this.drivetrain = drivetrain;
		this.paths = new Paths();
        }
    
	/**
	 * Places a cube in a low node. Expects that the robot has its back facing the grid and has a cube on its base.
	 */
    private void ScoreBerry() {
        addCommands(    
            new DriveForwardWVision(drivetrain, -0.26, 0.5), // 
            new DriveForwardWVision(drivetrain, +0.65, 1.2), //
            new DriveForwardWVision(drivetrain, -0.85, 0.8) // 
        );
        // 
    }

	/**
	 * Exits the community.
	 */
    private void exitDen() {
        addCommands(
            new DriveForwardWVision(drivetrain, +3, 1.0), // exit community
            new InstantCommand(drivetrain :: stop)
        );
    }

	/**
	 * Exits the community. Uses PathPlanner instead of the `DriveForward`.
	 */

     /* 
	private void exitDenPathPlanner() {
		addCommands(
			drivetrain.getRamseteCommand(paths.ExitCommunity)
		);
	}
    */
}

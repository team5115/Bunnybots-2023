package frc.team5115.Classes.Accessory;

public class MechanismCoordination {

    public enum State {
        FullyStowed, // both arms are stowed, with the berry on the outside and the bunny catcher locked in behind it
        BunnyStowedBerryDeployed, // the bunny catcher is stowed, but the berry catcher is out
        FullyDeployed // both arms are deployed
    }

    public enum Action {
        DeployArm,
        StowArm,
        DeployCatcher,
        StowCatcher
    }

    private State state;

    public MechanismCoordination(State mechState) {
        this.state = mechState;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        System.out.println("MechanismCoordination sets a new state of: " + state.toString());
        this.state = state;
    }

    /**
     * Performs an action on the state of the machine. Will update the state based on that
     * @param action the action to attempt to perform
     * @return if the action actually changed the state
     */
    public boolean tryPerformAction(Action action) {
        State newState = attemptActionOnState(state, action);
        boolean success = newState == state;
        state = newState;
        return success;
    }

    /**
     * Try to perform an action on a state, and then tells you what the resulting state would be
     * @param state the current state
     * @param action the action to perform
     * @return returns the new state, given the action
     */
    public static State attemptActionOnState(State state, Action action) {
        switch (action) {
            case DeployArm:
                if (state == State.FullyStowed) return State.BunnyStowedBerryDeployed;
                break;

            case StowArm:
                if (state == State.BunnyStowedBerryDeployed) return State.FullyStowed;
                break;

            case DeployCatcher:
                if (state == State.BunnyStowedBerryDeployed) return State.FullyDeployed;
                break;

            case StowCatcher:
                if (state == State.FullyDeployed) return State.BunnyStowedBerryDeployed;
                break;
            default:
                break;
        }
        // the action couldn't be run on the state, so the state does not change
        return state;
    }
}

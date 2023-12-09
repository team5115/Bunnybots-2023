package frc.team5115.Classes.Accessory;

public class MechanismCoordination {
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

    public enum State {
        FullyStowed, // both arms are stowed, with the berry on the outside and the bunny catcher locked in behind it
        BunnyStowedBerryDeployed, // the bunny catcher is stowed, but the berry catcher is out
        FullyDeployed // both arms are deployed
    }
}

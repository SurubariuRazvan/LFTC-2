package JSON;

import java.util.Set;

public class FiniteStateMachineJSON {
    String         description;
    String         startState;
    Set<StateJSON> states;
    
    public String getDescription() {
        return description;
    }
    
    public String getStartState() {
        return startState;
    }
    
    public Set<StateJSON> getStates() {
        return states;
    }
}
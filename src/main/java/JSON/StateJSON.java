package JSON;

import java.util.Map;

public class StateJSON {
    String             name;
    boolean            isFinalState;
    Map<String,String> transitions;
    
    public String getName() {
        return name;
    }
    
    public boolean isFinalState() {
        return isFinalState;
    }
    
    public Map<String,String> getTransitions() {
        return transitions;
    }
}
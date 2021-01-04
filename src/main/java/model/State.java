package model;

import JSON.StateJSON;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;
import java.util.stream.Collectors;

public class State {
    private final String                        name;
    private final boolean                       isFinalState;
    private final Multimap<String,State>        transitions;
    private       Map<State,TransitionFunction> inverseTransitions;
    
    public State(StateJSON stateJSON) {
        this.name = stateJSON.getName();
        this.isFinalState = stateJSON.isFinalState();
        this.transitions = HashMultimap.create();
    }
    
    public void addTransition(String symbol, State state) {
        this.transitions.put(symbol, state);
    }
    
    public State getNextState(String character) {
        var a = transitions.get(character);
        if(a.size() > 0)
            return (State) a.toArray()[0];
        else
            return null;
    }
    
    @Override
    public String toString() {
        return "State{" +
                "name='" + name +
                ", isFinalState=" + isFinalState +
                ", transitions=" + transitions.keySet()
                .stream()
                .collect(Collectors.toMap(key->key, key->transitions.get(key)
                        .stream()
                        .map(State::getName)
                        .collect(Collectors.toList()))) +
                '}';
    }
    
    public String getName() {
        return name;
    }
    
    public Map<State,TransitionFunction> getInverseTransitions() {
        return inverseTransitions;
    }
    
    public void setInverseTransitions(Map<State,TransitionFunction> inverseTransitions) {
        this.inverseTransitions = inverseTransitions;
    }
    
    public boolean isFinalState() {
        return isFinalState;
    }
}

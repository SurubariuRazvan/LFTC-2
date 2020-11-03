package model;

import JSON.FiniteStateMachineJSON;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.stream.Collectors;

public class FiniteStateMachine {
    private String            description;
    private State             startState;
    private Map<String,State> states;
    
    public FiniteStateMachine(String filePath) {
        try {
            convertJSON(new Gson().fromJson(new FileReader(filePath), FiniteStateMachineJSON.class));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void convertJSON(FiniteStateMachineJSON json) {
        this.description = json.getDescription();
        this.states = json.getStates().stream().map(State::new).collect(Collectors.toMap(State::getName, state->state));
        
        json.getStates().forEach(stateJSON->stateJSON.getTransitions().keySet()
                .forEach(stateID->TransitionFunction.extractSymbols(stateJSON.getTransitions().get(stateID))
                        .forEach(symbol->this.states.get(stateJSON.getName()).addTransition(symbol, this.states.get(stateID)))));
        
        json.getStates().forEach(stateJSON->this.states.get(stateJSON.getName())
                .setInverseTransitions(stateJSON.getTransitions().keySet().stream().collect
                        (Collectors.toMap(this.states::get, key->new TransitionFunction(stateJSON.getTransitions().get(key))))));
        this.startState = states.get(json.getStartState());
    }
    
    public FiniteStateMachine(FiniteStateMachineJSON json) {
        convertJSON(json);
    }
    
    @Override
    public String toString() {
        return "FiniteStateMachine{" +
                "description='" + description +
                ",\nstartState=" + startState +
                ",\nstates=" + states.values().stream().map(State::toString).collect(Collectors.joining("\n")) +
                '}';
    }
    
    public Map<String,State> getStates() {
        return states;
    }
    
    public boolean checkSequence(String sequence) {
        return checkSequence(sequence, 0) == sequence.length();
    }
    
    public int checkSequence(String sequence, int startIndex) {
        char[] charSequence = sequence.toCharArray();
        int length = 0;
        State currentState = startState;
        for(int i = startIndex; i < charSequence.length; i++) {
            State nextState = currentState.getNextState(String.valueOf(charSequence[i]));
            if(nextState == null)
                if(currentState.isFinalState())
                    return length;
                else
                    return 0;
            else
                currentState = nextState;
            length++;
        }
        if(currentState.isFinalState())
            return length;
        else
            return 0;
    }
}

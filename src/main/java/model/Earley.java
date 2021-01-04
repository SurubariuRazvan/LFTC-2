package model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Earley {
    private final Grammar grammar;
    private final List<Set<State>> stateSets;

    public Earley(Grammar grammar) {
        this.grammar = grammar;
        this.stateSets = new ArrayList<>();
    }

    public Earley(List<String> lines) {
        this.grammar = new Grammar(lines);
        this.stateSets = new ArrayList<>();
    }

    public Earley(String path) {
        this.grammar = new Grammar(path);
        this.stateSets = new ArrayList<>();
    }

    public Pair<Boolean, List<String>> solve(String sequence) {
        return solve(Arrays.asList(sequence.replaceAll("\\s+", " ").split(" ")));
    }

    public Pair<Boolean, List<String>> solve(List<String> sequence) {
        stateSets.clear();
        List<String> words = new ArrayList<>(sequence);
        words.add(" "); //ending symbol (can't be in the grammar because everything is split by it)
        Set<State> firstStateSet = new HashSet<>();
        Production firstProduction = new Production(grammar.getFirstNonTerminal(), new String[]{grammar.getFirstNonTerminal()});
        firstStateSet.add(new State(firstProduction, 0, 0));
        stateSets.add(firstStateSet);
        for (int k = 0; k < words.size(); k++) {
            stateSets.add(new HashSet<>()); // added on k+1
            process(words, k, stateSets.get(k));
        }
        return isAccepted(firstProduction, words.size() - 1);
    }

    private void process(List<String> words, int k, Set<State> stateSet) {
        Set<State> toAdd;
        do {
            toAdd = new HashSet<>();
            for (State state : stateSet) {
                if (state.getRhsIdx() == state.getProd().getProdRhs().length)
                    toAdd.addAll(completer(state));
                else if (grammar.getNonTerminals().contains(state.getProd().getProdRhs()[state.getRhsIdx()]))
                    toAdd.addAll(predictor(state, k));
                    // TODO  check if this part can be deleted
                else if (state.getProd().getProdRhs()[state.getRhsIdx()].equals(words.get(k)))
                    stateSets.get(k + 1).add(scanner(state));
            }
        } while(stateSet.addAll(toAdd));
    }

    private Pair<Boolean, List<String>> isAccepted(Production production, int lastIndex) {
        State finalState = new State(production, 1, 0);
        Set<State> lastStateSet = stateSets.get(lastIndex);
        for (State s : lastStateSet) {
            if (s.equals(finalState))
                return new Pair<>(true, print());
        }
        return new Pair<>(false, print());
    }

    private Set<State> completer(State state) {
        return this.stateSets.get(state.getPrevSet()).stream()
                .filter((s) -> s.getRhsIdx() < s.getProd().getProdRhs().length
                        && s.getProd().getProdRhs()[s.getRhsIdx()].equals(state.getProd().getProdHead()))
                .map(s -> new State(s.getProd(), s.getRhsIdx() + 1, s.getPrevSet()))
                .collect(Collectors.toSet());
    }

    private Set<State> predictor(State state, int k) {
        String b = state.getProd().getProdRhs()[state.getRhsIdx()];
        return this.grammar.getProductions().stream()
                .filter(p -> p.getProdHead().equals(b)).map(p -> new State(p, 0, k))
                .collect(Collectors.toSet());
    }

    private State scanner(State state) {
        return new State(state.getProd(), state.getRhsIdx() + 1, state.getPrevSet());
    }

    private List<String> print() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < this.stateSets.size(); i++) {
            StringBuilder stateResult = new StringBuilder("----------- State set " + i + " -----------\n");
            for (State s : this.stateSets.get(i)) {
                stateResult.append(s.getProd().getProdHead()).append(" -> ");
                for (int j = 0; j < s.getRhsIdx() && j < s.getProd().getProdRhs().length; j++) {
                    stateResult.append(s.getProd().getProdRhs()[j]).append(" ");
                }
                stateResult.append("*");
                for (int j = s.getRhsIdx(); j < s.getProd().getProdRhs().length; j++) {
                    stateResult.append(s.getProd().getProdRhs()[j]).append(" ");
                }
                stateResult.append(", Origin(").append(s.getPrevSet()).append(")\n");
            }
            result.add(stateResult.toString());
        }
        return result;
    }

    private static class State {
        private final Production prod;
        private final int rhsIdx;
        private final int prevSet;

        public State(Production prod, int rhsIdx, int prevSet) {
            this.prod = prod;
            this.rhsIdx = rhsIdx;
            this.prevSet = prevSet;
        }

        @Override
        public int hashCode() {
            int result = getProd() != null ? getProd().hashCode() : 0;
            result = 31 * result + getRhsIdx();
            result = 31 * result + getPrevSet();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof State)) {
                return false;
            } else {
                State s = (State) o;
                return getRhsIdx() == s.getRhsIdx() && getPrevSet() == s.getPrevSet() && getProd().equals(s.getProd());
            }
        }

        public Production getProd() {
            return prod;
        }

        public int getRhsIdx() {
            return rhsIdx;
        }

        public int getPrevSet() {
            return prevSet;
        }
    }
}
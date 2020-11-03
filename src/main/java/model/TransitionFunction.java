package model;

import java.util.HashSet;
import java.util.Set;

public class TransitionFunction {
    private final Set<String> symbols;
    
    public TransitionFunction(String rawString) {
        this.symbols = extractSymbols(rawString);
    }
    
    public static Set<String> extractSymbols(String rawString) {
        Set<String> symbols = new HashSet<>();
        for(String symbol : rawString.split(",")) {
            if(symbol.contains("-") && !symbol.equals("-")) {
                for(char current = symbol.split("-")[0].charAt(0); current <= symbol.split("-")[1].charAt(0); current++) {
                    symbols.add(String.valueOf(current));
                }
            } else
                symbols.add(symbol);
        }
        if(rawString.charAt(0) == ',')
            symbols.add(",");
        return symbols;
    }
    
    @Override
    public String toString() {
        return "TransitionFunction{" +
                "symbols=" + symbols +
                '}';
    }
    
    public Set<String> getSymbols() {
        return symbols;
    }
}
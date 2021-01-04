package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Grammar {
    private final Set<String> terminals;
    private final Set<String> nonTerminals;
    //<nonTerminal, terminals>
    private final Map<String, Set<String>> productionRules;
    //<terminal, nonTerminals>
    private final Map<String, Set<String>> inverseProductionRules;
    private final ArrayList<Production> productions;
    private String firstNonTerminal;

    public Grammar(String path) {
        this(readFile(path));
    }

    public Grammar(List<String> lines) {
        this.terminals = new HashSet<>();
        this.nonTerminals = new HashSet<>();
        this.productionRules = new HashMap<>();
        this.inverseProductionRules = new HashMap<>();
        this.productions = new ArrayList<>();

        lines.stream().filter(line -> line.contains("->"))
                .forEach(line -> nonTerminals.add(line.split("->", 2)[0].trim()));

        String lastNonTerminal = null;
        for (String line : lines) {
            String[] leftAndRight = line.split("->", 2);
            if (leftAndRight.length == 2) {
                if (lastNonTerminal == null)
                    this.firstNonTerminal = leftAndRight[0].trim();
                lastNonTerminal = leftAndRight[0].trim();
            }
            if (lastNonTerminal == null)
                throw new RuntimeException("The file doesn't start with a nonTerminal.");
            for (String rightRules : leftAndRight[leftAndRight.length - 1].trim().split("\\|")) {
                rightRules = rightRules.replaceAll("\\s+", " ").trim();
                if (!rightRules.equals("")) {
                    for (String rightRule : rightRules.split(" ")) {
                        rightRule = rightRule.trim();
                        if (!rightRule.equals("") && !nonTerminals.contains(rightRule)) {
                            terminals.add(rightRule);
                            addToMultiMap(inverseProductionRules, rightRule, lastNonTerminal);
                        }
                    }
                    addToMultiMap(productionRules, lastNonTerminal, rightRules);
                    productions.add(new Production(lastNonTerminal, rightRules.split(" ")));
                }
            }
        }
    }

    private static List<String> readFile(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static <K, V> void addToMultiMap(Map<K, Set<V>> map, K key, V value) {
        if (!map.containsKey(key))
            map.put(key, new HashSet<>());
        map.get(key).add(value);
    }

    public String getFirstNonTerminal() {
        return firstNonTerminal;
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Map<String, Set<String>> getProductionRules() {
        return productionRules;
    }

    public Map<String, Set<String>> getProductionRulesForTerminal(String terminal) {
        return inverseProductionRules.get(terminal).stream().collect(Collectors.toMap(key -> key, productionRules::get));
    }

    public void printTerminals() {
        System.out.println("printTerminals:");
        System.out.println(this.terminals);
    }

    public void printNonTerminals() {
        System.out.println("printNonTerminals:");
        System.out.println(this.nonTerminals);
    }

    public void printProductionRules() {
        System.out.println("printProductionRules:");
        System.out.println(this.productionRules);
    }

    public void printProductionRulesForATerminal(String terminal) {
        System.out.println("printProductionRulesForATerminal:");
        System.out.println(inverseProductionRules.get(terminal));
    }

    public void printProductions() {
        System.out.println("printProductions:");
        System.out.println(this.productions);
    }
}

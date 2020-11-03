package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    public List<String> tokenize(String path) {
        return tokenize(readFile(path));
    }
    
    public List<String> tokenize(List<String> rawLines) {
        List<String> tokens = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for(String line : rawLines) {
            int end = 0;
            Matcher m = Pattern.compile("(\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\")").matcher(line);
            while(m.find()) {
                lines.add(line.substring(end, m.start(1)));
                lines.add(line.substring(m.start(1), m.end(1)));
                end = m.end(1);
            }
            if(end == 0)
                lines.add(line);
            else
                lines.add(line.substring(end));
        }
        for(var line : lines) {
            if(!line.isEmpty())
                if(line.matches("^\".*\"$"))
                    tokens.add(line);
                else
                    tokens.addAll(Arrays.asList(line.replaceAll(
                            " *(<<|>>)|(<=|>=|==|!=|&&|\\|\\||<[a-zA-Z][a-zA-Z0-9]*(\\.[a-zA-Z]+)?>|[/;=<>()*%{}+-])", "$1 $2 ")
                                                        .trim()
                                                        .split("\\s+")));
        }
        
        return tokens;
    }
    
    private List<String> readFile(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

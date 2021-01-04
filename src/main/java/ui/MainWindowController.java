package ui;

import JSON.FiniteStateMachineJSON;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Earley;
import model.FiniteStateMachine;
import model.Grammar;
import model.MyTreeMap;
import model.State;
import model.Tokenizer;
import model.TransitionFunction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    public JFXListView<String> p1List;
    public JFXButton finalStateSetButton;
    public JFXButton transitionsButton;
    public JFXButton alphabetButton;
    public JFXButton stateSetButton;
    public MaterialDesignIconView exitButton;
    public MaterialDesignIconView alwaysOnTopToggle;
    public JFXButton p1ChooseFileButton;
    public JFXTextArea finiteStateMachineText;
    public Tab p1InterpretTab;
    public StackPane rooPane;
    public JFXTabPane p1TabPane;
    public Tab p1InputDataTab;
    public Tab testSequenceTab;
    public JFXTextArea testSequenceTextField;
    public JFXButton checkButton;
    public JFXTextField testSequenceResult;
    public JFXDrawer p1FileDrawer;
    public JFXListView<String> p1FileDrawerListView;
    public JFXHamburger p1HamburgerButton;
    public Tab p2InputDataTab;
    public JFXTextArea codeText;
    public JFXHamburger p2HamburgerButton;
    public JFXDrawer p2FileDrawer;
    public JFXListView<String> p2FileDrawerListView;
    public JFXButton p2ChooseFileButton;
    public Tab p2InterpretTab;
    public JFXListView<String> p2FIP;
    public JFXListView<String> p2TSID;
    public JFXListView<String> p2TSCONST;
    public JFXListView<String> p2Errors;
    public JFXTabPane p2TabPane;
    public JFXTabPane p5TabPane;
    public Tab p5InputDataTab;
    public JFXTextArea grammarText;
    public JFXHamburger p5HamburgerButton;
    public JFXDrawer p5FileDrawer;
    public JFXListView<String> p5FileDrawerListView;
    public JFXButton p5ChooseFileButton;
    public Tab p5InterpretTab;
    public JFXListView<String> p5List;
    public JFXButton terminalsButton;
    public JFXButton nonTerminalsButton;
    public JFXButton productionRulesButton;
    public JFXButton productionRulesForTerminalButton;
    public JFXComboBox<String> terminalsComboBox;
    public Tab p5TestSequenceTab;
    public JFXListView<String> p5SequenceResultListView;
    public JFXTextField p5TestSequenceResult;
    public JFXButton p5CheckButton;
    public JFXTextArea p5TestSequenceTextArea;
    public Tab p6InputCode;
    public JFXButton p6ChooseFileButton;
    public JFXTextArea p6CodeText;
    public Tab p6InterpretCode;
    public JFXTabPane p6TabPane;
    public Tab p6ChooseCode;
    public JFXListView<String> p6InterpretCodeList;
    private Stage stage;
    private double x;
    private double y;
    private FiniteStateMachine finiteStateMachine;
    private Grammar grammar;
    private Earley earley;
    private Earley earleyMLP;
    private HamburgerBasicCloseTransition p1Task;
    private HamburgerBasicCloseTransition p2Task;
    private HamburgerBasicCloseTransition p5Task;
    private MyTreeMap<String, Integer> operatorsMap;
    private FiniteStateMachine doubleAFD;
    private FiniteStateMachine stringAFD;
    private FiniteStateMachine libraryAFD;
    private FiniteStateMachine idAFD;

    @FXML
    void mouseDrag(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void mousePress(MouseEvent event) {
        this.x = event.getSceneX();
        this.y = event.getSceneY();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operatorsMap = getOperatorsMap();
        doubleAFD = new FiniteStateMachine("./finiteStateMachine/doubleNo0.json");
        stringAFD = new FiniteStateMachine(("./finiteStateMachine/string.json"));
        libraryAFD = new FiniteStateMachine("./finiteStateMachine/library.json");
        idAFD = new FiniteStateMachine("./finiteStateMachine/id.json");
        p1FileDrawerListView.setItems(FXCollections.observableArrayList(
                "<program>     := {\"description\":\"CONST\",\"startState\":\"ID\",\"states\":[<list>]}",
                "<list>        := <list>, <state> | <state>",
                "<state>       := { \"name\": \"ID\", \"isFinalState\":<bool>, \"transitions\":<transitions>}",
                "<bool>        := true | false",
                "<transitions> := <transitions>, <transition> | <transition>",
                "<transition>  := \"ID\":\"SYMBOLS\"",
                "",
                "ID:      ^[a-zA-Z][a-zA-Z0-9]*",
                "SYMBOLS: ^([^\"],|[^\"]-[^\"],)*([^\"]|[^\"]-[^\"])",
                "COSNT:   ^[^\"]*"
        ));
        p1FileDrawer.setSidePane(p1FileDrawerListView);
        p1Task = new HamburgerBasicCloseTransition(p1HamburgerButton);
        p1Task.setRate(-1);

        p2FileDrawerListView.setItems(FXCollections.observableArrayList(
                "<program>        := <libraries> <functions> | <functions>",
                "<libraries>      := <library> <libraries> | <library>",
                "<library>        := #include \" \" LIBRARY \"\\n\"",
                "<functions>      := <function> \"\\n\" <functions> | <function> \"\\n\"",
                "<function>       := <declaration> \" \" (<parameters>){ <function_body> }",
                "<declaration>    := <type> ID",
                "<type>           := int | double | int[]",
                "<parameters>     := <declaration>, <parameters>",
                "<parameters>     := <declaration>",
                "<parameters>     := \"\"",
                "<function_body>  := <instructions> return 0;",
                "<instructions>   := <instruction> <instructions>",
                "<instructions>   := <instruction> ;",
                "<instruction>    := <initialization>",
                "<instruction>    := <assignation>",
                "<instruction>    := <if_instruction> ",
                "<instruction>    := <for_instruction>",
                "<initialization> := <declaration> = <expression> <init_list>;",
                "<initialization> := <declaration> <init_list>;",
                "<init_list>      := , ID = <expression> <init_list>",
                "<init_list>      := , ID <init_list>",
                "<assignation>    := ID = <expression> ;",
                "<expression>     := <expression> || <expression> ",
                "<expression>     := <expression> && <expression> ",
                "<expression>     := <expression> % <operator> ",
                "<expression>     := <expression> / <operator> ",
                "<expression>     := <expression> < <operator> ",
                "<expression>     := <expression> > <operator> ",
                "<expression>     := <expression> == <operator> ",
                "<expression>     := <expression> * <operator>",
                "<expression>     := <expression> != <operator>",
                "<expression>     := <expression> <= <operator>",
                "<expression>     := <expression> >= <operator>",
                "<expression>     := <operator>",
                "<operator>       := COSNT | ID",
                "<if_instruction> := if(<expression>){ <instructions> }else{ <instructions> }",
                "<if_instruction> := if(<expression>){ <instructions> }",
                "<for_instruction>:= for(<initialization>;<expression>;<assignation>){ <instructions>}",
                "<read>           := std::cin>> ID",
                "<write>          := std::cout<< <operator> | STRING",
                "",
                "ID: ^[a-zA-Z][a-zA-Z0-9]*",
                "LIBRARY: ^[a-zA-Z][a-zA-Z0-9.]*",
                "COSNT: ^\\d+(\\.\\d+)?",
                "STRING:  ^\".*\"$"
        ));
        p2FileDrawer.setSidePane(p2FileDrawerListView);
        p2Task = new HamburgerBasicCloseTransition(p2HamburgerButton);
        p2Task.setRate(-1);

        p5TabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (!newTab.equals(p6InputCode) && !newTab.equals(p5InputDataTab))
                try {
                    this.grammar = new Grammar(Arrays.asList(grammarText.getText().split("\n")));
                    this.terminalsComboBox.setItems(FXCollections.observableList(new ArrayList<>(this.grammar.getTerminals())));
                    this.terminalsComboBox.setValue(null);
                    this.p5List.setItems(FXCollections.observableArrayList());
                    this.earley = new Earley(this.grammar);
                } catch (Exception e) {
                    showError("Error", e.getMessage(), () -> p5TabPane.getSelectionModel().select(p5InputDataTab));
                }
        });
        p5FileDrawerListView.setItems(FXCollections.observableArrayList(""));
        p5FileDrawer.setSidePane(p5FileDrawerListView);
        p5Task = new HamburgerBasicCloseTransition(p5HamburgerButton);
        p5Task.setRate(-1);
    }

    private MyTreeMap<String, Integer> getOperatorsMap() {
        List<String> operators = Arrays.asList("ID", "CONST", "LIBRARY", "#include", "<=", ">=", "==", "!=", "&&", "||", "<", ">",
                                               "/", "+", "-", "*", "%", "=", "(", ")", "{", "}", "int", "double",
                                               "std::cin>>", "std::cout<<", "if", "for", "else", "return", ";");
        MyTreeMap<String, Integer> operatorsMap = new MyTreeMap<>();
        for (int i = 0; i < operators.size(); i++) {
            operatorsMap.put(operators.get(i), i);
        }
        return operatorsMap;
    }

    private void showError(String title, String message, Runnable afterClose) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(rooPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> dialog.close());

        dialogLayout.setHeading(new Label(title));
        dialogLayout.getStyleClass().add("errorHeading");
        dialogLayout.setBody(new Label(message));
        dialogLayout.setActions(button);
        dialog.show();
        BoxBlur blur = new BoxBlur(3, 3, 2);
        p1TabPane.setEffect(blur);
        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            p1TabPane.setEffect(null);
            afterClose.run();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void alwaysOnTopToggle(MouseEvent mouseEvent) {
        stage.setAlwaysOnTop(!stage.isAlwaysOnTop());
    }

    public void close(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void showStateSet(ActionEvent actionEvent) {
        p1List.setItems(FXCollections.observableArrayList(finiteStateMachine.getStates().keySet().stream().sorted().collect(
                Collectors.toList())));
    }

    public void showAlphabet(ActionEvent actionEvent) {
        p1List.setItems(FXCollections.observableArrayList(
                finiteStateMachine.getStates().values().stream()
                        .map(state -> state.getInverseTransitions().values().stream()
                                .map(TransitionFunction::getSymbols).collect(Collectors.toSet()))
                        .flatMap(Set::stream).collect(Collectors.toSet()).stream().flatMap(Set::stream)
                        .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList())));
    }

    public void showTransitions(ActionEvent actionEvent) {
        p1List.setItems(FXCollections.observableArrayList(
                finiteStateMachine.getStates().values().stream()
                        .map(state -> state.getName() + ": transitions:{ " +
                                state.getInverseTransitions().keySet().stream()
                                        .map(transitionState -> transitionState.getName() + ":"
                                                + state.getInverseTransitions().get(transitionState).getSymbols())
                                        .collect(Collectors.joining(", ")) + " }")
                        .sorted()
                        .collect(Collectors.toList())));
    }

    public void showFinalStateSet(ActionEvent actionEvent) {
        p1List.setItems(FXCollections.observableArrayList(finiteStateMachine.getStates().values().stream()
                                                                  .filter(State::isFinalState)
                                                                  .map(State::getName)
                                                                  .sorted()
                                                                  .collect(Collectors.toList())));
    }

    public void p1ChooseFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./finiteStateMachine/"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.canRead())
            finiteStateMachineText.setText(String.join("\n", Files.readAllLines(selectedFile.toPath())));
    }

    public void tabChanged(Event event) {
        if (!p1InputDataTab.isSelected())
            try {
                this.finiteStateMachine = new FiniteStateMachine(
                        new Gson().fromJson(finiteStateMachineText.getText(), FiniteStateMachineJSON.class));
            } catch (Exception e) {
                showError("Error", "The state machine declaration can't be converted.",
                          () -> p1TabPane.getSelectionModel().select(p1InputDataTab));
            }
    }

    public void checkSequence(ActionEvent actionEvent) {
        String sequence = testSequenceTextField.getText();
        if (sequence == null || sequence.equals("")) {
            showError("Error", "The sequence can't be empty.", () -> {});
            testSequenceResult.setText("");
        } else {
            int length = finiteStateMachine.checkSequence(sequence, 0);
            if (length != -1)
                testSequenceResult.setText(sequence.substring(0, length));
            else
                testSequenceResult.setText("");
        }
    }

    public void p1ToggleFileFormat(MouseEvent actionEvent) {
        p1Task.setRate(p1Task.getRate() * -1);
        p1Task.play();
        if (p1FileDrawer.isClosed()) {
            p1FileDrawer.open();
        } else {
            p1FileDrawer.close();
        }
    }

    public void p2ToggleFileFormat(MouseEvent mouseEvent) {
        p2Task.setRate(p2Task.getRate() * -1);
        p2Task.play();
        if (p2FileDrawer.isClosed()) {
            p2FileDrawer.open();
        } else {
            p2FileDrawer.close();
        }
    }

    public void p5ToggleFileFormat(MouseEvent mouseEvent) {
        p5Task.setRate(p5Task.getRate() * -1);
        p5Task.play();
        if (p5FileDrawer.isClosed()) {
            p5FileDrawer.open();
        } else {
            p5FileDrawer.close();
        }
    }

    public void p2ChooseFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./code/"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.canRead())
            codeText.setText(String.join("\n", Files.readAllLines(selectedFile.toPath())));
    }

    public void p2Interpret(Event event) {
        if (!p2InputDataTab.isSelected())
            try {
                List<String> fip = new ArrayList<>();
                List<String> errors = new ArrayList<>();
                MyTreeMap<String, Integer> tsID = new MyTreeMap<>();
                MyTreeMap<String, Integer> tsCONST = new MyTreeMap<>();

                createFIP(fip, errors, tsID, tsCONST);

                p2TSID.setItems(FXCollections.observableArrayList(tsID.keySet().stream()
                                                                          .map(key -> String.format("%-20s: ", key) + tsID.get(key))
                                                                          .collect(Collectors.toList())));
                p2TSCONST.setItems(FXCollections.observableArrayList(tsCONST.keySet().stream()
                                                                             .map(key -> String.format("%-20s: ", key) + tsCONST.get(key))
                                                                             .collect(Collectors.toList())));
                p2FIP.setItems(FXCollections.observableArrayList(fip));
                p2Errors.setItems(FXCollections.observableArrayList(errors));
            } catch (Exception e) {
                showError("Error", "The code can't be converted.", () -> p2TabPane.getSelectionModel().select(p2InputDataTab));
            }
    }

    private void createFIP(List<String> fip, List<String> errors, MyTreeMap<String, Integer> tsID, MyTreeMap<String, Integer> tsCONST) {
        List<String> tokens = new Tokenizer().tokenize(Arrays.asList(codeText.getText().split("\n")));
        for (var t : tokens) {
            if (operatorsMap.containsKey(t))
                fip.add(String.format("%-20s: ", t) + String.format("%-5s: ", operatorsMap.get(t)) + "-");
            else if (doubleAFD.checkSequence(t) || libraryAFD.checkSequence(t) || stringAFD.checkSequence(t)) {
                if (!tsCONST.containsKey(t))
                    tsCONST.put(t, tsCONST.size());
                fip.add(String.format("%-20s: ", t) + String.format("%-5s: ", operatorsMap.get("CONST")) + tsCONST.get(t));
            } else if (idAFD.checkSequence(t))
                if (t.length() <= 8) {
                    if (!tsID.containsKey(t))
                        tsID.put(t, tsID.size());
                    fip.add(String.format("%-20s: ", t) + String.format("%-5s: ", operatorsMap.get("ID")) + tsID.get(t));
                } else
                    errors.add(t);
            else
                errors.add(t);
        }
    }

    public void p5ChooseFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./grammar/"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.canRead())
            grammarText.setText(String.join("\n", Files.readAllLines(selectedFile.toPath())));
    }

    public void showTerminals(ActionEvent actionEvent) {
        p5List.setItems(FXCollections.observableArrayList(new ArrayList<>(grammar.getTerminals())));
    }

    public void showNonTerminals(ActionEvent actionEvent) {
        p5List.setItems(FXCollections.observableArrayList(new ArrayList<>(grammar.getNonTerminals())));
    }

    public void showProductionRules(ActionEvent actionEvent) {
        p5List.setItems(getObservableRulesFormat(grammar.getProductionRules()));
    }

    private ObservableList<String> getObservableRulesFormat(Map<String, Set<String>> rules) {
        return FXCollections.observableArrayList(
                rules.entrySet().stream()
                        .map(entry -> String.format("%-20s: ", entry.getKey()) + String.join(String.format("\n%20s| ", "")
                                , entry.getValue()) + "\n\n")
                        .collect(Collectors.toList()));
    }

    public void showProductionRulesForTerminal(MouseEvent actionEvent) {
        try {
            Map<String, Set<String>> nonTerminals = grammar.getProductionRulesForTerminal(terminalsComboBox.getValue());
            p5List.setItems(getObservableRulesFormat(nonTerminals));
        } catch (Exception e) {
            showError("Error", "Select a terminal");
        }
    }

    private void showError(String title, String message) {
        showError(title, message, () -> {});
    }

    public void p5CheckSequence(ActionEvent actionEvent) {
        String sequence = p5TestSequenceTextArea.getText();
        if (sequence == null || sequence.equals("")) {
            showError("Error", "The sequence can't be empty.", () -> {});
            p5TestSequenceTextArea.setText("");
            p5SequenceResultListView.setItems(FXCollections.observableArrayList(""));
        } else {
            Pair<Boolean, List<String>> result = earley.solve(sequence);
            if (result.getKey()) {
                p5TestSequenceResult.getStyleClass().remove("red-text");
                if (!p5TestSequenceResult.getStyleClass().contains("green-text"))
                    p5TestSequenceResult.getStyleClass().add("green-text");
                p5TestSequenceResult.setText("matches");
            } else {
                p5TestSequenceResult.getStyleClass().remove("green-text");
                if (!p5TestSequenceResult.getStyleClass().contains("red-text"))
                    p5TestSequenceResult.getStyleClass().add("red-text");
                p5TestSequenceResult.setText("does not match");
            }
            p5SequenceResultListView.setItems(FXCollections.observableArrayList(result.getValue()));
        }
    }

    public void p6ChooseFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./code/"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.canRead())
            p6CodeText.setText(String.join("\n", Files.readAllLines(selectedFile.toPath())));
    }

    public void p6TabChanged(Event event) {
        if (p6InterpretCode.isSelected())
            try {
                if (earleyMLP == null) {
                    Grammar grammarMLP = new Grammar("./grammar/grammar.txt");
                    earleyMLP = new Earley(grammarMLP);
                }
                List<Integer> fip = new ArrayList<>();
                List<String> errors = new ArrayList<>();

                List<String> tokens = new Tokenizer().tokenize(Arrays.asList(p6CodeText.getText().split("\n")));
                for (var t : tokens) {
                    if (operatorsMap.containsKey(t)) {
                        fip.add(operatorsMap.get(t));
                    } else if (doubleAFD.checkSequence(t) || stringAFD.checkSequence(t)) {
                        fip.add(operatorsMap.get("CONST"));
                    } else if (libraryAFD.checkSequence(t)) {
                        fip.add(operatorsMap.get("LIBRARY"));
                    } else if (idAFD.checkSequence(t))
                        if (t.length() <= 8) {
                            fip.add(operatorsMap.get("ID"));
                        } else
                            errors.add(t);
                    else
                        errors.add(t);
                }

                Map<Integer, String> sequenceMap = initSequenceMap();

                List<String> sequence = new ArrayList<>();
                for (int i = 0; i < fip.size(); i++) {
                    if (isLibrary(fip, i)) {
                        sequence.add("LIBRARY");
                        i += 1;
                    } else
                        sequence.add(sequenceMap.get(fip.get(i)));
                }

                Pair<Boolean, List<String>> result = earleyMLP.solve(sequence);

                List<String> resultList = result.getValue();
                resultList.add(0, result.getKey() ? "Syntactically correct.\n" : "Not syntactically correct.\n");
                p6InterpretCodeList.setItems(FXCollections.observableArrayList(
                        resultList.stream()
                                .map(string -> string.split("\n"))
                                .flatMap(Arrays::stream)
                                .collect(Collectors.toList())
                ));
            } catch (Exception e) {
                showError("Error", e.getMessage(), () -> p6TabPane.getSelectionModel().select(p6ChooseCode));
            }
    }

    private Map<Integer, String> initSequenceMap() {
        Map<Integer, String> sequenceMap = new HashMap<>();
        sequenceMap.put(operatorsMap.get("ID"), "ID");
        sequenceMap.put(operatorsMap.get("CONST"), "CONST");
        sequenceMap.put(operatorsMap.get("return"), "RETURN");
        sequenceMap.put(operatorsMap.get(","), "','");
        sequenceMap.put(operatorsMap.get(";"), "';'");
        sequenceMap.put(operatorsMap.get("int"), "TYPE");
        sequenceMap.put(operatorsMap.get("double"), "TYPE");
        sequenceMap.put(operatorsMap.get("std::cin>>"), "CIN");
        sequenceMap.put(operatorsMap.get("std::cout<<"), "COUT");
        sequenceMap.put(operatorsMap.get("if"), "IF");
        sequenceMap.put(operatorsMap.get("for"), "FOR");
        sequenceMap.put(operatorsMap.get("else"), "ELSE");
        sequenceMap.put(operatorsMap.get("<="), "OPERATORS");
        sequenceMap.put(operatorsMap.get(">="), "OPERATORS");
        sequenceMap.put(operatorsMap.get("=="), "OPERATORS");
        sequenceMap.put(operatorsMap.get("!="), "OPERATORS");
        sequenceMap.put(operatorsMap.get("&&"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("||"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("<"), "OPERATORS");
        sequenceMap.put(operatorsMap.get(">"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("/"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("+"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("-"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("*"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("%"), "OPERATORS");
        sequenceMap.put(operatorsMap.get("("), "'('");
        sequenceMap.put(operatorsMap.get(")"), "')'");
        sequenceMap.put(operatorsMap.get("{"), "'{'");
        sequenceMap.put(operatorsMap.get("}"), "'}'");
        sequenceMap.put(operatorsMap.get("="), "'='");
        return sequenceMap;
    }

    private boolean isLibrary(List<Integer> fip, int i) {
        return i + 1 < fip.size() && fip.get(i).equals(operatorsMap.get("#include")) && fip.get(i + 1).equals(operatorsMap.get("LIBRARY"));
    }
}
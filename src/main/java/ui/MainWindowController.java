package ui;

import JSON.FiniteStateMachineJSON;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
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
import model.FiniteStateMachine;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    public  JFXListView<String>           p1List;
    public  JFXButton                     finalStateSetButton;
    public  JFXButton                     transitionsButton;
    public  JFXButton                     alphabetButton;
    public  JFXButton                     stateSetButton;
    public  MaterialDesignIconView        exitButton;
    public  MaterialDesignIconView        alwaysOnTopToggle;
    public  JFXButton                     p1ChooseFileButton;
    public  JFXTextArea                   finiteStateMachineText;
    public  Tab                           p1InterpretTab;
    public  StackPane                     rooPane;
    public  JFXTabPane                    p1TabPane;
    public  Tab                           p1InputDataTab;
    public  Tab                           testSequenceTab;
    public  JFXTextArea                   testSequenceTextField;
    public  JFXButton                     checkButton;
    public  JFXTextField                  testSequenceResult;
    public  JFXDrawer                     p1FileDrawer;
    public  JFXListView<String>           p1FileDrawerListView;
    public  JFXHamburger                  p1HamburgerButton;
    public  Tab                           p2InputDataTab;
    public  JFXTextArea                   codeText;
    public  JFXHamburger                  p2HamburgerButton;
    public  JFXDrawer                     p2FileDrawer;
    public  JFXListView<String>           p2FileDrawerListView;
    public  JFXButton                     p2ChooseFileButton;
    public  Tab                           p2InterpretTab;
    public  JFXListView<String>           p2FIP;
    public  JFXListView<String>           p2TSID;
    public  JFXListView<String>           p2TSCONST;
    public  JFXListView<String>           p2Errors;
    public  JFXTabPane                    p2TabPane;
    private Stage                         stage;
    private double                        x;
    private double                        y;
    private FiniteStateMachine            finiteStateMachine;
    private HamburgerBasicCloseTransition p1Task;
    private HamburgerBasicCloseTransition p2Task;
    
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
        //                "<program> := <object> \n",
        //                "<object>  := { <pairs> } | { } \n",
        //                "<pairs>   := <pair>, <pairs> | <pair> \n",
        //                "<pair>    := <key>:<value> \n",
        //                "<key>     := \"ID\" \n",
        //                "<value>   := \"ID\" | true | false | <object> | <list> \n",
        //                "<list>    := [<list>, <value> ] | [<value>] | [ ] \n"
        
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
                        .map(state->state.getInverseTransitions().values().stream()
                                .map(TransitionFunction::getSymbols).collect(Collectors.toSet()))
                        .flatMap(Set::stream).collect(Collectors.toSet()).stream().flatMap(Set::stream)
                        .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList())));
    }
    
    public void showTransitions(ActionEvent actionEvent) {
        p1List.setItems(FXCollections.observableArrayList(
                finiteStateMachine.getStates().values().stream()
                        .map(state->state.getName() + ": transitions:{ " +
                                state.getInverseTransitions().keySet().stream()
                                        .map(transitionState->transitionState.getName() + ":"
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
        fileChooser.setInitialDirectory(new File("C:\\Users\\Ilove\\Google Drive\\Faculta\\AN 3\\Sem 1\\LFTC\\Projects\\LFTC-2"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null && selectedFile.canRead())
            finiteStateMachineText.setText(String.join("\n", Files.readAllLines(selectedFile.toPath())));
    }
    
    public void tabChanged(Event event) {
        if(!p1InputDataTab.isSelected())
            try {
                this.finiteStateMachine = new FiniteStateMachine(
                        new Gson().fromJson(finiteStateMachineText.getText(), FiniteStateMachineJSON.class));
            } catch(Exception e) {
                showError("Error", "The state machine declaration can't be converted.",
                          ()->p1TabPane.getSelectionModel().select(p1InputDataTab));
            }
    }
    
    protected void showError(String title, String message, Runnable afterClose) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        JFXDialog dialog = new JFXDialog(rooPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event)->dialog.close());
        
        dialogLayout.setHeading(new Label(title));
        dialogLayout.getStyleClass().add("errorHeading");
        dialogLayout.setBody(new Label(message));
        dialogLayout.setActions(button);
        dialog.show();
        BoxBlur blur = new BoxBlur(3, 3, 2);
        p1TabPane.setEffect(blur);
        dialog.setOnDialogClosed((JFXDialogEvent event)->{
            p1TabPane.setEffect(null);
            afterClose.run();
        });
    }
    
    public void checkSequence(ActionEvent actionEvent) {
        String sequence = testSequenceTextField.getText();
        if(sequence == null || sequence.equals("")) {
            showError("Error", "The sequence can't be empty.", ()->{});
            testSequenceResult.setText("");
        } else {
            int length = finiteStateMachine.checkSequence(sequence, 0);
            if(length != -1) {
                testSequenceResult.setText(sequence.substring(0, length));
            } else
                testSequenceResult.setText("");
        }
        
    }
    
    public void p1ToggleFileFormat(MouseEvent actionEvent) {
        p1Task.setRate(p1Task.getRate() * -1);
        p1Task.play();
        if(p1FileDrawer.isClosed()) {
            p1FileDrawer.open();
        } else {
            p1FileDrawer.close();
        }
    }
    
    public void p2ToggleFileFormat(MouseEvent mouseEvent) {
        p2Task.setRate(p2Task.getRate() * -1);
        p2Task.play();
        if(p2FileDrawer.isClosed()) {
            p2FileDrawer.open();
        } else {
            p2FileDrawer.close();
        }
    }
    
    public void p2ChooseFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\Ilove\\Google Drive\\Faculta\\AN 3\\Sem 1\\LFTC\\Projects\\LFTC-2"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null && selectedFile.canRead())
            codeText.setText(String.join("\n", Files.readAllLines(selectedFile.toPath())));
    }
    
    public void p2Interpret(Event event) {
        if(!p2InputDataTab.isSelected())
            try {
                List<String> operators = Arrays.asList("ID", "CONST", "<=", ">=", "==", "!=", "&&", "||", "/", ";", "=", "<", ">",
                                                       "(", ")", "*", "%", "{", "}", "+", "-", "#include", "int", "double",
                                                       "std::cin>>", "std::cout<<", "if", "for", "else", "return");
                MyTreeMap<String,Integer> operatorsMap = new MyTreeMap<>();
                for(int i = 0; i < operators.size(); i++) {
                    operatorsMap.put(operators.get(i), i);
                }
                
                List<String> tokens = new Tokenizer().tokenize(Arrays.asList(codeText.getText().split("\n")));
                
                List<String> fip = new ArrayList<>();
                List<String> errors = new ArrayList<>();
                MyTreeMap<String,Integer> tsID = new MyTreeMap<>();
                MyTreeMap<String,Integer> tsCONST = new MyTreeMap<>();
                FiniteStateMachine doubleAFD = new FiniteStateMachine("doubleNo0.json");
                FiniteStateMachine stringAFD = new FiniteStateMachine(("string.json"));
                FiniteStateMachine libraryAFD = new FiniteStateMachine("library.json");
                FiniteStateMachine idAFD = new FiniteStateMachine("id.json");
                
                for(var t : tokens) {
                    if(operatorsMap.containsKey(t))
                        fip.add(String.format("%-20s: ", t) + String.format("%-5s: ", operatorsMap.get(t)) + "-");
                    else if(doubleAFD.checkSequence(t) || libraryAFD.checkSequence(t) || stringAFD.checkSequence(t)) {
                        if(!tsCONST.containsKey(t))
                            tsCONST.put(t, tsCONST.size());
                        fip.add(String.format("%-20s: ", t) + String.format("%-5s: ", operatorsMap.get("CONST")) + tsCONST.get(
                                t));
                    } else if(idAFD.checkSequence(t))
                        if(t.length() <= 8) {
                            if(!tsID.containsKey(t))
                                tsID.put(t, tsID.size());
                            fip.add(String.format("%-20s: ", t) + String.format("%-5s: ", operatorsMap.get("ID")) + tsID.get(t));
                        } else
                            errors.add(t);
                    else
                        errors.add(t);
                }
                
                p2TSID.setItems(
                        FXCollections.observableArrayList(tsID.keySet().stream()
                                                                  .map(key->String.format("%-20s: ", key) + tsID.get(key))
                                                                  .collect(Collectors.toList())));
                p2TSCONST.setItems(
                        FXCollections.observableArrayList(tsCONST.keySet().stream()
                                                                  .map(key->String.format("%-20s: ", key) + tsCONST.get(key))
                                                                  .collect(Collectors.toList())));
                p2FIP.setItems(FXCollections.observableArrayList(fip));
                p2Errors.setItems(FXCollections.observableArrayList(errors));
            } catch(Exception e) {
                showError("Error", "The code can't be converted.",
                          ()->p2TabPane.getSelectionModel().select(p2InputDataTab));
            }
    }
}
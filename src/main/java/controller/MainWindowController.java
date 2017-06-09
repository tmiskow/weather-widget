package controller;

import event.EventStream;
import event.RefreshRequestEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import rx.observables.JavaFxObservable;

public class MainWindowController {

    @FXML
    public Button refreshButton;

    @FXML
    public void initialize() {
        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent()));
    }
}

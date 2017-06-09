import com.jfoenix.controls.JFXDecorator;
import data.AirQualityData;
import data.WeatherData;
import event.AirQualityDataEvent;
import event.DataEvent;
import event.EventStream;
import event.WeatherDataEvent;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import network.DataSource;
import network.OpenWeatherDataSource;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class AppMain extends Application {

    private static final String FXML_MAIN = "/fxml/main.fxml";

    private static final EventStream eventStream = EventStream.getInstance();
    private static final Logger logger = Logger.getLogger(AppMain.class);

    private Stage mainWindow;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;

        setupMainWindow();
        setupLogger();

        mainWindow.show();
    }

    private void setupMainWindow() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FXML_MAIN));

        // TODO
        //JFXDecorator decorator = new JFXDecorator(mainWindow, root, false, false, true);
        //decorator.setOnCloseButtonAction(this::onClose);

        Scene scene = new Scene(root, 300, 500);
        scene.setFill(null);

        // TODO
        //scene.getStylesheets().addAll(getClass().getResource(FONT_CSS).toExternalForm());

        mainWindow.setScene(scene);
        mainWindow.setWidth(300);
        mainWindow.setHeight(500);
        mainWindow.setResizable(false);
    }

    private void setupLogger() {
        logger.info("Setting logger up...");

        eventStream.getEvents().ofType(WeatherDataEvent.class)
            .map(weatherDataEvent ->
                weatherDataEvent + "\n\t" + weatherDataEvent.getWeatherData())
            .subscribe(logger::info);

        eventStream.getEvents().ofType((AirQualityDataEvent.class))
            .map(airQualityDataEvent ->
                airQualityDataEvent + "\n\t" + airQualityDataEvent.getAirQualityData())
            .subscribe(logger::info);
    }
}

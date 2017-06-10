package controller;

import com.jfoenix.controls.JFXButton;
import data.WeatherData;
import event.*;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import network.*;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.Schedulers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainWindowController {

    private static final int POLL_INTERVAL = 5 * 60;
    private static final int INITIAL_DELAY = 0;

    // Data sources
    private WeatherDataSource openWeatherDataSource = new OpenWeatherDataSource();
    private WeatherDataSource meteoWeatherDataSource = new MeteoWeatherDataSource();
    private AirQualityDataSource airQualityDataSource = new AirQualityDataSource();

    private WeatherDataSource activeWeatherDataSource = openWeatherDataSource;

    @FXML
    public JFXButton refreshButton;

    @FXML
    public Label temperatureLabel;

    @FXML
    public FontIcon weatherIcon;

    @FXML
    public Label pressureLabel;

    @FXML
    public Label cloudinessLabel;

    @FXML
    public Label humidityLabel;

    @FXML
    public Label windSpeedLabel;

    @FXML
    public FontIcon windDirectionIcon;

    @FXML
    public Label pm25Label;

    @FXML
    public Label pm10Label;

    @FXML
    public JFXButton settingsButton;

    @FXML
    public ContextMenu settingsPopupMenu;

    @FXML
    public ToggleGroup sourceToggleGroup;

    @FXML
    public RadioMenuItem openWeatherToggle;

    @FXML
    public RadioMenuItem meteoWeatherToggle;

    @FXML
    public Label infoLabel;

    @FXML
    public void initialize() {

        EventStream.getInstance().join(openWeatherDataSource.dataSourceStream());
        EventStream.getInstance().join(meteoWeatherDataSource.dataSourceStream());
        EventStream.getInstance().join(airQualityDataSource.dataSourceStream());

        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent(activeWeatherDataSource)));

        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent(airQualityDataSource)));

        EventStream.getInstance().join(fixedIntervalStream()
            .map(l -> new RefreshRequestEvent(activeWeatherDataSource)));

        EventStream.getInstance().join(fixedIntervalStream()
                .map(l -> new RefreshRequestEvent(airQualityDataSource)));

        setupDataLabels();
        setupInfoLabel();
        setupWeatherIcon();
        setupWeatherDataSourceSelection();

    }

    private void setupWeatherIcon() {
        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> {
                weatherIcon.styleProperty().set("-fx-icon-size: 70px;\n" +
                        "    -fx-icon-code: " + weatherData.getIconCode() + ";");
            });
    }

    private Observable<Long> fixedIntervalStream() {
        return Observable.interval(INITIAL_DELAY, POLL_INTERVAL, TimeUnit.SECONDS, Schedulers.io());
    }

    @FXML
    public void showPopupMenu() {
        settingsPopupMenu.show(settingsButton, Side.BOTTOM, 0, 0);
    }

    private void setupDataLabels() {
        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> temperatureLabel.setText(weatherData.getTemperatureString()));

        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> pressureLabel.setText(weatherData.getPressureString()));

        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> cloudinessLabel.setText(weatherData.getCloudinessString()));

        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> humidityLabel.setText(weatherData.getHumidityString()));

        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> windSpeedLabel.setText(weatherData.getWindSpeedString()));

        getWeatherDataEvents()
            .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
            .map(WeatherDataEvent::getWeatherData)
            .map(WeatherData::getWindDegree)
            .subscribe(windDegree -> windDirectionIcon.setRotate(windDegree + 180));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
            .subscribe(airQualityData -> pm25Label.setText(airQualityData.getPM25String()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
            .subscribe(airQualityData -> pm10Label.setText(airQualityData.getPM10String()));
    }

    private void setupInfoLabel() {
        EventStream.getInstance().eventsInFx().ofType(NetworkRequestIssuedEvent.class)
                .map(networkRequestIssuedEvent -> 1)
                .mergeWith(EventStream.getInstance().eventsInFx()
                        .ofType(NetworkRequestFinishedEvent.class)
                        .map(networkRequestFinishedEvent -> -1))
                .scan((x,y) -> x + y)
                .subscribe(v -> {
                    if (v > 0)
                        infoLabel.setText("Updating data...");

                    else if (v == 0) {
                        if (infoLabel.getText().equals("Updating data..."))
                            infoLabel.setText("");
                    }
                });

        EventStream.getInstance().eventsInFx().ofType(ErrorEvent.class)
                .subscribe(errorEvent -> infoLabel.setText("Error: Could not update data."));
    }

    private void setupWeatherDataSourceSelection() {
        sourceToggleGroup.selectToggle(openWeatherToggle);

        EventStream.getInstance().join(JavaFxObservable.changesOf(sourceToggleGroup.selectedToggleProperty())
            .filter(toggleChange -> toggleChange.getNewVal() != null)
            .map(toggleChange -> {
                if (toggleChange.getNewVal() == openWeatherToggle) {
                    return new WeatherDataSourceChangeEvent(openWeatherDataSource);
                }

                else {
                    return new WeatherDataSourceChangeEvent(meteoWeatherDataSource);
                }
            }));

        EventStream.getInstance().eventsInFx().ofType(WeatherDataSourceChangeEvent.class)
            .map(WeatherDataSourceChangeEvent::getNewSource)
            .subscribe(weatherDataSource -> activeWeatherDataSource = weatherDataSource);

        EventStream.getInstance()
            .join(EventStream.getInstance().eventsInFx().ofType(WeatherDataSourceChangeEvent.class)
            .map(WeatherDataSourceChangeEvent::getNewSource)
            .map(RefreshRequestEvent::new));
    }



    private Observable<WeatherDataEvent> getWeatherDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(WeatherDataEvent.class);
    }

    private Observable<AirQualityDataEvent> getAirQualityDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(AirQualityDataEvent.class);
    }
}

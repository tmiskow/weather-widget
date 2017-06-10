package controller;

import com.jfoenix.controls.JFXButton;
import event.*;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import network.*;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class MainWindowController {

    private static final int POLL_INTERVAL = 60;
    private static final int INITIAL_DELAY = 0;

    // Data sources
    private WeatherDataSource openWeatherDataSource = new OpenWeatherDataSource();
    private WeatherDataSource meteoWeatherDataSource = new MeteoWeatherDataSource();
    private AirQualitySource airQualitySource = new AirQualitySource();

    private WeatherDataSource activeWeatherDataSource = openWeatherDataSource;

    @FXML
    public JFXButton refreshButton;

    @FXML
    public Label temperatureLabel;

    @FXML
    public Label pressureLabel;

    @FXML
    public Label cloudinessLabel;

    @FXML
    public Label windSpeedLabel;

    @FXML
    public Label windDegreeLabel;

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
    public void initialize() {

        EventStream.getInstance().join(openWeatherDataSource.dataSourceStream());
        EventStream.getInstance().join(meteoWeatherDataSource.dataSourceStream());
        EventStream.getInstance().join(airQualitySource.dataSourceStream());

        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent(activeWeatherDataSource)));

        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent(airQualitySource)));

        EventStream.getInstance().join(fixedIntervalStream()
            .map(l -> new RefreshRequestEvent(activeWeatherDataSource)));

        EventStream.getInstance().join(fixedIntervalStream()
                .map(l -> new RefreshRequestEvent(airQualitySource)));

        setupLabels();
        setupWeatherDataSourceSelection();

    }

    private Observable<Long> fixedIntervalStream() {
        return Observable.interval(INITIAL_DELAY, POLL_INTERVAL, TimeUnit.SECONDS, Schedulers.io());
    }

    @FXML
    public void showPopupMenu() {
        settingsPopupMenu.show(settingsButton, Side.BOTTOM, 0, 0);
    }

    private void setupLabels() {
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
                .subscribe(weatherData -> windSpeedLabel.setText(weatherData.getWindSpeedString()));

        getWeatherDataEvents()
                .filter(weatherDataEvent -> weatherDataEvent.getSource() == activeWeatherDataSource)
                .map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> windDegreeLabel.setText(weatherData.getWindDegreeString()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> pm25Label.setText(airQualityData.getPM25String()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> pm10Label.setText(airQualityData.getPM10String()));
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

package controller;

import data.AirQualityData;
import data.WeatherData;
import event.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import network.*;
import rx.Observable;
import rx.observables.JavaFxObservable;

public class MainWindowController {

    private WeatherDataSource openWeatherDataSource = new OpenWeatherDataSource();
    private WeatherDataSource meteoWeatherDataSource = new MeteoWeatherDataSource();
    private AirQualitySource airQualitySource = new AirQualitySource();

    @FXML
    public Button refreshButton;

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
    public void initialize() {

        EventStream.getInstance().join(meteoWeatherDataSource.dataSourceStream());

        EventStream.getInstance().join(airQualitySource.dataSourceStream());

        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent()));

        setupLabels();

    }

    private void setLabel(Label label, Float data) {

        String string = "-";

        if (data != null) {
            string = data.toString();
        }

        label.setText(string);
    }

    private void setupLabels() {
        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
            .subscribe(weatherData -> setLabel(temperatureLabel, weatherData.getTemperature()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> setLabel(pressureLabel, weatherData.getPressure()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> setLabel(cloudinessLabel, weatherData.getCloudiness()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> setLabel(windSpeedLabel, weatherData.getWindSpeed()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> setLabel(windDegreeLabel, weatherData.getWindDegree()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> setLabel(pm25Label, airQualityData.getPM25()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> setLabel(pm10Label, airQualityData.getPM10()));
    }

    private Observable<WeatherDataEvent> getWeatherDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(WeatherDataEvent.class);
    }

    private Observable<AirQualityDataEvent> getAirQualityDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(AirQualityDataEvent.class);
    }
}

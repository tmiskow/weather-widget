package controller;

import data.AirQualityData;
import data.WeatherData;
import event.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import network.AirQualitySource;
import network.DataSource;
import network.OpenWeatherDataSource;
import network.WeatherDataSource;
import rx.Observable;
import rx.observables.JavaFxObservable;

public class MainWindowController {

    private WeatherDataSource openWeatherDataSource = new OpenWeatherDataSource();
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

        EventStream.getInstance().join(openWeatherDataSource.dataSourceStream());

        EventStream.getInstance().join(airQualitySource.dataSourceStream());

        EventStream.getInstance().join(JavaFxObservable.actionEventsOf(refreshButton)
            .map(actionEvent -> new RefreshRequestEvent()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData).subscribe(weatherData ->
            temperatureLabel.setText(weatherData.getTemperature().toString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData).subscribe(weatherData ->
            pressureLabel.setText(weatherData.getPressure().toString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData).subscribe(weatherData ->
            cloudinessLabel.setText(weatherData.getCloudiness().toString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData).subscribe(weatherData ->
            windSpeedLabel.setText(weatherData.getWindSpeed().toString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData).subscribe(weatherData ->
            windDegreeLabel.setText(weatherData.getWindDegree().toString()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
            .subscribe(airQualityData -> pm25Label.setText(airQualityData.getPM25().toString()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> pm10Label.setText(airQualityData.getPM10().toString()));


    }

    private Observable<WeatherDataEvent> getWeatherDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(WeatherDataEvent.class);
    }

    private Observable<AirQualityDataEvent> getAirQualityDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(AirQualityDataEvent.class);
    }
}

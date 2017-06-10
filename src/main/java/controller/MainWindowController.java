package controller;

import com.jfoenix.controls.JFXButton;
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
    public void initialize() {

        EventStream.getInstance().join(openWeatherDataSource.dataSourceStream());

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
            .subscribe(weatherData -> temperatureLabel.setText(weatherData.getTemperatureString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> pressureLabel.setText(weatherData.getPressureString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> cloudinessLabel.setText(weatherData.getCloudinessString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> windSpeedLabel.setText(weatherData.getWindSpeedString()));

        getWeatherDataEvents().map(WeatherDataEvent::getWeatherData)
                .subscribe(weatherData -> windDegreeLabel.setText(weatherData.getWindDegreeString()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> pm25Label.setText(airQualityData.getPM25String()));

        getAirQualityDataEvents().map(AirQualityDataEvent::getAirQualityData)
                .subscribe(airQualityData -> pm10Label.setText(airQualityData.getPM10String()));
    }

    private Observable<WeatherDataEvent> getWeatherDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(WeatherDataEvent.class);
    }

    private Observable<AirQualityDataEvent> getAirQualityDataEvents() {
        return EventStream.getInstance().eventsInFx().ofType(AirQualityDataEvent.class);
    }
}

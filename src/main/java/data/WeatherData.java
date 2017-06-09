package data;

public class WeatherData {

    private Float temperature;
    private Float pressure;
    private Float cloudiness;
    private Float windSpeed;
    private Float windDegree;

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Float cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Float windDegree) {
        this.windDegree = windDegree;
    }

    @Override
    public String toString() {
        return "Weather Data:" +
                "\n\ttemperature = " + temperature +
                "\n\tpressure = " + pressure +
                "\n\tcloudiness = " + cloudiness +
                "\n\twindSpeed = " + windSpeed +
                "\n\twindDegree = " + windDegree;
    }
}

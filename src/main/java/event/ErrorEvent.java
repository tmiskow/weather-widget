package event;

public class ErrorEvent extends AppEvent{

    private final Throwable cause;

    public ErrorEvent(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ErrorEvent{}";
    }
}

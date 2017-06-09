package event;

import rx.Observable;
import rx.Subscription;
import rx.javafx.sources.CompositeObservable;

public class EventStream {

    private static EventStream instance = new EventStream();
    private CompositeObservable<AppEvent> composite = new CompositeObservable<>();

    // Empty private constructor to prevent the creation of other instances
    private EventStream() {}

    public static EventStream getInstance() {
        return instance;
    }

    public Observable<AppEvent> getEvents() {
        return composite.toObservable();
    }

    public Subscription join(Observable<AppEvent> observable) {
        return composite.add(observable);
    }
}

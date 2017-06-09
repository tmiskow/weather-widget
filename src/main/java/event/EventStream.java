package event;

import rx.Observable;
import rx.Subscription;
import rx.javafx.sources.CompositeObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

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

    public Observable<AppEvent> eventsInFx() {
        return getEvents().compose(fxTransformer());
    }

    public Observable<AppEvent> eventsInIO() {
        return getEvents().compose(ioTransformer());
    }

    private <T> Observable.Transformer<T, T> fxTransformer() {
        return observable -> observable.observeOn(JavaFxScheduler.getInstance());
    }

    private <T> Observable.Transformer<T, T> ioTransformer() {
        return observable -> observable.observeOn(Schedulers.io());
    }
}

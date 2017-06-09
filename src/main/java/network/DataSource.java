package network;


import event.*;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public abstract class DataSource {

    private static final Logger logger = Logger.getLogger(DataSource.class);

    private static final int POLL_INTERVAL = 10;
    private static final int INITIAL_DELAY = 0;
    private static final int TIMEOUT = 20;

    protected abstract Observable<DataEvent> makeRequest();

    public Observable<AppEvent> dataSourceStream() {
        return fixedIntervalStream().compose(this::wrapRequest)
            .mergeWith(EventStream.getInstance().eventsInIO().ofType(RefreshRequestEvent.class)
            .compose(this::wrapRequest));
    }

    private <T> Observable<AppEvent> wrapRequest(Observable<T> observable) {
        return observable.flatMap(ignore ->
            Observable.concat(Observable.just(new NetworkRequestIssuedEvent()),
            makeRequest().timeout(TIMEOUT, TimeUnit.SECONDS).doOnError(logger::error)
                .cast(AppEvent.class).onErrorReturn(ErrorEvent::new),
            Observable.just(new NetworkRequestFinishedEvent()))
        );
    }

    protected Observable<Long> fixedIntervalStream() {
        return Observable.interval(INITIAL_DELAY, POLL_INTERVAL, TimeUnit.SECONDS, Schedulers.io());
    }

    protected HttpClientRequest<ByteBuf> prepareHttpGETRequest(String url) {
        return HttpClientRequest.createGet(url);
    }

    protected Observable<String> unpackResponse(Observable<HttpClientResponse<ByteBuf>> responseObservable) {
        return responseObservable.flatMap(HttpClientResponse::getContent)
                .map(buffer -> buffer.toString(CharsetUtil.UTF_8));
    }

    // TODO

}

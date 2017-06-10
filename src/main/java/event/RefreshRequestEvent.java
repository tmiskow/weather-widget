package event;

import network.DataSource;

public class RefreshRequestEvent extends AppEvent {

    private DataSource source;

    public RefreshRequestEvent(DataSource source) {
        this.source = source;
    }

    public DataSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "RefreshRequestEvent";
    }
}

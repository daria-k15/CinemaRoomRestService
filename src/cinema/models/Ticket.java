package cinema.models;

import java.util.UUID;

public class Ticket {
    public final UUID token;
    public final Seats seat;

    public Ticket(Seats seat) {
        this.token = UUID.randomUUID();
        this.seat = seat;
    }

    public UUID getToken() {
        return token;
    }

    public Seats getSeat() {
        return seat;
    }
}

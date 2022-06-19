package cinema.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Cinema {
    private final int totalRows = 9;
    private final int totalColumn = 9;
    private final List<Seats> seats = new ArrayList<>();
    private final List<Ticket> soldTickets = new ArrayList<>();

    public Cinema() {
        initializeSeats();
    }

    private void initializeSeats() {
        for (int row = 1; row <= totalRows; row++) {
            for (int column = 1; column <= 9; column++) {
                seats.add(new Seats(row, column));
            }
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumn() {
        return totalColumn;
    }

    public List<Seats> getAvailableSeats() {
        return seats.stream().filter(Seats::isAvailable).collect(Collectors.toList());
    }

    public boolean isSeatExists(int row, int column){
        return seats.stream().anyMatch(seats -> seats.getRow() == row && seats.getColumn() == column);
    }

    public boolean isSeatAvailable(int row, int column){
        return seats.stream()
                .filter(seats -> seats.getRow() == row && seats.getColumn() == column)
                .anyMatch(Seats::isAvailable);
    }

    public ResponseEntity<?> buyTicket(int row, int column){
        Optional<Seats> seats = getSeatsByRowColumn(row, column);
        if (seats.isEmpty()){
            throw new NoSuchElementException();
        }
        seats.get().setAvailable(false);
        Ticket ticket = new Ticket(seats.get());
        soldTickets.add(ticket);
        return new ResponseEntity<>(Map.of(
                "token", ticket.getToken(),
                "ticket", seats.get()
        ), HttpStatus.OK);
    }

    public ResponseEntity<?> returnTicket(UUID token){
        Optional<Ticket> ticket = getTicketByToken(token);
        if (ticket.isEmpty()){
            throw new NoSuchElementException();
        }
        Seats seats = ticket.get().getSeat();
        seats.setAvailable(true);
        soldTickets.remove(ticket.get());
        return new ResponseEntity<>(Map.of(
                "returned_ticket", seats
        ), HttpStatus.OK);
    }

    private Optional<Seats> getSeatsByRowColumn(int row, int column){
        return seats.stream().filter(seats -> seats.getRow() == row && seats.getColumn() == column).findFirst();
    }

    private Optional<Ticket> getTicketByToken(UUID token){
        return soldTickets.stream().filter(ticket -> Objects.equals(token, ticket.getToken())).findFirst();
    }

    public int getCurrentIncome(){
        return soldTickets.stream().map(Ticket::getSeat).mapToInt(Seats::getPrice).sum();
    }

    public int getNumOfAvailableSeats(){
        return getAvailableSeats().size();
    }

    public int getNumOfPurchasedTickets(){
        return soldTickets.size();
    }
    public boolean isTokenExists(UUID token){
        return soldTickets.stream().anyMatch(ticket -> Objects.equals(token, ticket.getToken()));
    }

}

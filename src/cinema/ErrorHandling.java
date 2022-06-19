package cinema;

public enum ErrorHandling {
    OUT_OF_BOUNDS("The number of a row or a column is out of bounds!"),
    ALREADY_PURCHASED("The ticket has been already purchased!"),
    WRONG_TOKEN("Wrong token!"),
    WRONG_PASSWORD("The password is wrong!");

    private final String errorMessage;

    ErrorHandling(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return this.errorMessage;
    }
}

public class DateOutOfRangeException extends Throwable {
    // Custom type of exception that acts as a default exception with a given text argument.
    public DateOutOfRangeException(String incremented_past_available_range) {
        super(incremented_past_available_range);
    }
}

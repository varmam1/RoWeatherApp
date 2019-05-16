public class DateOutOfRangeException extends Throwable {
    public DateOutOfRangeException(String incremented_past_available_range) {
        super(incremented_past_available_range);
    }
}

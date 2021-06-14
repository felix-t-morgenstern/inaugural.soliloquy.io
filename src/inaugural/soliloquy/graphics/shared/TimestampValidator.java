package inaugural.soliloquy.graphics.shared;

public class TimestampValidator {
    private Long _mostRecentTimestamp;

    public TimestampValidator() {
    }

    public void validateTimestamp(long timestamp) {
        if (_mostRecentTimestamp != null && timestamp < _mostRecentTimestamp) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            throw new IllegalArgumentException(stackTrace[1].getClassName() + "." +
                    stackTrace[1].getMethodName() + ": provided outdated timestamp (" + timestamp +
                    ")");
        }
        _mostRecentTimestamp = timestamp;
    }

    public void validateTimestamp(String className, long timestamp) {
        if (_mostRecentTimestamp != null && timestamp < _mostRecentTimestamp) {
            String invokingClass = "undiscoveredClass";
            String invokingMethod = "undiscoveredMethod";

            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for(StackTraceElement element : stackTrace) {
                if (element.getClassName().equals(className)) {
                    invokingClass = element.getClassName();
                    invokingMethod = element.getMethodName();

                    break;
                }
            }

            throw new IllegalArgumentException(invokingClass + "." +invokingMethod +
                    ": provided outdated timestamp (" + timestamp + ")");
        }
        _mostRecentTimestamp = timestamp;
    }
}

package supplychaintrackingsystem;

public enum OrderType implements OrderTypeReadOnly {
    STANDARD,
    EXPRESS,
    COLD_CHAIN
    ;

    @Override
    public String getTypeName() {
        return name();
    }
}

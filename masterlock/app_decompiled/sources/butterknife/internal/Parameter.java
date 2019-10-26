package butterknife.internal;

final class Parameter {
    static final Parameter[] NONE = new Parameter[0];
    private final int listenerPosition;
    private final String type;

    Parameter(int i, String str) {
        this.listenerPosition = i;
        this.type = str;
    }

    /* access modifiers changed from: 0000 */
    public int getListenerPosition() {
        return this.listenerPosition;
    }

    /* access modifiers changed from: 0000 */
    public String getType() {
        return this.type;
    }
}

package p009rx.exceptions;

/* renamed from: rx.exceptions.UnsubscribeFailedException */
public final class UnsubscribeFailedException extends RuntimeException {
    private static final long serialVersionUID = 4594672310593167598L;

    public UnsubscribeFailedException(Throwable th) {
        if (th == null) {
            th = new NullPointerException();
        }
        super(th);
    }

    public UnsubscribeFailedException(String str, Throwable th) {
        if (th == null) {
            th = new NullPointerException();
        }
        super(str, th);
    }
}

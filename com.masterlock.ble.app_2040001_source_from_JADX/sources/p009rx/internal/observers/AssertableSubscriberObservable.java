package p009rx.internal.observers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import p009rx.Producer;
import p009rx.Subscriber;
import p009rx.functions.Action0;
import p009rx.observers.AssertableSubscriber;
import p009rx.observers.TestSubscriber;

/* renamed from: rx.internal.observers.AssertableSubscriberObservable */
public class AssertableSubscriberObservable<T> extends Subscriber<T> implements AssertableSubscriber<T> {

    /* renamed from: ts */
    private final TestSubscriber<T> f235ts;

    public AssertableSubscriberObservable(TestSubscriber<T> testSubscriber) {
        this.f235ts = testSubscriber;
    }

    public static <T> AssertableSubscriberObservable<T> create(long j) {
        TestSubscriber testSubscriber = new TestSubscriber(j);
        AssertableSubscriberObservable<T> assertableSubscriberObservable = new AssertableSubscriberObservable<>(testSubscriber);
        assertableSubscriberObservable.add(testSubscriber);
        return assertableSubscriberObservable;
    }

    public void onStart() {
        this.f235ts.onStart();
    }

    public void onCompleted() {
        this.f235ts.onCompleted();
    }

    public void setProducer(Producer producer) {
        this.f235ts.setProducer(producer);
    }

    public final int getCompletions() {
        return this.f235ts.getCompletions();
    }

    public void onError(Throwable th) {
        this.f235ts.onError(th);
    }

    public List<Throwable> getOnErrorEvents() {
        return this.f235ts.getOnErrorEvents();
    }

    public void onNext(T t) {
        this.f235ts.onNext(t);
    }

    public final int getValueCount() {
        return this.f235ts.getValueCount();
    }

    public AssertableSubscriber<T> requestMore(long j) {
        this.f235ts.requestMore(j);
        return this;
    }

    public List<T> getOnNextEvents() {
        return this.f235ts.getOnNextEvents();
    }

    public AssertableSubscriber<T> assertReceivedOnNext(List<T> list) {
        this.f235ts.assertReceivedOnNext(list);
        return this;
    }

    public final AssertableSubscriber<T> awaitValueCount(int i, long j, TimeUnit timeUnit) {
        if (this.f235ts.awaitValueCount(i, j, timeUnit)) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Did not receive enough values in time. Expected: ");
        sb.append(i);
        sb.append(", Actual: ");
        sb.append(this.f235ts.getValueCount());
        throw new AssertionError(sb.toString());
    }

    public AssertableSubscriber<T> assertTerminalEvent() {
        this.f235ts.assertTerminalEvent();
        return this;
    }

    public AssertableSubscriber<T> assertUnsubscribed() {
        this.f235ts.assertUnsubscribed();
        return this;
    }

    public AssertableSubscriber<T> assertNoErrors() {
        this.f235ts.assertNoErrors();
        return this;
    }

    public AssertableSubscriber<T> awaitTerminalEvent() {
        this.f235ts.awaitTerminalEvent();
        return this;
    }

    public AssertableSubscriber<T> awaitTerminalEvent(long j, TimeUnit timeUnit) {
        this.f235ts.awaitTerminalEvent(j, timeUnit);
        return this;
    }

    public AssertableSubscriber<T> awaitTerminalEventAndUnsubscribeOnTimeout(long j, TimeUnit timeUnit) {
        this.f235ts.awaitTerminalEventAndUnsubscribeOnTimeout(j, timeUnit);
        return this;
    }

    public Thread getLastSeenThread() {
        return this.f235ts.getLastSeenThread();
    }

    public AssertableSubscriber<T> assertCompleted() {
        this.f235ts.assertCompleted();
        return this;
    }

    public AssertableSubscriber<T> assertNotCompleted() {
        this.f235ts.assertNotCompleted();
        return this;
    }

    public AssertableSubscriber<T> assertError(Class<? extends Throwable> cls) {
        this.f235ts.assertError(cls);
        return this;
    }

    public AssertableSubscriber<T> assertError(Throwable th) {
        this.f235ts.assertError(th);
        return this;
    }

    public AssertableSubscriber<T> assertNoTerminalEvent() {
        this.f235ts.assertNoTerminalEvent();
        return this;
    }

    public AssertableSubscriber<T> assertNoValues() {
        this.f235ts.assertNoValues();
        return this;
    }

    public AssertableSubscriber<T> assertValueCount(int i) {
        this.f235ts.assertValueCount(i);
        return this;
    }

    public AssertableSubscriber<T> assertValues(T... tArr) {
        this.f235ts.assertValues(tArr);
        return this;
    }

    public AssertableSubscriber<T> assertValue(T t) {
        this.f235ts.assertValue(t);
        return this;
    }

    public final AssertableSubscriber<T> assertValuesAndClear(T t, T... tArr) {
        this.f235ts.assertValuesAndClear(t, tArr);
        return this;
    }

    public final AssertableSubscriber<T> perform(Action0 action0) {
        action0.call();
        return this;
    }

    public String toString() {
        return this.f235ts.toString();
    }

    public final AssertableSubscriber<T> assertResult(T... tArr) {
        this.f235ts.assertValues(tArr);
        this.f235ts.assertNoErrors();
        this.f235ts.assertCompleted();
        return this;
    }

    public final AssertableSubscriber<T> assertFailure(Class<? extends Throwable> cls, T... tArr) {
        this.f235ts.assertValues(tArr);
        this.f235ts.assertError(cls);
        this.f235ts.assertNotCompleted();
        return this;
    }

    public final AssertableSubscriber<T> assertFailureAndMessage(Class<? extends Throwable> cls, String str, T... tArr) {
        this.f235ts.assertValues(tArr);
        this.f235ts.assertError(cls);
        this.f235ts.assertNotCompleted();
        String message = ((Throwable) this.f235ts.getOnErrorEvents().get(0)).getMessage();
        if (message == str || (str != null && str.equals(message))) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Error message differs. Expected: '");
        sb.append(str);
        sb.append("', Received: '");
        sb.append(message);
        sb.append("'");
        throw new AssertionError(sb.toString());
    }
}

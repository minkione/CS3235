package flow;

import flow.Backstack.Builder;
import flow.Backstack.Entry;
import java.util.Iterator;

public final class Flow {
    /* access modifiers changed from: private */
    public Backstack backstack;
    /* access modifiers changed from: private */
    public final Listener listener;
    /* access modifiers changed from: private */
    public Transition transition;

    public interface Callback {
        void onComplete();
    }

    public enum Direction {
        FORWARD,
        BACKWARD,
        REPLACE
    }

    public interface Listener {
        /* renamed from: go */
        void mo17059go(Backstack backstack, Direction direction, Callback callback);
    }

    private abstract class Transition implements Callback {
        boolean finished;
        Transition next;
        Backstack nextBackstack;

        /* access modifiers changed from: protected */
        public abstract void execute();

        private Transition() {
        }

        /* access modifiers changed from: 0000 */
        public void enqueue(Transition transition) {
            Transition transition2 = this.next;
            if (transition2 == null) {
                this.next = transition;
            } else {
                transition2.enqueue(transition);
            }
        }

        public void onComplete() {
            if (!this.finished) {
                Backstack backstack = this.nextBackstack;
                if (backstack != null) {
                    Flow.this.backstack = backstack;
                }
                this.finished = true;
                Transition transition = this.next;
                if (transition != null) {
                    Flow.this.transition = transition;
                    Flow.this.transition.execute();
                    return;
                }
                return;
            }
            throw new IllegalStateException("onComplete already called for this transition");
        }

        /* access modifiers changed from: protected */
        /* renamed from: go */
        public void mo21649go(Backstack backstack, Direction direction) {
            this.nextBackstack = backstack;
            Flow.this.listener.mo17059go(backstack, direction, this);
        }
    }

    public Flow(Backstack backstack2, Listener listener2) {
        this.listener = listener2;
        this.backstack = backstack2;
    }

    public Backstack getBackstack() {
        return this.backstack;
    }

    public void goTo(final Object obj) {
        move(new Transition() {
            public void execute() {
                mo21649go(Flow.this.backstack.buildUpon().push(obj).build(), Direction.FORWARD);
            }
        });
    }

    public void resetTo(final Object obj) {
        move(new Transition() {
            public void execute() {
                Object obj;
                Builder buildUpon = Flow.this.backstack.buildUpon();
                Iterator reverseIterator = Flow.this.backstack.reverseIterator();
                int i = 0;
                while (true) {
                    obj = null;
                    if (!reverseIterator.hasNext()) {
                        break;
                    } else if (((Entry) reverseIterator.next()).getScreen().equals(obj)) {
                        for (int i2 = 0; i2 < Flow.this.backstack.size() - i; i2++) {
                            obj = buildUpon.pop().getScreen();
                        }
                    } else {
                        i++;
                    }
                }
                if (obj != null) {
                    buildUpon.push(obj);
                    mo21649go(buildUpon.build(), Direction.BACKWARD);
                    return;
                }
                buildUpon.push(obj);
                mo21649go(buildUpon.build(), Direction.FORWARD);
            }
        });
    }

    public void replaceTo(final Object obj) {
        move(new Transition() {
            public void execute() {
                mo21649go(Flow.preserveEquivalentPrefix(Flow.this.backstack, Backstack.fromUpChain(obj)), Direction.REPLACE);
            }
        });
    }

    public boolean goUp() {
        boolean z;
        if (!(this.backstack.current().getScreen() instanceof HasParent)) {
            Transition transition2 = this.transition;
            if (transition2 == null || transition2.finished) {
                z = false;
                move(new Transition() {
                    public void execute() {
                        Object screen = Flow.this.backstack.current().getScreen();
                        if (screen instanceof HasParent) {
                            mo21649go(Flow.preserveEquivalentPrefix(Flow.this.backstack, Backstack.fromUpChain(((HasParent) screen).getParent())), Direction.BACKWARD);
                            return;
                        }
                        onComplete();
                    }
                });
                return z;
            }
        }
        z = true;
        move(new Transition() {
            public void execute() {
                Object screen = Flow.this.backstack.current().getScreen();
                if (screen instanceof HasParent) {
                    mo21649go(Flow.preserveEquivalentPrefix(Flow.this.backstack, Backstack.fromUpChain(((HasParent) screen).getParent())), Direction.BACKWARD);
                    return;
                }
                onComplete();
            }
        });
        return z;
    }

    public boolean goBack() {
        boolean z = true;
        if (this.backstack.size() <= 1) {
            Transition transition2 = this.transition;
            if (transition2 == null || transition2.finished) {
                z = false;
            }
        }
        move(new Transition() {
            public void execute() {
                if (Flow.this.backstack.size() == 1) {
                    onComplete();
                    return;
                }
                Builder buildUpon = Flow.this.backstack.buildUpon();
                buildUpon.pop();
                mo21649go(buildUpon.build(), Direction.BACKWARD);
            }
        });
        return z;
    }

    public void forward(final Backstack backstack2) {
        move(new Transition() {
            public void execute() {
                mo21649go(backstack2, Direction.FORWARD);
            }
        });
    }

    public void backward(final Backstack backstack2) {
        move(new Transition() {
            public void execute() {
                mo21649go(backstack2, Direction.BACKWARD);
            }
        });
    }

    private void move(Transition transition2) {
        Transition transition3 = this.transition;
        if (transition3 == null || transition3.finished) {
            this.transition = transition2;
            transition2.execute();
            return;
        }
        this.transition.enqueue(transition2);
    }

    /* access modifiers changed from: private */
    public static Backstack preserveEquivalentPrefix(Backstack backstack2, Backstack backstack3) {
        Iterator reverseIterator = backstack2.reverseIterator();
        Iterator reverseIterator2 = backstack3.reverseIterator();
        Builder emptyBuilder = Backstack.emptyBuilder();
        while (true) {
            if (!reverseIterator2.hasNext()) {
                break;
            }
            Entry entry = (Entry) reverseIterator2.next();
            if (reverseIterator.hasNext()) {
                Entry entry2 = (Entry) reverseIterator.next();
                if (!entry2.getScreen().equals(entry.getScreen())) {
                    emptyBuilder.push(entry.getScreen());
                    break;
                }
                emptyBuilder.push(entry2.getScreen());
            } else {
                emptyBuilder.push(entry.getScreen());
                break;
            }
        }
        while (reverseIterator2.hasNext()) {
            emptyBuilder.push(((Entry) reverseIterator2.next()).getScreen());
        }
        return emptyBuilder.build();
    }
}

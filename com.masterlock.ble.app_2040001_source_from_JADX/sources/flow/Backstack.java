package flow;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public final class Backstack implements Iterable<Entry> {
    private final Deque<Entry> backstack;

    public static final class Builder {
        private final Deque<Entry> backstack;
        private long highestId;

        private Builder(Collection<Entry> collection) {
            this.highestId = this.highestId;
            this.backstack = new ArrayDeque(collection);
        }

        public Builder push(Object obj) {
            this.backstack.push(new Entry(obj));
            return this;
        }

        public Builder addAll(Collection<Object> collection) {
            for (Object entry : collection) {
                this.backstack.push(new Entry(entry));
            }
            return this;
        }

        public Entry peek() {
            return (Entry) this.backstack.peek();
        }

        public Entry pop() {
            return (Entry) this.backstack.pop();
        }

        public Builder clear() {
            this.backstack.clear();
            return this;
        }

        public Backstack build() {
            if (!this.backstack.isEmpty()) {
                return new Backstack(this.backstack);
            }
            throw new IllegalStateException("Backstack may not be empty");
        }
    }

    public static final class Entry {
        private final Object screen;

        private Entry(Object obj) {
            this.screen = obj;
        }

        public Object getScreen() {
            return this.screen;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(this.screen);
            sb.append("}");
            return sb.toString();
        }
    }

    private static class ReadIterator<T> implements Iterator<T> {
        private final Iterator<T> iterator;

        public ReadIterator(Iterator<T> it) {
            this.iterator = it;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public T next() {
            return this.iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static Backstack from(Parcelable parcelable, Parcer<Object> parcer) {
        ArrayList parcelableArrayList = ((Bundle) parcelable).getParcelableArrayList("ENTRIES");
        ArrayDeque arrayDeque = new ArrayDeque(parcelableArrayList.size());
        Iterator it = parcelableArrayList.iterator();
        while (it.hasNext()) {
            Bundle bundle = (Bundle) it.next();
            Screen screen = (Screen) parcer.unwrap(bundle.getParcelable("PATH"));
            Entry entry = new Entry(screen);
            screen.setViewState(bundle.getSparseParcelableArray("VIEW_STATE"));
            arrayDeque.add(entry);
        }
        return new Backstack(arrayDeque);
    }

    public static Builder emptyBuilder() {
        return new Builder(Collections.emptyList());
    }

    public static Backstack single(Object obj) {
        return emptyBuilder().push(obj).build();
    }

    private Backstack(Deque<Entry> deque) {
        this.backstack = deque;
    }

    public Iterator<Entry> iterator() {
        return new ReadIterator(this.backstack.iterator());
    }

    public Iterator<Entry> reverseIterator() {
        return new ReadIterator(this.backstack.descendingIterator());
    }

    public Parcelable getParcelable(Parcer<Object> parcer) {
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList(this.backstack.size());
        for (Entry entry : this.backstack) {
            Bundle bundle2 = new Bundle();
            Screen screen = (Screen) entry.getScreen();
            bundle2.putParcelable("PATH", parcer.wrap(screen));
            bundle2.putSparseParcelableArray("VIEW_STATE", screen.getViewState());
            arrayList.add(bundle2);
        }
        bundle.putParcelableArrayList("ENTRIES", arrayList);
        return bundle;
    }

    public int size() {
        return this.backstack.size();
    }

    public Entry current() {
        return (Entry) this.backstack.peek();
    }

    public Builder buildUpon() {
        return new Builder(this.backstack);
    }

    public String toString() {
        return this.backstack.toString();
    }

    public static Backstack fromUpChain(Object obj) {
        LinkedList linkedList = new LinkedList();
        while (obj instanceof HasParent) {
            linkedList.addFirst(obj);
            obj = ((HasParent) obj).getParent();
        }
        linkedList.addFirst(obj);
        Builder emptyBuilder = emptyBuilder();
        emptyBuilder.addAll(linkedList);
        return emptyBuilder.build();
    }
}

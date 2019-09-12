package flow;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    private transient SparseArray<Parcelable> viewState;

    /* access modifiers changed from: protected */
    public void buildPath(List<Screen> list) {
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof Screen) && getName().equals(((Screen) obj).getName());
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
        return ObjectUtils.getClass(this).getName();
    }

    /* access modifiers changed from: protected */
    public SparseArray<Parcelable> getViewState() {
        return this.viewState;
    }

    public void setViewState(SparseArray<Parcelable> sparseArray) {
        this.viewState = sparseArray;
    }

    public void restoreHierarchyState(View view) {
        if (getViewState() != null) {
            view.restoreHierarchyState(getViewState());
        }
    }

    public final List<Screen> getPath() {
        ArrayList arrayList = new ArrayList();
        buildPath(arrayList);
        if (arrayList.isEmpty() || isPathLeaf(arrayList)) {
            arrayList.add(this);
        }
        return arrayList;
    }

    private boolean isPathLeaf(List<Screen> list) {
        return !equals(list.get(list.size() - 1));
    }
}

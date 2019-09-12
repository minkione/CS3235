package com.square.flow.appflow;

import android.os.Bundle;
import com.square.flow.util.Preconditions;
import flow.Backstack;
import flow.Flow;
import flow.Flow.Listener;
import flow.Parcer;

public class FlowBundler {
    private static final String FLOW_KEY = "flow_key";
    private final Object defaultScreen;

    /* renamed from: flow reason: collision with root package name */
    private Flow f279flow;
    private final Listener listener;
    private final Parcer<Object> parcer;

    /* access modifiers changed from: protected */
    public Backstack getBackstackToSave(Backstack backstack) {
        return backstack;
    }

    public FlowBundler(Object obj, Listener listener2, Parcer<Object> parcer2) {
        this.listener = listener2;
        this.defaultScreen = obj;
        this.parcer = parcer2;
    }

    public AppFlow onCreate(Bundle bundle) {
        Backstack backstack;
        Preconditions.checkArgument(this.f279flow == null, "Flow already created.", new Object[0]);
        if (bundle == null || !bundle.containsKey(FLOW_KEY)) {
            backstack = Backstack.fromUpChain(this.defaultScreen);
        } else {
            backstack = Backstack.from(bundle.getParcelable(FLOW_KEY), this.parcer);
        }
        this.f279flow = new Flow(backstack, this.listener);
        return new AppFlow(this.f279flow);
    }

    public void onSaveInstanceState(Bundle bundle) {
        Backstack backstackToSave = getBackstackToSave(this.f279flow.getBackstack());
        if (backstackToSave != null) {
            bundle.putParcelable(FLOW_KEY, backstackToSave.getParcelable(this.parcer));
        }
    }

    public final Flow getFlow() {
        return this.f279flow;
    }
}

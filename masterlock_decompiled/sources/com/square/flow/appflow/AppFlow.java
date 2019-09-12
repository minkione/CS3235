package com.square.flow.appflow;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import flow.Flow;

public final class AppFlow {
    private static final String APP_FLOW_SERVICE = "app_flow";

    /* renamed from: flow reason: collision with root package name */
    private final Flow f278flow;

    private static final class LocalScreenWrapper extends ContextWrapper {
        static final String LOCAL_WRAPPER_SERVICE = "flow_local_screen_context_wrapper";
        private LayoutInflater inflater;
        final Object localScreen;

        static LocalScreenWrapper get(Context context) {
            return (LocalScreenWrapper) context.getSystemService(LOCAL_WRAPPER_SERVICE);
        }

        LocalScreenWrapper(Context context, Object obj) {
            super(context);
            this.localScreen = obj;
        }

        public Object getSystemService(String str) {
            if (LOCAL_WRAPPER_SERVICE.equals(str)) {
                return this;
            }
            if (!"layout_inflater".equals(str)) {
                return super.getSystemService(str);
            }
            if (this.inflater == null) {
                this.inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return this.inflater;
        }
    }

    protected AppFlow(Flow flow2) {
        this.f278flow = flow2;
    }

    public static Flow get(Context context) {
        return ((AppFlow) context.getSystemService(APP_FLOW_SERVICE)).f278flow;
    }

    public static <T> T getScreen(Context context) {
        return LocalScreenWrapper.get(context).localScreen;
    }

    public static void loadInitialScreen(Context context) {
        get(context).resetTo(get(context).getBackstack().current().getScreen());
    }

    public static boolean isAppFlowSystemService(String str) {
        return APP_FLOW_SERVICE.equals(str);
    }

    static Context setScreen(Context context, Object obj) {
        return new LocalScreenWrapper(context, obj);
    }
}

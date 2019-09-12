package com.masterlock.api.module;

import com.google.gson.Gson;
import com.masterlock.api.client.AccountClient;
import com.masterlock.api.client.GuestClient;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.api.client.ProductInvitationClient;
import com.masterlock.api.client.TermsOfServiceClient;
import com.masterlock.api.client.TimezoneClient;
import com.masterlock.api.util.ApiErrorHandler;
import com.masterlock.api.util.IResourceWrapper;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public final class ApiModule$$ModuleAdapter extends ModuleAdapter<ApiModule> {
    private static final Class<?>[] INCLUDES = new Class[0];
    private static final String[] INJECTS = new String[0];
    private static final Class<?>[] STATIC_INJECTIONS = new Class[0];

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvideAccountClientProvidesAdapter extends ProvidesBinding<AccountClient> implements Provider<AccountClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvideAccountClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.AccountClient", true, "com.masterlock.api.module.ApiModule", "provideAccountClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public AccountClient get() {
            return this.module.provideAccountClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvideKMSDeviceClientProvidesAdapter extends ProvidesBinding<KMSDeviceClient> implements Provider<KMSDeviceClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvideKMSDeviceClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.KMSDeviceClient", true, "com.masterlock.api.module.ApiModule", "provideKMSDeviceClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public KMSDeviceClient get() {
            return this.module.provideKMSDeviceClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvideKMSDeviceLogClientProvidesAdapter extends ProvidesBinding<KMSDeviceLogClient> implements Provider<KMSDeviceLogClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvideKMSDeviceLogClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.KMSDeviceLogClient", true, "com.masterlock.api.module.ApiModule", "provideKMSDeviceLogClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public KMSDeviceLogClient get() {
            return this.module.provideKMSDeviceLogClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvideProductClientProvidesAdapter extends ProvidesBinding<ProductClient> implements Provider<ProductClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvideProductClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.ProductClient", true, "com.masterlock.api.module.ApiModule", "provideProductClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public ProductClient get() {
            return this.module.provideProductClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvideTermsOfServiceClientProvidesAdapter extends ProvidesBinding<TermsOfServiceClient> implements Provider<TermsOfServiceClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvideTermsOfServiceClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.TermsOfServiceClient", true, "com.masterlock.api.module.ApiModule", "provideTermsOfServiceClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public TermsOfServiceClient get() {
            return this.module.provideTermsOfServiceClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvideTimezoneClientProvidesAdapter extends ProvidesBinding<TimezoneClient> implements Provider<TimezoneClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvideTimezoneClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.TimezoneClient", true, "com.masterlock.api.module.ApiModule", "provideTimezoneClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public TimezoneClient get() {
            return this.module.provideTimezoneClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvidesAdapterProvidesAdapter extends ProvidesBinding<RestAdapter> implements Provider<RestAdapter> {
        private Binding<ApiErrorHandler> apiErrorHandler;
        private Binding<Gson> gson;
        private final ApiModule module;
        private Binding<RequestInterceptor> requestInterceptor;

        public ProvidesAdapterProvidesAdapter(ApiModule apiModule) {
            super("retrofit.RestAdapter", true, "com.masterlock.api.module.ApiModule", "providesAdapter");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.gson = linker.requestBinding("com.google.gson.Gson", ApiModule.class, getClass().getClassLoader());
            this.requestInterceptor = linker.requestBinding("retrofit.RequestInterceptor", ApiModule.class, getClass().getClassLoader());
            this.apiErrorHandler = linker.requestBinding("com.masterlock.api.util.ApiErrorHandler", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.gson);
            set.add(this.requestInterceptor);
            set.add(this.apiErrorHandler);
        }

        public RestAdapter get() {
            return this.module.providesAdapter((Gson) this.gson.get(), (RequestInterceptor) this.requestInterceptor.get(), (ApiErrorHandler) this.apiErrorHandler.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvidesApiErrorHandlerProvidesAdapter extends ProvidesBinding<ApiErrorHandler> implements Provider<ApiErrorHandler> {
        private final ApiModule module;
        private Binding<IResourceWrapper> resourceWrapper;

        public ProvidesApiErrorHandlerProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.util.ApiErrorHandler", true, "com.masterlock.api.module.ApiModule", "providesApiErrorHandler");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.resourceWrapper = linker.requestBinding("com.masterlock.api.util.IResourceWrapper", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.resourceWrapper);
        }

        public ApiErrorHandler get() {
            return this.module.providesApiErrorHandler((IResourceWrapper) this.resourceWrapper.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvidesGsonProvidesAdapter extends ProvidesBinding<Gson> implements Provider<Gson> {
        private final ApiModule module;

        public ProvidesGsonProvidesAdapter(ApiModule apiModule) {
            super("com.google.gson.Gson", true, "com.masterlock.api.module.ApiModule", "providesGson");
            this.module = apiModule;
            setLibrary(true);
        }

        public Gson get() {
            return this.module.providesGson();
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvidesGuestClientProvidesAdapter extends ProvidesBinding<GuestClient> implements Provider<GuestClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvidesGuestClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.GuestClient", true, "com.masterlock.api.module.ApiModule", "providesGuestClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public GuestClient get() {
            return this.module.providesGuestClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvidesProductInvitationClientProvidesAdapter extends ProvidesBinding<ProductInvitationClient> implements Provider<ProductInvitationClient> {
        private final ApiModule module;
        private Binding<RestAdapter> restAdapter;

        public ProvidesProductInvitationClientProvidesAdapter(ApiModule apiModule) {
            super("com.masterlock.api.client.ProductInvitationClient", true, "com.masterlock.api.module.ApiModule", "providesProductInvitationClient");
            this.module = apiModule;
            setLibrary(true);
        }

        public void attach(Linker linker) {
            this.restAdapter = linker.requestBinding("retrofit.RestAdapter", ApiModule.class, getClass().getClassLoader());
        }

        public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
            set.add(this.restAdapter);
        }

        public ProductInvitationClient get() {
            return this.module.providesProductInvitationClient((RestAdapter) this.restAdapter.get());
        }
    }

    /* compiled from: ApiModule$$ModuleAdapter */
    public static final class ProvidesRequestInterceptorProvidesAdapter extends ProvidesBinding<RequestInterceptor> implements Provider<RequestInterceptor> {
        private final ApiModule module;

        public ProvidesRequestInterceptorProvidesAdapter(ApiModule apiModule) {
            super("retrofit.RequestInterceptor", true, "com.masterlock.api.module.ApiModule", "providesRequestInterceptor");
            this.module = apiModule;
            setLibrary(true);
        }

        public RequestInterceptor get() {
            return this.module.providesRequestInterceptor();
        }
    }

    public ApiModule$$ModuleAdapter() {
        super(ApiModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, false, true);
    }

    public void getBindings(BindingsGroup bindingsGroup, ApiModule apiModule) {
        bindingsGroup.contributeProvidesBinding("com.google.gson.Gson", new ProvidesGsonProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("retrofit.RequestInterceptor", new ProvidesRequestInterceptorProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("retrofit.RestAdapter", new ProvidesAdapterProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.AccountClient", new ProvideAccountClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.ProductClient", new ProvideProductClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.TermsOfServiceClient", new ProvideTermsOfServiceClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.KMSDeviceClient", new ProvideKMSDeviceClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.KMSDeviceLogClient", new ProvideKMSDeviceLogClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.GuestClient", new ProvidesGuestClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.ProductInvitationClient", new ProvidesProductInvitationClientProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.util.ApiErrorHandler", new ProvidesApiErrorHandlerProvidesAdapter(apiModule));
        bindingsGroup.contributeProvidesBinding("com.masterlock.api.client.TimezoneClient", new ProvideTimezoneClientProvidesAdapter(apiModule));
    }
}

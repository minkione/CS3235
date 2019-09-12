package dagger.internal;

public abstract class StaticInjection {
    public abstract void attach(Linker linker);

    public abstract void inject();
}

package dagger.internal.codegen;

import dagger.internal.Binding;
import dagger.internal.Loader;
import dagger.internal.ModuleAdapter;
import dagger.internal.StaticInjection;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public final class GraphAnalysisLoader extends Loader {
    private final ProcessingEnvironment processingEnv;

    public GraphAnalysisLoader(ProcessingEnvironment processingEnvironment) {
        this.processingEnv = processingEnvironment;
    }

    public Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z) {
        TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(str2.replace('$', '.'));
        if (typeElement == null || typeElement.getKind() == ElementKind.INTERFACE) {
            return null;
        }
        return GraphAnalysisInjectBinding.create(typeElement, z);
    }

    public <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls) {
        throw new UnsupportedOperationException();
    }

    public StaticInjection getStaticInjection(Class<?> cls) {
        throw new UnsupportedOperationException();
    }
}

package dagger.internal.codegen;

import dagger.internal.Linker;
import dagger.internal.StaticInjection;
import javax.inject.Inject;
import javax.lang.model.element.Element;

public final class GraphAnalysisStaticInjection extends StaticInjection {
    private final Element enclosingClass;

    public GraphAnalysisStaticInjection(Element element) {
        this.enclosingClass = element;
    }

    public void attach(Linker linker) {
        for (Element element : this.enclosingClass.getEnclosedElements()) {
            if (element.getKind().isField() && Util.isStatic(element) && ((Inject) element.getAnnotation(Inject.class)) != null) {
                linker.requestBinding(GeneratorKeys.get(element.asType()), this.enclosingClass.toString(), getClass().getClassLoader());
            }
        }
    }

    public void inject() {
        throw new UnsupportedOperationException();
    }
}

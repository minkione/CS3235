package dagger.internal.codegen;

import dagger.internal.Linker.ErrorHandler;
import java.util.List;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

final class GraphAnalysisErrorHandler implements ErrorHandler {
    private final String moduleName;
    private final ProcessingEnvironment processingEnv;

    GraphAnalysisErrorHandler(ProcessingEnvironment processingEnvironment, String str) {
        this.processingEnv = processingEnvironment;
        this.moduleName = str;
    }

    public void handleErrors(List<String> list) {
        TypeElement typeElement = this.processingEnv.getElementUtils().getTypeElement(this.moduleName);
        for (String str : list) {
            Messager messager = this.processingEnv.getMessager();
            Kind kind = Kind.ERROR;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" for ");
            sb.append(this.moduleName);
            messager.printMessage(kind, sb.toString(), typeElement);
        }
    }
}

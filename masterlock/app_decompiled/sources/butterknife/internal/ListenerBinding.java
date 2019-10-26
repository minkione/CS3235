package butterknife.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class ListenerBinding implements Binding {
    private final String name;
    private final List<Parameter> parameters;
    private final boolean required;

    ListenerBinding(String str, List<Parameter> list, boolean z) {
        this.name = str;
        this.parameters = Collections.unmodifiableList(new ArrayList(list));
        this.required = z;
    }

    public String getName() {
        return this.name;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("method '");
        sb.append(this.name);
        sb.append("'");
        return sb.toString();
    }

    public boolean isRequired() {
        return this.required;
    }
}

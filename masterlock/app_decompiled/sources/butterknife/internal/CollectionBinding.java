package butterknife.internal;

final class CollectionBinding implements Binding {
    private final Kind kind;
    private final String name;
    private final boolean required;
    private final String type;

    enum Kind {
        ARRAY,
        LIST
    }

    CollectionBinding(String str, String str2, Kind kind2, boolean z) {
        this.name = str;
        this.type = str2;
        this.kind = kind2;
        this.required = z;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Kind getKind() {
        return this.kind;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("field '");
        sb.append(this.name);
        sb.append("'");
        return sb.toString();
    }
}

package butterknife.internal;

final class ViewBinding implements Binding {
    private final String name;
    private final boolean required;
    private final String type;

    ViewBinding(String str, String str2, boolean z) {
        this.name = str;
        this.type = str2;
        this.required = z;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("field '");
        sb.append(this.name);
        sb.append("'");
        return sb.toString();
    }

    public boolean isRequired() {
        return this.required;
    }
}

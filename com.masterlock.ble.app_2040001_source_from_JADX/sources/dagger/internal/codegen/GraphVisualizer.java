package dagger.internal.codegen;

import dagger.internal.Binding;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GraphVisualizer {
    private static final Pattern KEY_PATTERN = Pattern.compile("(?:@(?:[\\w$]+\\.)*([\\w$]+)(?:\\(.*\\))?/)?(?:members/)?(?:[\\w$]+\\.)*([\\w$]+)(\\<[^/]+\\>)?((\\[\\])*)");

    private static class BindingComparator implements Comparator<Binding<?>> {
        private BindingComparator() {
        }

        public int compare(Binding<?> binding, Binding<?> binding2) {
            return getStringForBinding(binding).compareTo(getStringForBinding(binding2));
        }

        private String getStringForBinding(Binding<?> binding) {
            return binding == null ? "" : binding.toString();
        }
    }

    public void write(Map<String, Binding<?>> map, GraphVizWriter graphVizWriter) throws IOException {
        Map buildNamesIndex = buildNamesIndex(map);
        graphVizWriter.beginGraph("concentrate", "true");
        for (Entry entry : buildNamesIndex.entrySet()) {
            Binding binding = (Binding) entry.getKey();
            String str = (String) entry.getValue();
            TreeSet<Binding> treeSet = new TreeSet<>(new BindingComparator());
            binding.getDependencies(treeSet, treeSet);
            for (Binding binding2 : treeSet) {
                String str2 = (String) buildNamesIndex.get(binding2);
                if (str2 == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unbound:");
                    sb.append(binding2.provideKey);
                    str2 = sb.toString();
                }
                graphVizWriter.edge(str, str2, new String[0]);
            }
        }
        graphVizWriter.endGraph();
    }

    private Map<Binding<?>, String> buildNamesIndex(Map<String, Binding<?>> map) {
        TreeMap treeMap = new TreeMap();
        HashSet hashSet = new HashSet();
        for (Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            Binding binding = (Binding) entry.getValue();
            Binding binding2 = (Binding) treeMap.put(shortName(str), binding);
            if (!(binding2 == null || binding2 == binding)) {
                hashSet.add(binding);
                hashSet.add(binding2);
            }
        }
        for (Entry entry2 : map.entrySet()) {
            Binding binding3 = (Binding) entry2.getValue();
            if (hashSet.contains(binding3)) {
                String str2 = (String) entry2.getKey();
                treeMap.remove(shortName(str2));
                treeMap.put(str2, binding3);
            }
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Entry entry3 : treeMap.entrySet()) {
            linkedHashMap.put(entry3.getValue(), entry3.getKey());
        }
        return linkedHashMap;
    }

    /* access modifiers changed from: 0000 */
    public String shortName(String str) {
        Matcher matcher = KEY_PATTERN.matcher(str);
        if (matcher.matches()) {
            StringBuilder sb = new StringBuilder();
            String group = matcher.group(1);
            if (group != null) {
                sb.append('@');
                sb.append(group);
                sb.append(' ');
            }
            sb.append(matcher.group(2));
            String group2 = matcher.group(3);
            if (group2 != null) {
                sb.append(group2);
            }
            String group3 = matcher.group(4);
            if (group3 != null) {
                sb.append(group3);
            }
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unexpected key: ");
        sb2.append(str);
        throw new IllegalArgumentException(sb2.toString());
    }
}

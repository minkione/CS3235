package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@Beta
public final class TypeResolver {
    private final TypeTable typeTable;

    private static final class TypeMappingIntrospector extends TypeVisitor {
        private static final WildcardCapturer wildcardCapturer = new WildcardCapturer();
        private final Map<TypeVariable<?>, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariable<?>, Type> getTypeMappings(Type type) {
            TypeMappingIntrospector typeMappingIntrospector = new TypeMappingIntrospector();
            typeMappingIntrospector.visit(wildcardCapturer.capture(type));
            return ImmutableMap.copyOf(typeMappingIntrospector.mappings);
        }

        /* access modifiers changed from: 0000 */
        public void visitClass(Class<?> cls) {
            visit(cls.getGenericSuperclass());
            visit(cls.getGenericInterfaces());
        }

        /* access modifiers changed from: 0000 */
        public void visitParameterizedType(ParameterizedType parameterizedType) {
            Class cls = (Class) parameterizedType.getRawType();
            TypeVariable[] typeParameters = cls.getTypeParameters();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(typeParameters.length == actualTypeArguments.length);
            for (int i = 0; i < typeParameters.length; i++) {
                map(typeParameters[i], actualTypeArguments[i]);
            }
            visit(cls);
            visit(parameterizedType.getOwnerType());
        }

        /* access modifiers changed from: 0000 */
        public void visitTypeVariable(TypeVariable<?> typeVariable) {
            visit(typeVariable.getBounds());
        }

        /* access modifiers changed from: 0000 */
        public void visitWildcardType(WildcardType wildcardType) {
            visit(wildcardType.getUpperBounds());
        }

        private void map(TypeVariable<?> typeVariable, Type type) {
            if (!this.mappings.containsKey(typeVariable)) {
                Type type2 = type;
                while (type2 != null) {
                    if (typeVariable.equals(type2)) {
                        while (type != null) {
                            type = (Type) this.mappings.remove(type);
                        }
                        return;
                    }
                    type2 = (Type) this.mappings.get(type2);
                }
                this.mappings.put(typeVariable, type);
            }
        }
    }

    private static class TypeTable {
        private final ImmutableMap<TypeVariable<?>, Type> map;

        TypeTable() {
            this.map = ImmutableMap.m84of();
        }

        private TypeTable(ImmutableMap<TypeVariable<?>, Type> immutableMap) {
            this.map = immutableMap;
        }

        /* access modifiers changed from: 0000 */
        public final TypeTable where(Map<? extends TypeVariable<?>, ? extends Type> map2) {
            Builder builder = ImmutableMap.builder();
            builder.putAll(this.map);
            for (Entry entry : map2.entrySet()) {
                TypeVariable typeVariable = (TypeVariable) entry.getKey();
                Type type = (Type) entry.getValue();
                Preconditions.checkArgument(!typeVariable.equals(type), "Type variable %s bound to itself", typeVariable);
                builder.put(typeVariable, type);
            }
            return new TypeTable(builder.build());
        }

        /* access modifiers changed from: 0000 */
        public final Type resolve(final TypeVariable<?> typeVariable) {
            return resolveInternal(typeVariable, new TypeTable() {
                public Type resolveInternal(TypeVariable<?> typeVariable, TypeTable typeTable) {
                    if (typeVariable.getGenericDeclaration().equals(typeVariable.getGenericDeclaration())) {
                        return typeVariable;
                    }
                    return this.resolveInternal(typeVariable, typeTable);
                }
            });
        }

        /* access modifiers changed from: 0000 */
        public Type resolveInternal(TypeVariable<?> typeVariable, TypeTable typeTable) {
            Type type = (Type) this.map.get(typeVariable);
            if (type != null) {
                return new TypeResolver(typeTable).resolveType(type);
            }
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length == 0) {
                return typeVariable;
            }
            return Types.newTypeVariable(typeVariable.getGenericDeclaration(), typeVariable.getName(), new TypeResolver(typeTable).resolveTypes(bounds));
        }
    }

    private static final class WildcardCapturer {

        /* renamed from: id */
        private final AtomicInteger f144id;

        private WildcardCapturer() {
            this.f144id = new AtomicInteger();
        }

        /* access modifiers changed from: 0000 */
        public Type capture(Type type) {
            Preconditions.checkNotNull(type);
            if ((type instanceof Class) || (type instanceof TypeVariable)) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                return Types.newArrayType(capture(((GenericArrayType) type).getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                return Types.newParameterizedTypeWithOwner(captureNullable(parameterizedType.getOwnerType()), (Class) parameterizedType.getRawType(), capture(parameterizedType.getActualTypeArguments()));
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                if (wildcardType.getLowerBounds().length != 0) {
                    return type;
                }
                Type[] upperBounds = wildcardType.getUpperBounds();
                StringBuilder sb = new StringBuilder();
                sb.append("capture#");
                sb.append(this.f144id.incrementAndGet());
                sb.append("-of ? extends ");
                sb.append(Joiner.m39on('&').join((Object[]) upperBounds));
                return Types.newTypeVariable(WildcardCapturer.class, sb.toString(), wildcardType.getUpperBounds());
            } else {
                throw new AssertionError("must have been one of the known types");
            }
        }

        private Type captureNullable(@Nullable Type type) {
            if (type == null) {
                return null;
            }
            return capture(type);
        }

        private Type[] capture(Type[] typeArr) {
            Type[] typeArr2 = new Type[typeArr.length];
            for (int i = 0; i < typeArr.length; i++) {
                typeArr2[i] = capture(typeArr[i]);
            }
            return typeArr2;
        }
    }

    public TypeResolver() {
        this.typeTable = new TypeTable();
    }

    private TypeResolver(TypeTable typeTable2) {
        this.typeTable = typeTable2;
    }

    static TypeResolver accordingTo(Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }

    public TypeResolver where(Type type, Type type2) {
        HashMap newHashMap = Maps.newHashMap();
        populateTypeMappings(newHashMap, (Type) Preconditions.checkNotNull(type), (Type) Preconditions.checkNotNull(type2));
        return where(newHashMap);
    }

    /* access modifiers changed from: 0000 */
    public TypeResolver where(Map<? extends TypeVariable<?>, ? extends Type> map) {
        return new TypeResolver(this.typeTable.where(map));
    }

    /* access modifiers changed from: private */
    public static void populateTypeMappings(final Map<TypeVariable<?>, Type> map, Type type, final Type type2) {
        if (!type.equals(type2)) {
            new TypeVisitor() {
                /* access modifiers changed from: 0000 */
                public void visitTypeVariable(TypeVariable<?> typeVariable) {
                    map.put(typeVariable, type2);
                }

                /* access modifiers changed from: 0000 */
                public void visitWildcardType(WildcardType wildcardType) {
                    WildcardType wildcardType2 = (WildcardType) TypeResolver.expectArgument(WildcardType.class, type2);
                    Type[] upperBounds = wildcardType.getUpperBounds();
                    Type[] upperBounds2 = wildcardType2.getUpperBounds();
                    Type[] lowerBounds = wildcardType.getLowerBounds();
                    Type[] lowerBounds2 = wildcardType2.getLowerBounds();
                    Preconditions.checkArgument(upperBounds.length == upperBounds2.length && lowerBounds.length == lowerBounds2.length, "Incompatible type: %s vs. %s", wildcardType, type2);
                    for (int i = 0; i < upperBounds.length; i++) {
                        TypeResolver.populateTypeMappings(map, upperBounds[i], upperBounds2[i]);
                    }
                    for (int i2 = 0; i2 < lowerBounds.length; i2++) {
                        TypeResolver.populateTypeMappings(map, lowerBounds[i2], lowerBounds2[i2]);
                    }
                }

                /* access modifiers changed from: 0000 */
                public void visitParameterizedType(ParameterizedType parameterizedType) {
                    ParameterizedType parameterizedType2 = (ParameterizedType) TypeResolver.expectArgument(ParameterizedType.class, type2);
                    Preconditions.checkArgument(parameterizedType.getRawType().equals(parameterizedType2.getRawType()), "Inconsistent raw type: %s vs. %s", parameterizedType, type2);
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
                    Preconditions.checkArgument(actualTypeArguments.length == actualTypeArguments2.length, "%s not compatible with %s", parameterizedType, parameterizedType2);
                    for (int i = 0; i < actualTypeArguments.length; i++) {
                        TypeResolver.populateTypeMappings(map, actualTypeArguments[i], actualTypeArguments2[i]);
                    }
                }

                /* access modifiers changed from: 0000 */
                public void visitGenericArrayType(GenericArrayType genericArrayType) {
                    Type componentType = Types.getComponentType(type2);
                    Preconditions.checkArgument(componentType != null, "%s is not an array type.", type2);
                    TypeResolver.populateTypeMappings(map, genericArrayType.getGenericComponentType(), componentType);
                }

                /* access modifiers changed from: 0000 */
                public void visitClass(Class<?> cls) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("No type mapping from ");
                    sb.append(cls);
                    throw new IllegalArgumentException(sb.toString());
                }
            }.visit(type);
        }
    }

    public Type resolveType(Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type);
        }
        if (!(type instanceof WildcardType)) {
            return type;
        }
        WildcardType wildcardType = (WildcardType) type;
        return new WildcardTypeImpl(resolveTypes(wildcardType.getLowerBounds()), resolveTypes(wildcardType.getUpperBounds()));
    }

    /* access modifiers changed from: private */
    public Type[] resolveTypes(Type[] typeArr) {
        Type[] typeArr2 = new Type[typeArr.length];
        for (int i = 0; i < typeArr.length; i++) {
            typeArr2[i] = resolveType(typeArr[i]);
        }
        return typeArr2;
    }

    private Type resolveGenericArrayType(GenericArrayType genericArrayType) {
        return Types.newArrayType(resolveType(genericArrayType.getGenericComponentType()));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType parameterizedType) {
        Type type;
        Type ownerType = parameterizedType.getOwnerType();
        if (ownerType == null) {
            type = null;
        } else {
            type = resolveType(ownerType);
        }
        Type resolveType = resolveType(parameterizedType.getRawType());
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type[] typeArr = new Type[actualTypeArguments.length];
        for (int i = 0; i < actualTypeArguments.length; i++) {
            typeArr[i] = resolveType(actualTypeArguments[i]);
        }
        return Types.newParameterizedTypeWithOwner(type, (Class) resolveType, typeArr);
    }

    /* access modifiers changed from: private */
    public static <T> T expectArgument(Class<T> cls, Object obj) {
        try {
            return cls.cast(obj);
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(obj);
            sb.append(" is not a ");
            sb.append(cls.getSimpleName());
            throw new IllegalArgumentException(sb.toString());
        }
    }
}

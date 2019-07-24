package com.zadorovskyi.cars.rent.service.kafka.interceptor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.dataformat.avro.AvroAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.avro.AvroTypeResolverBuilder;

public class PolymorphicAvroAnnotationIntrospector extends AvroAnnotationIntrospector {

    public PolymorphicAvroAnnotationIntrospector() {
    }

    protected TypeResolverBuilder<?> _findTypeResolver(MapperConfig<?> config, Annotated ann, JavaType baseType) {
        if (baseType.isJavaLangObject() || this._getUnionTypes(ann) != null
            || baseType.isAbstract() && config.getAnnotationIntrospector().findDeserializer(ann) == null) {
            TypeResolverBuilder<?> resolver = new AvroTypeResolverBuilder();
            JsonTypeInfo typeInfo = (JsonTypeInfo) ann.getAnnotation(JsonTypeInfo.class);
            if (typeInfo != null && typeInfo.defaultImpl() != JsonTypeInfo.class) {
                resolver = ((TypeResolverBuilder) resolver).defaultImpl(typeInfo.defaultImpl());
            }

            return (TypeResolverBuilder) resolver;
        } else {
            return null;
        }
    }
}

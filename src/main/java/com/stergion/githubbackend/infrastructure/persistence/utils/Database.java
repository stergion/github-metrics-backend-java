package com.stergion.githubbackend.infrastructure.persistence.utils;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Database {
    DatabaseType value();  // "mongo" or "postgres"

    @SuppressWarnings("ClassExplicitlyAnnotation")
    final class Literal extends AnnotationLiteral<Database> implements Database {
        private final DatabaseType value;

        public Literal(DatabaseType value) {
            this.value = value;
        }

        @Override
        public DatabaseType value() {
            return value;
        }
    }
}

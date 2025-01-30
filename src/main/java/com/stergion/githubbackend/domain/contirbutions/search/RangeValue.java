package com.stergion.githubbackend.domain.contirbutions.search;

import java.util.Optional;

public class RangeValue<T extends Comparable<? super T>> {
    private final T min;
    private final T max;

    private RangeValue(T min, T max) {
        this.min = min;
        this.max = max;
        validate();
    }

    private void validate() {
        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
    }

    public Optional<T> getMin() {
        return Optional.ofNullable(min);
    }

    public Optional<T> getMax() {
        return Optional.ofNullable(max);
    }

    public static <T extends Comparable<? super T>> RangeValue<T> between(T min, T max) {
        return new RangeValue<>(min, max);
    }

    public static <T extends Comparable<? super T>> RangeValue<T> min(T min) {
        return new RangeValue<>(min, null);
    }

    public static <T extends Comparable<? super T>> RangeValue<T> max(T max) {
        return new RangeValue<>(null, max);
    }

    @Override
    public String toString() {
        return "{min: %s, max: %s}".formatted(min, max);
    }
}

package com.linklingua.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean property copy utility.
 *
 * <p>Wraps Spring's {@link BeanUtils} with common object/collection copy methods,
 * used to convert between Entity, DTO and VO.</p>
 *
 * @author LinkLingua
 */
public final class BeanConvertUtil {

    private BeanConvertUtil() {
        // Utility class, must not be instantiated
    }

    /**
     * Copies matching properties from the source object into a new target object.
     *
     * @param source         the source object
     * @param targetSupplier a constructor reference for the target object, e.g. {@code UserVO::new}
     * @param <S>            the source type
     * @param <T>            the target type
     * @return the populated target object; {@code null} if the source is {@code null}
     */
    public static <S, T> T convert(S source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * Converts a list of source objects into a list of target objects.
     *
     * @param sourceList     the source list
     * @param targetSupplier a constructor reference for the target object
     * @param <S>            the source type
     * @param <T>            the target type
     * @return the converted list of target objects
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Supplier<T> targetSupplier) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        return sourceList.stream()
                .map(source -> convert(source, targetSupplier))
                .collect(Collectors.toList());
    }
}

package com.jicg.os.liteman.utils;

import com.sun.istack.internal.NotNull;
import lombok.NonNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author jicg on 2021/2/14
 */

public class ScanUtils {
    @SafeVarargs
    public static Set<BeanDefinition> getScanBean(String basePackage, @NotNull Class<? extends Annotation>... annotationTypes) {
        ClassPathScanningCandidateComponentProvider scanningCandidateComponentProvider =
                new ClassPathScanningCandidateComponentProvider(false);
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            scanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        }
        return scanningCandidateComponentProvider.findCandidateComponents(basePackage);
    }
}

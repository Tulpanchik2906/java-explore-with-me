package ru.practicum.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Constraint(validatedBy = NotBlankWithNullValidator.class)
public @interface NotBlankWithNull {
    String message() default "Поле не может состояить только из пробелов.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


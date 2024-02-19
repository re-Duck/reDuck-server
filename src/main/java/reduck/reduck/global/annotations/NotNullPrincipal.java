package reduck.reduck.global.annotations;

import java.lang.annotation.*;
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullPrincipal {
}
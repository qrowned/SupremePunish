package dev.qrowned.punish.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    Class<?> boundToClass();

    String name();

    String permission() default "";

    int minArgs() default 1;

    int maxArgs() default 1;

    boolean ignoreMaxArgs() default false;

    int sortingId() default 1;

    String[] aliases() default {};

}

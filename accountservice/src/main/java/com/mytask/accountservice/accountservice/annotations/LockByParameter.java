package com.mytask.accountservice.accountservice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for locking by parameter specified by annotation {@link LockKey}
 * <p>Should be used only in pair with {@link LockKey}
 * <p>usage:
 * {@code @LockByParameter
 * public void method(@LockKey Object key){...}}
 *
 * @see LockKey
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LockByParameter {
}

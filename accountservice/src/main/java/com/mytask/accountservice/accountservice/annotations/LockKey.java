package com.mytask.accountservice.accountservice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specifying lock key for {@link LockByParameter} annotation
 * <p>usage:
 * {@code @LockByParameter
 * public void method(@LockKey Object key){...}}
 *
 * @see LockByParameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface LockKey {
}

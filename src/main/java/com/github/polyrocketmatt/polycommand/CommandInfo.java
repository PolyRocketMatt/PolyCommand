package com.github.polyrocketmatt.polycommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide basic information for an {@link AbstractCommand}.
 *
 * Created by PolyRocketMatt on 06/04/2021.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    /**
     * The name of the command.
     *
     * @return the name
     */
    String name();

    /**
     * The arguments the command expects.
     *
     * @return the arguments
     */
    String arguments();

    /**
     * The permission required to execute the command.
     *
     * @return the permission
     */
    String permission();

    /**
     * The description for the command.
     *
     * @return the description
     */
    String description();

}

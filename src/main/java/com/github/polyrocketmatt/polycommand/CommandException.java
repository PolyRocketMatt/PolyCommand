package com.github.polyrocketmatt.polycommand;

/**
 * Exception used by the {@link CommandHandler}.
 * <p>
 * Created by PolyRocketMatt on 06/04/2021.
 */

public class CommandException extends RuntimeException {

    /**
     * @param cause why this exception was thrown
     */
    public CommandException(String cause) {
        super(cause);
    }

}

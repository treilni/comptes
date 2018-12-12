package org.treil.comptes.save;

import org.jetbrains.annotations.NotNull;

/**
 * @author Nicolas
 * @since 10/12/2018.
 */
public class DataSaveException extends Exception {
    public enum Type {
        FORMER_RENAME, IO_EXCEPTION, NO_FILE_SPECIFIED
    }

    @NotNull
    private Type type;

    public DataSaveException(@NotNull Type type, String message) {
        super(message);
        this.type = type;
    }

    public DataSaveException(@NotNull Type type, Throwable cause) {
        super(cause);
        this.type = type;
    }

    @NotNull
    public Type getType() {
        return type;
    }
}

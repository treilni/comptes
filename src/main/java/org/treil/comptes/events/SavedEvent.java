package org.treil.comptes.events;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Nicolas
 * @since 11/12/2018.
 */
public class SavedEvent {
    @NotNull
    private final File file;

    public SavedEvent(@NotNull File f) {
        this.file = f;
    }

    @NotNull
    public File getFile() {
        return file;
    }
}

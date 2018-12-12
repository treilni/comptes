package org.treil.comptes.conf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author Nicolas
 * @since 12/12/2018.
 */
public class UserPreferences {
    private static final Logger logger = LoggerFactory.getLogger(UserPreferences.class);

    private enum Field {
        LastSavedFile
    }

    @NotNull
    private final Preferences preferences;

    @Nullable
    private File lastSavedFile = null;

    public UserPreferences() {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        String name = preferences.get(Field.LastSavedFile.name(), null);
        if (name != null) {
            lastSavedFile = new File(name);
            if (!lastSavedFile.exists()) {
                logger.warn(String.format("File %s does not exist", name));
                lastSavedFile = null;
            }
        }
    }

    @Nullable
    public File getLastSavedFile() {
        return lastSavedFile;
    }

    @Nullable
    public void setLastSavedFile(@NotNull File file) {
        lastSavedFile = file;
        preferences.put(Field.LastSavedFile.name(), file.getAbsolutePath());
    }

    @Nullable
    public File getLastSaveDir() {
        return lastSavedFile != null ? lastSavedFile.getParentFile() : null;
    }
}

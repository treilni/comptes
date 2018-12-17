package org.treil.comptes.save;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.Main;
import org.treil.comptes.events.SavedEvent;
import org.treil.comptes.finance.Account;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Nicolas
 * @since 05/12/2018.
 */
public class DataSaver {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final String SUFFIX = "ncpt";
    public final EventBus eventBus;

    @Nullable
    public File currentSave = null;

    public DataSaver(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void saveAgain(Account account) throws JsonProcessingException, DataSaveException {
        if (currentSave == null) {
            throw new DataSaveException(DataSaveException.Type.NO_FILE_SPECIFIED, "No file specified");
        }
    }

    public void save(Account account, File f) throws JsonProcessingException, DataSaveException {
        String dottedSuffix = "." + SUFFIX;
        if (!f.getName().endsWith(dottedSuffix)) {
            f = new File(f.getParent(), f.getName() + "." + SUFFIX);
        }
        logger.warn(String.format("Saving to %s", f.getAbsolutePath()));
        SaveBundle savedAccounts = new SaveBundle(account);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String s = objectMapper.writeValueAsString(savedAccounts);
        if (f.exists()) {
            File directory = f.getParentFile();
            File newFile;
            int i = 1;
            do {
                String newName = f.getName() + "." + i++;
                newFile = new File(directory, newName);
            } while (newFile.exists());
            boolean success = f.renameTo(newFile);
            if (!success) {
                String msg = String.format("Could not rename %s to %s", f.getAbsolutePath(), newFile.getAbsolutePath());
                logger.error(msg);
                throw new DataSaveException(DataSaveException.Type.FORMER_RENAME, msg);
            } else {
                String msg = String.format("Former file %s renamed to %s", f.getAbsolutePath(), newFile.getAbsolutePath());
                logger.warn(msg);
            }
        }
        try {
            FileWriter w = new FileWriter(f);
            w.write(s);
            w.close();
            setLastSaved(f);

        } catch (IOException e) {
            throw new DataSaveException(DataSaveException.Type.IO_EXCEPTION, e);
        }
    }

    @NotNull
    public Account load(@NotNull File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SaveBundle saveBundle = mapper.readValue(file, SaveBundle.class);
        List<Account> accounts = saveBundle.getAccounts();
        return accounts.size() > 0 ? accounts.get(0) : new Account(0, Collections.emptyList());
    }

    private void setLastSaved(@NotNull File f) {
        currentSave = f;
        eventBus.post(new SavedEvent(f));
    }

    public boolean hasCurrentSave() {
        return currentSave != null;
    }

    public void manageUnsaved() {

    }
}

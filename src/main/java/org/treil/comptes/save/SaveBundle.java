package org.treil.comptes.save;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.treil.comptes.finance.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nicolas
 * @since 05/12/2018.
 */
class SaveBundle {
    @NotNull
    private List<Account> accounts = new ArrayList<>();

    @JsonCreator
    public SaveBundle(@NotNull @JsonProperty("accounts") List<Account> accounts) {
        this.accounts.addAll(accounts);
    }

    public SaveBundle(Account account) {
        this(Collections.singletonList(account));
    }

    @Deprecated
    public SaveBundle() {
        // for serialization only
    }

    @NotNull
    public List<Account> getAccounts() {
        return accounts;
    }
}

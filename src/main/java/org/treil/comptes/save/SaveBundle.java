package org.treil.comptes.save;

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
    private final List<Account> accounts = new ArrayList<>();

    public SaveBundle(@NotNull List<Account> accounts) {
        this.accounts.addAll(accounts);
    }

    public SaveBundle(Account account) {
        this(Collections.singletonList(account));
    }
}

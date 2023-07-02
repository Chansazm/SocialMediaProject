package DAO;
import java.util.ArrayList;
import java.util.List;

import Model.Account;

public class AccountDAO {
    private List<Account> accounts;
    private int nextAccountId;

    public AccountDAO() {
        accounts = new ArrayList<>();
        nextAccountId = 1; // Start account_id from 1
    }

    public void save(Account account) {
        account.setAccount_id(nextAccountId++);
        accounts.add(account);
    }

    public boolean existsByUsername(String username) {
        return accounts.stream()
                .anyMatch(account -> account.getUsername().equals(username));
    }

    public Account findByUsername(String username) {
        return accounts.stream()
                .filter(account -> account.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }
}

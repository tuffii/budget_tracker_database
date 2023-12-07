package bd.TableModels;

import java.time.LocalDateTime;

/**
 * Класс описывающий структуру таблицы Balances
 */
public class BalancesTableModel {
    private int id;
    private LocalDateTime date;
    private int debit;
    private int credit;
    private int amount;

    public BalancesTableModel(int id, LocalDateTime date, int debit, int credit, int amount) {
        this.id = id;
        this.date = date;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public int getDebit() {
        return debit;
    }
    public void setDebit(int debit) {
        this.debit = debit;
    }
    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

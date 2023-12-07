package bd.TableModels;

import java.time.LocalDateTime;

public class OperationsTableModel {

    private int id;
    int article_id;
    private int debit;
    private int credit;
    private LocalDateTime date;

    private int balance_id;

    public OperationsTableModel(int id, int article_id, int debit, int credit, LocalDateTime date, int balance_id) {
        this.id = id;
        this.article_id = article_id;
        this.date = date;
        this.debit = debit;
        this.credit = credit;
        this.balance_id = balance_id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getArticle_id() {
        return article_id;
    }
    public void setArticle_id(int article_id) {
        this.article_id = article_id;
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
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public int getBalance_id() {
        return balance_id;
    }
    public void setBalance_id(int balance_id) {
        this.balance_id = balance_id;
    }
}



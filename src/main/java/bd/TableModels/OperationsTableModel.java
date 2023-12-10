package bd.TableModels;

import java.time.LocalDateTime;

public class OperationsTableModel extends BaseTableModel {

    private int id;
    private int articleId;
    private int debit;
    private int credit;
    private LocalDateTime date;
    private int balance_id;

    public OperationsTableModel(int id, int articleId, int debit, int credit, LocalDateTime date, int balance_id) {
        this.id = id;
        this.articleId = articleId;
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
        return articleId;
    }
    public void setArticle_id(int article_id) {
        this.articleId = article_id;
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

    @Override
    public String getCsvHeader() {
        return "ID,ArticleId,Debit,Credit,Date,BalanceId";
    }

    @Override
    public String toCsvString() {
        return getId() + "," + getArticle_id() + "," + getDebit() + "," + getCredit() + "," + getDate() + "," + getBalance_id();
    }
}



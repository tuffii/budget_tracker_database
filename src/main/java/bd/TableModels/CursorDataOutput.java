package bd.TableModels;

public class CursorDataOutput {
    private int articleId;
    private double financialPercentage;

    public CursorDataOutput(int articleId, double financialPercentage) {
        this.articleId = articleId;
        this.financialPercentage = financialPercentage;
    }

    public int getArticleId() {
        return articleId;
    }

    public double getFinancialPercentage() {
        return financialPercentage;
    }

    @Override
    public String toString() {
        return "Статья ID " + articleId + ": Процент финансов = " + financialPercentage;
    }
}

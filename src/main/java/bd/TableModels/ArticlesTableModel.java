package bd.TableModels;

import java.time.LocalDateTime;

public class ArticlesTableModel extends BaseTableModel {
    private int id;
    private String name;

    public ArticlesTableModel(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCsvHeader() {
        return "ID,Name";
    }

    @Override
    public String toCsvString() {
        return getId() + "," + getName();
    }
}

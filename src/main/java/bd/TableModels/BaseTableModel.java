package bd.TableModels;

import java.time.LocalDateTime;

public abstract class BaseTableModel {
    public abstract String getCsvHeader();

    public abstract String toCsvString();
}


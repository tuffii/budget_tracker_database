package bd.dataAccessor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserRole {

    private static boolean isAdmin;

    public static final Map<String, Boolean> userRoleAccess = new HashMap<String, Boolean>() {
        {
            put("read", true);
            put("write", false);
            put("delete", false);
            put("getStatistic", true);
            put("editPlugins", false);
            put("execute", true);
        }
    };

    private static final List<String> admins = new LinkedList<>() {
        {
            add("admin");
        }
    };
    public static void setRole(String name) {
        for (var current : admins) {
            if (current.equals(name)) {
                isAdmin = true;
                break;
            }
        }
    }

    public static boolean getIsAdmin() {
        return isAdmin;
    }
    public static void setIsAdmin(boolean isAdmin) {
        UserRole.isAdmin = isAdmin;
    }
}

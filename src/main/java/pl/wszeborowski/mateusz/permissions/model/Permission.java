package pl.wszeborowski.mateusz.permissions.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "permissions")
@NamedQuery(name = Permission.Queries.CHECK_PERMISSION,
        query = "SELECT permission FROM Permission permission " +
                "WHERE permission.roleName = :roleName " +
                "AND permission.operationName = :operationName")
public class Permission {

    public static class Queries {
        public static final String CHECK_PERMISSION = "Permission.checkPermission";
    }

    public static class PermissionType {
        public static final String GRANTED = "GRANTED";

        public static final String DENIED = "DENIED";

        public static final String IF_OWNER = "IF_OWNER";
    }

    @GeneratedValue
    @Id
    private Integer Id;

    private String roleName;

    private String operationName;

    private String permissionType;

    public Permission(String roleName, String operationName, String permissionType) {
        this.roleName = roleName;
        this.operationName = operationName;
        this.permissionType = permissionType;
    }
}

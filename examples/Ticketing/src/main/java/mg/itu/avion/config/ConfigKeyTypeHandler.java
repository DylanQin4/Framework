package mg.itu.avion.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ConfigKey.class)
public class ConfigKeyTypeHandler extends BaseTypeHandler<ConfigKey> {
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ConfigKey parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name().toLowerCase());
    }

    @Override
    public ConfigKey getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : toConfigKey(value);
    }

    @Override
    public ConfigKey getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : toConfigKey(value);
    }

    @Override
    public ConfigKey getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : toConfigKey(value);
    }
    
    private ConfigKey toConfigKey(String value) {
        try {
            return ConfigKey.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No enum constant " + ConfigKey.class.getCanonicalName() + "." + value, e);
        }
    }
}
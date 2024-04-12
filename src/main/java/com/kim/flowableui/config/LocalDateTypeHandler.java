package com.kim.flowableui.config;

/**
 * @author JinXin
 * @date 2024/4/7 20:36
 * @description joda time type handler
 * https://github.com/LukeL99/joda-time-mybatis/blob/master/src/main/java/org/joda/time/mybatis/handlers/LocalDateTypeHandler.java
 */
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import java.sql.*;

@MappedTypes(LocalDate.class)
public class LocalDateTypeHandler implements TypeHandler {

    /* (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {

        LocalDate date = (LocalDate) parameter;
        if (date != null) {
            ps.setDate(i, new Date(date.toDateTimeAtStartOfDay().toDate().getTime()));
        } else {
            ps.setDate(i, null);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
     */
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        if (date != null) {
            return new LocalDate(date.getTime(), DateTimeZone.UTC);
        } else {
            return null;
        }
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        if (date != null) {
            return new LocalDate(date.getTime(), DateTimeZone.UTC);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
     */
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        if (date != null) {
            return new LocalDate(date.getTime(), DateTimeZone.UTC);
        } else {
            return null;
        }
    }

}

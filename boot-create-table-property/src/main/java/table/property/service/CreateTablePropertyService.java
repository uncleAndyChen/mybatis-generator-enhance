package table.property.service;

import table.property.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreateTablePropertyService {
    private static Statement statement;

    public static StringBuilder getTableProperties(DatabaseConfig databaseConfig) throws Exception {
        String getPrimaryKeySqlFormat = "select column_name from information_schema.key_column_usage where table_schema='" + databaseConfig.getSchemaName() + "' and table_name='%s';";
        String getTinyintKeySqlFormat = "show columns from " + databaseConfig.getSchemaName() + ".%s";
        String getPrimaryKeySql;
        String getTinyintKeySql;
        String primaryKey;
        String tableNameForMapper;
        List<String> tableNameList = new ArrayList();
        StringBuilder stringBuilderTableProperty = new StringBuilder();
        ResultSet resultSetTemp;

        // 请修改成你自己的数据库连接配置
        initConnection(databaseConfig);
        ResultSet resultSet = getMetaDataTables();

        while (resultSet.next()) {
            tableNameList.add(resultSet.getString("Tables_in_" + databaseConfig.getSchemaName()));
        }

        for (String tableName : tableNameList) {
            tableNameForMapper = tableName;
            getPrimaryKeySql = String.format(getPrimaryKeySqlFormat, tableName);
            resultSetTemp = statement.executeQuery(getPrimaryKeySql);

            resultSetTemp.next(); //只有一条记录，因为只定义了一个主键，如果有多个主键（联合主键），请根据情况调整此处代码。

            // 没定义主键，则没有记录
            if (resultSetTemp.getRow() == 0) {
                String errInfo = String.format("\n--------\n%s.%s，请先定义主键再生成表属性。\n--------\n", databaseConfig.getSchemaName(), tableName);
                System.out.println(errInfo);
                stringBuilderTableProperty.append(errInfo);
                return stringBuilderTableProperty;
            }

            primaryKey = resultSetTemp.getString("column_name");
            resultSetTemp.close();

            stringBuilderTableProperty.append("<table tableName=\"");
            stringBuilderTableProperty.append(tableName);
            stringBuilderTableProperty.append("\"");
            stringBuilderTableProperty.append(" domainObjectName=\"");

            // 如果不需要去掉表名前缀
            //  1. 删除下面的 if 语句块即可。
            //  2. 或者将 tableNamePrefixCount 设置为：0
            if (databaseConfig.getTableNamePrefixCount() > 0
                    && !databaseConfig.getKeepPrefixTableList().contains(tableName)
                    && (databaseConfig.getKeepPrefix().length() == 0 || !tableName.startsWith(databaseConfig.getKeepPrefix()))) {
                tableNameForMapper = tableName.substring(databaseConfig.getTableNamePrefixCount());
            }

            stringBuilderTableProperty.append(toCamelNameButFirstWord(tableNameForMapper));

            if (databaseConfig.isFlagUseActualColumnNames()) {
                stringBuilderTableProperty.append("\"><property name=\"useActualColumnNames\" value=\"true\"/>");
            } else {
                //sb.append("\"><property name=\"useActualColumnNames\" value=\"false\"/>");
                stringBuilderTableProperty.append("\">");
            }

            stringBuilderTableProperty.append("<generatedKey identity=\"true\" type=\"post\" column=\"" + primaryKey + "\" sqlStatement=\"Mysql\"/>");

            getTinyintKeySql = String.format(getTinyintKeySqlFormat, tableName) + " where type like 'tinyint%';";
            resultSetTemp = statement.executeQuery(getTinyintKeySql);

            // 将 tinyint 替换成 Integer，否则会生成 bool 类型的字段，会导致程序处理复杂化，建议根据自己的业务权衡
            while (resultSetTemp.next()) {
                stringBuilderTableProperty.append("<columnOverride column=\"" + resultSetTemp.getString("Field") + "\" javaType=\"java.lang.Integer\" jdbcType=\"INTEGER\" />");
            }

            resultSetTemp.close();

            stringBuilderTableProperty.append("</table>\r\n");
        }

        System.out.println(stringBuilderTableProperty);

        return stringBuilderTableProperty;
    }

    private static void initConnection(DatabaseConfig databaseConfig) throws Exception {
        Class.forName(databaseConfig.getDriverClassName());//指定连接类型
        //获取连接
        Connection connection = DriverManager.getConnection(
                databaseConfig.getDatabaseUrl(),
                databaseConfig.getUsername(),
                databaseConfig.getPassword());
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    private static ResultSet getMetaDataTables() throws Exception {
        String sql = "show tables;";
        return statement.executeQuery(sql);
    }

    private static String toCamelNameButFirstWord(String tableName) {
        StringBuilder result = new StringBuilder();

        // 快速检查
        if (tableName == null || tableName.isEmpty()) {
            // 没必要转换
            return "";
        }

        if (!tableName.contains("_")) {
            result.append(tableName.substring(0, 1).toUpperCase());
            result.append(tableName.substring(1));
            return result.toString();
        }

        // 用下划线将原始字符串分割
        String[] words = tableName.split("_");

        for (String word : words) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (word.isEmpty()) {
                continue;
            }

            result.append(word.substring(0, 1).toUpperCase());
            result.append(word.substring(1).toLowerCase());
        }

        return result.toString();
    }
}
package org.mybatis.generator.table.property;

import table.property.service.CreateTablePropertyService;
import table.property.config.DatabaseConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertFalse;

public class WithApplicationContextTest extends BasicUtClass {
    @Autowired
    private DatabaseConfig databaseConfig;

    @Test
    public void getTablePropertiesTest() {
        try {
            StringBuilder sb = CreateTablePropertyService.getTableProperties(databaseConfig);
            // 每张表会有一个换行符
            assertFalse(sb.isEmpty());
            //assertEquals(S
            // tringHelper.appearNumber(sb.toString(), "\r\n"),5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

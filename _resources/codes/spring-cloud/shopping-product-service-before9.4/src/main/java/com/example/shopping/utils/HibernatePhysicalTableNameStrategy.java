package com.example.shopping.utils;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class HibernatePhysicalTableNameStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment context) {
        if (logicalName == null || !StringUtils.hasText(logicalName.getText())) {
            return logicalName;
        }
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        String newName = logicalName.getText()
                // 在小写与大写连写的部分添加下划线
                .replaceAll(regex, replacement)
                // 全部转换成小写
                .toLowerCase(Locale.ROOT);
        return Identifier.toIdentifier(newName);
    }
}

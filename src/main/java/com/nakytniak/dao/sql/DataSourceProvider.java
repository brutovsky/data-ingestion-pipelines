package com.nakytniak.dao.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import org.apache.beam.sdk.options.ValueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Serial;
import java.io.Serializable;

import static com.nakytniak.utils.CommonUtil.getValue;

@Builder
public class DataSourceProvider implements Serializable {
    @Serial private static final long serialVersionUID = 7271547488871616837L;

    private static final Logger log = LoggerFactory.getLogger(DataSourceProvider.class);

    private final ValueProvider<String> dbConnection;
    private final ValueProvider<String> dbName;
    private final ValueProvider<String> username;
    private final ValueProvider<String> password;
    private final DataSourceVendor dataSourceVendor;

    private transient volatile DataSource dataSource;

    public DataSourceVendor getDataSourceVendor() {
        return dataSourceVendor;
    }

    public DataSource createDataSource(final boolean isSecret) {
        DataSource localRef = dataSource;
        if (localRef == null) {
            synchronized (this) {
                log.info("Trying to get DataSource for the vendor: {}", dataSourceVendor);
                localRef = dataSource;
                if (localRef == null) {
                    log.info("Created DataSource for the vendor: {}", dataSourceVendor);
                    dataSource = localRef = new HikariDataSource(this.createHikariConfig(isSecret));
                }
            }
        }
        return localRef;
    }

    private HikariConfig createHikariConfig(final boolean isSecret) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                dataSourceVendor.resolveConnectionUrl(getValue(dbName, isSecret), getValue(dbConnection, isSecret)));
        config.setUsername(getValue(username, isSecret));
        config.setPassword(getValue(password, isSecret));
        if (dataSourceVendor.getDriverClassName() != null) {
            config.setDriverClassName(dataSourceVendor.getDriverClassName());
        }
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        return config;
    }

}

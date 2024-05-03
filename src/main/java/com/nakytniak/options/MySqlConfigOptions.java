package com.nakytniak.options;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.ValueProvider;

public interface MySqlConfigOptions extends CommonOptions {
    @Description("JDBC MySql database connection string in format jdbc:mysql://HOST;database=DATABASE_NAME")
    ValueProvider<String> getMySqlConnectionUrl();

    void setMySqlConnectionUrl(ValueProvider<String> value);

    @Description("MySql Database username")
    @Default.String("")
    ValueProvider<String> getMySqlUsername();

    void setMySqlUsername(ValueProvider<String> value);

    @Description("MySql Database user password")
    @Default.String("")
    ValueProvider<String> getMySqlPassword();

    void setMySqlPassword(ValueProvider<String> value);
}

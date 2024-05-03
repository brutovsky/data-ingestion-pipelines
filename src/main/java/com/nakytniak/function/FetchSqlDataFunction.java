package com.nakytniak.function;

import com.nakytniak.dao.sql.DataSourceProvider;
import com.nakytniak.helper.SerializableSupplier;
import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.mapper.SqlEntityRowMapper;
import com.nakytniak.model.mapping.Mapping;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FetchSqlDataFunction<T extends BaseEntityModel> extends DoFn<Mapping, T> {

    private static final Logger log = LoggerFactory.getLogger(FetchSqlDataFunction.class);

    private static final long serialVersionUID = -4860178909925539683L;

    private final DataSourceProvider sqlDataSourceProvider;
    private final SerializableSupplier<SqlEntityRowMapper<? extends T>> rowMapperSupplier;

    public FetchSqlDataFunction(final DataSourceProvider sqlDataSourceProvider,
            final SerializableSupplier<SqlEntityRowMapper<? extends T>> rowMapperSupplier) {
        this.sqlDataSourceProvider = sqlDataSourceProvider;
        this.rowMapperSupplier = rowMapperSupplier;
    }

    @ProcessElement
    public void fetchData(@Element final Mapping mapping, final OutputReceiver<T> outputReceiver) {
        try (
                final Connection connection = sqlDataSourceProvider.createDataSource().getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(mapping.getQuery())
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            log.info("Successfully fetched data from Sql DB [{}] with query=[{}]",
                    mapping.getSourceVendor(), mapping.getQuery());
            final SqlEntityRowMapper<? extends T> mapper = rowMapperSupplier.get();
            mapper.setMapping(mapping);
            while (resultSet.next()) {
                outputReceiver.output(mapper.mapRow(resultSet));
            }
        } catch (Exception e) {
            log.error("Unable to fetch data from database", e);
            throw new RuntimeException("Unable to fetch data from database", e);
        }
    }
}

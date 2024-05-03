package com.nakytniak.utils;

import com.nakytniak.helper.SerializableSupplier;
import com.nakytniak.mapper.CsvHeaderMapper;
import com.nakytniak.mapper.CsvRowMapper;
import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.mapper.SqlEntityRowMapper;
import com.nakytniak.model.ModelContainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.sdk.options.ValueProvider;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MySqlModelUtil {

    public static SerializableSupplier<SqlEntityRowMapper<? extends BaseEntityModel>>
    createSqlEntityRowMapperSupplier(
            final ValueProvider<String> modelName, final ModelContainer modelContainer) {
        return () -> {
            final BaseEntityModel baseModel = modelContainer.getBaseModel(modelName.get());
            return new SqlEntityRowMapper<>(baseModel.getClass());
        };
    }

    public static SerializableSupplier<CsvRowMapper<? extends BaseEntityModel>>
    createCsvEntityRowMapperSupplier(
            final ValueProvider<String> modelName, final ModelContainer modelContainer) {
        return () -> {
            final BaseEntityModel baseModel = modelContainer.getBaseModel(modelName.get());
            return new CsvRowMapper<>(baseModel.getClass());
        };
    }

    public static SerializableSupplier<CsvHeaderMapper<? extends BaseEntityModel>>
    createCsvHeaderSupplier(
            final ValueProvider<String> modelName, final ModelContainer modelContainer) {
        return () -> {
            final BaseEntityModel baseModel = modelContainer.getBaseModel(modelName.get());
            return new CsvHeaderMapper(baseModel.getClass());
        };
    }

}
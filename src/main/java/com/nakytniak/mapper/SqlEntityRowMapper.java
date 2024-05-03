package com.nakytniak.mapper;

import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.model.mapping.Mapping;
import com.nakytniak.model.mapping.SourceSqlField;
import com.nakytniak.utils.CaseUtils;
import com.nakytniak.utils.EntityUtils;
import lombok.NonNull;
import org.apache.beam.sdk.io.jdbc.JdbcIO;

import java.io.Serial;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

import static com.nakytniak.utils.EntityUtils.getNonStaticFields;

public final class SqlEntityRowMapper<T extends BaseEntityModel> implements JdbcIO.RowMapper<T> {

    @Serial private static final long serialVersionUID = 5185358344259596744L;

    private final Class<T> entityClass;
    private Mapping mapping;

    public SqlEntityRowMapper(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T mapRow(@NonNull final ResultSet resultSet) throws Exception {
        final T model = entityClass.getDeclaredConstructor().newInstance();
        final List<Field> entityFields = getNonStaticFields(entityClass);
        for (Field field : entityFields) {
            final SourceSqlField sourceSqlField = mapping.getTableMappings()
                    .get(CaseUtils.toSnakeCase(field.getName()));
            final Class<?> fieldType = field.getType();
            EntityUtils.callSetter(model, field.getName(),
                    resultSet.getObject(sourceSqlField.getName(), fieldType), fieldType);
        }
        return model;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }
}

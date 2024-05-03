package com.nakytniak.function;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.nakytniak.model.BaseEntityModel;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

import static com.nakytniak.utils.EntityUtils.JAVA_CLASS_TO_FIRESTORE_FORMAT_MAP;
import static com.nakytniak.utils.EntityUtils.getNonStaticFields;

public class WriteToFirestoreFn extends DoFn<KV<String, Iterable<BaseEntityModel>>, Void> implements Serializable {

    @Serial private static final long serialVersionUID = 331811765771246281L;
    private static final Logger log = LoggerFactory.getLogger(WriteToFirestoreFn.class);

    private final ValueProvider<String> projectId;
    private final ValueProvider<String> databaseId;
    private final ValueProvider<String> collectionName;
    private transient Firestore db;

    public WriteToFirestoreFn(final ValueProvider<String> projectId, final ValueProvider<String> databaseId,
            final ValueProvider<String> collectionName) {
        this.projectId = projectId;
        this.databaseId = databaseId;
        this.collectionName = collectionName;
    }

    @Setup
    public void setup() {
        // Initialize Firestore
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId(this.projectId.get())
                .setDatabaseId(this.databaseId.get())
                .build();
        db = firestoreOptions.getService();
    }

    @ProcessElement
    public void processElement(@Element final KV<String, Iterable<BaseEntityModel>> input,
            final OutputReceiver<Void> outputReceiver) throws ExecutionException, InterruptedException {
        WriteBatch batch = db.batch();

        final List<BaseEntityModel> modelList =
                StreamSupport.stream(input.getValue().spliterator(), false).toList();

        final String firestoreCollectionName = collectionName.get();

        for (BaseEntityModel entity : modelList) {
            // Create Firestore document data
            Map<String, Object> firestoreData = convertToMap(entity);
            // Add write operation to the batch
            DocumentReference docRef = db.collection(firestoreCollectionName).document();
            batch.set(docRef, firestoreData);
        }

        final DocumentReference metadataRef = db.collection("metadata").document(firestoreCollectionName);
        batch.update(metadataRef, "count", FieldValue.increment(modelList.size()));

        ApiFuture<List<WriteResult>> future = batch.commit();
        try {
            future.get();
            log.info("Batch committed with {} documents", modelList.size());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error committing batch to Firestore: ", e);
            throw e;
        }
    }

    public static Map<String, Object> convertToMap(final Object entity) {
        Map<String, Object> firestoreData = new HashMap<>();
        final List<Field> entityFields = getNonStaticFields(entity.getClass());
        entityFields.forEach(field -> {
            final String fieldName = field.getName();
            field.setAccessible(true);
            final DateTimeFormatter dateTimeFormatter = JAVA_CLASS_TO_FIRESTORE_FORMAT_MAP.get(field.getType());
            try {
                Object fieldValue = field.get(entity);
                if (dateTimeFormatter != null && fieldValue != null) {
                    fieldValue = dateTimeFormatter.format((TemporalAccessor) fieldValue);
                }
                firestoreData.put(fieldName, fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.warn("Cannot access value for field {}", fieldName);
            }
        });
        return firestoreData;
    }
}

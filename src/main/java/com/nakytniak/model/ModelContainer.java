package com.nakytniak.model;

import com.nakytniak.model.enitites.StudentsInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModelContainer implements Serializable {

    @Serial private static final long serialVersionUID = -1437593274781547335L;

    private final Map<String, BaseEntityModel> nameToModelClass;

    public ModelContainer() {
        nameToModelClass = new HashMap<>();
        nameToModelClass.put("STUDENTS_INFO", new StudentsInfo());
    }

    public BaseEntityModel getBaseModel(final String viewName) {
        return nameToModelClass.get(viewName);
    }
}

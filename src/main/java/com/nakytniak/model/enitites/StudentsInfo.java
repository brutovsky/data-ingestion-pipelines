package com.nakytniak.model.enitites;

import com.nakytniak.model.BaseEntityModel;
import com.nakytniak.model.SourceTableInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentsInfo extends BaseEntityModel {

    private Integer internalId;
    private String email;
    private String firstName;
    private String lastName;
    private String cityOfBirth;
    private Integer grade;

    @Override
    public SourceTableInfo getSourceTableInfo() {
        return null;
    }

}

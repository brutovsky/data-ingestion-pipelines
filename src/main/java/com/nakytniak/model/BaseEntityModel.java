package com.nakytniak.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public abstract class BaseEntityModel implements Serializable {

    public abstract SourceTableInfo getSourceTableInfo();
}
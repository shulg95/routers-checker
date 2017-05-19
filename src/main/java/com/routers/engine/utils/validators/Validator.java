package com.routers.engine.utils.validators;

public interface Validator<T> {
    boolean validate(T data);
}
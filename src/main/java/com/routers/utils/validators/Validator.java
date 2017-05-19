package com.routers.utils.validators;

public interface Validator<T> {
    boolean validate(T data);
}
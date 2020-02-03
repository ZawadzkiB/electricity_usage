package com.example.electricity.report.validator;

import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

@Component
public class RequestValidatorProvider {

  private Set<Validator> validators;

  public RequestValidatorProvider(Set<Validator> validators) {
    this.validators = validators;
  }

  @SuppressWarnings("unchecked")
  public Validator provideValidator(Object o) {
    Validator actualValidator = validators.stream().filter(validator -> findSuperClassParameterType(validator) == o.getClass())
            .findFirst().orElseThrow();
    return actualValidator.set(o);
  }

  private Type findSuperClassParameterType(Object instance) {
    return ((ParameterizedType) instance.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

}

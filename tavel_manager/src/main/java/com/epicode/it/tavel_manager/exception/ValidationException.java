package com.epicode.it.tavel_manager.exception;

import org.springframework.validation.BindingResult;

public class ValidationException extends RuntimeException {
  private final BindingResult bindingResult;

  public ValidationException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
  }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}

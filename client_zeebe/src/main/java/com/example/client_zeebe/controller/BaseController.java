package com.example.client_zeebe.controller;

import com.example.client_zeebe.common.ProcessResult;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected ResponseEntity<ProcessResult> wrapResponse(ProcessResult processResult) {
        if (processResult.isSucceed()) {
            return ResponseEntity.ok(processResult);
        }
        return ResponseEntity.badRequest().body(processResult);
    }

}

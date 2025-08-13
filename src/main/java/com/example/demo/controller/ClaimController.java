package com.example.demo.controller;

import com.example.demo.common.ProcessResult;
import com.example.demo.dto.ClaimDto;
import com.example.demo.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
public class ClaimController extends BaseController {


    @Autowired
    private ClaimService claimService;

    @PostMapping("/claim")
    public ResponseEntity<ProcessResult> claimOfPolicy(@RequestBody ClaimDto claimDto) {
        return wrapResponse(claimService.doClaim(claimDto));
    }

    @PostMapping(value = "/uploadDoc/{claimId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadDoc(@PathVariable UUID claimId, @RequestParam("file") MultipartFile file) {
        claimService.saveDoc(claimId,file);
    }

    @GetMapping("/process/{id}")
    public void getProcesses(@PathVariable String id) {
         claimService.getProcessByDefId(id.toString());
    }

}

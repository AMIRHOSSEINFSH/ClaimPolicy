package com.example.clientZeebe.service;

import com.example.clientZeebe.common.ProcessResult;
import com.example.clientZeebe.dto.ClaimDto;
import com.example.clientZeebe.entity.ClaimEntity;
import com.example.clientZeebe.entity.PolicyEntity;
import com.example.clientZeebe.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.clientZeebe.common.enums.Const.MESSAGE.Message_madarek_claim;
import static com.example.clientZeebe.common.enums.Const.PROCESS.Process_claim_policy;

@Service
public class ClaimService extends BaseService {

    @Autowired
    private ClaimRepository claimRepository;

    public ProcessResult doClaim(ClaimDto claimDto) {
        Map<String, Object> processVariables = new HashMap<>(getProcessPrefixMap(Process_claim_policy));
        processVariables.put("claimDto",claimDto);
        processVariables.put("error_list", List.of());
        ProcessResult processResult = process(Process_claim_policy.name(),
                processVariables,
                (variableMap)-> variableMap.getOrDefault("Result",null));
        return processResult;
    }

    public String convertByteArrayToDataUrl(byte[] imageBytes, String mimeType) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        var f = "data:" + mimeType + ";base64," + base64Image;
        System.out.println(f);
        return f;
    }

    public byte[] optimizeImage(byte[] originalBytes, int maxWidth, int maxHeight) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(originalBytes));

            // Calculate new dimensions maintaining aspect ratio
            int newWidth = Math.min(original.getWidth(), maxWidth);
            int newHeight = Math.min(original.getHeight(), maxHeight);

            BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpeg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return originalBytes; // Fallback to original
        }
    }


    public void saveDoc(UUID id, MultipartFile file) {
        claimRepository.findById(id).ifPresent(claim -> {
            try {
                claim.setImageData(file.getBytes());
                claimRepository.save(claim);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            zeebeClient.newPublishMessageCommand()
                    .messageName(Message_madarek_claim.name())
                    .correlationKey(id.toString())
                    .variables(Map.of("imageVariable",convertByteArrayToDataUrl(optimizeImage(file.getBytes(),300,300),"image/jpeg")))
                    .send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void getProcessByDefId(String defId) {
        zeebeClient.newProcessInstanceQuery().filter(item->{
            item.bpmnProcessId(Process_claim_policy.name());
            item.active(true);
        }).send().join()
                .items().forEach(System.out::println);

    }

    public ClaimEntity fillEntity(ClaimDto claimDto) {
        ClaimEntity claimEntity = new ClaimEntity();
        claimEntity.setAccidentDate(claimDto.getAccidentDate());
        claimEntity.setDamageType(claimDto.getDamageType());
        claimEntity.setPolicyEntity(new PolicyEntity(claimDto.getPolicyId()));
        claimEntity.setAccidentLocation(claimDto.getAccidentLocation());
        claimEntity.setDamageDescription(claimDto.getDamageDescription());
        return claimEntity;
    }

    public ClaimEntity save(ClaimEntity claim) {
        return claimRepository.save(claim);
    }

    public void getError() {

    }

}

package pro.ganyushkin.demo.demospringintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import pro.ganyushkin.demo.demospringintegration.domain.Plant;

import java.util.Objects;

@Slf4j
@Service
public class PlantService {

    public Plant savePlant(Plant request) {
        log.warn("save new plant " + request);
        return request;
    }

    public Plant processPlant(Plant request) {
        log.warn("start processing for " + request);
        return request;
    }

    public Plant markAsInProgress(Plant request) {
        log.warn("set status IN-PROGRESS " + request);
        return request;
    }

    public void markAsCompleted(Plant request) {
        log.warn("set status COMPLETED " + request);
    }

    public String getProcessingStatus(Message<String> arg) {
        var plantId = Objects.requireNonNull(arg.getHeaders().get("plantId")).toString();
        log.info("get processing status for " + plantId);
        return "IN-PROGRESS;plantId=" + plantId;
    }
}

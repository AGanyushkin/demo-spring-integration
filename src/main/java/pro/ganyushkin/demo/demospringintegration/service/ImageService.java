package pro.ganyushkin.demo.demospringintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.ganyushkin.demo.demospringintegration.domain.Plant;

@Slf4j
@Service
public class ImageService {

    public Plant processImage(Plant request) {
        for(var imageUrl : request.getPlantImages()) {
            log.warn("image has been processed: " + imageUrl);
        }
        return request;
    }
}

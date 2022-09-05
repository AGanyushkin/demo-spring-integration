package pro.ganyushkin.demo.demospringintegration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PlantRequest {
    private String plantId;
    private List<String> plantImages;
}

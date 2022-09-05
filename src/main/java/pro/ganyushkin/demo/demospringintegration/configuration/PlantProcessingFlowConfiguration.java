package pro.ganyushkin.demo.demospringintegration.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;
import pro.ganyushkin.demo.demospringintegration.domain.Plant;
import pro.ganyushkin.demo.demospringintegration.domain.PlantRequest;
import pro.ganyushkin.demo.demospringintegration.service.ImageService;
import pro.ganyushkin.demo.demospringintegration.service.PlantService;

@RequiredArgsConstructor
@Configuration
public class PlantProcessingFlowConfiguration {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private final PlantService plantService;
    private final ImageService imageService;

    @Bean
    public MessageChannel inputRequests() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow plantProcessingStatusFlow() {
        return IntegrationFlows
                .from(Http.inboundGateway("/plant/{plantId}")
                        .replyChannel("plantStatusReply")
                        .requestMapping(r -> r.methods(HttpMethod.GET))
                        .headerExpression("plantId", PARSER.parseExpression("#pathVariables.plantId")))
                .handle(this.plantService, "getProcessingStatus")
                .channel("plantStatusReply")
                .get();
    }

    @Bean
    public IntegrationFlow plantProcessingRequestFlow() {
        return IntegrationFlows
                .from(Http.inboundChannelAdapter("/plant")
                        .requestMapping(r -> r.methods(HttpMethod.POST))
                        .requestPayloadType(PlantRequest.class))
                .log()
                .filter((PlantRequest p) -> !p.getPlantId().isBlank())
                .channel(inputRequests())
                .get();
    }

    @Bean
    public IntegrationFlow plantProcessingFlow() {
        return IntegrationFlows
                .from(inputRequests())
                .<PlantRequest, Plant>transform(p -> Plant.builder()
                        .plantId(p.getPlantId())
                        .plantImages(p.getPlantImages())
                        .build())
                .handle(this.plantService, "savePlant")
                .handle(this.plantService, "markAsInProgress")
                .handle(this.imageService, "processImage")
                .handle(this.plantService, "processPlant")
                .handle(this.plantService, "markAsCompleted")
                .get();
    }
}

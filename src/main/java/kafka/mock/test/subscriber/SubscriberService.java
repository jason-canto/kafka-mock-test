package kafka.mock.test.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import kafka.mock.test.dto.ApiRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SubscriberService {

	@KafkaListener(topics = "${topic.input.destination}")
	public void consume(@Payload Message<ApiRequestDto> message) {
		log.info(message.getPayload().toString());
	}
}
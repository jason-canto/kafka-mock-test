package kafka.producer.kafkamocktest.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import kafka.producer.kafkamocktest.dto.PositionDto;

@Service
public class PublisherService {

	@Value("${spring.output.destination}")
	private String topic;

	@Autowired
	private KafkaTemplate<String, PositionDto> publisher;

	public void sendMessage(PositionDto message) {
		publisher.send(topic, message);
		publisher.flush();
	}
}
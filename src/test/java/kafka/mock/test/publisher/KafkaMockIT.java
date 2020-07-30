package kafka.mock.test.publisher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@EmbeddedKafka
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaMockIT {

	private static final String TOPIC = "test-topic";

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	BlockingQueue<ConsumerRecord<String, String>> messages;

	KafkaMessageListenerContainer<String, String> container;

	@BeforeAll
	void setUp() {
		Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("test-group", "false", embeddedKafkaBroker));
		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer());
		ContainerProperties containerProperties = new ContainerProperties(TOPIC);
		container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
		messages = new LinkedBlockingQueue<>();
		container.setupMessageListener((MessageListener<String, String>) messages::add);
		container.start();
		ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
	}

	@AfterAll
	void tearDown() {
		container.stop();
	}

	@Test
	public void kafkaSetup_withTopic_ensureSendMessageIsReceived() throws Exception {
		Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(),
				new StringSerializer()).createProducer();
		producer.send(new ProducerRecord<>(TOPIC, "message-id", "{\"event\":\"kafka test\"}"));
		producer.flush();
		ConsumerRecord<String, String> singleRecord = messages.poll(500, TimeUnit.MILLISECONDS);
		assertThat(singleRecord).isNotNull();
		assertThat(singleRecord.key()).isEqualTo("message-id");
		assertThat(singleRecord.value()).isEqualTo("{\"event\":\"kafka test\"}");
	}
}

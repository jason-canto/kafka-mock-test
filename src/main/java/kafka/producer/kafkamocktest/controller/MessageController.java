package kafka.producer.kafkamocktest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kafka.producer.kafkamocktest.dto.PositionDto;
import kafka.producer.kafkamocktest.publisher.PublisherService;

@RestController
public class MessageController {

	@Autowired
	private PublisherService service;

	@PostMapping("/api/messege")
	public ResponseEntity<PositionDto> addMessage(@RequestBody PositionDto message) {
		service.sendMessage(message);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
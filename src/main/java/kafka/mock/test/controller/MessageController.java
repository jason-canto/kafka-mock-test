package kafka.mock.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kafka.mock.test.dto.PositionDto;
import kafka.mock.test.publisher.PublisherService;

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
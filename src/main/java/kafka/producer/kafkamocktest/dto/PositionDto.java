package kafka.producer.kafkamocktest.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDto implements Serializable {

	private static final long serialVersionUID = 6978117561747358518L;

	@JsonProperty("id")
	private String plate;

	@JsonProperty("worder")
	private String workingOrder;

	@JsonProperty("containerno")
	private String container;

	@JsonProperty("gpslat")
	private Double latitude;

	@JsonProperty("gpslong")
	private Double longitude;

	@JsonProperty("gpsdate")
	private Date gpsDate;
}

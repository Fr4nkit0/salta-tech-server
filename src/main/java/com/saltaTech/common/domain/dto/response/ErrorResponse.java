package com.saltaTech.common.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
		@JsonProperty("http_code")
		int httCode,
		String url,
		@JsonProperty("http_method")
		String httpMethod,
		String message,
		@JsonProperty("backend_message")
		String backendMessage,
		@JsonProperty("times_tamp")
		@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		LocalDateTime timeTamp,
		List<String> details
)implements Serializable {
}

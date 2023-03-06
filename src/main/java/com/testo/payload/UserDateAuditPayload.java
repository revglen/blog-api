package com.testo.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserDateAuditPayload extends DateAuditPayload {
	private Long createdBy;
	private Long updatedBy;
}

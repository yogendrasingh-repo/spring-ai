package org.springframework.ai.chat.engine2;

public interface Augmentor {

	AugmentResponse augment(AugmentRequest augmentRequest);

}

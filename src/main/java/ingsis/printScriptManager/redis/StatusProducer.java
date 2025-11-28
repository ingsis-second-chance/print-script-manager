package ingsis.printScriptManager.redis;

import events.StatusPublishEvent;
import ingsis.printScriptManager.configuration.RedisStreamProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatusProducer extends RedisStreamProducer implements StatusProducerInterface {

  private static final Logger logger = LoggerFactory.getLogger(StatusProducer.class);

  @Autowired
  public StatusProducer(
      @Value("${stream.redis.stream.status.key}") String streamKey,
      ReactiveRedisTemplate<String, String> redis) {
    super(streamKey, redis);
  }

  @Override
  public void publishEvent(StatusPublishEvent event) {
    logger.info("Publishing event: {}", event.toString());
    emit(event).subscribe();
  }
}

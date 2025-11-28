package ingsis.printScriptManager.redis;

import events.StatusPublishEvent;

public interface StatusProducerInterface {
  void publishEvent(StatusPublishEvent event);
}

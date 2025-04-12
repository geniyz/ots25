package site.geniyz.ots.rim

import site.geniyz.ots.UObject

class Receiver(
    private val config: UObject
) {
    init {
        Kafka(config).consume { topic, key, content ->
            ReceiveCommand(content as String).execute()
        }
    }
}

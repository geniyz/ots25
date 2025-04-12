package site.geniyz.ots.rim

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer


interface KafkaCustomizable {
    val hosts:     List<String>
    val group:     String
    val inTopics:  List<String>
    val outTopics: List<String>

    fun createConsumer(): KafkaConsumer<String, String>
    fun createProducer(): KafkaProducer<String, String>
}

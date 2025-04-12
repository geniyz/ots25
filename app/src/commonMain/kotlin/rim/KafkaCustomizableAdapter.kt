package site.geniyz.ots.rim

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import site.geniyz.ots.UObject
import java.util.*

class KafkaCustomizableAdapter(
    private val o: UObject,
): KafkaCustomizable {
    private val c = o["kafka"] as Map<*, *>

    override val hosts: List<String>
        get() = c["hosts"]?.let{
            (it as? List<*> ?: ((it as String?) ?: "").split(",", ";")) as List<String>?
        } ?: emptyList()

    override val group: String
        get() = (c["group"] as String?) ?: ""

    override val inTopics: List<String>
        get() = c["inTopics"]?.let{
            (it as? List<*> ?: ((it as String?) ?: "").split(",", ";")) as List<String>?
        } ?: emptyList()

    override val outTopics: List<String>
        get() = c["outTopics"]?.let{
            (it as? List<*> ?: ((it as String?) ?: "").split(",", ";")) as List<String>?
        } ?: emptyList()

    override fun createConsumer() : KafkaConsumer<String, String> {
        val props = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
            put(ConsumerConfig.GROUP_ID_CONFIG, group)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        }
        return KafkaConsumer<String, String>(props)
    }

    override fun createProducer(): KafkaProducer<String, String> {
        val props = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        }
        return KafkaProducer<String, String>(props)
    }

    override fun toString(): String =
        """
            hosts:     ${hosts}
            inTopics:  ${inTopics}
            outTopics: ${outTopics}
            group:     ${group}
        """.trimIndent()

}

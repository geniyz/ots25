package site.geniyz.ots.rim

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import kotlin.collections.forEach
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.slf4j.LoggerFactory
import site.geniyz.ots.UObject

class Kafka(
    private val config: UObject,
    // private val log: Logger,
) : AutoCloseable {
    private val log = LoggerFactory.getLogger({}::class.qualifiedName?.substringBefore("$$"))

    private val process = atomic(true)

    private val kafkaSettings: KafkaCustomizable = KafkaCustomizableAdapter(config)
    private val consumer: Consumer<String, String> = kafkaSettings.createConsumer()
    private val producer: Producer<String, String> = kafkaSettings.createProducer()

    /**
     * Блокирующая функция старта получения сообщений из Кафки. Для неблокирующей версии см. [[consumeSusp]]
     */
    fun consume(
        topics: List<String> = kafkaSettings.outTopics,
        block: (String, String, Any)->Unit
    ): Unit = runBlocking {
        consumeSusp(
            topics = topics,
            block = block,
        )
    }

    /**
     * Неблокирующая функция старта получения и обработки сообщений из Кафки. Блокирующая версия - см. [[consume]]
     */
    suspend fun consumeSusp(
        topics: List<String> = kafkaSettings.outTopics,
        block: (String, String, Any)->Unit
    ) {
        process.value = true
        try {
            consumer.subscribe( topics )
            while (process.value) {
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                // if (!records.isEmpty) log.debug("получено ${records.count()} сообщений")

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        block( record.topic(), record.key(), record.value() )
                    } catch (t: Throwable) {
                        withContext(NonCancellable) {
                            throw t
                        }
                    }
                }
            }
        } catch (ex: WakeupException) {
        // игнор
        } catch (ex: RuntimeException) {
            // дабы не падать
            withContext(NonCancellable) { throw ex }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    fun produce(
        data: String,
        topics: List<String> = kafkaSettings.inTopics,
    ): Unit = runBlocking((Dispatchers.IO)) {
        produceCore(
            data = data,
            topics = topics,
        )
    }

    suspend fun produceSusp(
        data: String,
        topics: List<String> = kafkaSettings.inTopics,
    ) {
        withContext(Dispatchers.IO) {
            produceCore(
                data = data,
                topics = topics,
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun produceCore(
        data: String,
        topics: List<String> = kafkaSettings.inTopics,
    ) {
        topics.forEach {
            try {
                val resRecord = ProducerRecord(
                    it,
                    Uuid.Companion.random().toString(),
                    data
                )

                val f = try{
                    producer.send(resRecord)
                }catch (ex: Throwable){
                    println(ex)
                    throw ex
                }

                val rez = f.get()

            }catch (t: Throwable){
                log.error("Ошибка отправки данных в топики «${topics}» данные: «$data», err: {}", t)
            }
        }
    }

    /**
     * Корректное завершение
     */
    override fun close() {
        process.value = false
        consumer.close()
        producer.close()
    }

}

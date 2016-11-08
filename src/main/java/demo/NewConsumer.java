package demo;

import com.google.protobuf.InvalidProtocolBufferException;
import domain.UnionClickLogProto;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by mhw on 2016/11/8.
 */
public class NewConsumer {

    private static String ZOOKEEPER_LIST = "192.168.147.48:2181";
    private static String BROKER_LIST = "192.168.147.48:9092";

    private static String TOPIC = "stromtest1";

    private static Properties initProducer() {
        Properties props = new Properties();

        props.setProperty("bootstrap.servers",BROKER_LIST);
        props.setProperty("key.serializer" , StringSerializer.class.getName());
        props.setProperty("value.serializer" , ByteArraySerializer.class.getName());
        props.setProperty("acks" , "all");
        props.setProperty("batch.size" , "1");

        return props;
    }
    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        return new ConsumerConfig(props);
    }
    public static void main(String[] args) {
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(ZOOKEEPER_LIST,"test110801"));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(TOPIC);
        KafkaStream kafkaStream = streams.get(0);
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
            byte[] pb = messageAndMetadata.message();
            try {
                UnionClickLogProto.ClickLogInfo clickLogInfo = UnionClickLogProto.ClickLogInfo.parseFrom(pb);
                System.out.println("---------clickid;" + clickLogInfo.getClickId());
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
    }
}

package demo;

import domain.UnionClickLogProto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Created by mhw on 2016/11/8.
 */
public class NewProducer {

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

    public static void main(String[] args) {
        KafkaProducer<String,byte[]> producer = new KafkaProducer<String, byte[]>(initProducer());

        while( 1 == 1){
            UnionClickLogProto.ClickLogInfo.Builder clickLogInfoBuilder = UnionClickLogProto.ClickLogInfo.newBuilder();

            String clickId = System.currentTimeMillis()+"";

            clickLogInfoBuilder.setClickId(clickId);

            ProducerRecord<String,byte[]> record = new ProducerRecord<String, byte[]>(TOPIC,clickId,clickLogInfoBuilder.build().toByteArray());
            producer.send(record);
            System.out.println("----------send clicid : " + clickId + "--------");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

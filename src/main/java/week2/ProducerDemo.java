package week2;

import domain.UnionClickLogProto;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class ProducerDemo {

  static private final String TOPIC = "stormtest1";
  static private final String ZOOKEEPER = "192.168.147.48:2181";
  static private final String BROKER_LIST = "192.168.147.48:9092";
//  static private final int PARTITIONS = TopicAdmin.partitionNum(ZOOKEEPER, TOPIC);
  static private final int PARTITIONS = 3;


  public static void main(String[] args) throws Exception {
    Producer<String, byte[]> producer = initProducer();
    sendOne(producer, TOPIC);
  }

  private static Producer<String, byte[]> initProducer() {
    Properties props = new Properties();
    props.put("metadata.broker.list", BROKER_LIST);
    // props.put("serializer.class", "kafka.serializer.StringEncoder");
    //props.put("serializer.class", ByteArraySerializer.class.getName());
    props.put("partitioner.class", HashPartitioner.class.getName());
    // props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
//    props.put("compression.codec", "0");
    props.put("producer.type", "sync");
    props.put("batch.num.messages", "1");
    props.put("queue.buffer.max.ms", "10000000");
    props.put("queue.buffering.max.messages", "1000000");
    props.put("queue.enqueue.timeout.ms", "20000000");

    ProducerConfig config = new ProducerConfig(props);
    Producer<String, byte[]> producer = new Producer<String, byte[]>(config);
    return producer;
  }

  public static void sendOne(Producer<String, byte[]> producer, String topic) throws InterruptedException {
    while( 1 == 1){
      UnionClickLogProto.ClickLogInfo.Builder clickLogInfoBuilder = UnionClickLogProto.ClickLogInfo.newBuilder();

      String clickId = System.currentTimeMillis()+"";

      clickLogInfoBuilder.setClickId(clickId);


      KeyedMessage<String, byte[]> message1 = new KeyedMessage<String, byte[]>(topic, clickId, clickLogInfoBuilder.build().toByteArray());
      producer.send(message1);
      System.out.println("----------send clicid : " + clickId + "--------");
      Thread.sleep(5000);
    }
  }

}

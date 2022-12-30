package i2f.spring.serialize.test;


import i2f.core.serialize.ISerializer;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import i2f.spring.serialize.jackson.JacksonJsonWithTypeSerializer;
import i2f.spring.serialize.jackson.JacksonXmlSerializer;
import i2f.spring.serialize.jackson.JacksonYamlSerializer;

public class TestJackson {
    public static void main(String[] args) {
        testSerializer(new JacksonJsonSerializer());
        testSerializer(new JacksonJsonWithTypeSerializer());
        testSerializer(new JacksonXmlSerializer());
        testSerializer(new JacksonYamlSerializer());
//        testSerializer(new JacksonProtobufSerializer());
//        testSerializer(new JacksonCborSerializer());
//        testSerializer(new JacksonCsvSerializer());
//        testSerializer(new JacksonSmileSerializer());
    }

    public static <T> void testSerializer(ISerializer<T> serializer) {
        System.out.println("==================================================");
        System.out.println("serializer=" + serializer.getClass().getSimpleName());
        System.out.println("==================================================");

        UserDto user = UserDto.makeRandom();
        T xml = serializer.serialize(user, true);
        System.out.println(xml);

        UserDto bean = serializer.deserialize(xml, UserDto.class);
        System.out.println(bean);
    }
}

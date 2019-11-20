package ea.sof.ms_elastic_worker.config;

import ea.sof.ms_elastic_worker.service.ElasticService;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    public String host;
    @Value("${elasticsearch.port}")
    public int port;

    @Value("${questionIndex}")
    public String questionIndex;

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticConfig.class);

    @Bean
    Client client() {
        TransportClient client = null;
        try{
            Settings settings = Settings.builder()
//                    .put("cluster.name", "elasticsearch")
//                    .put("client.transport.sniff", false)
                    .build();
            client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(new TransportAddress (InetAddress.getByName(host), port));

            //check if index exists then create index
            boolean exists = client.admin().indices()
                    .prepareExists(questionIndex)
                    .execute().actionGet().isExists();
            if(!exists){
                client.admin().indices().prepareCreate(questionIndex)
//                        .addMapping("_doc", "name", "type=text")
                        .get();

            }
            LOGGER.info(String.format("Connect to Elastic Engine host: %s, port: %s successfully!", host, port));
            return client;
        }
        catch (UnknownHostException e) {
            LOGGER.error(String.format("Connect to Elastic Engine host: %s, port: %s failed!", host, port));
            LOGGER.error(e.getMessage());
//            e.printStackTrace();
        }
        return client;
    }
}

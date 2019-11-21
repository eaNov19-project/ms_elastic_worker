package ea.sof.ms_elastic_worker.service;

import ea.sof.shared.queue_models.QuestionQueueModel;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class ElasticService {

    @Value("${questionIndex}")
    public String questionIndex;

    @Value("_doc")
    public String docType;

    @Autowired
    Client client;

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticService.class);

    public XContentBuilder buildJSONObj(QuestionQueueModel question) throws IOException {
        return jsonBuilder()
                .startObject()
                .field("id", question.getId())
                .field("title", question.getTitle())
                .field("body", question.getBody())
                .field("active", question.getActive())
                .endObject();
    }

    public boolean add(QuestionQueueModel question){
        try{
            IndexResponse response = client.prepareIndex(questionIndex, docType)
                    .setSource(buildJSONObj(question))
                    .get();
//            System.out.println("response id:"+response.getId());
            LOGGER.info("Add Question " + question.getId() + " successfully!");
            return true;
        }
        catch(Exception e){
            LOGGER.error("Add Question " + question.getId() + " failed!");
            LOGGER.error(e.getMessage());
            return false;
        }
    }
}


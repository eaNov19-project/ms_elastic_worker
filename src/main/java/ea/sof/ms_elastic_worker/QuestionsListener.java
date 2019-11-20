package ea.sof.ms_elastic_worker;

import com.google.gson.Gson;
import ea.sof.ms_elastic_worker.config.ElasticConfig;
import ea.sof.ms_elastic_worker.service.ElasticService;
import ea.sof.shared.queue_models.QuestionQueueModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class QuestionsListener {
    @Autowired
    ElasticService elasticService;

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionsListener.class);

	@KafkaListener(topics = "${topicNewQuestion}", groupId = "${subsNewQuestionElastic}")
	public void newQuestion(String message) {

		System.out.println("New message from topicNewQuestion topic: " + message);

		Gson gson = new Gson();
        QuestionQueueModel question =  gson.fromJson(message, QuestionQueueModel.class);

		System.out.println("Question object: " + question);

		//push to elasticsearch
        boolean res = elasticService.add(question);
        System.out.println("Add question " + question + " to Elastic Search: " + res);
        LOGGER.info(String.format("Add question %s: %s", question.getId(), res));
	}
}

package ea.sof.ms_elastic_worker;

import com.google.gson.Gson;
import ea.sof.shared.models.Question;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class QuestionsListener {
	@KafkaListener(topics = "${topicNewQuestion}", groupId = "${subsNewQuestionElastic}")
	public void newQuestion(String message) {

		System.out.println("New message from topicNewQuestion topic: " + message);

		Gson gson = new Gson();
		Question question =  gson.fromJson(message, Question.class);

		System.out.println("Question object: " + question);

	}
}

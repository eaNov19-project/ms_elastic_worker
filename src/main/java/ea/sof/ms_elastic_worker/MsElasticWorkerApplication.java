package ea.sof.ms_elastic_worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsElasticWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsElasticWorkerApplication.class, args);
	}

}

package com.cinema.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.cinema.project.config.property.CinemaApiProperty;

/**
* Essa é a classe de configuração do S3.
* @author JoaoLassi
*/
@Configuration
public class S3Config {

	@Autowired
	private CinemaApiProperty property;

	/**
	* Esse métodos configura as credenciais, região e a criação de um novo "bucket" caso não exista.
	*/
	@Bean
	public AmazonS3 amazonS3() {

		AWSCredentials credenciais = new BasicAWSCredentials(property.getS3().getAccessKeyId(),
				property.getS3().getSecretAccessKey());

		AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credenciais)).withRegion(Regions.US_EAST_1).build();

		if (!amazonS3.doesBucketExistV2(property.getS3().getBucket())) {
			amazonS3.createBucket(new CreateBucketRequest(property.getS3().getBucket()));

			BucketLifecycleConfiguration.Rule expiracao = new BucketLifecycleConfiguration.Rule()
					.withId("Expiração de imagens temporárias")
					.withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expirar", "true"))))
					.withExpirationInDays(1).withStatus(BucketLifecycleConfiguration.ENABLED);

			BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration().withRules(expiracao);

			amazonS3.setBucketLifecycleConfiguration(property.getS3().getBucket(), configuration);
		}

		return amazonS3;
	}
}

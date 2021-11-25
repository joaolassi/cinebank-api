package com.cinema.project.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Essa classe contém as propriedades da aplicação.
 * @author JoaoLassi
 */
@ConfigurationProperties("cinema")
@Component
public class CinemaApiProperty {
	
	/**
	 * Novo objeto do tipo S3, que é onde são guardada as imagens.
	 */
	private final S3 s3 = new S3();
	
	/**
	 * Variável que define a origen permitida
	 */
	private String originPermitida = "http://localhost:4200";
	
	/**
	 * Método que pega os atributos da classe S3.
	 */
	public S3 getS3() {
		return s3;
	}
	
	
	
	public String getOriginPermitida() {
		return originPermitida;
	}



	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}
	
	/**
	 * Configura a seguranã da aplicação
	 */

	private final Seguranca seguranca = new Seguranca();

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

	}


	/**
	 * Classe que define os atributos de S3.
	 */
	public static class S3 {

		private String accessKeyId;

		private String secretAccessKey;
		
		private String bucket = "joaolassi-cinema-imagens";

		public String getBucket() {
			return bucket;
		}

		public void setBucket(String bucket) {
			this.bucket = bucket;
		}

		public String getAccessKeyId() {
			return accessKeyId;
		}

		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}

		public String getSecretAccessKey() {
			return secretAccessKey;
		}

		public void setSecretAccessKey(String secretAccessKey) {
			this.secretAccessKey = secretAccessKey;
		}
	}
}

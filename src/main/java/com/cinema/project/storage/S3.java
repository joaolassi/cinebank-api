package com.cinema.project.storage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;

import com.cinema.project.config.property.CinemaApiProperty;

/**
 * Essa classe contém as configurações para salvar a imagem na plataforma AWS.
 * @author JoaoLassi
 */
@Component
public class S3 {

	private static final Logger logger = LoggerFactory.getLogger(S3.class);

	@Autowired
	private CinemaApiProperty property;

	@Autowired
	private AmazonS3 amazonS3;
	
	/**
	 * Esse método salva temporariamente a imagem na AWS.
	 * @param imagem a imagem a ser salva
	 */
	public String salvarTemporariamente(MultipartFile imagem) {
		AccessControlList acl = new AccessControlList();
		acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(imagem.getContentType());
		objectMetadata.setContentLength(imagem.getSize());

		String nomeUnico = gerarNomeUnico(imagem.getOriginalFilename());

		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(property.getS3().getBucket(), nomeUnico,
					imagem.getInputStream(), objectMetadata).withAccessControlList(acl);

			putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true"))));

			amazonS3.putObject(putObjectRequest);

			if (logger.isDebugEnabled()) {
				logger.debug("Imagem {} enviada para o S3.", imagem.getOriginalFilename());
			}

			return nomeUnico;
		} catch (IOException e) {
			throw new RuntimeException("Imagem não foi enviada para o S3.", e);
		}
	}
	
	/**
	 * Esse método configura a url da imagem.
	 * @param imagem 
	 */
	public String configurarUrl(String objeto) {
		return property.getS3().getBucket() + ".s3.amazonaws.com/" + objeto;
	}

	/**
	 * Esse método salva definidamente a imagem na AWS.
	 * @param imagem a imagem a ser salva
	 */
	public void salvar(String imagem) {
		SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
				property.getS3().getBucket(),
				imagem, 
				new ObjectTagging(Collections.emptyList()));

		amazonS3.setObjectTagging(setObjectTaggingRequest);
	}
	
	/**
	 * Esse método remove a imagem da AWS.
	 * @param objeto imagem a ser excluida
	 */
	public void remover(String imagem) {
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
				property.getS3().getBucket(), imagem).withKey(property.getS3().getAccessKeyId());
		
		amazonS3.deleteObject(deleteObjectRequest);
	}
	
	/**
	 * Esse método substitui a imagem da AWS.
	 * @param objeto imagem a ser excluida
	 */
	public void substituir(String imagemAntiga, String imagemNova) {
		if (StringUtils.hasText(imagemAntiga)) {
			this.remover(imagemAntiga);
		}
		salvar(imagemNova);
	}

	/**
	 * Esse método gera um nome único para a imagem.
	 * @param originalFilename noma da imagem
	 */
	private String gerarNomeUnico(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}
}

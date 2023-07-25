package org.forwardingproxy.repo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.forwardingproxy.dto.AnalysedLog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "proxy_log")
@Data
@SuperBuilder
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class AnalysedProxyLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "analysed")
	private Boolean analysed;

	@Column(name = "referenceid_01")
	private String referenceId_01;

	@Column(name = "referenceid_02")
	private String referenceId_02;

	@Column(name = "referenceid_03")
	private String referenceId_03;

	@Column(name = "referenceid_04")
	private String referenceId_04;

	@Column(name = "referenceid_05")
	private String referenceId_05;

	@Column(name = "referenceid_06")
	private String referenceId_06;

	@Column(name = "referenceid_07")
	private String referenceId_07;

	@Column(name = "referenceid_08")
	private String referenceId_08;

	@Column(name = "referenceid_09")
	private String referenceId_09;

	@Column(name = "referenceid_10")
	private String referenceId_10;

	public static AnalysedProxyLogEntity from(AnalysedLog analysedLog) {
		AnalysedProxyLogEntity result = AnalysedProxyLogEntity.builder()
				.id(analysedLog.getId())
				.analysed(analysedLog.getAnalysed())
				.referenceId_01(analysedLog.getReferenceId_01())
				.referenceId_02(analysedLog.getReferenceId_02())
				.referenceId_03(analysedLog.getReferenceId_03())
				.referenceId_04(analysedLog.getReferenceId_04())
				.referenceId_05(analysedLog.getReferenceId_05())
				.referenceId_06(analysedLog.getReferenceId_06())
				.referenceId_07(analysedLog.getReferenceId_07())
				.referenceId_08(analysedLog.getReferenceId_08())
				.referenceId_09(analysedLog.getReferenceId_09())
				.referenceId_10(analysedLog.getReferenceId_10())
				.build();
		return result;
	}
}

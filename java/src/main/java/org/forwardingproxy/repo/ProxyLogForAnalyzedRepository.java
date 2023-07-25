package org.forwardingproxy.repo;

import java.util.List;

import org.forwardingproxy.repo.entity.ProxyLogForAnalyzedEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyLogForAnalyzedRepository extends CrudRepository<ProxyLogForAnalyzedEntity, Long> {
	public List<ProxyLogForAnalyzedEntity> findByAnalysedFalse();
}

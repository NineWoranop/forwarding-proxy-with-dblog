package org.forwardingproxy.repo;

import org.forwardingproxy.repo.entity.AnalysedProxyLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysedProxyLogRepository extends CrudRepository<AnalysedProxyLogEntity, Long> {

}

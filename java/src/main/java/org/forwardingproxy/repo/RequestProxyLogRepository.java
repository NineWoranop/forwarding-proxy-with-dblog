package org.forwardingproxy.repo;

import org.forwardingproxy.repo.entity.RequestProxyLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestProxyLogRepository extends CrudRepository<RequestProxyLogEntity, Long> {

}

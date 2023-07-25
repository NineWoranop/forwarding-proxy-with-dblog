package org.forwardingproxy.repo;

import org.forwardingproxy.repo.entity.ResponseProxyLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseProxyLogRepository extends CrudRepository<ResponseProxyLogEntity, Long> {

}

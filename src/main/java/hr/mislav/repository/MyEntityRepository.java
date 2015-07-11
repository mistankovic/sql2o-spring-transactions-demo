package hr.mislav.repository;

import hr.mislav.domain.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
	MyEntity findByName(String test);
}

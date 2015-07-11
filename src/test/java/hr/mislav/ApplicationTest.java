package hr.mislav;

import hr.mislav.domain.MyEntity;
import hr.mislav.repository.MyEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class ApplicationTest {
	@Autowired
	Sql2o sql2o;
	@Autowired
	MyEntityRepository myEntityRepository;

	private MyEntity saved;

	@Before
	public void setUp() throws Exception {
		saved = myEntityRepository.saveAndFlush(new MyEntity("test"));
	}

	@Test
	public void testJpaAndSql2oWorkTogetherInTransaction() throws Exception {
		assertThat(myEntityRepository.findByName("test"), equalTo(saved));
		assertThat(selectNamesFromMyEntitiesWithSql2o(), hasItem("test"));
	}

	@Test
	public void testSql2oInsert() throws Exception {
		sql2o.open().createQuery("insert into my_entity (id, name) values (nextval('hibernate_sequence'), 'test2')")
				.executeUpdate();
		assertThat(selectNamesFromMyEntitiesWithSql2o(), hasItems("test", "test2"));
	}

	@Test
	public void entityNotVisibleWithSql2oWhenSavedWithSpringDataWithoutFlush() throws Exception {
		MyEntity anotherEntity = myEntityRepository.save(new MyEntity("anotherTest"));
		assertThat(selectNamesFromMyEntitiesWithSql2o(), not(hasItem("anotherTest")));
		assertThat(myEntityRepository.findByName("anotherTest"), equalTo(anotherEntity));
	}

	@Test
	public void entityVisibleWithSql2oAfterSavingAndFetchingItWithSpringData() throws Exception {
		MyEntity anotherEntity = myEntityRepository.save(new MyEntity("anotherTest"));
		assertThat(myEntityRepository.findByName("anotherTest"), equalTo(anotherEntity));
		assertThat(selectNamesFromMyEntitiesWithSql2o(), hasItem("anotherTest"));
	}

	@Test(expected = Sql2oException.class)
	public void exceptionThrownWhenTriedToCloseSql2oConnection() throws Exception {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("SELECT now()").executeScalar(Date.class);
		}
	}

	private List<String> selectNamesFromMyEntitiesWithSql2o() {
		return sql2o.open().createQuery("select name from my_entity").executeAndFetch(String.class);
	}
}

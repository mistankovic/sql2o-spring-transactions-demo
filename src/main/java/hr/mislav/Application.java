package hr.mislav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.sql2o.Sql2o;
import org.sql2o.spring.SpringSql2oConnectionHandler;

import javax.sql.DataSource;

import static org.sql2o.QuirksMode.PostgreSQL;

@SpringBootApplication
@EnableTransactionManagement
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Sql2o sql2o(DataSource dataSource){
		Sql2o sql2o = new Sql2o(dataSource, PostgreSQL);
		sql2o.setConnectionHandler(sql2oConnectionHandler(sql2o));
		return sql2o;
	}

	@Bean
	public SpringSql2oConnectionHandler sql2oConnectionHandler(Sql2o sql2o){
		return new SpringSql2oConnectionHandler(sql2o);
	}
}

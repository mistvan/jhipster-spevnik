package sk.mistvan.spevnik.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

@Configuration
@Profile(Constants.SPRING_PROFILE_HEROKU)
public class HerokuDatabaseConfiguration {

	private final Logger log = LoggerFactory.getLogger(HerokuDatabaseConfiguration.class);

	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties) {
		log.debug("Configuring Heroku Datasource");

		String herokuUrl = System.getenv("JDBC_DATABASE_URL");
		if (herokuUrl != null) {
			log.info("Using Heroku, parsing their $JDBC_DATABASE_URL to use it with JDBC");
			URI dbUri = null;
			try {
				dbUri = new URI(herokuUrl);
			} catch (URISyntaxException e) {
				throw new ApplicationContextException("Heroku database connection pool is not configured correctly");
			}

			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];
			String dbUrl = "jdbc:mysql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

			HikariConfig config = new HikariConfig();

			// MySQL optimizations, see
			// https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
			if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(dataSourceProperties.getDriverClassName())) {
				config.addDataSourceProperty("cachePrepStmts", jHipsterProperties.getDatasource().isCachePrepStmts());
				config.addDataSourceProperty("prepStmtCacheSize",
						jHipsterProperties.getDatasource().getPrepStmtCacheSize());
				config.addDataSourceProperty("prepStmtCacheSqlLimit",
						jHipsterProperties.getDatasource().getPrepStmtCacheSqlLimit());
			}

			config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
			config.addDataSourceProperty("url", dbUrl);
			config.addDataSourceProperty("user", username);
			config.addDataSourceProperty("password", password);
			return new HikariDataSource(config);
		} else {
			throw new ApplicationContextException(
					"Heroku database URL is not configured, you must set $JDBC_DATABASE_URL");
		}
	}
}

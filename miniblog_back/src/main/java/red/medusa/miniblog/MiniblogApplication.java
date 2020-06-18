package red.medusa.miniblog;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import red.medusa.miniblog.web.blog.bean.Blog;
import red.medusa.miniblog.web.blog.bean.BlogParam;
import red.medusa.miniblog.web.blog.bean.BlogTag;
import red.medusa.miniblog.web.blog.bean.TagBlob;
import red.medusa.miniblog.web.blog.mapper.TagMapper;
import red.medusa.miniblog.web.pad.bean.Card;
import red.medusa.miniblog.web.pad.bean.Pad;
import red.medusa.miniblog.web.pad.mapper.PadMapper;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@SpringBootApplication()
@MapperScan("red.medusa.miniblog.**.mapper")
public class MiniblogApplication {

    @Autowired
    private PadMapper padMapper;

    @Autowired
    private TagMapper tagMapper;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/miniblog?useunicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        dataSource.setUser("root");
        dataSource.setPassword("root1234");
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setTypeAliases(new Class[]{Pad.class, TagBlob.class, Card.class, Blog.class, BlogTag.class, BlogParam.class});
        sessionFactory.setDataSource(dataSource());
        return sessionFactory.getObject();
    }

    public static void main(String[] args) {
        SpringApplication.run(MiniblogApplication.class, args);
    }

    /**
     * 初始化数据库
     */
    @PostConstruct
    public void init() {
        long defaultPadNum = padMapper.findAll().size();
        if (defaultPadNum == 0) {
            Pad defaultPad = new Pad("0f49491c-4509-4bd6-a1d8-84f6de7b4b84", null, "DEFAULT", "JavaScript", true, false, true, new Date(), new Date(), 0, true, new ArrayList<>());
            padMapper.insert(defaultPad);
        }

        TagBlob tagBlob = tagMapper.findBlogTag();
        if (Objects.isNull(tagBlob)) {
            BlogTag blogTag = new BlogTag("blogTags", new String[][]{});
            tagBlob = new TagBlob(blogTag);
            tagMapper.insert(tagBlob);
        }
    }
}


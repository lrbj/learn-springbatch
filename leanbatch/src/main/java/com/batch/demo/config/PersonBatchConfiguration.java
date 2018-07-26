package com.batch.demo.config;


import com.batch.demo.enty.Person;
import com.batch.demo.listener.JobCompletionNotificationListener;
import com.batch.demo.processor.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * @Author: Kayla,Ye
 * @Description: 处理具体的工作业务： 读数据、处理数据、 写数据
 * @Date:Created in 2:25 PM 7/25/2018
 */
@Configuration
@EnableBatchProcessing
public class PersonBatchConfiguration {
    //插入语句
    private static final String PERSON_INSERT = "INSERT INTO Person (person_name, person_age,person_sex) VALUES (:personName, :personAge,:personSex)";


    //1、读数据
    @Bean
    public ItemReader<Person> reader(){
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person> ();

        //加载外部文件
        reader.setResource ( new ClassPathResource ( "sample-data.csv" ) );

        reader.setLineMapper(new DefaultLineMapper<Person> (){{
            setLineTokenizer( new DelimitedLineTokenizer(){{
                setNames(new String[] { "personName","personAge","personSex" });
            }} );

            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person> () {{
                setTargetType(Person.class);
            }});
        }});

        return reader;
    }

    //处理数据
    @Bean
    public PersonItemProcessor processor(){
        return  new PersonItemProcessor ();
    }

    //写数据
    @Bean
    public ItemWriter<Person> write(DataSource dataSource){
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person> ();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql(PERSON_INSERT);
        writer.setDataSource(dataSource);
        return  writer;
    }


    @Bean
    public Job importUserJob(JobBuilderFactory jobs,@Qualifier("step1")Step s1,JobCompletionNotificationListener listener) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer ())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader<Person> reader,
                      ItemWriter<Person> writer,ItemProcessor<Person, Person> processor) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate (dataSource);
    }

}

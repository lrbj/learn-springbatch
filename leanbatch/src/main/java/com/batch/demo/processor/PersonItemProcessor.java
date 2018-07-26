package com.batch.demo.processor;

import com.batch.demo.enty.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: Kayla,Ye
 * @Description:中间转化器
 * @Date:Created in 10:33 AM 7/25/2018
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    //查询
    private static final  String  GET_PRODUCT = "select * from Person where person_name = ?";
    private static  final Logger log = LoggerFactory.getLogger ( PersonItemProcessor.class );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Person process(Person person) throws Exception {
        List<Person> personList = jdbcTemplate.query ( GET_PRODUCT,new Object[]{person.getPersonName ()},new RowMapper<Person> () {
            @Override
            public Person mapRow(ResultSet resultSet,int rowNum) throws SQLException {
                Person p = new Person (  );
                p.setPersonName ( resultSet.getString ( 1 ) );
                p.setPersonAge ( resultSet.getString ( 2 ) );
                p.setPersonSex ( resultSet.getString ( 3 ) );
                return  p;
            }
        } );

        if( personList.size () > 0 ){
            log.info ( "该数据已录入" );
        }

        String sex = null;
        if(person.getPersonSex ().equals ( "0" )){
            sex = "男";
        }else{
            sex = "女";
        }

        log.info ( "转换(性别"+ person.getPersonSex () + ") 为(" + sex + ")");
        final Person transformedPerson = new Person ( person.getPersonName (), person.getPersonAge (), sex );
        log.info ( "转化(" + person + ") 为（" + transformedPerson + ")" );

        return  transformedPerson;

    }
}

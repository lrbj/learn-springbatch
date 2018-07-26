package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @Author: Kayla,Ye
 * @Description: 中间转化器（不需要输入和输出的类型相同）
 * @Date:Created in 4:35 PM 7/25/2018
 */
public class PersonItemProcessor implements ItemProcessor<Person , Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) throws Exception {
        //处理器将人物的名字转化为大写字母
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}

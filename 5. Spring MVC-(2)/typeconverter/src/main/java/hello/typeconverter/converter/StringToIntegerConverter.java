package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String,Integer> {

    @Override
    public Integer convert(String source) {
        log.info("converter source ={}", source);
        Integer integer = Integer.valueOf(source); //string -> integer
        return integer;
    }
}

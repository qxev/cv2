package cn.clickvalue.cv2.test;

import junit.framework.TestCase;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import cn.clickvalue.cv2.services.AppModule;

/**
 * 
    public static StringTransformService build(
            @InjectService("PipelineBuilder") PipelineBuilder builder,
            List<StringTransformFilter> configuration, Logger log) {

        StringTransformService terminator = new StringTransformService() {
            public String transform(String input) {
                return input;
            }
        };
        
        return builder.build(log, StringTransformService.class, StringTransformFilter.class, configuration, terminator);
    }
 * 
 * @author yu
 * 
 */
public class StringTransformTest extends TestCase {

    public void testCompute() {

        RegistryBuilder builder = new RegistryBuilder();

        builder.add(AppModule.class);

        Registry registry = builder.build();

        registry.performRegistryStartup();

        StringTransformService stringTransform = registry.getService(
                "StringTransformService", StringTransformService.class);

        UpcasePreFilter upf = new UpcasePreFilter();

        assertEquals("TEST", upf.transform("test", stringTransform));

        assertFalse("test".equals(upf.transform("TEST", stringTransform)));
    }

}

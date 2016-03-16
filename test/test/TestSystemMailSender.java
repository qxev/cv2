package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.email.SystemMailSender;

public class TestSystemMailSender extends AbstractTransactionalDataSourceSpringContextTests {

    private SystemMailSender systemMailSender;
    
    @Override
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:spring-main.xml" };
    }

    public void setSystemMailSender(SystemMailSender systemMailSender) {
        this.systemMailSender = systemMailSender;
    }
    
    public void testSendMail() {
    	while(true){
    		systemMailSender.send("测试", "这里是测试，请不要回复！！！", new String[] {"Larry.lang@darwinmarketing.com","Larry.lang@darwinmarketing.com","Larry.lang@darwinmarketing.com"});
    	}
    }
}

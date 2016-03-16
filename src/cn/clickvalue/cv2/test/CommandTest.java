package cn.clickvalue.cv2.test;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import cn.clickvalue.cv2.services.AppModule;

/**
 * 
  public static ICommand buildDateParsers(List<ICommand> commands,
            @InjectService("ChainBuilder") ChainBuilder chainBuilder) {
        return chainBuilder.build(ICommand.class, commands);
  }

  public static void contributeDateParsers(
            OrderedConfiguration<ICommand> configuration) {
        // The chain will be created from the contribution below.
        configuration.add("parser1", new ParserCommandA());
        configuration.add("parser2", new ParserCommandB());
  }
   
 * @author yu
 * 
 */
public class CommandTest extends TestCase {

    public void testChain() {

        RegistryBuilder builder = new RegistryBuilder();

        builder.add(AppModule.class);

        Registry registry = builder.build();

        registry.performRegistryStartup();

        ICommand chain = registry.getService("dateParsers", ICommand.class);

        // Failed commandA and Failed commandB
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(AbstractCommand.DATE_KEY, "2006 November 40");
        boolean validDate = chain.execute(context);
        assertFalse(validDate);

        // Failed commandA and Passed commandB
        context.clear();
        context.put(AbstractCommand.DATE_KEY, "12 Jun 2008");
        validDate = chain.execute(context);
        assertTrue(validDate);
        GregorianCalendar cal = new GregorianCalendar(2008, 5, 12);
        assertEquals(cal.getTime(), context
                .get(AbstractCommand.PARSED_DATE_KEY));

        // Passed commandA
        context.clear();
        context.put(AbstractCommand.DATE_KEY, "2008 January 8");
        validDate = chain.execute(context);
        assertTrue(validDate);
        cal = new GregorianCalendar(2008, 0, 8);
        assertEquals(cal.getTime(), context
                .get(AbstractCommand.PARSED_DATE_KEY));

    }

}

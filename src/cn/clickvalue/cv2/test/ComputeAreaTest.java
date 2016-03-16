package cn.clickvalue.cv2.test;

import junit.framework.TestCase;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import cn.clickvalue.cv2.services.AppModule;


/**
 * Add to AppModule
   public static IComputeArea buildAreaCalculator(
            Map<Class, IComputeArea> configuration,
            @InjectService("StrategyBuilder")
            StrategyBuilder builder) {
        StrategyRegistry<IComputeArea> registry = StrategyRegistry.newInstance(
                IComputeArea.class, configuration);

        return builder.build(registry);
    }

    public static void contributeAreaCalculator(
            MappedConfiguration<Class, IComputeArea> configuration) {
        configuration.add(Circle.class, new CircleArea());
        configuration.add(Square.class, new SquareArea());
    }
 * 
 * @author yu
 *
 */
public class ComputeAreaTest extends TestCase {

    public void testCompute() {

        RegistryBuilder builder = new RegistryBuilder();

        builder.add(AppModule.class);

        Registry registry = builder.build();

        registry.performRegistryStartup();

        IComputeArea calculator = registry.getService("areaCalculator",
                IComputeArea.class);

        Square square = new Square();
        square.setSize(1.5);
        assertEquals(2.25, calculator.compute(square));

        Circle circle = new Circle();
        circle.setRadius(2);
        assertEquals(4 * Math.PI, calculator.compute(circle), 0.00000001);

        Object obj = new Object();
        try {
            calculator.compute(obj);
            assertFalse(false);
        } catch (IllegalArgumentException iae) {
            // should get exception : Could not find strategy instance for class
            // java.lang.Object
            System.out.println("test");
            assertTrue(true);
        }

    }

}
